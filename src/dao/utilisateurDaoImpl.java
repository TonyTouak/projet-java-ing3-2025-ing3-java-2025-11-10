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
}
