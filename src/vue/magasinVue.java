package vue;

import modele.Client;

import javax.swing.*;
import java.awt.*;

public class magasinVue extends JFrame {

    public magasinVue(Client client) {
        menuVue menuVue = new menuVue(client, this);
        setJMenuBar(menuVue.creerMenuBar());

        setVisible(true);
        setTitle("Bienvenue");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(30, 144, 255));
        headerPanel.setPreferredSize(new Dimension(0, 250));
        headerPanel.setLayout(null);

        JLabel titleLabel = new JLabel("Bienvenue sur notre magasin de shopping ");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        titleLabel.setBounds(300, 30, 1000, 40);
        headerPanel.add(titleLabel);

        JButton boutonBoutique = new JButton("Boutique");
        JButton boutonInscription = new JButton("Connexion");

        boutonBoutique.setBounds(600, 150, 140, 45);
        boutonInscription.setBounds(800, 150, 140, 45);

        headerPanel.add(boutonBoutique);
        headerPanel.add(boutonInscription);

        boutonInscription.addActionListener(e -> {
            new loginVue();
            dispose();
        });

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(2, 3, 30, 30));
        contentPanel.setBackground(new Color(245, 245, 220));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        String[] nomsFichiers = {
                "Alphafly.png", "brassiere_femme.png", "chaussettes.png",
                "nike_chaussures.png", "short_sport.png", "teeshirt_homme.png"
        };

        String[] nomsProduits = {
                "Nike Alphafly", "Brassière Femme", "Chaussettes Sport",
                "Chaussures Nike", "Short Sport", "T-shirt Homme"
        };

        String[] prixProduits = {
                "250 €", "35 €", "12 €",
                "180 €", "40 €", "25 €"
        };

        for (int i = 0; i < nomsFichiers.length; i++) {
            JPanel produitPanel = new JPanel();
            produitPanel.setLayout(new BoxLayout(produitPanel, BoxLayout.Y_AXIS));
            produitPanel.setBackground(Color.WHITE);
            produitPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

            ImageIcon icon = new ImageIcon("images/" + nomsFichiers[i]);
            Image scaledImg = icon.getImage().getScaledInstance(130, 130, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImg));
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel nomLabel = new JLabel(nomsProduits[i]);
            nomLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            nomLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel prixLabel = new JLabel(prixProduits[i]);
            prixLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
            prixLabel.setForeground(new Color(0, 102, 0));
            prixLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            produitPanel.add(Box.createVerticalStrut(10));
            produitPanel.add(imageLabel);
            produitPanel.add(Box.createVerticalStrut(10));
            produitPanel.add(nomLabel);
            produitPanel.add(prixLabel);

            int index = i;
            produitPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            produitPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    new produitVue(nomsProduits[index], client);
                    dispose();
                }
            });

            contentPanel.add(produitPanel);
        }

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(headerPanel, BorderLayout.NORTH);
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

}
