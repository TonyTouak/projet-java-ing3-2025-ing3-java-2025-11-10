package vue;

import modele.Client;

import javax.swing.*;
import java.awt.*;

public class accueilVue extends JFrame {

    public accueilVue(Client client) {
        setTitle("Accueil - ShopTopie");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        menuVue menu = new menuVue(client, this);
        setJMenuBar(menu.creerMenuBar());

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(100, 50, 100, 50));

        JLabel titre = new JLabel("ShopTopie", SwingConstants.CENTER);
        titre.setFont(new Font("SansSerif", Font.BOLD, 48));
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);
        titre.setForeground(new Color(30, 144, 255));

        JLabel slogan = new JLabel("Faites de vos achats rêvés une réalité", SwingConstants.CENTER);
        slogan.setFont(new Font("SansSerif", Font.ITALIC, 20));
        slogan.setAlignmentX(Component.CENTER_ALIGNMENT);
        slogan.setForeground(new Color(70, 70, 70));

        panelPrincipal.add(titre);
        panelPrincipal.add(Box.createVerticalStrut(20));
        panelPrincipal.add(slogan);

        add(panelPrincipal, BorderLayout.CENTER);

        setVisible(true);
    }
}
