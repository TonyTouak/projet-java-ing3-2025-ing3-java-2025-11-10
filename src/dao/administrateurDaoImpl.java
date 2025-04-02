package dao;

import modele.Administrateur;
import java.sql.*;
import java.util.ArrayList;

public class administrateurDaoImpl implements administrateurDao {
    private DaoFactory daoFactory;

    public administrateurDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public ArrayList<Administrateur> getAll() {
        ArrayList<Administrateur> listeAdmins = new ArrayList<>();
        String query = "SELECT * FROM Administrateur a JOIN Utilisateur u ON a.IDUtilisateur = u.IDUtilisateur";

        try (Connection connexion = daoFactory.getConnection();
             Statement statement = connexion.createStatement();
             ResultSet resultats = statement.executeQuery(query)) {

            while (resultats.next()) {
                int idUtilisateur = resultats.getInt("IDUtilisateur");
                int idAdmin = resultats.getInt("IDAdmin");
                String nom = resultats.getString("nom");
                String email = resultats.getString("email");
                String motDePasse = resultats.getString("mot_de_passe");

                Administrateur admin = new Administrateur(idUtilisateur, nom, email, motDePasse, idAdmin);
                listeAdmins.add(admin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Création de la liste des administrateurs impossible");
        }

        return listeAdmins;
    }

    @Override
    public void ajouter(Administrateur admin) {
        String queryUtilisateur = "INSERT INTO Utilisateur (nom, email, mot_de_passe) VALUES (?, ?, ?)";
        String queryAdmin = "INSERT INTO Administrateur (IDUtilisateur) VALUES (?)";

        try (Connection connexion = daoFactory.getConnection()) {
            connexion.setAutoCommit(false);

            try (PreparedStatement preparedStatementUtilisateur = connexion.prepareStatement(queryUtilisateur, Statement.RETURN_GENERATED_KEYS)) {

                preparedStatementUtilisateur.setString(1, admin.getNom());
                preparedStatementUtilisateur.setString(2, admin.getEmail());
                preparedStatementUtilisateur.setString(3, admin.getMotDePasse());
                preparedStatementUtilisateur.executeUpdate();

                try (ResultSet generatedKeys = preparedStatementUtilisateur.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idUtilisateur = generatedKeys.getInt(1);
                        admin.setId(idUtilisateur);

                        try (PreparedStatement preparedStatementAdmin = connexion.prepareStatement(queryAdmin, Statement.RETURN_GENERATED_KEYS)) {
                            preparedStatementAdmin.setInt(1, idUtilisateur);
                            preparedStatementAdmin.executeUpdate();

                            try (ResultSet generatedKeysAdmin = preparedStatementAdmin.getGeneratedKeys()) {
                                if (generatedKeysAdmin.next()) {
                                    admin.setIdAdmin(generatedKeysAdmin.getInt(1));
                                }
                            }
                        }
                    }
                }
            }
            connexion.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ajout de l'administrateur impossible");
        }
    }

    @Override
    public Administrateur chercher(int idAdmin) {
        Administrateur admin = null;
        String query = "SELECT * FROM Administrateur a JOIN Utilisateur u ON a.IDUtilisateur = u.IDUtilisateur WHERE a.IDAdmin = ?";

        try (Connection connexion = daoFactory.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(query)) {

            preparedStatement.setInt(1, idAdmin);
            try (ResultSet resultats = preparedStatement.executeQuery()) {
                if (resultats.next()) {
                    int idUtilisateur = resultats.getInt("IDUtilisateur");
                    String nom = resultats.getString("nom");
                    String email = resultats.getString("email");
                    String motDePasse = resultats.getString("mot_de_passe");

                    admin = new Administrateur(idUtilisateur, nom, email, motDePasse, idAdmin);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Administrateur non trouvé dans la base de données");
        }

        return admin;
    }

    @Override
    public void supprimer(Administrateur admin) {
        String queryAdmin = "DELETE FROM Administrateur WHERE IDAdmin = ?";
        String queryUtilisateur = "DELETE FROM Utilisateur WHERE IDUtilisateur = ?";

        try (Connection connexion = daoFactory.getConnection()) {
            connexion.setAutoCommit(false);

            try (PreparedStatement preparedStatementAdmin = connexion.prepareStatement(queryAdmin);
                 PreparedStatement preparedStatementUtilisateur = connexion.prepareStatement(queryUtilisateur)) {

                preparedStatementAdmin.setInt(1, admin.getIdAdmin());
                preparedStatementAdmin.executeUpdate();

                preparedStatementUtilisateur.setInt(1, admin.getId());
                preparedStatementUtilisateur.executeUpdate();
            }

            connexion.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Suppression de l'administrateur impossible");
        }
    }
}
