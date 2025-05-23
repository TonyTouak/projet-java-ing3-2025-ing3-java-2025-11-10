package dao;

import modele.Article;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class statistiqueDaoImpl implements statistiqueDao {
    private final DaoFactory daoFactory;

    public statistiqueDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /**
     * Récupère le nombre total de commandes enregistrées dans la base de données
     *
     * @return : Le nombre total de commandes
     */
    @Override
    public int getNombreTotalCommandes() {
        String query = "SELECT COUNT(*) FROM commande";
        try (Connection connection = daoFactory.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Calcule le montant total des ventes, soit le chiffre d'affaires
     *
     * @return : Le montant total des ventes.
     */
    @Override
    public double getMontantTotalVentes() {
        String query = "SELECT ac.IDArticle, SUM(ac.quantite) AS quantiteVendue " +
                "FROM articlecommande ac " +
                "GROUP BY ac.IDArticle";
        double totalVentes = 0.0;
        try (Connection connection = daoFactory.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int idArticle = resultSet.getInt("IDArticle");
                int quantiteVendue = resultSet.getInt("quantiteVendue");

                Article article = getArticleById(idArticle);
                if (article != null) {
                    totalVentes += article.calculerPrix(quantiteVendue);  // on calcule la recette avec réduction
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalVentes;
    }


    /**
     * Récupère la quantité totale vendue pour chaque article
     *
     * @return : Une map contenant les articles et leurs quantités vendues
     */
    @Override
    public Map<Article, Integer> getQuantitesVenduesParArticle() {
        Map<Article, Integer> ventesParArticle = new HashMap<>();
        String query = "SELECT ac.IDArticle, SUM(ac.quantite) AS quantiteVendue " +
                "FROM articlecommande ac " +
                "GROUP BY ac.IDArticle";

        try (Connection connection = daoFactory.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int idArticle = resultSet.getInt("IDArticle");
                int quantiteVendue = resultSet.getInt("quantiteVendue");

                Article article = getArticleById(idArticle);
                if (article != null) {
                    ventesParArticle.put(article, quantiteVendue);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ventesParArticle;
    }

    /**
     * Récupère un article à partir de son identifiant
     *
     * @param idArticle : L'ID de l'article à chercher
     * @return : l'article si trouvé ou un objet null sinon
     */
    private Article getArticleById(int idArticle) {
        String query = "SELECT * FROM article WHERE IDArticle = ?";
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, idArticle);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Article(
                            resultSet.getInt("IDArticle"),
                            resultSet.getDouble("prix_unique"),
                            resultSet.getDouble("prix_vrac"),
                            resultSet.getString("marque"),
                            resultSet.getInt("quantite_vrac"),
                            resultSet.getString("taille"),
                            resultSet.getString("type"),
                            resultSet.getString("nom"),
                            resultSet.getString("image"),
                            resultSet.getString("sexe"),
                            resultSet.getInt("quantite")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
