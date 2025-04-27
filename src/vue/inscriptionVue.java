package vue;

import dao.DaoFactory;
import dao.clientDao;
import dao.clientDaoImpl;
import modele.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class inscriptionVue extends JFrame {
    private JTextField nomField, emailField, adresseField, telephoneField;
    private JPasswordField passwordField;
    private JButton validerButton;

    /**
     * Initialise l'interface de création d'un compte client.
     */
    public inscriptionVue() {
        setTitle("Inscription");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);

        JPanel header = new JPanel();
        header.setBackground(new Color(30, 144, 255));
        header.setPreferredSize(new Dimension(1000, 80));
        JLabel titre = new JLabel("Créer un compte");
        titre.setForeground(Color.WHITE);
        titre.setFont(new Font("SansSerif", Font.BOLD, 26));
        header.add(titre);
        add(header, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        nomField = Formulaire(contentPanel, gbc, "👤 Nom :", 0);
        emailField = Formulaire(contentPanel, gbc, "📧 Email :", 1);
        passwordField = MDP(contentPanel, gbc, "🔒 Mot de passe :", 2);
        adresseField = Formulaire(contentPanel, gbc, "📍 Adresse :", 3);
        telephoneField = Formulaire(contentPanel, gbc, "📞 Téléphone :", 4);

        validerButton = new JButton("S'inscrire");
        validerButton.setBackground(new Color(30, 144, 255));
        validerButton.setForeground(Color.WHITE);
        validerButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        validerButton.setPreferredSize(new Dimension(200, 40));
        validerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inscrire();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(validerButton, gbc);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(Color.WHITE);
        JLabel compteLabel = new JLabel("Déjà un compte ?");
        JButton loginButton = new JButton("Se connecter");
        loginButton.setBorder(BorderFactory.createEmptyBorder());
        loginButton.setContentAreaFilled(false);
        loginButton.setForeground(new Color(30, 144, 255));
        loginButton.addActionListener(e -> {
            dispose();
            new loginVue();
        });

        footerPanel.add(compteLabel);
        footerPanel.add(loginButton);

        add(contentPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JTextField Formulaire(JPanel panel, GridBagConstraints gbc, String label, int row) {
        JTextField field = new JTextField(20);
        field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(jLabel, gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);

        return field;
    }

    private JPasswordField MDP(JPanel panel, GridBagConstraints gbc, String label, int row) {
        JPasswordField field = new JPasswordField(20);
        field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(jLabel, gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);

        return field;
    }

    /**
     * Traite l'inscription d'un nouvel utilisateur dans la base de données.
     */
    private void inscrire() {
        String nom = nomField.getText().trim();
        String email = emailField.getText().trim();
        String motDePasse = new String(passwordField.getPassword()).trim();
        String adresse = adresseField.getText().trim();
        String telephone = telephoneField.getText().trim();

        if (nom.isEmpty() || email.isEmpty() || motDePasse.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez remplir tous les champs obligatoires",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        DaoFactory daoFactory = DaoFactory.getInstance("shopping", "root", "");
        clientDao clientDao = new clientDaoImpl(daoFactory);

        Client nouveauClient = new Client(0, nom, email, motDePasse, 0, adresse, telephone);
        clientDao.ajouter(nouveauClient);

        JOptionPane.showMessageDialog(this,
                "Inscription réussie ! Bienvenue " + nom + " !",
                "Félicitations",
                JOptionPane.INFORMATION_MESSAGE);

        new clientVue(nouveauClient);
        dispose();
    }
}