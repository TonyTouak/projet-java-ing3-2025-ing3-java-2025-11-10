package dao;

import modele.Commande;
import modele.Client;
import modele.Article;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class commandeDaoImpl implements commandeDao {
    private DaoFactory daoFactory;

    public commandeDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public ArrayList<Commande> getAll() {
        ArrayList<Commande> listeCommandes = new ArrayList<>();
        String query = "SELECT * FROM commande";

        try (Connection connexion = daoFactory.getConnection();
             Statement statement = connexion.createStatement();
             ResultSet resultats = statement.executeQuery(query)) {

            while (resultats.next()) {
                int idCommande = resultats.getInt("IDCommande");
                int idClient = resultats.getInt("IDClient");
                int idArticle = resultats.getInt("IDarticle");
                Date date = resultats.getDate("date");
                double prixFinal = resultats.getDouble("prix");
                int quantite = resultats.getInt("quantite");

                Client client = new clientDaoImpl(daoFactory).chercher(idClient);
                Article article = new articleDaoImpl(daoFactory).chercher(idArticle);

                Commande commande = new Commande(idCommande, client, article, date, prixFinal, quantite);
                listeCommandes.add(commande);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Création de la liste de commandes impossible");
        }

        return listeCommandes;
    }

    @Override
    public void ajouter(Commande commande) {
        String query = "INSERT INTO commande (IDClient, IDarticle, date, prix, quantite) VALUES (?, ?, ?, ?, ?)";

        try (Connection connexion = daoFactory.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, commande.getClient().getIDClient());
            preparedStatement.setInt(2, commande.getArticle().getId());
            preparedStatement.setDate(3, new java.sql.Date(commande.getDate().getTime()));
            preparedStatement.setDouble(4, commande.getPrixFinal());
            preparedStatement.setInt(5, commande.getQuantite());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    commande.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ajout de la commande impossible");
        }
    }

    @Override
    public Commande chercher(int idCommande) {
        Commande commande = null;
        String query = "SELECT * FROM commande WHERE IDCommande = ?";

        try (Connection connexion = daoFactory.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(query)) {

            preparedStatement.setInt(1, idCommande);
            try (ResultSet resultats = preparedStatement.executeQuery()) {
                if (resultats.next()) {
                    int idClient = resultats.getInt("IDClient");
                    int idArticle = resultats.getInt("IDarticle");
                    Date date = resultats.getDate("date");
                    double prixFinal = resultats.getDouble("prix");
                    int quantite = resultats.getInt("quantite");

                    Client client = new clientDaoImpl(daoFactory).chercher(idClient);
                    Article article = new articleDaoImpl(daoFactory).chercher(idArticle);

                    commande = new Commande(idCommande, client, article, date, prixFinal, quantite);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Commande non trouvée dans la base de données");
        }

        return commande;
    }

    @Override
    public void supprimer(Commande commande) {
        String query = "DELETE FROM commande WHERE IDCommande = ?";

        try (Connection connexion = daoFactory.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(query)) {

            preparedStatement.setInt(1, commande.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Suppression de la commande impossible");
        }
    }
}