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

    /**
     * Récupère tous les articles de la base de données
     *
     * @return : Une liste contenant tous les articles
     */
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
                double reduction = resultats.getDouble("reduction");




                Article article = new Article(id, prixUnique, prixVrac, marque, quantiteVrac, taille, type, nom, image, sexe, quantite, reduction);
                listeArticles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Création de la liste d'articles impossible");
        }

        return listeArticles;
    }

    /**
     * Ajoute un nouvel article dans la base de données
     *
     * @param article : L'article à ajouter
     */
    @Override
    public void ajouter(Article article) {
        String query = "INSERT INTO article (prix_unique, prix_vrac, marque, quantite_vrac, taille, type, nom, image, sexe, quantite, reduction) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
            preparedStatement.setDouble(11, article.getReduction());




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

    /**
     * Cherche un article dans la base de données en fonction de son identifiant
     *
     * @param idArticle : L'identifiant de l'article
     * @return : L'article correspondant s'il est trouvé ou un objet null
     */
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
                    double reduction = resultats.getDouble("reduction");




                    article = new Article(idArticle, prixUnique, prixVrac, marque, quantiteVrac, taille, type, nom, image, sexe, quantite, reduction);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Article non trouvé dans la base de données");
        }

        return article;
    }


    /**
     * Supprime un article de la base de données
     *
     * @param article : L'article à supprimer
     */
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

    /**
     * Récupère tous les articles pour homme
     *
     * @param page : Le numéro de la page souhaitée
     * @param taillePage : Le nombre d'articles par page
     * @return : La liste des articles pour homme
     */
    @Override
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
                    double reduction = resultats.getDouble("reduction");



                    Article article = new Article(id, prixUnique, prixVrac, marque, quantiteVrac, taille, type, nom, image, sexe, quantite, reduction);
                    listeArticles.add(article);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Chargement des articles paginés impossible");
        }

        return listeArticles;
    }

    /**
     * Récupère tous les articles pour femme
     *
     * @param page : Le numéro de la page souhaitée
     * @param taillePage : Le nombre d'articles par page
     * @return : La liste des articles pour femme
     */
    @Override
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
                    double reduction = resultats.getDouble("reduction");



                    Article article = new Article(id, prixUnique, prixVrac, marque, quantiteVrac, taille, type, nom, image, sexe, quantite, reduction);
                    listeArticles.add(article);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Chargement des articles paginés impossible");
        }

        return listeArticles;
    }


    /**
     * Récupère toutes les informations qui sont intéressantes à afficher
     *
     * @param marque : La marque de l'article.
     * @param type : Le type de l'article.
     * @param sexe : Le sexe qui porte l'article.
     * @param nom : Le nom de l'article.
     * @return : Une l iste attributs intéressants
     */
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


    /**
     * Convertit un objet de type ResultSet en un article, utile pour obtenir
     * les infos qui nous intéressent pour l'affichage des articles dans le magasin
     *
     * @param result : L'objet resultSet à convertir
     * @return : l'article obtenue
     * @throws SQLException
     */
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
                result.getInt("quantite"),
                result.getDouble("reduction")
                );
    }

    /**
     * Met à jour la quantité d'un article dans la base de données.
     *
     * @param article : L'article dont la quantité doit être modifiée
     * @throws SQLException
     */
    @Override
    public void mettreAJour(Article article) throws SQLException {
        String sql = "UPDATE article SET quantite = ? WHERE IDArticle = ?";
        try (Connection conn = daoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, article.getQuantite());
            stmt.setInt(2, article.getId());

            stmt.executeUpdate();
        }
    }


    /**
     * Décrémente le stock d'un article qui a été acheté ou ajouter au panier
     *
     * @param connection : La connexion à la base de données
     * @param idArticle : L'identifiant de l'article à modifier
     * @param quantite : La quantité à enlever
     * @return : true si la quantité a bien été retiré, false sinon
     * @throws SQLException
     */
    @Override
    public boolean decrementerStock(Connection connection, int idArticle, int quantite) throws SQLException {
        String sql = "UPDATE article SET quantite = quantite - ? WHERE IDArticle = ? AND quantite >= ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, quantite);
            ps.setInt(2, idArticle);
            ps.setInt(3, quantite);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Vérifie si un article possède une quantité suffisante en stock
     *
     * @param idArticle : L'identifiant de l'article à vérifier
     * @param quantite : La quantité demandée de l'article
     * @return : true si on possède suffisamment l'article en stock, sinon false
     */
    @Override
    public boolean verifierStock(int idArticle, int quantite) {
        String sql = "SELECT quantite FROM article WHERE IDArticle = ?";

        try (Connection connection = daoFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, idArticle);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("quantite") >= quantite;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur vérification stock: " + e.getMessage());
        }
        return false;
    }

    /**
     * Applique une réduction à un article (droit d'un administrateur)
     *
     * @param idArticle : L'identifiant de l'article en promotion
     * @param pourcentage_reduction : Le pourcentage de réduction à appliquer
     */
    public void appliquerReduction(int idArticle, double pourcentage_reduction) {
        try (Connection connexion = daoFactory.getConnection()) {
            String sql = "UPDATE article SET reduction = ? WHERE IDArticle = ?";
            PreparedStatement ps = connexion.prepareStatement(sql);
            ps.setDouble(1, pourcentage_reduction);
            ps.setInt(2, idArticle);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




}

