package vue;

import dao.*;
import modele.Article;
import modele.Commande;
import modele.Client;
import modele.ArticleCommande;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.util.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class inventaireVue extends JFrame {
    private articleDaoImpl articleDao;
    private commandeDaoImpl commandeDao;
    private articleCommandeDaoImpl articleCommandeDao;
    private JTable tableCommandes;
    private DefaultTableModel modelCommandes;
    private JPanel panelDetails;
    private JLabel headerDetailsLabel;

    /**
     * Initialise la vue de gestion des commandes.
     *
     * @param articleDao : DAO pour les articles
     * @param commandeDao : DAO pour les commandes
     * @param articleCommandeDao : DAO pour les relations articles-commandes
     */
    public inventaireVue(articleDaoImpl articleDao, commandeDaoImpl commandeDao, articleCommandeDaoImpl articleCommandeDao) {
        this.articleDao = articleDao;
        this.commandeDao = commandeDao;
        this.articleCommandeDao = articleCommandeDao;
        initialisationAffichage();
        chargerCommandes();
        setVisible(true);
    }

    /**
     * Initialise l'interface graphique générale.
     */
    private void initialisationAffichage() {
        setTitle("Gestion des Commandes");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));

        JPanel header = Header();
        add(header, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(250);
        splitPane.setResizeWeight(0.4);

        JPanel panelCommandes = Commandes();
        splitPane.setTopComponent(panelCommandes);

        panelDetails = new JPanel();
        panelDetails.setLayout(new BoxLayout(panelDetails, BoxLayout.Y_AXIS));
        panelDetails.setBackground(new Color(248, 248, 248));
        panelDetails.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JPanel headerDetailsPanel = new JPanel(new BorderLayout());
        headerDetailsPanel.setBackground(new Color(230, 240, 250));
        headerDetailsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        headerDetailsLabel = new JLabel();
        headerDetailsLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        headerDetailsPanel.add(headerDetailsLabel, BorderLayout.CENTER);

        panelDetails.add(headerDetailsPanel);
        panelDetails.add(Box.createVerticalStrut(15));

        JScrollPane scrollDetails = new JScrollPane(panelDetails);
        scrollDetails.setBorder(null);
        splitPane.setBottomComponent(scrollDetails);

        add(splitPane, BorderLayout.CENTER);

        JPanel panelBoutons = Boutons();
        add(panelBoutons, BorderLayout.SOUTH);
    }

    private JPanel Header() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(30, 144, 255));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titre = new JLabel("Gestion des Commandes", SwingConstants.CENTER);
        titre.setForeground(Color.WHITE);
        titre.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.add(titre, BorderLayout.CENTER);

        JButton btnFermer = new JButton("Fermer");
        btnFermer.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnFermer.setBackground(new Color(70, 130, 180));
        btnFermer.setForeground(Color.WHITE);
        btnFermer.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnFermer.addActionListener(e -> dispose());
        header.add(btnFermer, BorderLayout.EAST);

        return header;
    }

    /**
     * Crée le panneau affichant la liste des commandes.
     *
     * @return : un JPanel contenant le tableau des commandes
     */
    private JPanel Commandes() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] colonnesCommandes = {"ID Commande", "Client", "Date", "Total"};

        modelCommandes = new DefaultTableModel(colonnesCommandes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableCommandes = new JTable(modelCommandes);
        Table(tableCommandes);

        tableCommandes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                afficherDetailsCommande();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableCommandes);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void Table(JTable table) {
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 45));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));
                if (isSelected) {
                    c.setBackground(new Color(220, 230, 240));
                    c.setForeground(Color.BLACK);
                } else {
                    c.setForeground(Color.BLACK);
                }
                setBorder(noFocusBorder);
                return c;
            }
        });
    }

    /**
     * Crée le panneau contenant les boutons d'action sur les commandes.
     *
     * @return : un JPanel contenant les boutons
     */
    private JPanel Boutons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));
        panel.setBackground(Color.WHITE);



        JButton btnAjouter = styleBouton("Ajouter", new Color(52, 152, 219));
        JButton btnModifier = styleBouton("Modifier", new Color(155, 89, 182));
        JButton btnSupprimer = styleBouton("Supprimer", new Color(231, 76, 60));




        btnAjouter.addActionListener(e -> ajouterCommande());
        btnModifier.addActionListener(e -> modifierCommande());
        btnSupprimer.addActionListener(e -> supprimerCommande());


        panel.add(btnAjouter);
        panel.add(btnModifier);
        panel.add(btnSupprimer);


        return panel;
    }

    private JButton styleBouton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return button;
    }

    /**
     * Charge toutes les commandes depuis la base de données.
     */
    private void chargerCommandes() {
        // on utilise un SwingWorker pour charger les commandes sans affecter l'interface
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            //on redéfinit donc doInBackground qui comme son nom l'indique effectue des tâches en fond
            @Override
            protected Void doInBackground() {
                try {
                    List<Commande> commandes = commandeDao.getAll();
                    modelCommandes.setRowCount(0);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                    for (Commande commande : commandes) {
                        modelCommandes.addRow(new Object[]{
                                commande.getId(),
                                commande.getClient().getNom(),
                                dateFormat.format(commande.getDate()),
                                String.format("%.2f €", commande.getPrix())
                        });
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        worker.execute();
    }

    /**
     * Affiche les détails d'une commande sélectionnée.
     */
    private void afficherDetailsCommande() {
        for (int i = panelDetails.getComponentCount() - 1; i >= 1; i--) {
            panelDetails.remove(i);
        }

        int row = tableCommandes.getSelectedRow();
        //on vérifie qu'une commande a bien été sélectionnée par l'utilisateur
        if (row == -1) {
            headerDetailsLabel.setText("Aucune commande sélectionnée");
            panelDetails.revalidate();
            panelDetails.repaint();
            return;
        }

        int idCommande = (int) modelCommandes.getValueAt(row, 0);
        try {
            //on récupère tous les détails de la commande via son ID pour ensuite les afficher
            Commande commande = commandeDao.chercher(idCommande);
            List<ArticleCommande> articlesCommandes = articleCommandeDao.getArticlesParCommande(idCommande);

            headerDetailsLabel.setText(String.format(
                    "Commande #%d - %s • %s • Total: %.2f € • %d article%s",
                    commande.getId(),
                    commande.getClient().getNom(),
                    new SimpleDateFormat("dd/MM/yyyy").format(commande.getDate()),
                    commande.getPrix(),
                    articlesCommandes.size(),
                    articlesCommandes.size() > 1 ? "s" : ""
            ));

            if (articlesCommandes.isEmpty()) {
                JLabel emptyLabel = new JLabel("Aucun article dans cette commande");
                emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                emptyLabel.setForeground(new Color(120, 120, 120));
                emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                panelDetails.add(emptyLabel);
            } else {
                for (ArticleCommande ac : articlesCommandes) {
                    Article article = articleDao.chercher(ac.getIdArticle());
                    if (article != null) {
                        ajouterArticlePanel(article, ac.getQuantite());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Erreur lors du chargement des détails");
            errorLabel.setForeground(Color.RED);
            panelDetails.add(errorLabel);
        }

        panelDetails.revalidate();
        panelDetails.repaint();
    }

    /**
     * Ajoute un panneau pour un article dans les détails de la commande.
     *
     * @param article : l'article concerné
     * @param quantite : la quantité commandée
     */
    private void ajouterArticlePanel(Article article, int quantite) {
        JPanel articlePanel = new JPanel(new BorderLayout());
        articlePanel.setBackground(Color.WHITE);
        articlePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        articlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JPanel imagePanel = new JPanel();
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setPreferredSize(new Dimension(100, 100));

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        String imagePath = "./images/" + article.getImage();
        if (new File(imagePath).exists()) {
            ImageIcon icon = new ImageIcon(imagePath);
            Image scaled = icon.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaled));
        } else {
            imageLabel.setText("No Image");
            imageLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        }
        imagePanel.add(imageLabel);

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 0));

        JLabel nomLabel = new JLabel(
                article.getNom() + " (x" + quantite + ")"
        );
        nomLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel prixLabel = new JLabel(
                String.format("%.2f € (%.2f € l'unité)", article.getPrixUnique() * quantite, article.getPrixUnique())
        );
        prixLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        prixLabel.setForeground(new Color(46, 134, 193));

        JLabel detailsLabel = new JLabel(
                "Marque: " + article.getMarque() + " • Type: " + article.getType() + " • Taille: " + article.getTaille()
        );


        detailsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        detailsLabel.setForeground(new Color(100, 100, 100));

        detailsPanel.add(nomLabel);
        detailsPanel.add(Box.createVerticalStrut(5));
        detailsPanel.add(prixLabel);
        detailsPanel.add(Box.createVerticalStrut(3));
        detailsPanel.add(detailsLabel);

        articlePanel.add(imagePanel, BorderLayout.WEST);
        articlePanel.add(detailsPanel, BorderLayout.CENTER);
        panelDetails.add(articlePanel);
        panelDetails.add(Box.createVerticalStrut(10));
    }


    /**
     * Ouvre la fenêtre pour ajouter une nouvelle commande.
     */
    public void ajouterCommande() {
        JDialog dialog = new JDialog((Frame) null, "Nouvelle Commande", true);
        dialog.setSize(700, 600);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout(10, 10));


        JPanel panelClient = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelClient.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Client",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 12)));

        JComboBox<String> comboClients = new JComboBox<>();
        comboClients.setPreferredSize(new Dimension(300, 25));
        clientDao clientDao = new clientDaoImpl(DaoFactory.getInstance("shopping", "root", ""));
        for (Client client : clientDao.getAll()) {
            comboClients.addItem(client.getIDClient() + " - " + client.getNom());
        }
        panelClient.add(new JLabel("Client : "));
        panelClient.add(comboClients);

        JPanel panelArticles = new JPanel();
        panelArticles.setLayout(new BoxLayout(panelArticles, BoxLayout.Y_AXIS));
        panelArticles.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Articles",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 12)));

        Map<Article, JTextField> champsQuantite = new HashMap<>();
        Map<Article, JLabel> labelsPrix = new HashMap<>();

        for (Article article : articleDao.getAll()) {
            JPanel ligneArticle = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            ligneArticle.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            JLabel nomLabel = new JLabel(article.getNom() + " (Stock: " + article.getQuantite() + ")");
            nomLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            nomLabel.setPreferredSize(new Dimension(300, 30));
            ligneArticle.add(nomLabel);

            ligneArticle.add(new JLabel("Quantité:"));
            JTextField champQuantite = new JTextField(5);
            champQuantite.setHorizontalAlignment(JTextField.RIGHT);

            JLabel prixLabel = new JLabel("Prix: 0.00 €");
            prixLabel.setPreferredSize(new Dimension(100, 30));


            champQuantite.getDocument().addDocumentListener(new DocumentListener() {
                //Méthode appelée en cas de modification du texte
                public void changedUpdate(DocumentEvent e) { updatePrix(); }
                public void removeUpdate(DocumentEvent e) { updatePrix(); }
                public void insertUpdate(DocumentEvent e) { updatePrix(); }

                private void updatePrix() {
                    try {
                        int quantite = Integer.parseInt(champQuantite.getText().trim());
                        if (quantite > 0) {
                            //on calcule le nouveau montant d'un article apres mise à jour de la quantité
                            float prixTotal = article.calculerPrix(quantite);
                            prixLabel.setText(String.format("Prix: %.2f €", prixTotal));
                        } else {
                            prixLabel.setText("Prix: 0.00 €");
                        }
                    } catch (NumberFormatException ex) {
                        prixLabel.setText("Prix: 0.00 €");
                    }
                }
            });

            ligneArticle.add(champQuantite);
            ligneArticle.add(prixLabel);

            champsQuantite.put(article, champQuantite);
            labelsPrix.put(article, prixLabel);
            panelArticles.add(ligneArticle);

            panelArticles.add(new JSeparator(SwingConstants.HORIZONTAL));
        }

        JScrollPane scrollPane = new JScrollPane(panelArticles);
        scrollPane.setPreferredSize(new Dimension(650, 400));

        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnValider = new JButton("Valider");
        btnValider.setFont(new Font("Arial", Font.BOLD, 12));
        btnValider.setBackground(new Color(76, 175, 80));
        btnValider.setForeground(Color.WHITE);
        btnValider.setFocusPainted(false);
        panelBoutons.add(btnValider);

        btnValider.addActionListener(e -> {
            //on récupère le client sélectionné
            String client_texte = (String) comboClients.getSelectedItem();
            if (client_texte == null || client_texte.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Veuillez sélectionner un client", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int idClient = Integer.parseInt(client_texte.split(" - ")[0]);

            List<ArticleCommande> listeArticleCommandes = new ArrayList<>();
            boolean auMoinsUn = false;

            for (Map.Entry<Article, JTextField> entry : champsQuantite.entrySet()) {
                Article article = entry.getKey();
                String txt = entry.getValue().getText().trim();
                if (!txt.isEmpty()) {
                    try {
                        int quantite = Integer.parseInt(txt);
                        if (quantite > 0) {
                            //on vérifie que le stock est suffisant
                            if (quantite > article.getQuantite()) {
                                JOptionPane.showMessageDialog(dialog,
                                        "La quantité demandée pour " + article.getNom() +
                                                " dépasse le stock disponible (" + article.getQuantite() + ")",
                                        "Erreur de stock", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            listeArticleCommandes.add(new ArticleCommande(article.getId(), -1, quantite, article.calculerPrix(quantite)));
                            auMoinsUn = true;
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(dialog,
                                "Quantité invalide pour " + article.getNom(),
                                "Erreur", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }

            if (!auMoinsUn) {
                JOptionPane.showMessageDialog(dialog,
                        "Aucun article sélectionné avec une quantité valide.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Commande nouvelleCommande = new Commande();
            Client client = new Client();
            client.setIDClient(idClient);
            System.out.println(idClient);

            Client nouveauclient = clientDao.chercherIDCLient(client.getIDClient());

            nouvelleCommande.setClient(nouveauclient);
            nouvelleCommande.setDate(new Date());

            //on calcule le prix total de la commande
            float prixTotal = 0;
            for (ArticleCommande ac : listeArticleCommandes) {
                Article article = articleDao.chercher(ac.getIdArticle());
                if (article != null) {
                    prixTotal += article.calculerPrix(ac.getQuantite());
                }
            }
            nouvelleCommande.setPrix(prixTotal);

            try {
                int idCommande = commandeDao.ajouter(nouvelleCommande);
                nouvelleCommande.setId(idCommande);


                for (ArticleCommande ac : listeArticleCommandes) {
                    ac.setIdCommande(idCommande);
                    DaoFactory daoFactory = DaoFactory.getInstance("shopping", "root", "");
                    Connection conn = daoFactory.getConnexion();

                    articleCommandeDao.ajouterLigneCommande(conn,ac);

                    //on met à jour le stock
                    Article article = articleDao.chercher(ac.getIdArticle());
                    if (article != null) {
                        article.setQuantite(article.getQuantite() - ac.getQuantite());
                        articleDao.mettreAJour(article);
                    }
                }
                dialog.dispose();
                //on rafraîchit la commande avec la commande ajoutée
                chargerCommandes();
            } catch (SQLException e2) {
                e2.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Erreur lors de la commande : " + e2.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }

        });

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.add(panelClient, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(panelBoutons, BorderLayout.SOUTH);

        dialog.add(contentPanel);
        dialog.setVisible(true);
    }



    /**
     * Permet de modifier une commande existante.
     */
    private void modifierCommande() {
        int row = tableCommandes.getSelectedRow();
        //on affiche un message si aucune commande n'a été sélectionnée par l'administrateur
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner une commande à modifier",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            //on récupère l'id correspondant à la commande sélectionnée
            int idCommande = (int) tableCommandes.getValueAt(row, 0);
            Commande commande = commandeDao.chercher(idCommande);

            if (commande == null) {
                JOptionPane.showMessageDialog(this, "Commande introuvable.");
                return;
            }

            JDialog dialog = new JDialog((Frame) null, "Modifier Commande #" + idCommande, true);
            dialog.setSize(500, 400);
            dialog.setLocationRelativeTo(null);
            dialog.setLayout(new BorderLayout(10, 10));

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            articleCommandeDao articleCmdDao = new articleCommandeDaoImpl(DaoFactory.getInstance("shopping", "root", ""));
            articleDao articleDao = new articleDaoImpl(DaoFactory.getInstance("shopping", "root", ""));

            List<ArticleCommande> lignesOriginales = articleCmdDao.getArticlesParCommande(idCommande);

            List<ArticleCommande> lignesModifiees = new ArrayList<>();
            //on crée une copie de notre liste pour pouvoir effectuer des modifications
            for (ArticleCommande ac : lignesOriginales) {
                ArticleCommande nouvelleLigne = new ArticleCommande(ac.getIdArticle(), ac.getIdCommande(),
                        ac.getQuantite(), ac.getPrix());
                lignesModifiees.add(nouvelleLigne);
            }

            //on passe nos ArticleCommande en composant graphique pour les afficher
            Map<ArticleCommande, JTextField> quantiteFields = new HashMap<>();
            Map<ArticleCommande, JLabel> stockLabels = new HashMap<>();

            //on met à jour l'affichage
            Runnable refreshInterface = () -> {
                mainPanel.removeAll();

                for (ArticleCommande ac : lignesModifiees) {
                    Article article = articleDao.chercher(ac.getIdArticle());
                    if (article == null) continue;

                    JPanel lignePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
                    lignePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

                    JLabel stockLabel = new JLabel(article.getNom() + " (Stock: " + article.getQuantite() + ")");
                    stockLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
                    stockLabel.setPreferredSize(new Dimension(250, 30));
                    lignePanel.add(stockLabel);

                    lignePanel.add(new JLabel("Quantité:"));
                    JTextField quantiteField = new JTextField(String.valueOf(ac.getQuantite()), 5);
                    quantiteField.setHorizontalAlignment(JTextField.RIGHT);
                    lignePanel.add(quantiteField);

                    float prixTotal = article.calculerPrix(ac.getQuantite());
                    JLabel prixLabel = new JLabel(String.format("Prix: %.2f €", prixTotal));
                    lignePanel.add(prixLabel);

                    //comme pour l'ajout d'une commande, on modifie de manière dynamique le prix
                    quantiteField.getDocument().addDocumentListener(new DocumentListener() {
                        public void changedUpdate(DocumentEvent e) { update(); }
                        public void removeUpdate(DocumentEvent e) { update(); }
                        public void insertUpdate(DocumentEvent e) { update(); }

                        private void update() {
                            try {
                                int quantite = Integer.parseInt(quantiteField.getText().trim());
                                if (quantite > 0) {
                                    float nouveauPrix = article.calculerPrix(quantite);
                                    prixLabel.setText(String.format("Prix: %.2f €", nouveauPrix));
                                } else {
                                    prixLabel.setText("Prix: 0.00 €");
                                }
                            } catch (NumberFormatException ex) {
                                prixLabel.setText("Prix: 0.00 €");
                            }
                        }
                    });

                    quantiteFields.put(ac, quantiteField);
                    stockLabels.put(ac, stockLabel);
                    mainPanel.add(lignePanel);
                    mainPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
                }

                mainPanel.revalidate();
                mainPanel.repaint();
            };

            refreshInterface.run();

            JScrollPane scrollPane = new JScrollPane(mainPanel);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
            JButton btnValider = new JButton("Enregistrer");
            btnValider.setBackground(new Color(76, 175, 80));
            btnValider.setForeground(Color.WHITE);
            btnValider.setFocusPainted(false);

            btnValider.addActionListener(e -> {
                Connection conn = null;
                try {
                    conn = DaoFactory.getInstance("shopping", "root", "").getConnexion();
                    conn.setAutoCommit(false);

                    float nouveauPrixTotal = 0;
                    boolean modifications = false;

                    for (int i = 0; i < lignesModifiees.size(); i++) {
                        ArticleCommande ac = lignesModifiees.get(i);
                        String txt = quantiteFields.get(ac).getText().trim();

                        if (!txt.isEmpty()) {
                            int nouvelleQuantite = Integer.parseInt(txt);
                            Article article = articleDao.chercher(ac.getIdArticle());

                            int stockDisponible = article.getQuantite() + lignesOriginales.get(i).getQuantite();

                            if (nouvelleQuantite > stockDisponible) {
                                JOptionPane.showMessageDialog(dialog,
                                        "La quantité demandée pour " + article.getNom() +
                                                " dépasse le stock disponible (" + stockDisponible + ")",
                                        "Erreur de stock", JOptionPane.ERROR_MESSAGE);
                                conn.rollback();
                                return;
                            }

                            if (nouvelleQuantite != ac.getQuantite()) {
                                modifications = true;
                                ac.setQuantite(nouvelleQuantite);
                                ac.setPrix(article.calculerPrix(nouvelleQuantite));
                            }

                            nouveauPrixTotal += ac.getPrix();
                        }
                    }

                    if (!modifications) {
                        JOptionPane.showMessageDialog(dialog,
                                "Aucune modification détectée",
                                "Information", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    for (int i = 0; i < lignesModifiees.size(); i++) {
                        ArticleCommande acModifie = lignesModifiees.get(i);
                        ArticleCommande acOriginal = lignesOriginales.get(i);
                        Article article = articleDao.chercher(acModifie.getIdArticle());

                        if (acModifie.getQuantite() != acOriginal.getQuantite()) {
                            articleCmdDao.mettreAJourLigneCommande(acModifie);

                            int difference = acOriginal.getQuantite() - acModifie.getQuantite();
                            article.setQuantite(article.getQuantite() + difference);
                            articleDao.mettreAJour(article);
                        }
                    }

                    commande.setPrix(nouveauPrixTotal);
                    commandeDao.mettreAJour(commande);

                    conn.commit();

                    refreshInterface.run();

                    JOptionPane.showMessageDialog(dialog,
                            "Commande modifiée avec succès",
                            "Succès", JOptionPane.INFORMATION_MESSAGE);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Quantité invalide", "Erreur", JOptionPane.ERROR_MESSAGE);
                    try { if (conn != null) conn.rollback(); } catch (SQLException e1) {}
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    try { if (conn != null) conn.rollback(); } catch (SQLException e1) {}
                    JOptionPane.showMessageDialog(dialog,
                            "Erreur lors de la mise à jour: " + ex.getMessage(),
                            "Erreur SQL", JOptionPane.ERROR_MESSAGE);
                } finally {
                    try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException e1) {}
                }
            });

            buttonPanel.add(btnValider);

            JButton btnAnnuler = new JButton("Annuler");
            btnAnnuler.addActionListener(e -> dialog.dispose());
            buttonPanel.add(btnAnnuler);

            dialog.add(scrollPane, BorderLayout.CENTER);
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            dialog.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de la modification : " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Permet de supprimer une commande sélectionnée.
     */
    private void supprimerCommande() {
        int row = tableCommandes.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une commande à supprimer",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer cette commande et tous ses articles ?",
                "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            int idCommande = (int) modelCommandes.getValueAt(row, 0);
            try {
                articleCommandeDao.supprimerLignesCommande(idCommande);
                Commande commande = commandeDao.chercher(idCommande);
                if (commande != null) {
                    commandeDao.supprimer(commande);
                    chargerCommandes();
                    panelDetails.removeAll();
                    panelDetails.revalidate();
                    panelDetails.repaint();
                    JOptionPane.showMessageDialog(this,
                            "Commande #" + idCommande + " supprimée avec succès",
                            "Succès",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de la suppression de la commande",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}