package dao;

import modele.Article;

import java.util.ArrayList;

public interface articleDao {
    ArrayList<Article> getAll();

    void ajouter(Article article);

    Article chercher(int idArticle);

    Article modifier(Article article);

    void supprimer(Article article);
}
