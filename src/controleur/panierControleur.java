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

    private void lancerProcessusPaiement() {
        float montantTotal = panier.getTotal();

        // on utilise une fonction de rappel pour mettre à jour l'affichage en cas de paiement valide
        paiementVue vuePaiement = new paiementVue(client, panier, montantTotal, (success) -> {
            if (success) {
                commandeDao.finaliserCommande(client, panier, articleCmdDao, articleDao);
                panier.vider();
                mettreAJourVue();
            }
        });



        vuePaiement.setVisible(true);
    }


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

    public void supprimerArticle(Article article) {
        panier.supprimerArticle(article);
        mettreAJourVue();
    }

    public int getQuantiteArticle(Article article) {
        return panier.getQuantite(article);
    }


    public void viderPanier() {
        panier.vider();
        mettreAJourVue();
    }

    private boolean verifierStocks() {
        for (Map.Entry<Article, Integer> entry : panier.getArticles().entrySet()) {
            if (entry.getValue() > entry.getKey().getQuantite()) {
                return false;
            }
        }
        return true;
    }

    private void mettreAJourVue() {
        vue.actualiserAffichage();
        vue.actualiserTotal();
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
