package dao;

import modele.ArticleCommande;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface articleCommandeDao {
    boolean ajouterLigneCommande(Connection connection, ArticleCommande ac);

    List<ArticleCommande> getArticlesParCommande(int idCommande);

    void supprimerLignesCommande(int idCommande) throws Exception;

    void mettreAJourLigneCommande(ArticleCommande ac) throws SQLException;
}