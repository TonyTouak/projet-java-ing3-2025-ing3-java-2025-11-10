package dao;

import modele.Article;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface articleDao {
    ArrayList<Article> getAll();

    void ajouter(Article article);

    Article chercher(int idArticle);

    void supprimer(Article article);

    ArrayList<Article> getArticlesParPageHomme(int page, int taillePage);

    ArrayList<Article> getArticlesParPageFemme(int page, int taillePage);

    List<Article> getVariantesParArticle(String marque, String type, String sexe, String nom);

    void mettreAJour(Article article) throws SQLException;

    boolean decrementerStock(Connection connection, int idArticle, int quantite) throws SQLException;

    boolean verifierStock(int idArticle, int quantite);

    void appliquerReduction(int idArticle, double reductionPourcent);

}
