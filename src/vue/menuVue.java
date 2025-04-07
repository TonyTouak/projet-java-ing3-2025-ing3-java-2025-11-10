package vue;

import modele.Client;

import javax.swing.*;
import java.awt.*;

public class menuVue {
    private final Client client;
    private final JFrame frame;

    public menuVue(Client client, JFrame frame) {
        this.client = client;
        this.frame = frame;
    }

    public JMenuBar creerMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        menuBar.setBackground(new Color(30, 144, 255)); // Bleu doux
        menuBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Font menuFont = new Font("SansSerif", Font.BOLD, 14);
        Color textColor = Color.WHITE;

        menuBar.add(creerBoutonMenu("🏠 Accueil", menuFont, textColor, () -> {
            frame.dispose();
            new accueilVue(client);
        }));

        menuBar.add(creerBoutonMenu("🛍️ Magasin", menuFont, textColor, () -> {
            frame.dispose();
            new magasinVue(client);
        }));

        menuBar.add(creerBoutonMenu("🛒 Mon Panier", menuFont, textColor, () -> {
            frame.dispose();
            new panierVue(client);
        }));

        String profilText = client != null ? "👤 Mon Profil" : "🔐 Se connecter";
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

    private JButton creerBoutonMenu(String texte, Font font, Color textColor, Runnable action) {
        JButton bouton = new JButton(texte);
        bouton.setFont(font);
        bouton.setForeground(textColor);
        bouton.setBackground(new Color(30, 144, 255));
        bouton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        bouton.setFocusPainted(false);
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bouton.setOpaque(true);

        bouton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bouton.setBackground(new Color(65, 105, 225)); // Hover color
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                bouton.setBackground(new Color(30, 144, 255));
            }
        });

        bouton.addActionListener(e -> action.run());
        return bouton;
    }
}
