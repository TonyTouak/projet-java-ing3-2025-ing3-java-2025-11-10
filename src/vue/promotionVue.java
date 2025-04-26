package vue;

import controleur.promotionControleur;
import dao.DaoFactory;
import dao.articleDao;
import dao.articleDaoImpl;
import modele.Article;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class promotionVue extends JFrame {
    articleDao articleDao;

    public promotionVue(List<Article> articles, articleDao articleDao) {
        this.articleDao = articleDao;
        setTitle("Gestion des Promotions");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 144, 255));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Ajout de Promotions", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        JButton btnFermer = new JButton("Fermer");
        btnFermer.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnFermer.setBackground(new Color(70, 130, 180));
        btnFermer.setForeground(Color.WHITE);
        btnFermer.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnFermer.addActionListener(e -> dispose());
        headerPanel.add(btnFermer, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        JPanel panelArticles = new JPanel();
        panelArticles.setBackground(Color.WHITE);
        panelArticles.setLayout(new GridLayout(0, 3, 20, 20));
        panelArticles.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for (Article article : articles) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(new Color(240, 248, 255));
            panel.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
            panel.setMaximumSize(new Dimension(250, 300));

            JLabel lblNom = new JLabel(article.getNom());
            lblNom.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblNom.setFont(new Font("Arial", Font.BOLD, 14));
            lblNom.setForeground(new Color(0, 51, 102));

            //on charge l'image de chaque articles avec le bon chemin
            String imagePath = "./images/" + article.getImage();
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            JLabel lblImage = new JLabel(new ImageIcon(img));
            lblImage.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel lblTaille = new JLabel("Taille : " + article.getTaille());
            lblTaille.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblTaille.setFont(new Font("Arial", Font.PLAIN, 13));

            JLabel lblReduction = new JLabel("Réduction (%) :");
            lblReduction.setAlignmentX(Component.CENTER_ALIGNMENT);

            JTextField txtPromo = new JTextField("0");
            txtPromo.setMaximumSize(new Dimension(100, 25));
            txtPromo.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton btnAppliquer = new JButton("Appliquer");
            btnAppliquer.setBackground(new Color(0, 102, 204));
            btnAppliquer.setForeground(Color.WHITE);
            btnAppliquer.setFocusPainted(false);
            btnAppliquer.setAlignmentX(Component.CENTER_ALIGNMENT);

            Map<Article, JButton> mapBoutons = new HashMap<>();
            Map<Article, JTextField> mapChamps = new HashMap<>();
            mapBoutons.put(article, btnAppliquer);
            mapChamps.put(article, txtPromo);

            new promotionControleur(this, new articleDaoImpl(DaoFactory.getInstance("shopping", "root", "")), mapBoutons, mapChamps);


            panel.add(Box.createVerticalStrut(5));
            panel.add(lblNom);
            panel.add(Box.createVerticalStrut(10));
            panel.add(lblImage);
            panel.add(Box.createVerticalStrut(10));
            panel.add(lblTaille);
            panel.add(Box.createVerticalStrut(5));
            panel.add(lblReduction);
            panel.add(txtPromo);
            panel.add(Box.createVerticalStrut(10));
            panel.add(btnAppliquer);
            panelArticles.add(panel);
        }

        JScrollPane scrollPane = new JScrollPane(panelArticles);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }
}
