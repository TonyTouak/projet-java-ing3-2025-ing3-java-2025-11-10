package vue;

import modele.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class accueilVue extends JFrame {
    /**
     * La fonction initialise la fenêtre d'accueil et les composants graphiques.
     *
     * @param client : le client actuellement connecté à l'application.
     */
    public accueilVue(Client client) {
        setTitle("Accueil - ShopTopie");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Menu
        menuVue menu = new menuVue(client, this);
        setJMenuBar(menu.creerMenuBar());

        // Image de fond
        JLabel background = new JLabel(new ImageIcon("Images/accueil.jpg")); // <-- chemin vers l'image
        background.setLayout(new BorderLayout());
        setContentPane(background); // Définit l'image comme fond principal

        // Contenu principal transparent
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setOpaque(false); // fond transparent
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(100, 50, 100, 50));

        JLabel titre = new JLabel("ShopTopie", SwingConstants.CENTER);
        titre.setFont(new Font("SansSerif", Font.BOLD, 60));
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);
        titre.setForeground(new Color(30, 144, 255));

        JLabel slogan = new JLabel("Faites de vos achats rêvés une réalité", SwingConstants.CENTER);
        slogan.setFont(new Font("SansSerif", Font.ITALIC, 20));
        slogan.setAlignmentX(Component.CENTER_ALIGNMENT);
        slogan.setForeground(Color.WHITE);

        panelPrincipal.add(titre);
        panelPrincipal.add(Box.createVerticalStrut(20));
        panelPrincipal.add(slogan);
        panelPrincipal.add(Box.createVerticalStrut(40));

        // Boutons Homme / Femme avec couleur
        JPanel boutonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        boutonPanel.setOpaque(false); // transparent
        JButton boutonHomme = new JButton("Homme");
        boutonHomme.setPreferredSize(new Dimension(150, 40));
        boutonHomme.setFont(new Font("SansSerif", Font.PLAIN, 16));
        boutonHomme.setBackground(new Color(70, 130, 180)); // bleu
        boutonHomme.setForeground(Color.WHITE);
        boutonHomme.setFocusPainted(false);
        boutonHomme.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boutonHomme.addActionListener(e -> {
            new magasinHommeVue(client);
            dispose();
        });

        JButton boutonFemme = new JButton("Femme");
        boutonFemme.setPreferredSize(new Dimension(150, 40));
        boutonFemme.setFont(new Font("SansSerif", Font.PLAIN, 16));
        boutonFemme.setBackground(new Color(255, 105, 180)); // rose
        boutonFemme.setForeground(Color.WHITE);
        boutonFemme.setFocusPainted(false);
        boutonFemme.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boutonFemme.addActionListener(e -> {
            new magasinFemmeVue(client);
            dispose();
        });

        boutonPanel.add(boutonHomme);
        boutonPanel.add(boutonFemme);

        panelPrincipal.add(boutonPanel);

        background.add(panelPrincipal, BorderLayout.CENTER);

        setVisible(true);
    }
}
