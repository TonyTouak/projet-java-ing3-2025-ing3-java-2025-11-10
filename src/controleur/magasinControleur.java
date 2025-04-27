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

    /**
     * Constructeur du contrôleur de magasin.
     *
     * Initialise le contrôleur avec la vue du magasin, le DAO des articles et le sexe des articles affichés.
     * Charge les 18 premiers articles correspondant au sexe spécifié ("Homme" ou "Femme") pour l'affichage.
     *
     * @param vue La vue associée au magasin.
     * @param dao Le DAO pour accéder aux articles en base de données.
     * @param sexe Le sexe des articles à afficher ("Homme" ou "Femme").
     */

    public magasinControleur(magasinVue vue, articleDaoImpl dao, String sexe) {
        this.vue = vue;
        this.dao = dao;
        this.sexe = sexe;
        this.articles = sexe.equalsIgnoreCase("Homme")
                ? dao.getArticlesParPageHomme(1, 18)
                : dao.getArticlesParPageFemme(1, 18);
        //on affiche 18 articles par page
    }

    /**
     * Applique des filtres sur la liste des articles selon le type, la marque, le prix maximum et la présence d'une réduction,
     * puis met à jour l'affichage avec les articles filtrés.
     *
     * @param type      type d'article sélectionné
     * @param marque    marque sélectionnée
     * @param prixMax   prix maximum autorisé
     * @param reduction type de réduction ("Toutes", "Avec réduction", "Sans réduction")
     */
    public void appliquerFiltres(String type, String marque, int prixMax, String reduction) {
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

    /**
     * Applique une recherche d'articles par mot-clé sur leur nom,
     * en filtrant également selon le sexe sélectionné, puis met à jour l'affichage.
     *
     * @param motCle mot-clé saisi pour la recherche
     */
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
