package dao;

import modele.ArticleCommande;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface articleCommandeDao {
    boolean ajouterLigneCommande(Connection connection, ArticleCommande ac);
    List<ArticleCommande> getArticlesParCommande(int idCommande);
    void supprimerLignesCommande(int idCommande) throws Exception;

    boolean decrementerStock(Connection connection, int idArticle, int quantite);

    void mettreAJourLigneCommande(ArticleCommande ac) throws SQLException;
}