package controleur;

import dao.articleDaoImpl;
import modele.Article;
import vue.magasinVue;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class magasinControleur {

    private magasinVue vue; // Utilise l'interface magasinVue
    private articleDaoImpl dao;
    private ArrayList<Article> articles;

    public magasinControleur(magasinVue vue, articleDaoImpl dao, String sexe) {
        this.vue = vue;
        this.dao = dao;
        this.articles = sexe.equalsIgnoreCase("Homme")
                ? dao.getArticlesParPageHomme(1, 18)
                : dao.getArticlesParPageFemme(1, 18);
    }

    public void appliquerFiltres(String type, String marque, int prixMax) {
        ArrayList<Article> filtres = (ArrayList<Article>) articles.stream()
                .filter(a -> type.equals("Tous") || a.getType().equalsIgnoreCase(type))
                .filter(a -> marque.equals("Toutes") || a.getMarque().equalsIgnoreCase(marque))
                .filter(a -> a.getPrixUnique() <= prixMax)
                .collect(Collectors.toList());

        vue.afficherArticles(filtres); // Appelle la méthode de la vue
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }
}
