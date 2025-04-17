package vue;

import modele.Client;
import modele.Commande;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class clientVue extends JFrame {

    private Client client;

    public clientVue(Client client) {
        this.client = client;

        setTitle("Espace Client - " + client.getNom());
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(255, 255, 255));

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
        contentPanel.setBackground(new Color(255, 255, 255));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        contentPanel.add(createLabel("🆔 ID Client : " + client.getIDClient()));
        contentPanel.add(createLabel("🆔 ID Utilisateur : " + client.getId()));
        contentPanel.add(createLabel("📍 Adresse : " + client.getAdresse()));
        contentPanel.add(createLabel("📞 Téléphone : " + client.getTelephone()));
        contentPanel.add(createLabel("👤 Nom : " + client.getNom()));

        contentPanel.add(Box.createVerticalStrut(30));

        JLabel titreCommandes = new JLabel("🧾 Commandes passées :");
        titreCommandes.setFont(new Font("SansSerif", Font.BOLD, 18));
        contentPanel.add(titreCommandes);

        List<Commande> commandes = client.getCommandes();
        if (commandes != null && !commandes.isEmpty()) {
            for (Commande commande : commandes) {
                String texteCommande = "- Commande #" + commande.getId() +
                        " du " + commande.getDate() +
                        " | " + commande.getPrix() + " €" +
                        " x " + commande.getQuantite();
                contentPanel.add(createLabel(texteCommande));
            }
        } else {
            contentPanel.add(createLabel("Aucune commande passée."));
        }

        contentPanel.add(Box.createVerticalStrut(30));

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
        contentPanel.add(Box.createVerticalStrut(20));

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

    private JPanel creerPanelCommande(Commande commande) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        Article article = commande.getArticle();

        // Affichage image (Images/nomFichier.png)
        String imagePath = "Images/" + article.getImage(); // ex: Images/brassiere.png
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());
            Image scaled = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            JLabel imgLabel = new JLabel(new ImageIcon(scaled));
            imgLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(imgLabel);
        } else {
            panel.add(createLabel("[Image introuvable]"));
        }

        // Nom article
        JLabel nomArticle = new JLabel(article.getNom());
        nomArticle.setFont(new Font("SansSerif", Font.BOLD, 16));
        nomArticle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(nomArticle);

        // Détail commande
        String details = "Commande du " + commande.getDate() +
                " | " + commande.getPrixFinal() + " €" +
                " x " + commande.getQuantite();
        JLabel infos = createLabel(details);
        infos.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(infos);

        return panel;
    }
}
