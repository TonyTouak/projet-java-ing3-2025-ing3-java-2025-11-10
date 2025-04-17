package dao;

import modele.ArticleCommande;

import java.sql.Connection;
import java.util.List;

public interface articleCommandeDao {
     boolean ajouterLigneCommande(Connection connection, ArticleCommande ac);
    List<ArticleCommande> getArticlesParCommande(int idCommande) throws Exception;
    void supprimerLignesCommande(int idCommande) throws Exception;

    boolean decrementerStock(Connection connection, int idArticle, int quantite);
}