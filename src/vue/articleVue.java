package vue;

import dao.DaoFactory;
import dao.articleDaoImpl;
import modele.Article;
import modele.Client;
import modele.Panier;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

public class articleVue extends JFrame {
    private JComboBox<String> tailleComboBox;
    private JLabel quantiteDisponible;
    private JLabel prixVrac;
    private JSpinner quantite;
    private Article articleCourant;
    private List<Article> attributs;
    private articleDaoImpl dao;
    private JLabel panierInfo;
    private JPanel prixPanel;

    /**
     * Initialise la vue d'un article avec les détails (prix, taille, promo).
     *
     * @param article : l'article sélectionné
     * @param client : le client actuel
     */
    public articleVue(Article article, Client client) {
        this.dao = new articleDaoImpl(DaoFactory.getInstance("shopping", "root", ""));

        this.attributs = dao.getVariantesParArticle(
                article.getMarque(),
                article.getType(),
                article.getSexe(),
                article.getNom()
        );

        if (attributs == null || attributs.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Aucune variante disponible pour ce produit",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            this.dispose();
            return;
        }

        this.articleCourant = attributs.stream()
                .filter(a -> a.getId() == article.getId())
                .findFirst()
                .orElse(attributs.get(0));

        if (this.articleCourant == null) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement du produit",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            this.dispose();
            return;
        }

