package dao;

import modele.Commande;
import modele.Client;
import modele.Article;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class commandeDaoImpl implements commandeDao {
    private final DaoFactory daoFactory;

    public commandeDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public ArrayList<Commande> getAll() {
        ArrayList<Commande> commandes = new ArrayList<>();
        String query = "SELECT * FROM commande";

        try (Connection connexion = daoFactory.getConnection();
             Statement statement = connexion.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                int idCommande = rs.getInt("IDCommande");
                int idClient = rs.getInt("IDClient");
                Date date = rs.getDate("date");
                double prix = rs.getDouble("prix");

                Client client = new clientDaoImpl(daoFactory).chercher(idClient);

                // Récupération des articles associés
                List<Article> articles = getArticlesParCommande(idCommande);

                for (Article article : articles) {
                    // Par défaut on met quantité = 1 car non stockée ailleurs
                    commandes.add(new Commande(idCommande, client, article, date, prix, 1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur dans getAll()");
        }

        return commandes;
    }

    @Override
    public void ajouter(Commande commande) {
        String queryCommande = "INSERT INTO commande (IDClient, date, prix) VALUES (?, ?, ?)";
        String queryArticleCommande = "INSERT INTO articlecommande (IDCommande, IDArticle) VALUES (?, ?)";

        try (Connection connexion = daoFactory.getConnection()) {
            connexion.setAutoCommit(false);

            // Insertion commande
            try (PreparedStatement psCommande = connexion.prepareStatement(queryCommande, Statement.RETURN_GENERATED_KEYS)) {
                psCommande.setInt(1, commande.getClient().getIDClient());
                psCommande.setDate(2, new java.sql.Date(commande.getDate().getTime()));
                psCommande.setDouble(3, commande.getPrixFinal());
                psCommande.executeUpdate();

                try (ResultSet rs = psCommande.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idCommande = rs.getInt(1);
                        commande.setId(idCommande);

                        // Insertion dans articlecommande
                        try (PreparedStatement psArtCom = connexion.prepareStatement(queryArticleCommande)) {
                            psArtCom.setInt(1, idCommande);
                            psArtCom.setInt(2, commande.getArticle().getId());
                            psArtCom.executeUpdate();
                        }
                    }
                }
            }

            connexion.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur dans ajouter()");
        }
    }

    @Override
    public Commande chercher(int idCommande) {
        String query = "SELECT * FROM commande WHERE IDCommande = ?";
        try (Connection connexion = daoFactory.getConnection();
             PreparedStatement ps = connexion.prepareStatement(query)) {

            ps.setInt(1, idCommande);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int idClient = rs.getInt("IDClient");
                Date date = rs.getDate("date");
                double prix = rs.getDouble("prix");

                Client client = new clientDaoImpl(daoFactory).chercher(idClient);
                List<Article> articles = getArticlesParCommande(idCommande);

                if (!articles.isEmpty()) {
                    // On retourne une commande par article
                    return new Commande(idCommande, client, articles.get(0), date, prix, 1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur dans chercher()");
        }

        return null;
    }

    @Override
    public void supprimer(Commande commande) {
        String deleteArticles = "DELETE FROM articlecommande WHERE IDCommande = ?";
        String deleteCommande = "DELETE FROM commande WHERE IDCommande = ?";

        try (Connection connexion = daoFactory.getConnection()) {
            try (PreparedStatement ps1 = connexion.prepareStatement(deleteArticles);
                 PreparedStatement ps2 = connexion.prepareStatement(deleteCommande)) {

                ps1.setInt(1, commande.getId());
                ps1.executeUpdate();

                ps2.setInt(1, commande.getId());
                ps2.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur dans supprimer()");
        }
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
                double prix = rs.getDouble("prix");

                List<Article> articles = getArticlesParCommande(idCommande);

                for (Article article : articles) {
                    commandes.add(new Commande(idCommande, client, article, date, prix, 1));
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
}
