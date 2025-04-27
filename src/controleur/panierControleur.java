package controleur;

import dao.*;
import modele.*;
import vue.paiementVue;
import vue.panierVue;

import java.util.Map;

public class panierControleur {
    private final panierVue vue;
    private final Panier panier;
    private final Client client;
    private final commandeDao commandeDao;
    private final articleCommandeDao articleCmdDao;
    private final articleDao articleDao;
    private final paiementDao paiementDao;

    /**
     * Constructeur du contrôleur du panier.
     *
     * Initialise la vue du panier et configure les accès aux différentes tables DAO
     * nécessaires pour gérer les commandes, les articles, et les paiements.
     *
     * @param vue La vue associée au panier.
     * @param panier Le modèle du panier contenant les articles ajoutés par le client.
     * @param client Le client actuellement connecté.
     */

    public panierControleur(panierVue vue, Panier panier, Client client) {
        this.vue = vue;
        this.panier = panier;
        this.client = client;

        DaoFactory daoFactory = DaoFactory.getInstance("shopping", "root", "");
        this.commandeDao = new commandeDaoImpl(daoFactory);
        this.articleCmdDao = new articleCommandeDaoImpl(daoFactory);
        this.articleDao = new articleDaoImpl(daoFactory);
        this.paiementDao = new paiementDaoImpl(daoFactory);
    }

    public Panier getPanier() {
        return this.panier;
    }

    /**
     * Valide la commande en vérifiant que le panier n'est pas vide
     * et que les stocks sont suffisants avant de lancer le paiement.
     */
    public void validerCommande() {
        if (panier.estVide()) {
            vue.afficherErreur("Votre panier est vide");
            return;
        }

        if (!verifierStocks()) {
            vue.afficherErreur("Certains articles n'ont pas suffisamment de stock");
            return;
        }

        lancerProcessusPaiement();
    }

    /**
     * Lance le processus de paiement en ouvrant la fenêtre de paiement
     * et finalise la commande si le paiement réussit.
     */
    private void lancerProcessusPaiement() {
        float montantTotal = panier.getTotal();

        paiementVue vuePaiement = new paiementVue(client, panier, montantTotal, (success) -> {
            if (success) {
                commandeDao.finaliserCommande(client, panier, articleCmdDao, articleDao);
                panier.vider();
                mettreAJourVue();
            }
        });

        vuePaiement.setVisible(true);
    }

    /**
     * Modifie la quantité d'un article dans le panier.
     * Si la nouvelle quantité est inférieure ou égale à 0, l'article est supprimé.
     * Si elle dépasse le stock disponible, un message est affiché.
     *
     * @param article l'article concerné
     * @param delta la variation de quantité (+1 pour ajouter, -1 pour retirer)
     */
    public void modifierQuantite(Article article, int delta) {
        int nouvelleQuantite = panier.getQuantite(article) + delta;

        if (nouvelleQuantite <= 0) {
            supprimerArticle(article);
        } else if (nouvelleQuantite > article.getQuantite()) {
            vue.afficherMessage("Stock insuffisant. Quantité disponible: " + article.getQuantite());
        } else {
            panier.modifierQuantite(article, nouvelleQuantite);
            mettreAJourVue();
        }
    }

    /**
     * Supprime un article du panier et met à jour l'affichage.
     *
     * @param article l'article à supprimer
     */
    public void supprimerArticle(Article article) {
        panier.supprimerArticle(article);
        mettreAJourVue();
    }

    /**
     * Vide complètement le panier et met à jour l'affichage.
     */
    public void viderPanier() {
        panier.vider();
        mettreAJourVue();
    }

    /**
     * Vérifie que tous les articles du panier ont des stocks suffisants
     * par rapport aux quantités demandées.
     *
     * @return true si les stocks sont suffisants, false sinon
     */
    private boolean verifierStocks() {
        for (Map.Entry<Article, Integer> entry : panier.getArticles().entrySet()) {
            if (entry.getValue() > entry.getKey().getQuantite()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Met à jour l'affichage du panier et le total affiché.
     */
    private void mettreAJourVue() {
        vue.actualiserAffichage();
        vue.actualiserTotal();
    }

    public int getQuantiteArticle(Article article) {
        return panier.getQuantite(article);
    }


    public float getTotalPanier() {
        return panier.getTotal();
    }

    public int getNombreArticles() {
        return panier.getNombreArticles();
    }

    public Map<Article, Integer> getArticlesPanier() {
        return panier.getArticles();
    }
}