        initialisation(client);
    }

    /**
     * Initialise l'interface graphique de la vue article.
     *
     * @param client : le client actuel
     */
    private void initialisation(Client client) {
        setTitle("Détails du produit");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        menuVue menu = new menuVue(client, this);
        setJMenuBar(menu.creerMenuBar());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(30, 144, 255));
        header.setPreferredSize(new Dimension(0, 80));
        JLabel titre = new JLabel("Détails du produit", SwingConstants.CENTER);
        titre.setForeground(Color.WHITE);
        titre.setFont(new Font("SansSerif", Font.BOLD, 26));
        header.add(titre, BorderLayout.CENTER);

        panierInfo = new JLabel("Panier : 0 articles - 0.00 €");
        panierInfo.setForeground(Color.WHITE);
        panierInfo.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.add(panierInfo, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setPreferredSize(new Dimension(400, 0));
        imagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        ImageIcon originalIcon = new ImageIcon("images/" + articleCourant.getImage());
        Image scaledImage = originalIcon.getImage().getScaledInstance(380, 380, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage), SwingConstants.CENTER);
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nomLabel = new JLabel(articleCourant.getNom());
        nomLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        nomLabel.setForeground(new Color(0, 102, 204));
        nomLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(nomLabel);
        detailsPanel.add(Box.createVerticalStrut(20));

        JPanel taillePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        taillePanel.setBackground(Color.WHITE);
        taillePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        taillePanel.setMaximumSize(new Dimension(400, 30));

        JLabel tailleLabel = new JLabel("Taille :");
        tailleLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        tailleLabel.setPreferredSize(new Dimension(70, 25));
        taillePanel.add(tailleLabel);

        List<String> taillesDisponibles = attributs.stream()
                .map(Article::getTaille)
                .collect(Collectors.toList());

        tailleComboBox = new JComboBox<>(taillesDisponibles.toArray(new String[0]));
        tailleComboBox.setSelectedItem(articleCourant.getTaille());
        tailleComboBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tailleComboBox.setPreferredSize(new Dimension(100, 25));
        tailleComboBox.addActionListener(e -> {
            String tailleSelectionnee = (String) tailleComboBox.getSelectedItem();
            attributs.stream()
                    .filter(a -> a.getTaille().equals(tailleSelectionnee))
                    .findFirst()
                    .ifPresent(this::mettreAJourArticle);
        });
        taillePanel.add(tailleComboBox);
        detailsPanel.add(taillePanel);
        detailsPanel.add(Box.createVerticalStrut(15));

        quantiteDisponible = new JLabel();
        quantiteDisponible.setFont(new Font("SansSerif", Font.PLAIN, 16));
        quantiteDisponible.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(quantiteDisponible);
        detailsPanel.add(Box.createVerticalStrut(15));

        prixPanel = new JPanel();
        prixPanel.setLayout(new BoxLayout(prixPanel, BoxLayout.Y_AXIS));
        prixPanel.setBackground(Color.WHITE);
        prixPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel prixUnitairePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        prixUnitairePanel.setBackground(Color.WHITE);
        prixUnitairePanel.setMaximumSize(new Dimension(400, 30));

        JLabel prixUnitaireLabel = new JLabel("Prix unitaire :");
        prixUnitaireLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        prixUnitaireLabel.setPreferredSize(new Dimension(100, 25));
        prixUnitairePanel.add(prixUnitaireLabel);

        JLabel prixUnitaireValue = new JLabel(new DecimalFormat("0.00 €").format(articleCourant.getPrixUnique()));
        prixUnitaireValue.setFont(new Font("SansSerif", Font.BOLD, 16));
        prixUnitaireValue.setForeground(new Color(220, 20, 60));
        prixUnitairePanel.add(prixUnitaireValue);
        prixPanel.add(prixUnitairePanel);

        //on affiche la réduction pour les articles concernés
        if (articleCourant.getReduction() > 0) {
            JPanel reductionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            reductionPanel.setBackground(Color.WHITE);
            reductionPanel.setMaximumSize(new Dimension(400, 30));

            JLabel reductionLabel = new JLabel("Réduction :");
            reductionLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
            reductionLabel.setPreferredSize(new Dimension(100, 25));
            reductionPanel.add(reductionLabel);

            JLabel reductionValue = new JLabel("-" + new DecimalFormat("0.00").format(articleCourant.getReduction()) + " %");
            reductionValue.setFont(new Font("SansSerif", Font.BOLD, 16));
            reductionValue.setForeground(new Color(0, 150, 0));
            reductionPanel.add(reductionValue);

            prixPanel.add(reductionPanel);

            prixPanel.add(reductionPanel);

            float prixReduitUnitaire = articleCourant.calculerPrix(1);

            JPanel prixReduitPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            prixReduitPanel.setBackground(Color.WHITE);
            prixReduitPanel.setMaximumSize(new Dimension(400, 30));

            JLabel prixReduitLabel = new JLabel("Prix après réduction :");
            prixReduitLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
            prixReduitLabel.setPreferredSize(new Dimension(150, 25));
            prixReduitPanel.add(prixReduitLabel);

            JLabel prixReduitValue = new JLabel(String.format("%.2f €", prixReduitUnitaire));
            prixReduitValue.setFont(new Font("SansSerif", Font.BOLD, 16));
            prixReduitValue.setForeground(new Color(34, 139, 34));
            prixReduitPanel.add(prixReduitValue);

            prixPanel.add(prixReduitPanel);



        }


        //Le prix en Vrac n'est ajouté que pour les articles concernés
        prixVrac = new JLabel();
        prixVrac.setFont(new Font("SansSerif", Font.PLAIN, 16));
        prixVrac.setForeground(new Color(0, 100, 0));

        detailsPanel.add(prixPanel);
        detailsPanel.add(Box.createVerticalStrut(15));

        JPanel quantitePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        quantitePanel.setBackground(Color.WHITE);
        quantitePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        quantitePanel.setMaximumSize(new Dimension(400, 40));

        JLabel quantiteTextLabel = new JLabel("Quantité :");
        quantiteTextLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        quantiteTextLabel.setPreferredSize(new Dimension(70, 25));
        quantitePanel.add(quantiteTextLabel);

        quantite = new JSpinner();
        quantite.setFont(new Font("SansSerif", Font.PLAIN, 16));
        quantite.setPreferredSize(new Dimension(80, 25));
        quantitePanel.add(quantite);

        detailsPanel.add(quantitePanel);
        detailsPanel.add(Box.createVerticalStrut(30));

        JButton ajouterPanierButton = styleBouton("Ajouter au panier", new Color(50, 205, 50));
        ajouterPanierButton.addActionListener(e -> ajouterAuPanier());
        ajouterPanierButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(ajouterPanierButton);

        JButton retourButton = new JButton("Retour");
        retourButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        retourButton.setBackground(new Color(30, 144, 255));
        retourButton.setForeground(Color.WHITE);
        retourButton.setFocusPainted(false);
        retourButton.addActionListener(e -> {
            dispose();
            //on revient à la vue adéquat suivant le sexe de l'article
            if (articleCourant.getSexe().equalsIgnoreCase("Homme")) {
                new magasinHommeVue(client);
            } else {
                new magasinFemmeVue(client);
            }
        });
        JPanel retourPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        retourPanel.setBackground(Color.WHITE);
        retourPanel.add(retourButton);
        detailsPanel.add(Box.createVerticalGlue());
        detailsPanel.add(retourPanel);


        mettreAJourArticle(articleCourant);

        mainPanel.add(imagePanel, BorderLayout.WEST);
        mainPanel.add(detailsPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    /**
     * Met à jour l'affichage des informations d'un article sélectionné
     *
     * @param article : l'article sélectionné
     */
    private void mettreAJourArticle(Article article) {
        this.articleCourant = article;

        Panier panier = Panier.getInstance();
        int quantiteDansPanier = panier.getQuantite(article);
        int stockRestant = article.getQuantite() - quantiteDansPanier;

        boolean disponible = stockRestant > 0;
        //on regarde s'il reste encore du stock sur l'article
        quantiteDisponible.setText(disponible ?
                "Quantité disponible : " + stockRestant :
                "RUPTURE DE STOCK");
        quantiteDisponible.setForeground(disponible ? Color.BLACK : Color.RED);

        // on utilise un spinner pour que le client ne puisse sélectionner qu'entre 1 et la quantité maximale disponible de l'article
        quantite.setModel(new SpinnerNumberModel(
                1,
                1,
                Math.max(stockRestant, 1),
                1));
        quantite.setEnabled(disponible);
    }

    /**
     * Met à jour l'affichage du nombre d'articles et du total dans le panier.
     */
    public void mettreAJourAffichagePanier() {
        Panier panier = Panier.getInstance();
        panierInfo.setText("Panier : " + panier.getNombreArticles() + " articles - "
                + String.format("%.2f", panier.getTotal()) + " €");
    }

    /**
     * Retourne la quantité sélectionnée.
     *
     * @return : la quantité choisie
     */
    public int getQuantiteSelectionnee() {
        return (int) quantite.getValue();
    }

    /**
     * Ajoute l'article sélectionné au panier en prenant en compte du stock restant.
     */
    //on envoie au panier l'article
    private void ajouterAuPanier() {
        int quantite_panier = (int) quantite.getValue();

        Panier panier = Panier.getInstance();
        int quantiteDejaDansPanier = panier.getQuantite(articleCourant);
        int stockRestant = articleCourant.getQuantite() - quantiteDejaDansPanier;

        if (quantite_panier > stockRestant) {
            JOptionPane.showMessageDialog(this,
                    "Quantité indisponible. Stock restant: " + stockRestant,
                    "Erreur de stock",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        double prixTotal = articleCourant.calculerPrix(quantite_panier);
        panier.ajouterArticle(articleCourant, quantite_panier, prixTotal);

        //on met à jour le stock
        mettreAJourArticle(articleCourant);
        mettreAJourAffichagePanier();

        String message;
        //on affiche le prix détaillé pour les articles en vrac
        if (articleCourant.getQuantiteVrac() > 1 && quantite_panier >= articleCourant.getQuantiteVrac()) {
            int paquets = quantite_panier / articleCourant.getQuantiteVrac();
            int restant = quantite_panier % articleCourant.getQuantiteVrac();
            message = String.format(
                    "%d x %s ajouté(s) au panier\n" +
                            "(%d paquet(s) de %d à %.2f € + %d unité(s) à %.2f €)\n" +
                            "Total: %.2f €",
                    quantite_panier, articleCourant.getNom(),
                    paquets, articleCourant.getQuantiteVrac(), articleCourant.getPrixVrac(),
                    restant, articleCourant.getPrixUnique(),
                    prixTotal
            );
        } else {
            message = String.format(
                    "%d x %s ajouté(s) au panier\nPrix total: %.2f €",
                    quantite_panier, articleCourant.getNom(), prixTotal
            );
        }

        JOptionPane.showMessageDialog(this, message, "Article ajouté", JOptionPane.INFORMATION_MESSAGE);
    }


    private JButton styleBouton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 18));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 50));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}