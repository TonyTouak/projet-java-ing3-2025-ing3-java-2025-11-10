package controleur;

import dao.articleDaoImpl;
import modele.Article;
import vue.magasinVue;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class magasinControleur {

    private magasinVue vue;
    private articleDaoImpl dao;
    private ArrayList<Article> articles;
    private String sexe;

    public magasinControleur(magasinVue vue, articleDaoImpl dao, String sexe) {
        this.vue = vue;
        this.dao = dao;
        this.sexe = sexe;
        this.articles = sexe.equalsIgnoreCase("Homme")
                ? dao.getArticlesParPageHomme(1, 18)
                : dao.getArticlesParPageFemme(1, 18);
        //on affiche 18 articles par page
    }

    public void appliquerFiltres(String type, String marque, int prixMax, String reduction) {
        //on utilise java Stream pour le filtrage
        ArrayList<Article> filtres = (ArrayList<Article>) articles.stream()
                .filter(a -> type.equals("Tous") || a.getType().equalsIgnoreCase(type))
                .filter(a -> marque.equals("Toutes") || a.getMarque().equalsIgnoreCase(marque))
                .filter(a -> a.getPrixUnique() <= prixMax)
                .filter(a -> {
                    if (reduction.equals("Toutes")) return true;
                    if (reduction.equals("Avec réduction")) return a.getPrixReduit() < a.getPrixUnique();
                    if (reduction.equals("Sans réduction")) return a.getPrixReduit() >= a.getPrixUnique();
                    return true;
                })
                .collect(Collectors.toList());

        vue.afficherArticles(filtres);
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public void appliquerRecherche(String motCle) {
        ArrayList<Article> resultats = new ArrayList<>();
        for (Article article : dao.getAll()) {
            if (article.getSexe().equalsIgnoreCase(sexe) &&
                    article.getNom().toLowerCase().contains(motCle.toLowerCase())) {
                resultats.add(article);
            }
        }
        vue.afficherArticles(resultats);
    }
}
