package vue;

import modele.Client;

import javax.swing.*;
import java.awt.*;

public class clientVue extends JFrame {

    private Client client;

    public clientVue(Client client) {
        this.client = client;

        setTitle("Espace Client - " + client.getNom());
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
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

        JPanel panelWrapper = new JPanel(new GridBagLayout());
        panelWrapper.setBackground(new Color(255, 255, 255));

        JPanel infosPanel = new JPanel();
        infosPanel.setLayout(new BoxLayout(infosPanel, BoxLayout.Y_AXIS));
        infosPanel.setBackground(new Color(255, 255, 255));
        infosPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        infosPanel.setPreferredSize(new Dimension(500, 300));
        infosPanel.setMaximumSize(new Dimension(600, 400));

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

        panelWrapper.add(infosPanel);

        JScrollPane scroll = new JScrollPane(panelWrapper);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

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
