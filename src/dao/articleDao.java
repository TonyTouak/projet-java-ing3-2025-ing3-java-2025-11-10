package dao;

import modele.Article;

import java.util.ArrayList;
import java.util.List;

public interface articleDao {
    ArrayList<Article> getAll();

    void ajouter(Article article);

    Article chercher(int idArticle);

    Article modifier(Article article);

    void supprimer(Article article);

    List<Article> getVariantesParArticle(String marque, String type, String sexe, String nom);

    Article getArticleParAttributsEtTaille(String marque, String type, String sexe, String nom, String taille);
}
