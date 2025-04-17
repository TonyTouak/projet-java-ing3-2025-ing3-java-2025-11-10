package vue;

import dao.*;
import modele.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class loginVue extends JFrame {
    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final utilisateurDao utilisateurDao;

    public loginVue() {
        setTitle("Bienvenue sur ShopTopie");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        DaoFactory daoFactory = DaoFactory.getInstance("shopping", "root", "");
        utilisateurDao = new utilisateurDaoImpl(daoFactory);

        // En-tête
        JPanel header = new JPanel();
        header.setBackground(new Color(30, 144, 255));
        header.setPreferredSize(new Dimension(1000, 150));
        JLabel title = new JLabel("Bienvenue sur ShopTopie");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        header.add(title);

        // Formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Email :"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Mot de passe :"), gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        // Boutons
        JButton btnConnexion = new JButton("Se connecter");
        JButton btnInscription = new JButton("S'inscrire");
        JButton btnContinuer = new JButton("Continuer sans se connecter");

        btnConnexion.addActionListener(e -> seConnecter());
        btnInscription.addActionListener(e -> {
            dispose();
            new inscriptionVue();
        });
        btnContinuer.addActionListener(e -> {
            dispose();
            new accueilVue(null);
        });

        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(btnConnexion);
        btnPanel.add(btnInscription);
        btnPanel.add(btnContinuer);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);

        // Ajout des panels
        add(header, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void seConnecter() {
        String email = emailField.getText().trim();
        String mdp = new String(passwordField.getPassword());

        Utilisateur utilisateur = utilisateurDao.chercher(email, mdp);
        if (utilisateur == null) {
            JOptionPane.showMessageDialog(this, "Identifiants incorrects.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DaoFactory daoFactory = DaoFactory.getInstance("shopping", "root", "");
        Client client = new clientDaoImpl(daoFactory).chercher(utilisateur.getId());

        if (client != null) {
            List<Commande> commandes = new commandeDaoImpl(daoFactory).getCommandesParClientID(client.getIDClient());
            commandes.forEach(client::ajouterCommande);
            new clientVue(client);
        } else {
            Administrateur admin = new administrateurDaoImpl(daoFactory).chercher(utilisateur.getId());
            if (admin != null) {
                new adminVue(admin);
            } else {
                JOptionPane.showMessageDialog(this, "Aucun rôle associé à cet utilisateur.");
            }
        }
        dispose();
    }
}
