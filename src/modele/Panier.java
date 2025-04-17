package modele;

import java.util.HashMap;
import java.util.Map;

public class Panier {
    private static Panier instance;
    private Map<Article, Integer> articles;
    private float total;

    private Panier() {
        articles = new HashMap<>();
        total = 0.0F;
    }

    public static Panier getInstance() {
        if (instance == null) {
            instance = new Panier();
        }
        return instance;
    }

    public void ajouterArticle(Article article, int quantite, double prixTotal) {
        if (quantite <= 0) {
            throw new IllegalArgumentException("La quantité doit être positive");
        }

        if (articles.containsKey(article)) {
            articles.put(article, articles.get(article) + quantite);
        } else {
            articles.put(article, quantite);
        }

        this.total += prixTotal;
    }

    public void modifierQuantite(Article article, int nouvelleQuantite) {
        if (!articles.containsKey(article)) {
            throw new IllegalArgumentException("L'article n'est pas dans le panier");
        }

        if (nouvelleQuantite <= 0) {
            supprimerArticle(article);
            return;
        }

        int difference = nouvelleQuantite - articles.get(article);
        total += article.getPrixUnique() * difference;
        articles.put(article, nouvelleQuantite);
    }

    public void supprimerArticle(Article article) {
        if (articles.containsKey(article)) {
            total -= article.getPrixUnique() * articles.get(article);
            articles.remove(article);
        }
    }

    public void vider() {
        articles.clear();
        total = 0.0F;
    }

    public Map<Article, Integer> getArticles() {
        return new HashMap<>(articles);
    }

    public int getQuantite(Article article) {
        return articles.getOrDefault(article, 0);
    }

    public int getNombreArticles() {
        return articles.values().stream().mapToInt(Integer::intValue).sum();
    }

    public float getTotal() {
        return total;
    }

    public boolean estVide() {
        return articles.isEmpty();
    }

}