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

    private void initControl() {
        vue.getBtnInventaire().addActionListener(this);
        vue.getBtnPromotions().addActionListener(this);
        vue.getBtnClients().addActionListener(this);
        vue.getBtnStatistiques().addActionListener(this);
        vue.getBtnDeconnexion().addActionListener(this);
    }

    //on affiche la vue adéquate en fonction du choix de l'admin
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();

        if (source == vue.getBtnInventaire()) {
            new inventaireVue(articleDao,commandeDao,articleCommandeDao);
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
