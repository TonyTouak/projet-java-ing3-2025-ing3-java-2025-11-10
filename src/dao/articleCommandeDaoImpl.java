package dao;

import modele.ArticleCommande;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class articleCommandeDaoImpl implements articleCommandeDao {
    private DaoFactory daoFactory;

    public articleCommandeDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /**
     * Récupère tous les articles d'une commande à partir de l'identifiant de la commande
     *
     * @param idCommande : L'identifiant de la commande en question
     * @return : Une liste des articles faisant partie de la commande
     */
    @Override
    public List<ArticleCommande> getArticlesParCommande(int idCommande) {
        List<ArticleCommande> articles = new ArrayList<>();
        String sql = "SELECT * FROM articlecommande WHERE IDCommande = ?";

        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idCommande);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    articles.add(new ArticleCommande(
                            rs.getInt("IDArticle"),
                            rs.getInt("IDCommande"),
                            rs.getInt("quantite"),
                            rs.getFloat("prix")

                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }

    /**
     * Supprime tous les articles liées à une commande
     *
     * @param idCommande L'identifiant de la commande
     * @throws Exception Si la suppression présente un problème
     */
    @Override
    public void supprimerLignesCommande(int idCommande) throws Exception {
        String sql = "DELETE FROM articlecommande WHERE IDCommande = ?";

        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idCommande);
            statement.executeUpdate();
        }
    }

    /**
     * Ajoute un article à une commande avec sa quantité
     *
     * @param connection : La connexion à la base de données
     * @param ac : l'article commandé à ajouter à la base de données
     * @return : true si l'ajout est réussi sinon false
     */
    @Override
    public boolean ajouterLigneCommande(Connection connection, ArticleCommande ac) {
        String sql = "INSERT INTO articlecommande (IDArticle, IDCommande, quantite, prix) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ac.getIdArticle());
            ps.setInt(2, ac.getIdCommande());
            ps.setInt(3, ac.getQuantite());
            ps.setFloat(4, ac.getPrix());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la ligne de commande: " + e.getMessage());
            return false;
        }
    }


    /**
     * Met à jour la quantité et le prix d'un article dans une commande existante (droit d'un administrateur)
     *
     * @param articleCommande : L'article commandé modifié
     */
    @Override
    public void mettreAJourLigneCommande(ArticleCommande articleCommande) {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnexion();
            String sql = "UPDATE articlecommande SET quantite = ?, prix = ? " +
                    "WHERE IDArticle = ? AND IDCommande = ?";

            preparedStatement = connexion.prepareStatement(sql);

            preparedStatement.setInt(1, articleCommande.getQuantite());
            preparedStatement.setFloat(2, articleCommande.getPrix());
            preparedStatement.setInt(3, articleCommande.getIdArticle());
            preparedStatement.setInt(4, articleCommande.getIdCommande());

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated == 0) {
                System.err.println("Aucune ligne mise à jour pour ArticleCommande: " +
                        articleCommande.getIdArticle() + ", " +
                        articleCommande.getIdCommande());
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de ArticleCommande:");
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    System.err.println("Erreur fermeture PreparedStatement:");
                    e.printStackTrace();
                }
            }
            if (connexion != null) {
                try {
                    connexion.close();
                } catch (SQLException e) {
                    System.err.println("Erreur fermeture Connection:");
                    e.printStackTrace();
                }
            }
        }
    }

}