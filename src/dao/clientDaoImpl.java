package dao;

import modele.Client;
import java.sql.*;
import java.util.ArrayList;

public class clientDaoImpl implements clientDao {
    private DaoFactory daoFactory;

    public clientDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /**
     * Récupère tous les clients de la base de données en joignant les informations d'utilisateur
     *
     * @return : Une liste contenant tous les clients
     */
    @Override
    public ArrayList<Client> getAll() {
        ArrayList<Client> listeClients = new ArrayList<>();
        String query = "SELECT * FROM client c JOIN utilisateur u ON c.IDUtilisateur = u.IDUtilisateur";

        try (Connection connexion = daoFactory.getConnection();
             Statement statement = connexion.createStatement();
             ResultSet resultats = statement.executeQuery(query)) {

            while (resultats.next()) {
                int idUtilisateur = resultats.getInt("IDUtilisateur");
                int idClient = resultats.getInt("IDClient");
                String nom = resultats.getString("nom");
                String email = resultats.getString("email");
                String motDePasse = resultats.getString("mot_de_passe");
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

    /**
     * Ajoute un nouveau client dans la base de données en créant d'abord un utilisateur
     *
     * @param client : Le client à ajouter
     */
    @Override
    public void ajouter(Client client) {
        String queryUtilisateur = "INSERT INTO utilisateur (nom, email, mot_de_passe) VALUES (?, ?, ?)";
        String queryClient = "INSERT INTO client (IDUtilisateur, adresse, telephone) VALUES ( ?, ?, ?)";

        try (Connection connexion = daoFactory.getConnection()) {
            connexion.setAutoCommit(false);

            try (PreparedStatement preparedStatementUtilisateur = connexion.prepareStatement(queryUtilisateur, Statement.RETURN_GENERATED_KEYS)) {

                preparedStatementUtilisateur.setString(1, client.getNom());
                preparedStatementUtilisateur.setString(2, client.getEmail());
                preparedStatementUtilisateur.setString(3, client.getMotDePasse());

                preparedStatementUtilisateur.executeUpdate();

                try (ResultSet generatedKeys = preparedStatementUtilisateur.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idUtilisateur = generatedKeys.getInt(1);
                        client.setId(idUtilisateur);

                        try (PreparedStatement preparedStatementClient = connexion.prepareStatement(queryClient, Statement.RETURN_GENERATED_KEYS)) {
                            preparedStatementClient.setInt(1, idUtilisateur);
                            preparedStatementClient.setString(2, client.getAdresse());
                            preparedStatementClient.setString(3, client.getTelephone());

                            preparedStatementClient.executeUpdate();

                            try (ResultSet generatedKeysClient = preparedStatementClient.getGeneratedKeys()) {
                                if (generatedKeysClient.next()) {
                                    client.setIDClient(generatedKeysClient.getInt(1));
                                }
                            }
                        }
                    }
                }
            }
            connexion.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ajout du client impossible");
        }
    }


    /**
     * Cherche un client par le biais de son identifiant d'utilisateur
     *
     * @param idUtilisateur : L'identifiant utilisateur du client
     * @return : le client trouvé ou un objet null sinon
     */
    @Override
    public Client chercher(int idUtilisateur) {
        Client client = null;
        String query = "SELECT * FROM client c JOIN utilisateur u ON c.IDUtilisateur = u.IDUtilisateur WHERE c.IDUtilisateur = ?";

        try (Connection connexion = daoFactory.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(query)) {

            preparedStatement.setInt(1, idUtilisateur);

            try (ResultSet resultats = preparedStatement.executeQuery()) {
                if (resultats.next()) {
                    int idClient = resultats.getInt("IDClient");
                    String nom = resultats.getString("nom");
                    String email = resultats.getString("email");
                    String motDePasse = resultats.getString("mot_de_passe");
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


    /**
     * Cherche un client via son identifiant
     *
     * @param idClient : L'identifiant du client à chercher
     * @return : le client si trouvé, un objet null sinon
     */
    @Override
    public Client chercherIDCLient(int idClient) {
        Client client = null;
        String query = "SELECT IDUtilisateur FROM client WHERE IDClient = ?";

        try (Connection connexion = daoFactory.getConnection();
             PreparedStatement ps = connexion.prepareStatement(query)) {

            ps.setInt(1, idClient);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int idUtilisateur = rs.getInt("IDUtilisateur");

                client = chercher(idUtilisateur);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Client non trouvé dans la base de données");
        }

        return client;
    }

    /**
     * Modifie les informations d'un client
     *
     * @param client : le client modifié
     * @return : Le client mis à jour
     */
    @Override
    public Client modifier(Client client) {
        String queryClient = "UPDATE client SET adresse = ?, telephone = ? WHERE IDClient = ?";
        String queryUtilisateur = "UPDATE utilisateur SET nom = ?, email = ?, mot_de_passe = ? WHERE IDUtilisateur = ?";

        try (Connection connexion = daoFactory.getConnection()) {
            connexion.setAutoCommit(false);

            try (PreparedStatement preparedStatementUtilisateur = connexion.prepareStatement(queryUtilisateur);
                 PreparedStatement preparedStatementClient = connexion.prepareStatement(queryClient)) {

                preparedStatementUtilisateur.setString(1, client.getNom());
                preparedStatementUtilisateur.setString(2, client.getEmail());
                preparedStatementUtilisateur.setString(3, client.getMotDePasse());
                preparedStatementUtilisateur.setInt(4, client.getId());
                preparedStatementUtilisateur.executeUpdate();

                preparedStatementClient.setString(1, client.getAdresse());
                preparedStatementClient.setString(2, client.getTelephone());
                preparedStatementClient.setInt(3, client.getIDClient());
                preparedStatementClient.executeUpdate();
            }

            connexion.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Mise à jour du client impossible");
        }

        return client;
    }

    /**
     * Supprime un client et l'utilisateur associé de la base de données
     *
     * @param client : Le client à supprimer
     */
    @Override
    public void supprimer(Client client) {
        String queryClient = "DELETE FROM client WHERE IDClient = ?";
        String queryUtilisateur = "DELETE FROM utilisateur WHERE IDUtilisateur = ?";

        try (Connection connexion = daoFactory.getConnection()) {
            connexion.setAutoCommit(false);

            try (PreparedStatement preparedStatementClient = connexion.prepareStatement(queryClient);
                 PreparedStatement preparedStatementUtilisateur = connexion.prepareStatement(queryUtilisateur)) {

                preparedStatementClient.setInt(1, client.getIDClient());
                preparedStatementClient.executeUpdate();

                preparedStatementUtilisateur.setInt(1, client.getId());
                preparedStatementUtilisateur.executeUpdate();
            }

            connexion.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Suppression du client impossible");
        }
    }


    /**
     * Supprime un client et l'utilisateur associé de la base de données
     *
     * @param idClient : L'identifiant du client à supprimer
     */
    @Override
    public boolean supprimerID(int idClient) {
        String queryClient = "DELETE FROM client WHERE IDClient = ?";
        String queryUtilisateur = "DELETE FROM utilisateur WHERE IDUtilisateur = ?";

        try (Connection connexion = daoFactory.getConnection()) {
            connexion.setAutoCommit(false);

            int idUtilisateur = -1;
            String queryFindUser = "SELECT IDUtilisateur FROM client WHERE IDClient = ?";
            try (PreparedStatement psFind = connexion.prepareStatement(queryFindUser)) {
                psFind.setInt(1, idClient);
                try (ResultSet rs = psFind.executeQuery()) {
                    if (rs.next()) {
                        idUtilisateur = rs.getInt("IDUtilisateur");
                    } else {
                        connexion.rollback();
                        return false;
                    }
                }
            }

            try (PreparedStatement psClient = connexion.prepareStatement(queryClient);
                 PreparedStatement psUtilisateur = connexion.prepareStatement(queryUtilisateur)) {

                psClient.setInt(1, idClient);
                psClient.executeUpdate();

                psUtilisateur.setInt(1, idUtilisateur);
                psUtilisateur.executeUpdate();
            }

            connexion.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de la suppression du client ou de l'utilisateur: " + e.getMessage());
            return false;
        }
    }
}


