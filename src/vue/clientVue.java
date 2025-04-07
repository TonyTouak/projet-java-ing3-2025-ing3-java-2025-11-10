package vue;

import modele.Client;

import javax.swing.*;
import java.awt.*;

public class clientVue extends JFrame {

    private Client client;

    public clientVue(Client client) {
        this.client = client;

        setTitle("Espace Client - " + client.getNom());
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        menuVue menuVue = new menuVue(client, this);
        setJMenuBar(menuVue.creerMenuBar());

        setVisible(true);

        JPanel header = new JPanel();
        header.setBackground(new Color(30, 144, 255));
        JLabel bienvenue = new JLabel("Bienvenue, " + client.getNom() + " !");
        bienvenue.setForeground(Color.WHITE);
        bienvenue.setFont(new Font("SansSerif", Font.BOLD, 22));
        header.add(bienvenue);

        JPanel infosPanel = new JPanel();
        infosPanel.setLayout(new BoxLayout(infosPanel, BoxLayout.Y_AXIS));
        infosPanel.setBackground(new Color(245, 245, 220));
        infosPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        infosPanel.add(createLabel("🆔 ID Client : " + client.getIDClient()));
        infosPanel.add(createLabel("🆔 ID Utilisateur : " + client.getId()));
        infosPanel.add(createLabel("📍 Adresse : " + client.getAdresse()));
        infosPanel.add(createLabel("📞 Téléphone : " + client.getTelephone()));
        infosPanel.add(createLabel("👤 Nom : " + client.getNom()));

        JButton btnDeconnexion = new JButton("Déconnexion");
        btnDeconnexion.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDeconnexion.setBackground(new Color(30, 144, 255));
        btnDeconnexion.setForeground(Color.WHITE);
        btnDeconnexion.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnDeconnexion.addActionListener(e -> {
            dispose();
            new loginVue();
        });

        infosPanel.add(Box.createVerticalStrut(20));
        infosPanel.add(btnDeconnexion);

        JScrollPane scroll = new JScrollPane(infosPanel);
        scroll.setBorder(null);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(header, BorderLayout.NORTH);
        getContentPane().add(scroll, BorderLayout.CENTER);

        setVisible(true);
    }


    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 16));
        label.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        return label;
    }

}
