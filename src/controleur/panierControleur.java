package controleur;

import modele.Article;
import modele.Client;
import modele.Panier;
import vue.panierVue;

public class panierControleur {
    private panierVue vue;
    private Panier panier;
    private Client client;

    public panierControleur(panierVue vue, Panier panier, Client client) {
        this.vue = vue;
        this.panier = panier;
        this.client = client;
    }

    public void modifierQuantite(Article article, int delta) {
        int nouvelleQuantite = panier.getQuantite(article) + delta;

        if (nouvelleQuantite <= 0) {
            supprimerArticle(article);
        } else if (nouvelleQuantite > article.getQuantite()) {
            vue.afficherMessage("Stock insuffisant pour cet article");
        } else {
            panier.modifierQuantite(article, nouvelleQuantite);
            vue.mettreAJourAffichage(panier);
        }
    }

    public void supprimerArticle(Article article) {
        panier.supprimerArticle(article);
        vue.mettreAJourAffichage(panier);
    }

    public void viderPanier() {
        panier.vider();
        vue.mettreAJourAffichage(panier);
    }

    public void validerCommande() {
        if (panier.estVide()) {
            vue.afficherMessage("Votre panier est vide");
            return;
        }

        vue.afficherMessage("Commande validée pour " + client.getNom());
        panier.vider();
        vue.mettreAJourAffichage(panier);
    }
}