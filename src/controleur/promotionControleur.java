package controleur;

import dao.articleDaoImpl;
import modele.Article;
import vue.promotionVue;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class promotionControleur {

    private promotionVue vue;
    private articleDaoImpl articleDao;

    /**
     * Constructeur du contrôleur de promotion.
     *
     * Associe les boutons "Appliquer" de chaque article avec leur champ de réduction respectif.
     * Lorsque l'utilisateur clique sur un bouton, la réduction saisie est appliquée à l'article
     * après validation du pourcentage.
     *
     * @param vue La vue promotion associée.
     * @param articleDao L'accès aux articles pour appliquer les réductions.
     * @param boutonsAppliquer La liste des boutons "Appliquer" pour chaque article.
     * @param champsPromo La liste des champs texte correspondant aux réductions pour chaque article.
     */

    public promotionControleur(promotionVue vue, articleDaoImpl articleDao, Map<Article, JButton> boutonsAppliquer, Map<Article, JTextField> champsPromo) {
        this.vue = vue;
        this.articleDao = articleDao;

        for (Map.Entry<Article, JButton> entry : boutonsAppliquer.entrySet()) {
            Article article = entry.getKey();
            JButton bouton = entry.getValue();
            JTextField champ = champsPromo.get(article);

            //on applique les réductions
            bouton.addActionListener(new ActionListener() {
                @Override

                /**
                 * Gère l'action de l'utilisateur pour appliquer une réduction sur un article.
                 * Vérifie que la réduction saisie est un pourcentage valide (entre 0 et 100).
                 * Applique la réduction à l'article via le DAO si la saisie est correcte,
                 * sinon affiche un message d'erreur.
                 *
                 * @param e L'événement déclenché par l'action utilisateur.
                 */

                public void actionPerformed(ActionEvent e) {
                    try {
                        double reduction = Double.parseDouble(champ.getText());
                        // on regarde si le pourcentage sélectionné est correct
                        if (reduction < 0 || reduction > 100) {
                            JOptionPane.showMessageDialog(vue, "La réduction doit être entre 0 et 100 %", "Erreur", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        articleDao.appliquerReduction(article.getId(), reduction);
                        JOptionPane.showMessageDialog(vue, "Réduction appliquée à l'article : " + article.getNom(), "Succès", JOptionPane.INFORMATION_MESSAGE);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(vue, "Veuillez entrer une réduction valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }

            });
        }
    }
}
