package vue;

import controleur.panierControleur;
import modele.Article;
import modele.Client;
import modele.Panier;
import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class panierVue extends JFrame {
    private panierControleur controleur;
    private JPanel articlesPanel;
    private JLabel totalLabel;
    private JButton validerBouton;

    /**
     * Constructeur de la vue du panier.
     *
     * @param client : le client connecté
     * @param panier : le panier associé au client
     */
    public panierVue(Client client, Panier panier) {
        setTitle("Panier - " + client.getNom());
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        this.controleur = new panierControleur(this, panier, client);

        menuVue menu = new menuVue(client, this);
        setJMenuBar(menu.creerMenuBar());

        JPanel header = Header();
        add(header, BorderLayout.NORTH);

        articlesPanel = new JPanel();
        articlesPanel.setLayout(new BoxLayout(articlesPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(articlesPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        add(Boutons(), BorderLayout.SOUTH);

        actualiserAffichage();
        setVisible(true);
    }

    /**
     * Crée le header du panier.
     *
     * @return JPanel du header
     */
    private JPanel Header() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(50, 205, 50));
        header.setPreferredSize(new Dimension(0, 80));

        JLabel titre = new JLabel("Votre panier", SwingConstants.CENTER);
        titre.setForeground(Color.WHITE);
        titre.setFont(new Font("SansSerif", Font.BOLD, 24));
        header.add(titre, BorderLayout.CENTER);

        totalLabel = new JLabel("", SwingConstants.RIGHT);
        totalLabel.setForeground(Color.WHITE);
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        header.add(totalLabel, BorderLayout.SOUTH);

        return header;
    }

    /**
     * Crée les boutons d'action en bas du panier (vider, valider).
     *
     * @return JPanel des boutons
     */
    private JPanel Boutons() {
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        buttonsPanel.setBackground(Color.WHITE);

        JButton viderBouton = creerBouton("Vider le panier", new Color(220, 20, 60));
        viderBouton.addActionListener(e -> controleur.viderPanier());

        validerBouton = creerBouton("Valider la commande", new Color(30, 144, 255));
        validerBouton.addActionListener(e -> controleur.validerCommande());

        buttonsPanel.add(viderBouton);
        buttonsPanel.add(validerBouton);

        return buttonsPanel;
    }

    /**
     * Crée un bouton personnalisé.
     *
     * @param texte : texte du bouton
     * @param couleur : couleur du bouton
     * @return JButton créé
     */
    private JButton creerBouton(String texte, Color couleur) {
        JButton bouton = new JButton(texte);
        bouton.setBackground(couleur);
        bouton.setForeground(Color.WHITE);
        bouton.setFont(new Font("SansSerif", Font.BOLD, 16));
        bouton.setPreferredSize(new Dimension(180, 40));
        bouton.setFocusPainted(false);
        bouton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return bouton;
    }

    /**
     * Affiche un message indiquant que le panier est vide.
     */
    private void afficherPanierVide() {
        JLabel emptyLabel = new JLabel("Votre panier est vide.", SwingConstants.CENTER);
        emptyLabel.setFont(new Font("SansSerif", Font.ITALIC, 18));
        emptyLabel.setForeground(Color.GRAY);
        emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        articlesPanel.add(emptyLabel);
        validerBouton.setEnabled(false);
    }

    /**
     * Affiche les articles présents dans le panier.
     */
    private void afficherArticles() {
        for (Map.Entry<Article, Integer> entry : controleur.getArticlesPanier().entrySet()) {
            articlesPanel.add(articlePanel(entry.getKey(), entry.getValue()));
        }
    }

    /**
     * Crée un panneau pour un article du panier.
     *
     * @param article : article affiché
     * @param quantite : quantité de l'article
     * @return JPanel représentant l'article
     */
    private JPanel articlePanel(Article article, int quantite) {
        JPanel itemPanel = new JPanel(new BorderLayout(10, 10));
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        itemPanel.add(composantImage(article), BorderLayout.WEST);

        itemPanel.add(Details(article, quantite), BorderLayout.CENTER);

        itemPanel.add(boutonSuppression(article), BorderLayout.EAST);

        return itemPanel;
    }

    /**
     * Crée un composant affichant l'image de l'article.
     *
     * @param article : article concerné
     * @return JLabel contenant l'image
     */
    private JLabel composantImage(Article article) {
        ImageIcon originalIcon = new ImageIcon("images/" + article.getImage());
        Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(scaledImage));
    }

    /**
     * Crée un panneau contenant les détails de l'article.
     *
     * @param article : article affiché
     * @param quantite : quantité de l'article
     * @return JPanel des détails
     */
    private JPanel Details(Article article, int quantite) {
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(Color.WHITE);

        JLabel nomLabel = new JLabel(article.getNom());
        nomLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        detailsPanel.add(nomLabel);

        JLabel prixLabel = new JLabel();
        prixLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        detailsPanel.add(prixLabel);

        JLabel stockLabel = new JLabel();
        stockLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        detailsPanel.add(stockLabel);

        mettreAJourLabels(article, quantite, prixLabel, stockLabel);

        detailsPanel.add(Quantite(article, quantite, prixLabel, stockLabel));

        return detailsPanel;
    }

    /**
     * Crée les boutons "+" et "-" pour modifier la quantité.
     *
     * @param article : article concerné
     * @param quantite : quantité actuelle
     * @param prixLabel : label du prix
     * @param stockLabel : label du stock
     * @return JPanel avec les boutons de quantité
     */
    private JPanel Quantite(Article article, int quantite, JLabel prixLabel, JLabel stockLabel) {
        JPanel quantitePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        quantitePanel.setBackground(Color.WHITE);

        JLabel quantiteLabel = new JLabel(String.valueOf(quantite));
        quantiteLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        quantiteLabel.setPreferredSize(new Dimension(30, 20));

        JButton moinsButton = new JButton("-");
        moinsButton.addActionListener(e -> {
            controleur.modifierQuantite(article, -1);
            quantiteLabel.setText(String.valueOf(controleur.getQuantiteArticle(article)));
            mettreAJourLabels(article, controleur.getQuantiteArticle(article), prixLabel, stockLabel);
            actualiserTotal();
        });
        quantitePanel.add(moinsButton);

        quantitePanel.add(quantiteLabel);

        JButton plusButton = new JButton("+");
        plusButton.addActionListener(e -> {
            controleur.modifierQuantite(article, 1);
            quantiteLabel.setText(String.valueOf(controleur.getQuantiteArticle(article)));
            mettreAJourLabels(article, controleur.getQuantiteArticle(article), prixLabel, stockLabel);
            actualiserTotal();
        });
        quantitePanel.add(plusButton);

        return quantitePanel;
    }

    /**
     * Crée un bouton permettant de supprimer un article du panier.
     *
     * @param article : article à supprimer
     * @return JButton supprimer
     */
    private JButton boutonSuppression(Article article) {
        JButton supprimerButton = new JButton("Supprimer");
        supprimerButton.addActionListener(e -> controleur.supprimerArticle(article));
        return supprimerButton;
    }

    /**
     * Met à jour l'affichage du total du panier.
     */
    public void actualiserTotal() {
        totalLabel.setText(String.format("Total: %.2f € (%d articles)",
                controleur.getTotalPanier(),
                controleur.getNombreArticles()));
    }

    /**
     * Affiche un message d'information.
     *
     * @param message : message à afficher
     */
    public void afficherMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    /**
     * Actualise tout l'affichage du panier.
     */
    public void actualiserAffichage() {
        articlesPanel.removeAll();

        if (controleur.getPanier().estVide()) {
            afficherPanierVide();
        } else {
            afficherArticles();
        }

        actualiserTotal();
        articlesPanel.revalidate();
        articlesPanel.repaint();
    }

    /**
     * Met à jour les labels de prix et de stock après une modification.
     *
     * @param article : article modifié
     * @param quantite : quantité actuelle
     * @param prixLabel : label affichant le prix
     * @param stockLabel : label affichant le stock
     */
    //on met à jour les quantités et prix en fonction de ce qui est ajouté/supprimé via la vue du panier
    private void mettreAJourLabels(Article article, int quantite, JLabel prixLabel, JLabel stockLabel) {
        prixLabel.setText(String.format("Prix: %.2f €", article.calculerPrix(quantite)));
        stockLabel.setText("Stock disponible: " + (article.getQuantite() - quantite));
    }

    /**
     * Affiche une erreur dans une boîte de dialogue.
     *
     * @param message : message d'erreur
     */
    public void afficherErreur(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}