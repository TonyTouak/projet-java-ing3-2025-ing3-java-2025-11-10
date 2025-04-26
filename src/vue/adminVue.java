package vue;

import dao.*;
import modele.Administrateur;
import javax.swing.*;
import java.awt.*;
import controleur.adminControleur;


public class adminVue extends JFrame {

    private JButton btnInventaire, btnPromotions, btnClients, btnStatistiques, btnDeconnexion;

    public adminVue(Administrateur admin, articleDaoImpl articleDao, clientDaoImpl clientDao,
                    statistiqueDaoImpl statDao, commandeDaoImpl commandeDao, articleCommandeDaoImpl articleCommandeDao) {

        setTitle("Espace Administrateur - " + admin.getNom());
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        JPanel header = new JPanel();
        header.setBackground(new Color(30, 144, 255));
        header.setPreferredSize(new Dimension(1000, 80));
        header.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        JLabel titre = new JLabel("Espace Administrateur", SwingConstants.CENTER);
        titre.setForeground(Color.WHITE);
        titre.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.add(titre);
        add(header, BorderLayout.NORTH);

        JPanel mainContent = new JPanel(new BorderLayout(30, 0));
        mainContent.setBackground(Color.WHITE);
        mainContent.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(240, 248, 255));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        infoPanel.setPreferredSize(new Dimension(350, 0));

        JLabel infoTitle = new JLabel("Informations Administrateur");
        infoTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        infoTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        infoPanel.add(infoTitle);


        infoPanel.add(createLabel("🔑 ID Administrateur : " + admin.getIdAdmin()));
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(createLabel("🔑 ID Utilisateur : " + admin.getId()));
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(createLabel("👤 Nom : " + admin.getNom()));
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(createLabel("✉️ Email : " + admin.getEmail()));

        mainContent.add(infoPanel, BorderLayout.WEST);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);

        JLabel actionsTitle = new JLabel("Actions disponibles");
        actionsTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        actionsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        actionsTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        centerPanel.add(actionsTitle);

        JPanel buttonsGrid = new JPanel(new GridLayout(2, 2, 20, 20));
        buttonsGrid.setBackground(Color.WHITE);
        buttonsGrid.setMaximumSize(new Dimension(500, 200));

        btnInventaire = createIconButton("📦", "Inventaire");
        btnPromotions = createIconButton("💸", "Promotions");
        btnClients = createIconButton("👥", "Gestion clients");
        btnStatistiques = createIconButton("📊", "Statistiques");

        buttonsGrid.add(btnInventaire);
        buttonsGrid.add(btnPromotions);
        buttonsGrid.add(btnClients);
        buttonsGrid.add(btnStatistiques);

        centerPanel.add(buttonsGrid);
        mainContent.add(centerPanel, BorderLayout.CENTER);

        add(mainContent, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        btnDeconnexion = new JButton(" Déconnexion");
        btnDeconnexion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDeconnexion.setBackground(new Color(220, 53, 69));
        btnDeconnexion.setForeground(Color.WHITE);
        btnDeconnexion.setFocusPainted(false);
        btnDeconnexion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 0, 0)),
                BorderFactory.createEmptyBorder(8, 25, 8, 25)
        ));
        bottomPanel.add(btnDeconnexion);
        add(bottomPanel, BorderLayout.SOUTH);

        adminControleur controleur = new adminControleur(this, articleDao, clientDao,
                statDao, commandeDao, articleCommandeDao, admin);

        setVisible(true);
    }


    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 16));
        label.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        return label;
    }

    private JButton createIconButton(String icon, String text) {
        JButton button = new JButton(icon + " " + text);
        button.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        button.setPreferredSize(new Dimension(180, 80));
        return button;
    }



    public JButton getBtnInventaire() { return btnInventaire; }
    public JButton getBtnPromotions() { return btnPromotions; }
    public JButton getBtnClients() { return btnClients; }
    public JButton getBtnStatistiques() { return btnStatistiques; }
    public JButton getBtnDeconnexion() { return btnDeconnexion; }
}