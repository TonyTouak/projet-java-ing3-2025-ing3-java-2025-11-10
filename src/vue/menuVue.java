package vue;

import modele.Client;
import modele.Panier;

import javax.swing.*;
import java.awt.*;

public class menuVue {
    private final Client client;
    private final JFrame frame;
    private JLabel panierCountLabel;

    private final Dimension BOUTON_TAILLE = new Dimension(140, 40);

    /**
     * Constructeur qui associe un client et une fenêtre à la barre de menu.
     *
     * @param client : le client actuellement connecté
     * @param frame : la fenêtre où sera affichée la barre de menu
     */
    public menuVue(Client client, JFrame frame) {
        this.client = client;
        this.frame = frame;
    }

    /**
     * Crée et retourne la barre de menu.
     *
     * @return : une JMenuBar configurée
     */
    public JMenuBar creerMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setLayout(new BoxLayout(menuBar, BoxLayout.X_AXIS));
        menuBar.setBackground(new Color(30, 144, 255));
        menuBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        Font menuFont = new Font("SansSerif", Font.BOLD, 14);
        Color textColor = Color.WHITE;

        // Accueil
        JButton accueilBtn = creerBoutonMenu("🏠 Accueil", menuFont, textColor, () -> {
            frame.dispose();
            new accueilVue(client);
        });
        menuBar.add(accueilBtn);

        // Magasin
        JMenu magasinMenu = creerMenuDeroulant("🛍️ Magasin", menuFont, textColor);
        magasinMenu.add(creerMenuItem("Homme", () -> {
            frame.dispose();
            new magasinHommeVue(client);
        }));
        magasinMenu.add(creerMenuItem("Femme", () -> {
            frame.dispose();
            new magasinFemmeVue(client);
        }));
        magasinMenu.setPreferredSize(BOUTON_TAILLE);
        magasinMenu.setMaximumSize(BOUTON_TAILLE);
        magasinMenu.setMinimumSize(BOUTON_TAILLE);
        menuBar.add(magasinMenu);

        // Glue pour pousser les 2 derniers à droite
        menuBar.add(Box.createHorizontalGlue());

        // Panier
        JPanel panierPanel = new JPanel(new BorderLayout());
        panierPanel.setOpaque(false);
        panierPanel.setMaximumSize(BOUTON_TAILLE);
        panierPanel.setPreferredSize(BOUTON_TAILLE);

        JButton panierBtn = creerBoutonMenu("🛒 Panier", menuFont, textColor, () -> {
            frame.dispose();
            new panierVue(client, Panier.getInstance());
        });
        panierBtn.setPreferredSize(BOUTON_TAILLE);

        panierCountLabel = new JLabel("", SwingConstants.CENTER);
        panierCountLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        panierCountLabel.setForeground(Color.YELLOW);
        panierCountLabel.setPreferredSize(new Dimension(20, 20));
        actualiserPanier();

        panierPanel.add(panierBtn, BorderLayout.CENTER);
        panierPanel.add(panierCountLabel, BorderLayout.EAST);
        menuBar.add(panierPanel);

        // Mon Compte
        String profilText = client != null ? "👤 " + client.getNom() : "🔐 Connexion";
        JButton profilBtn = creerBoutonMenu(profilText, menuFont, textColor, () -> {
            frame.dispose();
            if (client != null) {
                new clientVue(client);
            } else {
                new loginVue();
            }
        });
        profilBtn.setPreferredSize(BOUTON_TAILLE);
        menuBar.add(profilBtn);

        return menuBar;
    }

    /**
     * Crée un menu déroulant personnalisé.
     *
     * @param texte : texte affiché
     * @param font : police du texte
     * @param textColor : couleur du texte
     * @return : un JMenu configuré
     */
    private JMenu creerMenuDeroulant(String texte, Font font, Color textColor) {
        JMenu menu = new JMenu(texte);
        menu.setFont(font);
        menu.setForeground(textColor);
        menu.setBackground(new Color(30, 144, 255));
        menu.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return menu;
    }

    /**
     * Crée un item de menu qui exécute une action au clic.
     *
     * @param texte : texte de l'item
     * @param action : action à exécuter au clic
     * @return : un JMenuItem configuré
     */
    private JMenuItem creerMenuItem(String texte, Runnable action) {
        JMenuItem item = new JMenuItem(texte);
        item.addActionListener(e -> action.run());
        return item;
    }

    /**
     * Création d'un menu avec effet de survol et action au clic.
     *
     * @param texte : texte du bouton
     * @param font : police du texte
     * @param textColor : couleur du texte
     * @param action : action à exécuter au clic
     * @return : un JButton configuré
     */
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
        bouton.setPreferredSize(BOUTON_TAILLE);

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

    /**
     * Met à jour le nombre d'articles affiché à côté de l'icône panier.
     */
    public void actualiserPanier() {
        int count = Panier.getInstance().getNombreArticles();
        panierCountLabel.setText(count > 0 ? String.valueOf(count) : "");
        panierCountLabel.setVisible(count > 0);
    }
}
