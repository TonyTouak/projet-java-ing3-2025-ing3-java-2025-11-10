package vue;

import controleur.magasinControleur;
import dao.DaoFactory;
import dao.articleDaoImpl;
import modele.Article;
import modele.Client;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class magasinHommeVue extends JFrame implements magasinVue {
    private JPanel panel_principal;
    private JComboBox<String> filtreType, filtreMarque;
    private JSlider filtrePrix;
    private JTextField barreRecherche;
    private Client client;
    private magasinControleur controleur;
    private JComboBox<String> filtreReduction;

    public magasinHommeVue(Client client) {
        this.client = client;
        initialisationInterface();
        appelControleur();
        setVisible(true);
    }

    private void initialisationInterface() {
        setTitle("Liste des Articles Hommes");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        barMenu();
        Header();
        Filtres();
        creerPanelPrincipal();
    }

    private void appelControleur() {
        DaoFactory daoFactory = DaoFactory.getInstance("shopping", "root", "");
        articleDaoImpl dao = new articleDaoImpl(daoFactory);
        this.controleur = new magasinControleur(this, dao, "Homme");
        controleur.appliquerFiltres("Tous", "Toutes", 300, "Toutes");
    }

    private void barMenu() {
        menuVue menuVue = new menuVue(client, this);
        setJMenuBar(menuVue.creerMenuBar());
    }

    private void Header() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(30, 144, 255));
        header.setPreferredSize(new Dimension(0, 80));

        JLabel titre = new JLabel("Liste des Articles Hommes", SwingConstants.CENTER);
        titre.setForeground(Color.WHITE);
        titre.setFont(new Font("SansSerif", Font.BOLD, 26));

        header.add(titre, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);
    }

    private void Filtres() {
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.setBackground(new Color(240, 240, 240));
        filterPanel.setPreferredSize(new Dimension(220, 0));
        filterPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel filterTitle = new JLabel("Filtres");
        filterTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        filterTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        filterPanel.add(filterTitle);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Barre de recherche
        Alignement(filterPanel, "Recherche:", Recherche());

        // Filtre de type d'habits (chaussure, veste...)
        Alignement(filterPanel, "Type:", Type());

        // Filtre de la marque
        Alignement(filterPanel, "Marque:", Marque());

        // Filtre du prix
        Alignement(filterPanel, "Prix max:", Prix());

        // Filtre pour obtenir les réductions
        Alignement(filterPanel, "Réduction:", Reduction());

        filterPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        filterPanel.add(boutonAppliquer());

        add(filterPanel, BorderLayout.WEST);
    }

    private void Alignement(JPanel panel, String label, JComponent component) {
        JPanel aligner = new JPanel();
        aligner.setLayout(new BoxLayout(aligner, BoxLayout.Y_AXIS));
        aligner.setBackground(new Color(240, 240, 240));
        aligner.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel jlabel = new JLabel(label);
        jlabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        jlabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        jlabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        component.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (component instanceof JComboBox || component instanceof JTextField) {
            component.setMaximumSize(new Dimension(200, 30));
        }

        aligner.add(jlabel);
        aligner.add(component);

        panel.add(aligner);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private JSlider Prix() {
        JSlider slider = new JSlider(0, 300, 300);
        slider.setMajorTickSpacing(100);
        slider.setMinorTickSpacing(50);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setBackground(new Color(240, 240, 240));
        slider.setMaximumSize(new Dimension(200, 50));
        return slider;
    }

    private JTextField Recherche() {
        JTextField textField = new JTextField();
        textField.setMaximumSize(new Dimension(200, 30));
        textField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textField.addActionListener(e -> controleur.appliquerRecherche(textField.getText()));
        return textField;
    }

    private JComboBox<String> Type() {
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"Tous", "Chaussures", "T-shirt", "Short", "Veste", "Chausettes", "Bas de Survêtement", "Maillot"});
        configurerComboBox(comboBox);
        return comboBox;
    }

    private JComboBox<String> Marque() {
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"Toutes", "Nike", "Adidas", "Puma", "Jordan"});
        configurerComboBox(comboBox);
        return comboBox;
    }


    private JComboBox<String> Reduction() {
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"Toutes", "Avec réduction", "Sans réduction"});
        configurerComboBox(comboBox);
        return comboBox;
    }

    private void configurerComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        comboBox.setMaximumSize(new Dimension(200, 30));
    }

    private JButton boutonAppliquer() {
        JButton bouton = new JButton("Appliquer");
        bouton.setFont(new Font("SansSerif", Font.BOLD, 14));
        bouton.setBackground(new Color(30, 144, 255));
        bouton.setForeground(Color.WHITE);
        bouton.setFocusPainted(false);
        bouton.setMaximumSize(new Dimension(200, 40));
        bouton.addActionListener(e -> appliquerFiltres());
        return bouton;
    }

    private void creerPanelPrincipal() {
        panel_principal = new JPanel(new GridLayout(0, 3, 15, 15));
        panel_principal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel_principal.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(panel_principal);
        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void appliquerFiltres() {
        String type = filtreType.getSelectedItem().toString();
        String marque = filtreMarque.getSelectedItem().toString();
        int prixMax = filtrePrix.getValue();
        String reduction = filtreReduction.getSelectedItem().toString();
        controleur.appliquerFiltres(type, marque, prixMax, reduction);
    }

    @Override
    public void afficherArticles(ArrayList<Article> articles) {
        panel_principal.removeAll();
        Set<String> imagesAffichees = new HashSet<>();

        for (Article article : articles) {
            if (imagesAffichees.contains(article.getImage())) continue;
            imagesAffichees.add(article.getImage());

            JPanel panelArticle = PanelArticle(article);
            panel_principal.add(panelArticle);
        }

        panel_principal.revalidate();
        panel_principal.repaint();
    }

    private JPanel PanelArticle(Article article) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        ImageIcon originalIcon = new ImageIcon("images/" + article.getImage());
        Image scaledImage = originalIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        JLabel image = new JLabel(new ImageIcon(scaledImage));
        image.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nom = new JLabel(article.getNom());
        nom.setFont(new Font("SansSerif", Font.BOLD, 14));
        nom.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel prix = new JLabel(String.format("%.2f €", article.getPrixUnique()));
        prix.setForeground(new Color(0, 102, 0));
        prix.setFont(new Font("SansSerif", Font.PLAIN, 14));
        prix.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(image);
        panel.add(Box.createVerticalStrut(10));
        panel.add(nom);
        panel.add(prix);

        //on affiche l'article sur lequel le client à cliquer
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new articleVue(article, client);
                dispose();
            }
        });

        return panel;
    }
}