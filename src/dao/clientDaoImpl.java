package dao;

import modele.Client;
import java.sql.*;
import java.util.ArrayList;

public class clientDaoImpl implements clientDao {
    private DaoFactory daoFactory;

    public clientDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public ArrayList<Client> getAll() {
        ArrayList<Client> listeClients = new ArrayList<>();
        String query = "SELECT * FROM Client c JOIN Utilisateur u ON c.IDUtilisateur = u.id";

        try (Connection connexion = daoFactory.getConnection();
             Statement statement = connexion.createStatement();
             ResultSet resultats = statement.executeQuery(query)) {

            while (resultats.next()) {
                int idUtilisateur = resultats.getInt("IDUtilisateur");
                int idClient = resultats.getInt("IDClient");
                String nom = resultats.getString("nom");
                String email = resultats.getString("email");
                String motDePasse = resultats.getString("motDePasse");
                String adresse = resultats.getString("adresse");
                String telephone = resultats.getString("telephone");

                Client client = new Client(idUtilisateur, nom, email, motDePasse, idClient, adresse, telephone);
                listeClients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Création de la liste de clients impossible");
        }

        return listeClients;
    }

    @Override
    public void ajouter(Client client) {
        String query = "INSERT INTO Client (IDUtilisateur, adresse, telephone) VALUES (?, ?, ?)";

        try (Connection connexion = daoFactory.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, client.getId());
            preparedStatement.setString(2, client.getAdresse());
            preparedStatement.setString(3, client.getTelephone());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    client.setIDClient(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ajout du client impossible");
        }
    }

    @Override
    public Client chercher(int idClient) {
        Client client = null;
        String query = "SELECT * FROM Client c JOIN Utilisateur u ON c.IDUtilisateur = u.id WHERE c.IDClient = ?";

        try (Connection connexion = daoFactory.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(query)) {

            preparedStatement.setInt(1, idClient);
            try (ResultSet resultats = preparedStatement.executeQuery()) {
                if (resultats.next()) {
                    int idUtilisateur = resultats.getInt("IDUtilisateur");
                    String nom = resultats.getString("nom");
                    String email = resultats.getString("email");
                    String motDePasse = resultats.getString("motDePasse");
                    String adresse = resultats.getString("adresse");
                    String telephone = resultats.getString("telephone");

                    client = new Client(idUtilisateur, nom, email, motDePasse, idClient, adresse, telephone);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Client non trouvé dans la base de données");
        }

        return client;
    }

    @Override
    public Client modifier(Client client) {
        String query = "UPDATE Client SET adresse = ?, telephone = ? WHERE IDClient = ?";

        try (Connection connexion = daoFactory.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(query)) {

            preparedStatement.setString(1, client.getAdresse());
            preparedStatement.setString(2, client.getTelephone());
            preparedStatement.setInt(3, client.getIDClient());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Mise à jour échouée : client non trouvé");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Mise à jour du client impossible");
        }

        return client;
    }

    @Override
    public void supprimer(Client client) {
        String query = "DELETE FROM Client WHERE IDClient = ?";

        try (Connection connexion = daoFactory.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(query)) {

            preparedStatement.setInt(1, client.getIDClient());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Suppression du client impossible");
        }
    }
}