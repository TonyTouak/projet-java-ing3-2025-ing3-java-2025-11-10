package dao;

import modele.Utilisateur;
import java.sql.*;
import java.util.ArrayList;

public class utilisateurDaoImpl implements utilisateurDao {
    private DaoFactory daoFactory;

    public utilisateurDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public ArrayList<Utilisateur> getAll() {
        ArrayList<Utilisateur> listeUtilisateurs = new ArrayList<>();
        String query = "SELECT * FROM utilisateur";

        try (Connection connexion = daoFactory.getConnection();
             Statement statement = connexion.createStatement();
             ResultSet resultats = statement.executeQuery(query)) {

            while (resultats.next()) {
                int idUtilisateur = resultats.getInt("IDUtilisateur");
                String nom = resultats.getString("nom");
                String email = resultats.getString("email");
                String motDePasse = resultats.getString("mot_de_passe");

                Utilisateur utilisateur = new Utilisateur(idUtilisateur, nom, email, motDePasse) {};
                listeUtilisateurs.add(utilisateur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la récupération des utilisateurs.");
        }

        return listeUtilisateurs;
    }

    public Utilisateur chercher(String email, String motDePasse) {
        Utilisateur utilisateur = null;
        String query = "SELECT * FROM utilisateur WHERE email = ? AND mot_de_passe = ?";

        try (Connection connexion = daoFactory.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(query)) {

            preparedStatement.setString(1, email);
            System.out.println(preparedStatement);
            preparedStatement.setString(2, motDePasse);
            System.out.println(preparedStatement);
            try (ResultSet resultats = preparedStatement.executeQuery()) {
                System.out.println("----1-----");
                if (resultats.next()) {
                    int id = resultats.getInt("IDUtilisateur");
                    String nom = resultats.getString("nom");
                    utilisateur = new Utilisateur(id, nom, email, motDePasse){};
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Utilisateur introuvable avec ces identifiants");
        }

        return utilisateur;
    }


}
