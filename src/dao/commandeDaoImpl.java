package dao;

import modele.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class commandeDaoImpl implements commandeDao {
    private final DaoFactory daoFactory;


    public commandeDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;

    }

    @Override
    public ArrayList<Commande> getAll() throws SQLException {
        ArrayList<Commande> listeCommandes = new ArrayList<>();
        String query = "SELECT * FROM commande";

        try (Connection connexion = daoFactory.getConnection();
             Statement statement = connexion.createStatement();
             ResultSet resultats = statement.executeQuery(query)) {

            while (resultats.next()) {
                listeCommandes.add(creerCommandeDepuisResultSet(resultats));
            }
        }
        return listeCommandes;
    }

    @Override
    public int ajouter(Commande commande) throws SQLException {
        String query = "INSERT INTO commande (IDClient, date, prix, quantite) VALUES (?, ?, ?, ?)";

        try (Connection connexion = daoFactory.getConnection();
             PreparedStatement ps = connexion.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, commande.getClient().getId());
            ps.setTimestamp(2, new Timestamp(commande.getDate().getTime()));
            ps.setFloat(3, commande.getPrix());
            ps.setInt(4, commande.getQuantite());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            throw new SQLException("Échec de la création de commande, aucun ID généré.");
        }
    }

    @Override
    public Commande chercher(int idCommande) throws SQLException {
        String query = "SELECT * FROM commande WHERE IDCommande = ?";

        try (Connection connexion = daoFactory.getConnection();
             PreparedStatement ps = connexion.prepareStatement(query)) {

            ps.setInt(1, idCommande);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return creerCommandeDepuisResultSet(rs);
                }
            }
        }
        return null;
    }

    @Override
    public void supprimer(Commande commande) throws SQLException {
        String query = "DELETE FROM commande WHERE IDCommande = ?";

        try (Connection connexion = daoFactory.getConnection();
             PreparedStatement ps = connexion.prepareStatement(query)) {

            ps.setInt(1, commande.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public boolean validerCommandeComplete(Client client, Map<Article, Integer> articlesPanier) {
        Connection connection = null;
        try {
            connection = daoFactory.getConnection();
            connection.setAutoCommit(false);

            float montantTotal = calculerTotalPanier(articlesPanier);
            int quantiteTotale = calculerQuantiteTotale(articlesPanier);

            Commande commande = new Commande();
            commande.setClient(client);
            commande.setDate(new Date());
            commande.setPrix(montantTotal);
            commande.setQuantite(quantiteTotale);

            int idCommande = insererCommande(connection, commande);
            if (idCommande == -1) {
                connection.rollback();
                return false;
            }

            articleCommandeDao articleCmdDao = new articleCommandeDaoImpl(daoFactory);
            articleDao articleDao = new articleDaoImpl(daoFactory);

            for (Map.Entry<Article, Integer> entry : articlesPanier.entrySet()) {
                Article article = entry.getKey();
                int quantite = entry.getValue();

                if (!articleDao.verifierStock(article.getId(), quantite)) {
                    connection.rollback();
                    return false;
                }

                ArticleCommande ac = new ArticleCommande(article.getId(), idCommande, quantite);
                if (!articleCmdDao.ajouterLigneCommande(connection, ac)) {
                    connection.rollback();
                    return false;
                }

                if (!articleDao.decrementerStock(connection, article.getId(), quantite)) {
                    connection.rollback();
                    return false;
                }
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException ex) {
                System.err.println("Erreur rollback: " + ex.getMessage());
            }
            System.err.println("Erreur commande: " + e.getMessage());
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Erreur fermeture connexion: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void finaliserCommande(Client client, Panier panier,
                                  articleCommandeDao articleCmdDao,
                                  articleDao articleDao) {
        try (Connection connexion = DaoFactory.getConnection()) {
            connexion.setAutoCommit(false);

            int idCommande = creerNouvelleCommande(client.getId(), panier.getTotal());
            if (idCommande == -1) {
                System.err.println("Échec création commande");
                return;
            }

            for (Map.Entry<Article, Integer> entry : panier.getArticles().entrySet()) {
                Article article = entry.getKey();
                int quantite = entry.getValue();

                articleCmdDao.ajouterLigneCommande(connexion, new ArticleCommande(article.getId(), idCommande, quantite));
                articleDao.decrementerStock(connexion, article.getId(), quantite);
            }

            connexion.commit();
        } catch (Exception e) {
            System.err.println("Erreur lors de la finalisation de la commande : " + e.getMessage());
            e.printStackTrace();
        }
    }



    private int insererCommande(Connection connection, Commande commande) {
        String sql = "INSERT INTO commande (IDClient, date, prix, quantite) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, commande.getClient().getId());
            // On utilise une conversion de type pour s'adapter à notre définition en sql
            ps.setTimestamp(2, new java.sql.Timestamp(commande.getDate().getTime()));
            ps.setFloat(3, commande.getPrix());
            ps.setInt(4, commande.getQuantite());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur insertion commande: " + e.getMessage());
        }
        return -1;
    }

    @Override
    public Commande creerCommandeDepuisResultSet(ResultSet rs) throws SQLException {
        return new Commande(
                rs.getInt("IDCommande"),
                new clientDaoImpl(daoFactory).chercher(rs.getInt("IDClient")),
                rs.getTimestamp("date"),
                rs.getFloat("prix"),
                rs.getInt("quantite")
        );
    }

    @Override
    public int creerNouvelleCommande(int idClient, float prixTotal) throws SQLException {
        String sql = "INSERT INTO commande (IDClient, date, prix, quantite) VALUES (?, NOW(), ?, ?)";

        try (Connection connection = daoFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, idClient);
            ps.setFloat(2, prixTotal);
            ps.setInt(3, 0);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Échec de la création de commande, aucun ID généré");
    }

    private float calculerTotalPanier(Map<Article, Integer> articlesPanier) {
        return (float) articlesPanier.entrySet().stream()
                .mapToDouble(e -> e.getKey().getPrixUnique() * e.getValue())
                .sum();
    }

    private int calculerQuantiteTotale(Map<Article, Integer> articlesPanier) {
        return articlesPanier.values().stream().mapToInt(Integer::intValue).sum();
    }


}