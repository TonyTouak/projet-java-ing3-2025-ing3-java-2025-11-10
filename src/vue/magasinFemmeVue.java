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

public class magasinFemmeVue extends JFrame implements magasinVue {
    private JPanel panel_principal;
    private JComboBox<String> filtreType, filtreMarque;
    private JSlider filtrePrix;
    private Client client;
    private magasinControleur controleur;

    public magasinFemmeVue(Client client) {
        this.client = client;

        setTitle("Liste des Articles Femmes");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        menuVue menuVue = new menuVue(client, this);
        setJMenuBar(menuVue.creerMenuBar());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(255, 105, 180));
        header.setPreferredSize(new Dimension(0, 80));
        JLabel titre = new JLabel("Liste des Articles Femmes", SwingConstants.CENTER);
        titre.setForeground(Color.WHITE);
        titre.setFont(new Font("SansSerif", Font.BOLD, 26));
        header.add(titre, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        DaoFactory daoFactory = DaoFactory.getInstance("shopping", "root", "");
        articleDaoImpl dao = new articleDaoImpl(daoFactory);
        this.controleur = new magasinControleur(this, dao, "Femme");

        createFilterPanel();

        panel_principal = new JPanel(new GridLayout(0, 3, 15, 15));
        panel_principal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel_principal.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(panel_principal);
        add(scrollPane, BorderLayout.CENTER);

        controleur.appliquerFiltres("Tous", "Toutes", 300);

        setVisible(true);
    }

    private void createFilterPanel() {
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.setBackground(new Color(240, 240, 240));
        filterPanel.setPreferredSize(new Dimension(220, 0));
        filterPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel filterTitle = new JLabel("FILTRES");
        filterTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        filterTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        filterPanel.add(filterTitle);
        filterPanel.add(Box.createVerticalStrut(20));

        filtreType = new JComboBox<>(new String[]{"Tous", "Chaussures", "Short", "Legging", "Chausettes", "Brassière", "T-Shirt"});
        filtreType.setFont(new Font("SansSerif", Font.PLAIN, 14));
        filtreType.setMaximumSize(new Dimension(200, 30));
        filterPanel.add(createLabeledComponent("Type :", filtreType));

        filtreMarque = new JComboBox<>(new String[]{"Toutes", "Nike", "Adidas", "Puma", "Asics"});
        filtreMarque.setFont(new Font("SansSerif", Font.PLAIN, 14));
        filtreMarque.setMaximumSize(new Dimension(200, 30));
        filterPanel.add(createLabeledComponent("Marque :", filtreMarque));

        filtrePrix = new JSlider(0, 300, 300);
        filtrePrix.setMajorTickSpacing(100);
        filtrePrix.setPaintTicks(true);
        filtrePrix.setPaintLabels(true);
        filtrePrix.setBackground(new Color(240, 240, 240));
        filterPanel.add(createLabeledComponent("Prix maximum :", filtrePrix));

        JButton appliquerBouton = new JButton("Appliquer");
        appliquerBouton.setFont(new Font("SansSerif", Font.BOLD, 14));
        appliquerBouton.setBackground(new Color(255, 105, 180));
        appliquerBouton.setForeground(Color.WHITE);
        appliquerBouton.setFocusPainted(false);
        appliquerBouton.setMaximumSize(new Dimension(200, 40));
        appliquerBouton.addActionListener(e -> appliquerFiltres());
        filterPanel.add(Box.createVerticalStrut(15));
        filterPanel.add(appliquerBouton);

        add(filterPanel, BorderLayout.WEST);
    }

    private JPanel createLabeledComponent(String labelText, JComponent component) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(240, 240, 240));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createVerticalStrut(8));
        panel.add(component);

        return panel;
    }

    @Override
    public void appliquerFiltres() {
        String type = filtreType.getSelectedItem().toString();
        String marque = filtreMarque.getSelectedItem().toString();
        int prixMax = filtrePrix.getValue();
        controleur.appliquerFiltres(type, marque, prixMax);
    }

    @Override
    public void afficherArticles(ArrayList<Article> articles) {
        panel_principal.removeAll();

        Set<String> imagesAffichees = new HashSet<>();

        for (Article article : articles) {
            if (imagesAffichees.contains(article.getImage())) {
                continue;
            }

            imagesAffichees.add(article.getImage());

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

            panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            panel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    new articleVue(article, client);
                    dispose();
                }
            });

            panel_principal.add(panel);
        }
        panel_principal.revalidate();
        panel_principal.repaint();
    }
}
