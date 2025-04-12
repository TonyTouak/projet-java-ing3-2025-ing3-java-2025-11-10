package vue;

import modele.Client;
import modele.Panier;

import javax.swing.*;
import java.awt.*;


public class menuVue {
    private final Client client;
    private final JFrame frame;
    private JLabel panierCountLabel;

    public menuVue(Client client, JFrame frame) {
        this.client = client;
        this.frame = frame;
    }

    public JMenuBar creerMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        menuBar.setBackground(new Color(30, 144, 255));
        menuBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        Font menuFont = new Font("SansSerif", Font.BOLD, 14);
        Color textColor = Color.WHITE;

        menuBar.add(creerBoutonMenu("🏠 Accueil", menuFont, textColor, () -> {
            frame.dispose();
            new accueilVue(client);
        }));

        JMenu magasinMenu = creerMenuDeroulant("🛍️ Magasin", menuFont, textColor);

        JMenuItem hommeItem = creerMenuItem("Homme", () -> {
            frame.dispose();
            new magasinHommeVue(client);
        });

        JMenuItem femmeItem = creerMenuItem("Femme", () -> {
            frame.dispose();
            new magasinFemmeVue(client);
        });

        magasinMenu.add(hommeItem);
        magasinMenu.add(femmeItem);
        menuBar.add(magasinMenu);

        JPanel panierPanel = new JPanel(new BorderLayout());
        panierPanel.setOpaque(false);

        JButton panierBtn = creerBoutonMenu("🛒 Panier", menuFont, textColor, () -> {
            frame.dispose();
            new panierVue(client, Panier.getInstance());
        });

        panierCountLabel = new JLabel("", SwingConstants.CENTER);
        panierCountLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        panierCountLabel.setForeground(Color.YELLOW);
        panierCountLabel.setPreferredSize(new Dimension(20, 20));
        actualiserPanier();

        panierPanel.add(panierBtn, BorderLayout.CENTER);
        panierPanel.add(panierCountLabel, BorderLayout.EAST);
        menuBar.add(panierPanel);

        String profilText = client != null ? "👤 " + client.getNom() : "🔐 Connexion";
        menuBar.add(creerBoutonMenu(profilText, menuFont, textColor, () -> {
            frame.dispose();
            if (client != null) {
                new clientVue(client);
            } else {
                new loginVue();
            }
        }));

        return menuBar;
    }

    private JMenu creerMenuDeroulant(String texte, Font font, Color textColor) {
        JMenu menu = new JMenu(texte);
        menu.setFont(font);
        menu.setForeground(textColor);
        menu.setBackground(new Color(30, 144, 255));
        menu.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return menu;
    }

    private JMenuItem creerMenuItem(String texte, Runnable action) {
        JMenuItem item = new JMenuItem(texte);
        item.addActionListener(e -> action.run());
        return item;
    }

    private JButton creerBoutonMenu(String texte, Font font, Color textColor, Runnable action) {
        JButton bouton = new JButton(texte);
        bouton.setFont(font);
        bouton.setForeground(textColor);
        bouton.setBackground(new Color(30, 144, 255));
        bouton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        bouton.setFocusPainted(false);
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bouton.setContentAreaFilled(false);
        bouton.setOpaque(true);

        bouton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bouton.setBackground(new Color(65, 105, 225));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                bouton.setBackground(new Color(30, 144, 255));
            }
        });

        bouton.addActionListener(e -> action.run());
        return bouton;
    }

    public void actualiserPanier() {
        int count = Panier.getInstance().getNombreArticles();
        panierCountLabel.setText(count > 0 ? String.valueOf(count) : "");
        panierCountLabel.setVisible(count > 0);
    }

}