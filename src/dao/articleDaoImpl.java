package dao;

import modele.Article;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
                String taille = resultats.getString("taille");
                String type  = resultats.getString("type");
                String nom  = resultats.getString("nom");
                String image  = resultats.getString("image");
                String sexe = resultats.getString("sexe");
                int quantite = resultats.getInt("quantite");



                Article article = new Article(id, prixUnique, prixVrac, marque, quantiteVrac, taille, type, nom, image, sexe, quantite);
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
        String query = "INSERT INTO article (prix_unique, prix_vrac, marque, quantite_vrac, taille, type, nom, image, sexe, quantite) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connexion = daoFactory.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setDouble(1, article.getPrixUnique());
            preparedStatement.setDouble(2, article.getPrixVrac());
            preparedStatement.setString(3, article.getMarque());
            preparedStatement.setInt(4, article.getQuantiteVrac());
            preparedStatement.setString(5, article.getTaille());
            preparedStatement.setString(6, article.getType());
            preparedStatement.setString(7, article.getNom());
            preparedStatement.setString(8, article.getImage());
            preparedStatement.setString(9, article.getSexe());
            preparedStatement.setInt(10, article.getQuantite());



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
                    String taille = resultats.getString("taille");
                    String type  = resultats.getString("type");
                    String nom  = resultats.getString("nom");
                    String image  = resultats.getString("image");
                    String sexe  = resultats.getString("sexe");
                    int quantite = resultats.getInt("quantite");



                    article = new Article(idArticle, prixUnique, prixVrac, marque, quantiteVrac, taille, type, nom, image, sexe, quantite);
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
        String query = "UPDATE article SET prix_unique = ?, prix_vrac = ?, marque = ?, quantite_vrac = ?, type = ?, taille = ?, nom = ?, image = ?, sexe = ? WHERE IDArticle = ?";

        try (Connection connexion = daoFactory.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(query)) {

            preparedStatement.setDouble(1, article.getPrixUnique());
            preparedStatement.setDouble(2, article.getPrixVrac());
            preparedStatement.setString(3, article.getMarque());
            preparedStatement.setInt(4, article.getQuantiteVrac());
            preparedStatement.setString(5, article.getTaille());
            preparedStatement.setString(6, article.getType());
            preparedStatement.setString(7, article.getNom());
            preparedStatement.setString(8, article.getImage());
            preparedStatement.setString(9, article.getSexe());
            preparedStatement.setInt(10, article.getQuantite());
            preparedStatement.setInt(11, article.getId());



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

    public ArrayList<Article> getArticlesParPageHomme(int page, int taillePage) {
        ArrayList<Article> listeArticles = new ArrayList<>();
        int offset = (page - 1) * taillePage;
        String query = "SELECT * FROM article WHERE sexe = 'Homme' LIMIT ? OFFSET ?";

        try (Connection connexion = daoFactory.getConnection();
             PreparedStatement statement = connexion.prepareStatement(query)) {

            statement.setInt(1, taillePage);
            statement.setInt(2, offset);

            try (ResultSet resultats = statement.executeQuery()) {
                while (resultats.next()) {
                    int id = resultats.getInt("IDArticle");
                    double prixUnique = resultats.getDouble("prix_unique");
                    double prixVrac = resultats.getDouble("prix_vrac");
                    String marque = resultats.getString("marque");
                    int quantiteVrac = resultats.getInt("quantite_vrac");
                    String taille = resultats.getString("taille");
                    String type = resultats.getString("type");
                    String nom = resultats.getString("nom");
                    String image = resultats.getString("image");
                    String sexe = resultats.getString("sexe");
                    int quantite = resultats.getInt("quantite");


                    Article article = new Article(id, prixUnique, prixVrac, marque, quantiteVrac, taille, type, nom, image, sexe, quantite);
                    listeArticles.add(article);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Chargement des articles paginés impossible");
        }

        return listeArticles;
    }


    public ArrayList<Article> getArticlesParPageFemme(int page, int taillePage) {
        ArrayList<Article> listeArticles = new ArrayList<>();
        int offset = (page - 1) * taillePage;
        String query = "SELECT * FROM article WHERE sexe = 'Femme' LIMIT ? OFFSET ?";

        try (Connection connexion = daoFactory.getConnection();
             PreparedStatement statement = connexion.prepareStatement(query)) {

            statement.setInt(1, taillePage);
            statement.setInt(2, offset);

            try (ResultSet resultats = statement.executeQuery()) {
                while (resultats.next()) {
                    int id = resultats.getInt("IDArticle");
                    double prixUnique = resultats.getDouble("prix_unique");
                    double prixVrac = resultats.getDouble("prix_vrac");
                    String marque = resultats.getString("marque");
                    int quantiteVrac = resultats.getInt("quantite_vrac");
                    String taille = resultats.getString("taille");
                    String type = resultats.getString("type");
                    String nom = resultats.getString("nom");
                    String image = resultats.getString("image");
                    String sexe = resultats.getString("sexe");
                    int quantite = resultats.getInt("quantite");


                    Article article = new Article(id, prixUnique, prixVrac, marque, quantiteVrac, taille, type, nom, image, sexe, quantite);
                    listeArticles.add(article);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Chargement des articles paginés impossible");
        }

        return listeArticles;
    }



    public int getNombreTotalArticles() {
        String query = "SELECT COUNT(*) FROM article";
        int total = 0;

        try (Connection connexion = daoFactory.getConnection();
             Statement statement = connexion.createStatement();
             ResultSet resultats = statement.executeQuery(query)) {

            if (resultats.next()) {
                total = resultats.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Impossible de récupérer le nombre total d'articles");
        }

        return total;
    }


    public Article chercherParAttributs(String marque, String type, String sexe, String nom, String taille) {
        Article article = null;
        String query = "SELECT * FROM article WHERE marque = ? AND type = ? AND sexe = ? AND nom = ? AND taille = ?";

        try (Connection connexion = daoFactory.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(query)) {

            preparedStatement.setString(1, marque);
            preparedStatement.setString(2, type);
            preparedStatement.setString(3, sexe);
            preparedStatement.setString(4, nom);
            preparedStatement.setString(5, taille);

            try (ResultSet resultats = preparedStatement.executeQuery()) {
                if (resultats.next()) {
                    int quantite = Math.max(resultats.getInt("quantite"), 0);
                    int quantiteVrac = Math.max(resultats.getInt("quantite_vrac"), 0);

                    article = new Article(
                            resultats.getInt("IDArticle"),
                            resultats.getDouble("prix_unique"),
                            resultats.getDouble("prix_vrac"),
                            resultats.getString("marque"),
                            quantiteVrac,
                            taille,
                            resultats.getString("type"),
                            resultats.getString("nom"),
                            resultats.getString("image"),
                            resultats.getString("sexe"),
                            quantite
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Impossible de trouver l'article.");
        }

        return article;
    }

    public List<String> getTaillesDisponibles(String marque, String type, String sexe, String nom) {
        List<String> tailles = new ArrayList<>();
        String query = "SELECT taille FROM article WHERE marque = ? AND type = ? AND sexe = ? AND nom = ? AND quantite > 0";

        try (Connection connexion = daoFactory.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(query)) {

            preparedStatement.setString(1, marque);
            preparedStatement.setString(2, type);
            preparedStatement.setString(3, sexe);
            preparedStatement.setString(4, nom);

            try (ResultSet resultats = preparedStatement.executeQuery()) {
                while (resultats.next()) {
                    String taille = resultats.getString("taille");
                    if (taille != null && !taille.isEmpty()) {
                        tailles.add(taille);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la récupération des tailles disponibles.");
        }

        return tailles;
    }


    @Override
    public List<Article> getVariantesParArticle(String marque, String type, String sexe, String nom) {
        List<Article> variantes = new ArrayList<>();
        try {
            Connection connexion = daoFactory.getConnection();
            String sql = "SELECT * FROM article WHERE marque = ? AND type = ? AND sexe = ? AND nom = ? ORDER BY taille";
            PreparedStatement statement = connexion.prepareStatement(sql);
            statement.setString(1, marque);
            statement.setString(2, type);
            statement.setString(3, sexe);
            statement.setString(4, nom);

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                variantes.add(mapper(result));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return variantes;
    }

    @Override
    public Article getArticleParAttributsEtTaille(String marque, String type, String sexe, String nom, String taille) {
        try {
            Connection connexion = daoFactory.getConnection();
            String sql = "SELECT * FROM article WHERE marque = ? AND type = ? AND sexe = ? AND nom = ? AND taille = ?";
            PreparedStatement statement = connexion.prepareStatement(sql);
            statement.setString(1, marque);
            statement.setString(2, type);
            statement.setString(3, sexe);
            statement.setString(4, nom);
            statement.setString(5, taille);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return mapper(result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Article mapper(ResultSet result) throws SQLException {
        return new Article(
                result.getInt("IDArticle"),
                result.getDouble("prix_unique"),
                result.getDouble("prix_vrac"),
                result.getString("marque"),
                result.getInt("quantite_vrac"),
                result.getString("taille"),
                result.getString("type"),
                result.getString("nom"),
                result.getString("image"),
                result.getString("sexe"),
                result.getInt("quantite")
                );
    }
}

