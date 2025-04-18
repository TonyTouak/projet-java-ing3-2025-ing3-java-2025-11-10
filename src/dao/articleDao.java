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

    Article modifier(Article article);

    void supprimer(Article article);

    Article getArticleParId(int id);

    List<Article> getVariantesParArticle(String marque, String type, String sexe, String nom);

    Article getArticleParAttributsEtTaille(String marque, String type, String sexe, String nom, String taille);

    void mettreAJour(Article article) throws SQLException;

    boolean decrementerStock(Connection connection, int idArticle, int quantite) throws SQLException;

    boolean verifierStock(int idArticle, int quantite);
}
