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
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DaoFactory daoFactory = DaoFactory.getInstance("shopping", "root", "");
        utilisateurDao = new utilisateurDaoImpl(daoFactory);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 40));
        headerPanel.setBackground(new Color(30, 144, 255));
        headerPanel.setPreferredSize(new Dimension(0, 150));

        JLabel titleLabel = new JLabel("Bienvenue sur ShopTopie");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        headerPanel.add(titleLabel);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(245, 245, 220));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel emailLabel = new JLabel("Email :");
        emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        emailField = new JTextField(20);
        emailField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        emailField.setPreferredSize(new Dimension(300, 30));
        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(emailField, gbc);

        JLabel passwordLabel = new JLabel("Mot de passe :");
        passwordLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        passwordField.setPreferredSize(new Dimension(300, 30));
        gbc.gridx = 0; gbc.gridy = 1;
        contentPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(passwordField, gbc);

        JButton boutonConnexion = new JButton("Se connecter");
        JButton boutonInscription = new JButton("S'inscrire");
        JButton boutonContinuer = new JButton("Continuer sans se connecter");

        boutonConnexion.setFont(new Font("SansSerif", Font.BOLD, 16));
        boutonInscription.setFont(new Font("SansSerif", Font.BOLD, 16));
        boutonContinuer.setFont(new Font("SansSerif", Font.BOLD, 12));

        boutonConnexion.setPreferredSize(new Dimension(200, 50));
        boutonInscription.setPreferredSize(new Dimension(200, 50));
        boutonContinuer.setPreferredSize(new Dimension(250, 40));

        JPanel boutonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        boutonsPanel.setBackground(new Color(245, 245, 220));
        boutonsPanel.add(boutonConnexion);
        boutonsPanel.add(boutonInscription);
        boutonsPanel.add(boutonContinuer);

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
        boutonContinuer.addActionListener(e -> {
            dispose();
            new accueilVue(null); // remplace par accueilVue() si tu veux aller ailleurs
        });

        setVisible(true);
    }

    private void seConnecter() {
        String email = emailField.getText().trim();
        String motDePasse = new String(passwordField.getPassword());

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
