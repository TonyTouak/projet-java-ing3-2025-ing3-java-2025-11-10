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

    /**
     * Initialisation de l'interface de connexion des utilisateurs.
     */
    public loginVue() {
        setTitle("Bienvenue sur ShopTopie");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        DaoFactory daoFactory = DaoFactory.getInstance("shopping", "root", "");
        utilisateurDao = new utilisateurDaoImpl(daoFactory);

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(new Color(30, 144, 255));
        header.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));

        JLabel title = new JLabel("Bienvenue sur ShopTopie");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.add(title);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

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

        JButton btnConnexion = new JButton("Se connecter");
        JButton btnInscription = new JButton("S'inscrire");

        btnConnexion.addActionListener(e -> seConnecter());
        btnInscription.addActionListener(e -> {
            dispose();
            new inscriptionVue();
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(btnConnexion);
        btnPanel.add(btnInscription);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);

        add(header, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    /**
     * Vérifie les identifiants de connexion et redirige l'utilisateur vers son espace (client ou administrateur).
     */
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
                articleDaoImpl articleDao = new articleDaoImpl(daoFactory);
                clientDaoImpl clientDao = new clientDaoImpl(daoFactory);
                statistiqueDaoImpl statDao = new statistiqueDaoImpl(daoFactory);
                commandeDaoImpl commandeDao = new commandeDaoImpl(daoFactory);
                articleCommandeDaoImpl articleCommandeDao = new articleCommandeDaoImpl(daoFactory);


                new adminVue(admin, articleDao, clientDao, statDao, commandeDao, articleCommandeDao);
            } else {
                JOptionPane.showMessageDialog(this, "Aucun rôle associé à cet utilisateur.");
            }
        }
        dispose();
    }

}
