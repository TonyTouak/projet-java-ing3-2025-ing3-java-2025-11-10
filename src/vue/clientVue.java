package vue;

import dao.*;
import modele.Article;
import modele.ArticleCommande;
import modele.Client;
import modele.Commande;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class clientVue extends JFrame {

    private static final String IMAGE_FOLDER = "Images/"; // <== Correction ici
    private final Client client;

    /**
     * Initialise l'espace client avec ses informations et ses commandes.
     *
     * @param client : le client connecté
     */
    public clientVue(Client client) {
        this.client = client;

        setTitle("Espace Client - " + client.getNom());
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);

        menuVue menuVue = new menuVue(client, this);
        setJMenuBar(menuVue.creerMenuBar());

        JPanel header = new JPanel();
        header.setBackground(new Color(30, 144, 255));
        header.setPreferredSize(new Dimension(1000, 80));
        JLabel bienvenue = new JLabel("Bienvenue, " + client.getNom() + " !");
        bienvenue.setForeground(Color.WHITE);
        bienvenue.setFont(new Font("SansSerif", Font.BOLD, 26));
        header.add(bienvenue);
        add(header, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        contentPanel.add(createLabel("🔑 ID Client : " + client.getIDClient()));
        contentPanel.add(createLabel("🔑 ID Utilisateur : " + client.getId()));
        contentPanel.add(createLabel("📍 Adresse : " + client.getAdresse()));
        contentPanel.add(createLabel("📞 Téléphone : " + client.getTelephone()));
        contentPanel.add(createLabel("👤 Nom : " + client.getNom()));

        contentPanel.add(Box.createVerticalStrut(30));

        JLabel titreCommandes = new JLabel("🧾 Commandes passées :");
        titreCommandes.setFont(new Font("SansSerif", Font.BOLD, 18));
        contentPanel.add(titreCommandes);

        DaoFactory daoFactory = DaoFactory.getInstance("shopping", "root", "");
        articleDao articleDao = new articleDaoImpl(daoFactory);
        articleCommandeDao articleCommandeDao = new articleCommandeDaoImpl(daoFactory);
        commandeDao commandeDao = new commandeDaoImpl(daoFactory);

        List<Commande> commandes = commandeDao.getCommandesParClient(client.getIDClient());
        if (commandes.isEmpty()) {
            contentPanel.add(createLabel("Aucune commande passée"));
        } else {
            for (Commande commande : commandes) {
                JPanel commandePanel = new JPanel();
                commandePanel.setLayout(new BoxLayout(commandePanel, BoxLayout.Y_AXIS));
                commandePanel.setBackground(new Color(245, 250, 255));
                commandePanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
                commandePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel titreCommande = new JLabel("Commande du " + commande.getDate() + " (Total : " + commande.getPrix() + " €)");
                titreCommande.setFont(new Font("SansSerif", Font.BOLD, 16));
                titreCommande.setAlignmentX(Component.LEFT_ALIGNMENT);
                commandePanel.add(titreCommande);
                commandePanel.add(Box.createVerticalStrut(10));

                List<ArticleCommande> articlesCommandes = articleCommandeDao.getArticlesParCommande(commande.getId());

                for (ArticleCommande ac : articlesCommandes) {
                    Article article = articleDao.getArticleParId(ac.getIdArticle());

                    if (article != null) {
                        JPanel articlePanel = new JPanel();
                        articlePanel.setLayout(new BoxLayout(articlePanel, BoxLayout.X_AXIS));
                        articlePanel.setBackground(Color.WHITE);
                        articlePanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 10));
                        articlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

                        String imagePath = IMAGE_FOLDER + article.getImage(); // <-- Correction
                        File imageFile = new File(imagePath);
                        JLabel imgLabel;
                        if (imageFile.exists()) {
                            ImageIcon icon = new ImageIcon(imagePath);
                            Image scaled = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                            imgLabel = new JLabel(new ImageIcon(scaled));
                        } else {
                            imgLabel = new JLabel("[Image introuvable]");
                        }

                        JLabel detailsLabel = new JLabel("  Taille : " + article.getTaille());
                        detailsLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

                        articlePanel.add(imgLabel);
                        articlePanel.add(Box.createRigidArea(new Dimension(10, 0)));
                        articlePanel.add(detailsLabel);

                        commandePanel.add(articlePanel);
                    }
                }

                commandePanel.add(Box.createVerticalStrut(10));
                contentPanel.add(Box.createVerticalStrut(15));
                contentPanel.add(commandePanel);
            }
        }

        contentPanel.add(Box.createVerticalStrut(20));
        JButton btnDeconnexion = new JButton("Déconnexion");
        btnDeconnexion.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDeconnexion.setBackground(new Color(30, 144, 255));
        btnDeconnexion.setForeground(Color.WHITE);
        btnDeconnexion.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnDeconnexion.addActionListener(e -> {
            dispose();
            new loginVue();
        });
        contentPanel.add(btnDeconnexion);

        JScrollPane scroll = new JScrollPane(contentPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(10, Integer.MAX_VALUE));
        scroll.getVerticalScrollBar().setBackground(Color.WHITE);

        add(scroll, BorderLayout.CENTER);
        setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 16));
        label.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        return label;
    }
}
