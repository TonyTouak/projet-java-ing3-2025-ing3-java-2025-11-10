package dao;

import modele.Article;
import java.sql.*;
import java.util.ArrayList;

public class articleDaoImpl implements articleDao {
    private DaoFactory daoFactory;

    public articleDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public ArrayList<Article> getAll() {
        ArrayList<Article> listeArticles = new ArrayList<>();
        String query = "SELECT * FROM article";

        try (Connection connexion = daoFactory.getConnection();
             Statement statement = connexion.createStatement();
             ResultSet resultats = statement.executeQuery(query)) {

            while (resultats.next()) {
                int id = resultats.getInt("IDArticle");
                double prixUnique = resultats.getDouble("prix_unique");
                double prixVrac = resultats.getDouble("prix_vrac");
                String marque = resultats.getString("marque");
                int quantiteVrac = resultats.getInt("quantite_vrac");

                Article article = new Article(id, prixUnique, prixVrac, marque, quantiteVrac);
                listeArticles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Création de la liste d'articles impossible");
        }

        return listeArticles;
    }

    @Override
    public void ajouter(Article article) {
        String query = "INSERT INTO article (prix_unique, prix_vrac, marque, quantite_vrac) VALUES (?, ?, ?, ?)";

        try (Connection connexion = daoFactory.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setDouble(1, article.getPrixUnique());
            preparedStatement.setDouble(2, article.getPrixVrac());
            preparedStatement.setString(3, article.getMarque());
            preparedStatement.setInt(4, article.getQuantiteVrac());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    article.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ajout de l'article impossible");
        }
    }

    @Override
    public Article chercher(int idArticle) {
        Article article = null;
        String query = "SELECT * FROM article WHERE IDArticle = ?";

        try (Connection connexion = daoFactory.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(query)) {

            preparedStatement.setInt(1, idArticle);
            try (ResultSet resultats = preparedStatement.executeQuery()) {
                if (resultats.next()) {
                    double prixUnique = resultats.getDouble("prix_unique");
                    double prixVrac = resultats.getDouble("prix_vrac");
                    String marque = resultats.getString("marque");
                    int quantiteVrac = resultats.getInt("quantite_vrac");

                    article = new Article(idArticle, prixUnique, prixVrac, marque, quantiteVrac);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Article non trouvé dans la base de données");
        }

        return article;
    }

    @Override
    public Article modifier(Article article) {
        String query = "UPDATE article SET prix_unique = ?, prix_vrac = ?, marque = ?, quantite_vrac = ? WHERE IDArticle = ?";

        try (Connection connexion = daoFactory.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(query)) {

            preparedStatement.setDouble(1, article.getPrixUnique());
            preparedStatement.setDouble(2, article.getPrixVrac());
            preparedStatement.setString(3, article.getMarque());
            preparedStatement.setInt(4, article.getQuantiteVrac());
            preparedStatement.setInt(5, article.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Mise à jour échouée : article non trouvé");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Mise à jour de l'article impossible");
        }

        return article;
    }

    @Override
    public void supprimer(Article article) {
        String query = "DELETE FROM article WHERE IDArticle = ?";

        try (Connection connexion = daoFactory.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(query)) {

            preparedStatement.setInt(1, article.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Suppression de l'article impossible");
        }
    }
}
