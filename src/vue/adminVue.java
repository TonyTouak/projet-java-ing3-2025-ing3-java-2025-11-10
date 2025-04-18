package vue;

import modele.Administrateur;

import javax.swing.*;
import java.awt.*;

public class adminVue extends JFrame {

    public adminVue(Administrateur admin) {
        setTitle("Espace Administrateur - " + admin.getNom());
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel("Bienvenue admin " + admin.getNom() + " !");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);

        setVisible(true);
    }
}

/*package vue;

import javax.swing.*;
import java.awt.*;

public class adminVue extends JFrame {

    public adminVue() {
        // Fenêtre
        setTitle("Espace Administrateur - Admin");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE); // fond blanc

        // Bandeau supérieur bleu
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(0, 120, 215)); // bleu plus doux
        topPanel.setPreferredSize(new Dimension(600, 70));

        JLabel welcomeLabel = new JLabel("Espace Administrateur");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        welcomeLabel.setForeground(Color.WHITE);
        topPanel.add(welcomeLabel);
        add(topPanel, BorderLayout.NORTH);

        // Centre - panneau principal
        JPanel centerWrapper = new JPanel(new GridBagLayout()); // pour centrer verticalement
        centerWrapper.setBackground(Color.WHITE);
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        infoPanel.setMaximumSize(new Dimension(400, 300));

        // Infos
        JLabel idLabel = new JLabel("ID Admin : 0");
        JLabel roleLabel = new JLabel("Rôle : Administrateur");
        JLabel emailLabel = new JLabel("Email : admin@shoptopie.com");

        idLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        idLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(idLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(roleLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(emailLabel);
        infoPanel.add(Box.createVerticalStrut(25));

        // Titre des actions
        JLabel actionsLabel = new JLabel("Actions disponibles :");
        actionsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        actionsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(actionsLabel);
        infoPanel.add(Box.createVerticalStrut(15));

        // Boutons côte à côte
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnAdd = new JButton("Ajouter un article");
        JButton btnDelete = new JButton("Supprimer un article");
        JButton btnPromo = new JButton("Appliquer une promotion");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnPromo);
        infoPanel.add(buttonPanel);

        // Ajouter au wrapper pour centrage vertical
        centerWrapper.add(infoPanel);
        add(centerWrapper, BorderLayout.CENTER);

        // Bas - bouton déconnexion
        JButton logoutButton = new JButton("Déconnexion");
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(logoutButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(adminVue::new);
    }
}
*/