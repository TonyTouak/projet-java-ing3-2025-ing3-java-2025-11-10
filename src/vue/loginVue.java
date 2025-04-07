package vue;

import dao.DaoFactory;
import dao.utilisateurDao;
import dao.utilisateurDaoImpl;
import modele.Utilisateur;
import dao.clientDao;
import dao.clientDaoImpl;
import dao.administrateurDao;
import dao.administrateurDaoImpl;

import modele.Administrateur;
import modele.Client;


import javax.swing.*;
import java.awt.*;

public class loginVue extends JFrame {
    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final utilisateurDao utilisateurDao;

    public loginVue() {
        setTitle("Bienvenue");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DaoFactory daoFactory = DaoFactory.getInstance("shopping", "root", ""); // pour tester, à mettre uniquement dans le main sinon
        utilisateurDao = new utilisateurDaoImpl(daoFactory);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 25));
        headerPanel.setBackground(new Color(30, 144, 255));
        headerPanel.setPreferredSize(new Dimension(0, 100));

        JLabel titleLabel = new JLabel("Bienvenue sur ShoppingApp");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        headerPanel.add(titleLabel);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(245, 245, 220));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel emailLabel = new JLabel("Email :");
        emailField = new JTextField(15);
        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(emailField, gbc);

        JLabel passwordLabel = new JLabel("Mot de passe :");
        passwordField = new JPasswordField(15);
        gbc.gridx = 0; gbc.gridy = 1;
        contentPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(passwordField, gbc);

        JButton boutonConnexion = new JButton("Se connecter");
        JButton boutonInscription = new JButton("S'inscrire");

        boutonConnexion.setPreferredSize(new Dimension(140, 40));
        boutonInscription.setPreferredSize(new Dimension(140, 40));

        JPanel boutonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        boutonsPanel.setBackground(new Color(245, 245, 220));
        boutonsPanel.add(boutonConnexion);
        boutonsPanel.add(boutonInscription);

        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(boutonsPanel, gbc);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(headerPanel, BorderLayout.NORTH);
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        boutonConnexion.addActionListener(e -> seConnecter());
        boutonInscription.addActionListener(e -> {
            dispose();
            new inscriptionVue();
        });

        setVisible(true);
    }

    private void seConnecter() {
        String email = emailField.getText().trim();
        String motDePasse = new String(passwordField.getPassword());

        System.out.println(email);
        System.out.println(motDePasse);

        Utilisateur utilisateur = utilisateurDao.chercher(email, motDePasse);
        if (utilisateur != null) {
            JOptionPane.showMessageDialog(this, "Bienvenue " + utilisateur.getNom() + " !");
            verifierEtRediriger(email, motDePasse);
        } else {
            JOptionPane.showMessageDialog(this, "Identifiants incorrects.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void verifierEtRediriger(String email, String motDePasse) {
        utilisateurDao utilisateurDao = new utilisateurDaoImpl(DaoFactory.getInstance("shopping","root",""));
        Utilisateur utilisateur = utilisateurDao.chercher(email, motDePasse);

        if (utilisateur != null) {
            clientDao clientDao = new clientDaoImpl(DaoFactory.getInstance("shopping","root",""));
            Client client = clientDao.chercher(utilisateur.getId());

            if (client != null) {
                new clientVue(client);
            } else {
                administrateurDao adminDao = new administrateurDaoImpl(DaoFactory.getInstance("shopping","root",""));
                Administrateur admin = adminDao.chercher(utilisateur.getId());

                if (admin != null) {
                    new adminVue(admin);
                } else {
                    JOptionPane.showMessageDialog(null, "Aucun rôle trouvé pour cet utilisateur.");
                }
            }

            dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Identifiants incorrects !");
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(loginVue::new);
    }
}
