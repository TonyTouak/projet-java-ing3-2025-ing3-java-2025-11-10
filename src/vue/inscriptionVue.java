package vue;

import dao.DaoFactory;
import dao.clientDao;
import dao.clientDaoImpl;
import modele.Client;

import javax.swing.*;
import java.awt.*;

public class inscriptionVue extends JFrame {
    private JTextField nomField, emailField, adresseField, telephoneField;
    private JPasswordField passwordField;

    public inscriptionVue() {
        setTitle("Inscription");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 220));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nomField = addField(panel, gbc, "Nom :", 0);
        emailField = addField(panel, gbc, "Email :", 2);
        passwordField = new JPasswordField(15);
        addLabel(panel, gbc, "Mot de passe :", passwordField, 3);
        adresseField = addField(panel, gbc, "Adresse :", 4);
        telephoneField = addField(panel, gbc, "Téléphone :", 5);

        JButton validerButton = new JButton("S'inscrire");
        validerButton.addActionListener(e -> inscrire());

        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(validerButton, gbc);

        getContentPane().add(panel);
        setVisible(true);
    }

    private JTextField addField(JPanel panel, GridBagConstraints gbc, String label, int y) {
        JTextField field = new JTextField(15);
        addLabel(panel, gbc, label, field, y);
        return field;
    }

    private void addLabel(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int y) {
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void inscrire() {
        String nom = nomField.getText().trim();
        String email = emailField.getText().trim();
        String motDePasse = new String(passwordField.getPassword()).trim();
        String adresse = adresseField.getText().trim();
        String telephone = telephoneField.getText().trim();

        if (nom.isEmpty() || email.isEmpty() || motDePasse.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir les champs pour vous inscrire.");
            return;
        }

        DaoFactory daoFactory = DaoFactory.getInstance("shopping", "root", "");
        clientDao clientDao = new clientDaoImpl(daoFactory);

        Client nouveauClient = new Client(0, nom, email, motDePasse, 0, adresse, telephone);
        clientDao.ajouter(nouveauClient);


        JOptionPane.showMessageDialog(this, "Inscription réussie !");
        new clientVue(nouveauClient);
        dispose();
    }
}
