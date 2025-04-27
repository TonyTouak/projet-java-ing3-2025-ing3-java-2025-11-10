package controleur;

import dao.*;
import modele.Article;
import vue.*;
import modele.Administrateur;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class adminControleur implements ActionListener {

    private adminVue vue;
    private articleDaoImpl articleDao;
    private clientDaoImpl clientDao;
    private statistiqueDaoImpl statDao;
    private commandeDaoImpl commandeDao;
    private articleCommandeDaoImpl articleCommandeDao;
    private Administrateur admin;

    /**
     * Constructeur du contrôleur d'administration.
     *
     * Initialise le contrôleur avec la vue admin et les différents DAO nécessaires à la gestion
     * des articles, clients, statistiques et commandes.
     *
     * @param vue La vue de l'administrateur.
     * @param articleDao Le DAO pour gérer les articles.
     * @param clientDao Le DAO pour gérer les clients.
     * @param statDao Le DAO pour gérer les statistiques.
     * @param commandeDao Le DAO pour gérer les commandes.
     * @param articleCommandeDao Le DAO pour gérer les relations article-commande.
     * @param admin L'administrateur actuellement connecté.
     */

    public adminControleur(adminVue vue, articleDaoImpl articleDao, clientDaoImpl clientDao, statistiqueDaoImpl statDao, commandeDaoImpl commandeDao, articleCommandeDaoImpl articleCommandeDao, Administrateur admin) {
        this.vue = vue;
        this.articleDao = articleDao;
        this.clientDao = clientDao;
        this.statDao = statDao;
        this.commandeDao = commandeDao;
        this.articleCommandeDao = articleCommandeDao;
        this.admin = admin;
        initControl();
    }

    /**
     * Initialise les contrôleurs d'événements pour les boutons de la vue administrateur.
     */
    private void initControl() {
        vue.getBtnInventaire().addActionListener(this);
        vue.getBtnPromotions().addActionListener(this);
        vue.getBtnClients().addActionListener(this);
        vue.getBtnStatistiques().addActionListener(this);
        vue.getBtnDeconnexion().addActionListener(this);
    }

    /**
     * Réagit aux actions de l'administrateur sur les différents boutons,
     * et ouvre la vue correspondante selon le bouton cliqué.
     *
     * @param e événement déclenché par l'action utilisateur
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();

        if (source == vue.getBtnInventaire()) {
            new inventaireVue(articleDao, commandeDao, articleCommandeDao);
        } else if (source == vue.getBtnPromotions()) {
            List<Article> articles = articleDao.getAll();
            new promotionVue(articles, articleDao);
        } else if (source == vue.getBtnClients()) {
            new gestionClientVue(clientDao);
        } else if (source == vue.getBtnStatistiques()) {
            new statVue(statDao);
        } else if (source == vue.getBtnDeconnexion()) {
            vue.dispose();
            new loginVue();
        }
    }
}
