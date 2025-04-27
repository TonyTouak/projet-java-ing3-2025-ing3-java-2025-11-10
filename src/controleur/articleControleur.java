package controleur;

import modele.Article;
import modele.Panier;
import vue.articleVue;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class articleControleur implements ActionListener {
    private Article article;
    private articleVue vue;

    /**
     * Constructeur du contrôleur d'article.
     *
     * Initialise le contrôleur avec un article et sa vue associée.
     *
     * @param article L'article concerné.
     * @param vue La vue affichant les détails de l'article.
     */

    public articleControleur(Article article, articleVue vue) {
        this.article = article;
        this.vue = vue;
    }

    /**
     * Gère l'action lorsque l'utilisateur clique sur "Ajouter au panier".
     * Vérifie la disponibilité du stock avant d'ajouter l'article au panier,
     * calcule le prix total et met à jour l'affichage du panier.
     *
     * @param e événement déclenché par l'action utilisateur
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if ("Ajouter au panier".equals(e.getActionCommand())) {
            int quantite = vue.getQuantiteSelectionnee();

            Panier panier = Panier.getInstance();
            int quantiteDejaDansPanier = panier.getQuantite(article);
            int stockRestant = article.getQuantite() - quantiteDejaDansPanier;

            if (quantite > stockRestant) {
                JOptionPane.showMessageDialog(vue,
                        "Quantité indisponible. Stock restant: " + stockRestant,
                        "Erreur de stock",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            float prixTotal = article.calculerPrix(quantite);
            panier.ajouterArticle(article, quantite, prixTotal);

            String message;
            if (article.getQuantiteVrac() > 1 && quantite >= article.getQuantiteVrac()) {
                int paquets = quantite / article.getQuantiteVrac();
                int restant = quantite % article.getQuantiteVrac();

                message = String.format(
                        "%d x %s ajouté(s) au panier\n" +
                                "Détail: %d paquet(s) de %d à %.2f €\n" +
                                "%d unité(s) à %.2f €\n" +
                                "Total: %.2f €",
                        quantite, article.getNom(),
                        paquets, article.getQuantiteVrac(), article.getPrixVrac(),
                        restant, article.getPrixUnique(),
                        prixTotal
                );
            } else {
                message = String.format(
                        "%d x %s ajouté(s) au panier\n" +
                                "Prix total: %.2f €",
                        quantite, article.getNom(), prixTotal
                );
            }

            JOptionPane.showMessageDialog(vue,
                    message,
                    "Article ajouté",
                    JOptionPane.INFORMATION_MESSAGE);

            vue.mettreAJourAffichagePanier();
        }
    }
}