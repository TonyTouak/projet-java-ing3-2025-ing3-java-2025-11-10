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

    public panierVue(Client client, Panier panier) {
        this.controleur = new panierControleur(this, panier, client);

        setTitle("Panier");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        menuVue menu = new menuVue(client, this);
        setJMenuBar(menu.creerMenuBar());

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

        add(header, BorderLayout.NORTH);

        articlesPanel = new JPanel();
        articlesPanel.setLayout(new BoxLayout(articlesPanel, BoxLayout.Y_AXIS));
        articlesPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(articlesPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        buttonsPanel.setBackground(Color.WHITE);

        JButton validerBouton = new JButton("Valider");
        boutonVue(validerBouton, new Color(30, 144, 255));
        validerBouton.addActionListener(e -> controleur.validerCommande());

        JButton viderBouton = new JButton("Vider le panier");
        boutonVue(viderBouton, new Color(220, 20, 60));
        viderBouton.addActionListener(e -> controleur.viderPanier());

        buttonsPanel.add(viderBouton);
        buttonsPanel.add(validerBouton);
        add(buttonsPanel, BorderLayout.SOUTH);

        mettreAJourAffichage(panier);
        setVisible(true);
    }

    private void boutonVue(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(180, 40));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }

    public void mettreAJourAffichage(Panier panier) {
        articlesPanel.removeAll();

        if (panier.estVide()) {
            JLabel emptyLabel = new JLabel("Votre panier est vide.", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("SansSerif", Font.ITALIC, 18));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            articlesPanel.add(emptyLabel);
        } else {
            for (Map.Entry<Article, Integer> entry : panier.getArticles().entrySet()) {
                Article article = entry.getKey();
                int quantite = entry.getValue();

                JPanel itemPanel = new JPanel(new BorderLayout(10, 10));
                itemPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                itemPanel.setBackground(Color.WHITE);
                itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

                ImageIcon originalIcon = new ImageIcon("images/" + article.getImage());
                Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                itemPanel.add(imageLabel, BorderLayout.WEST);

                JPanel detailsPanel = new JPanel();
                detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
                detailsPanel.setBackground(Color.WHITE);

                JLabel nomLabel = new JLabel(article.getNom());
                nomLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
                detailsPanel.add(nomLabel);

                JLabel prixLabel = new JLabel(String.format("Prix unitaire: %.2f €", article.getPrixUnique()));
                prixLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
                detailsPanel.add(prixLabel);

                JLabel tailleLabel = new JLabel("Taille: " + article.getTaille());
                tailleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
                detailsPanel.add(tailleLabel);

                JPanel quantitePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                quantitePanel.setBackground(Color.WHITE);

                JButton moinsButton = new JButton("-");
                moinsButton.addActionListener(e -> controleur.modifierQuantite(article, -1));
                quantitePanel.add(moinsButton);

                JLabel quantiteLabel = new JLabel(String.valueOf(quantite));
                quantiteLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
                quantiteLabel.setPreferredSize(new Dimension(30, 20));
                quantitePanel.add(quantiteLabel);

                JButton plusButton = new JButton("+");
                plusButton.addActionListener(e -> controleur.modifierQuantite(article, 1));
                quantitePanel.add(plusButton);

                detailsPanel.add(quantitePanel);
                itemPanel.add(detailsPanel, BorderLayout.CENTER);

                JButton supprimerButton = new JButton("Supprimer");
                supprimerButton.addActionListener(e -> controleur.supprimerArticle(article));
                itemPanel.add(supprimerButton, BorderLayout.EAST);

                articlesPanel.add(itemPanel);
            }
        }

        totalLabel.setText(String.format("Total: %.2f € (%d articles)",
                panier.getTotal(), panier.getNombreArticles()));

        articlesPanel.revalidate();
        articlesPanel.repaint();
    }

    public void afficherMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}