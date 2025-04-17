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

    @Override
    public List<ArticleCommande> getArticlesParCommande(int idCommande) throws Exception {
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
                            rs.getInt("quantite")
                    ));
                }
            }
        }
        return articles;
    }

    @Override
    public void supprimerLignesCommande(int idCommande) throws Exception {
        String sql = "DELETE FROM articlecommande WHERE IDCommande = ?";

        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idCommande);
            statement.executeUpdate();
        }
    }

    @Override
    public boolean decrementerStock(Connection connection, int idArticle, int quantite) {
        String sql = "UPDATE article SET quantite = quantite - ? WHERE IDArticle = ? AND quantite >= ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, quantite);
            ps.setInt(2, idArticle);
            ps.setInt(3, quantite);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur décrément stock: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean ajouterLigneCommande(Connection connection, ArticleCommande ac) {
        String sql = "INSERT INTO articlecommande (IDArticle, IDCommande, quantite) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ac.getIdArticle());
            ps.setInt(2, ac.getIdCommande());
            ps.setInt(3, ac.getQuantite());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la ligne de commande: " + e.getMessage());
            return false;
        }
    }
}