package dao;

import modele.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
        String query = "INSERT INTO commande (IDClient, date, prix) VALUES (?, ?, ?)";

        try (Connection connexion = daoFactory.getConnection();
             PreparedStatement ps = connexion.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, commande.getClient().getIDClient());
            ps.setTimestamp(2, new Timestamp(commande.getDate().getTime()));
            ps.setFloat(3, commande.getPrix());

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
    public List<Commande> getCommandesParClient(int idClient) {
        List<Commande> commandes = new ArrayList<>();
        clientDao clientDao = new clientDaoImpl(daoFactory);

        Client client = clientDao.chercherIDCLient(idClient);

        if (client == null) {
            System.out.println("Client non trouvé pour l'ID : " + idClient);
            return commandes;
        }

        String sql = "SELECT idCommande, date, prix FROM commande WHERE idClient = ? ORDER BY date DESC";

        try (Connection con = daoFactory.getConnexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idClient);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int idCommande = rs.getInt("idCommande");
                Date dateCommande = rs.getDate("date");
                float montant = rs.getFloat("prix");

                Commande commande = new Commande(idCommande, client, dateCommande, montant);
                commandes.add(commande);
            }

            if (commandes.isEmpty()) {
                System.out.println("Aucune commande trouvée pour le client ID: " + idClient);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return commandes;
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

            Commande commande = new Commande();
            commande.setClient(client);
            commande.setDate(new Date());
            commande.setPrix(montantTotal);

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

                ArticleCommande ac = new ArticleCommande(article.getId(), idCommande, quantite, article.calculerPrix(quantite));
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

                articleCmdDao.ajouterLigneCommande(connexion, new ArticleCommande(article.getId(), idCommande, quantite, article.calculerPrix(quantite)));
                articleDao.decrementerStock(connexion, article.getId(), quantite);
            }

            connexion.commit();
        } catch (Exception e) {
            System.err.println("Erreur lors de la finalisation de la commande : " + e.getMessage());
            e.printStackTrace();
        }
    }



    private int insererCommande(Connection connection, Commande commande) {
        String sql = "INSERT INTO commande (IDClient, date, prix) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, commande.getClient().getId());
            // On utilise une conversion de type pour s'adapter à notre définition en sql
            ps.setTimestamp(2, new java.sql.Timestamp(commande.getDate().getTime()));
            ps.setFloat(3, commande.getPrix());

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
                new clientDaoImpl(daoFactory).chercherIDCLient(rs.getInt("IDClient")),
                rs.getTimestamp("date"),
                rs.getFloat("prix")
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

    @Override
    public List<Commande> getCommandesParClientID(int idClient) {
        List<Commande> commandes = new ArrayList<>();
        String query = "SELECT * FROM commande WHERE IDClient = ?";

        try (Connection connexion = daoFactory.getConnection();
             PreparedStatement ps = connexion.prepareStatement(query)) {

            ps.setInt(1, idClient);
            ResultSet rs = ps.executeQuery();

            Client client = new clientDaoImpl(daoFactory).chercher(idClient);

            while (rs.next()) {
                int idCommande = rs.getInt("IDCommande");
                Date date = rs.getDate("date");
                float prix = rs.getFloat("prix");

                List<Article> articles = getArticlesParCommande(idCommande);

                for (Article article : articles) {
                    commandes.add(new Commande(idCommande, client, date, prix));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur dans getCommandesParClientID()");
        }

        return commandes;
    }

    private List<Article> getArticlesParCommande(int idCommande) {
        List<Article> articles = new ArrayList<>();
        String query = "SELECT a.* FROM articlecommande ac " +
                "JOIN article a ON ac.IDArticle = a.IDArticle " +
                "WHERE ac.IDCommande = ?";

        try (Connection connexion = daoFactory.getConnection();
             PreparedStatement ps = connexion.prepareStatement(query)) {

            ps.setInt(1, idCommande);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Article article = new Article(
                        rs.getInt("IDArticle"),
                        rs.getDouble("prix_unique"),
                        rs.getDouble("prix_vrac"),
                        rs.getString("marque"),
                        rs.getInt("quantite_vrac"),
                        rs.getString("taille"),
                        rs.getString("type"),
                        rs.getString("nom"),
                        rs.getString("image"),
                        rs.getString("sexe"),
                        rs.getInt("quantite")
                );
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur dans getArticlesParCommande()");
        }

        return articles;
    }


    @Override
    public List<Commande> listerToutes() throws SQLException {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM commande";
        try (Connection conn = daoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Commande cmd = new Commande();
                cmd.setId(rs.getInt("IDCommande"));
                cmd.setDate(rs.getDate("date"));

                int idClient = rs.getInt("IDClient");
                Client client = new clientDaoImpl(daoFactory).chercher(idClient);
                cmd.setClient(client);

                commandes.add(cmd);
            }
        }
        return commandes;
    }

    @Override
    public void mettreAJour(Commande commande) {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnexion();
            String sql = "UPDATE commande SET IDClient = ?, date = ?, prix = ? WHERE IDCommande = ?";
            preparedStatement = connexion.prepareStatement(sql);

            preparedStatement.setInt(1, commande.getClient().getIDClient());
            preparedStatement.setDate(2, new java.sql.Date(commande.getDate().getTime()));
            preparedStatement.setFloat(3, commande.getPrix());
            preparedStatement.setInt(4, commande.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connexion != null) {
                try {
                    connexion.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}