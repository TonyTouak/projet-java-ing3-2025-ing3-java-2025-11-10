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

    public articleControleur(Article article, articleVue vue) {
        this.article = article;
        this.vue = vue;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("Ajouter au panier".equals(e.getActionCommand())) {
            int quantite = vue.getQuantiteSelectionnee();

            if (quantite > article.getQuantite()) {
                JOptionPane.showMessageDialog(vue,
                        "Quantité indisponible. Stock restant: " + article.getQuantite(),
                        "Erreur de stock",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            double prixTotal = article.calculerPrix(quantite);

            Panier panier = Panier.getInstance();
            panier.ajouterArticle(article, quantite, prixTotal);

            article.setQuantite(article.getQuantite() - quantite);
            vue.mettreAJourStock(article.getQuantite());

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