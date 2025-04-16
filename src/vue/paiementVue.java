package vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class paiementVue extends JFrame {

    private JTextField champNomTitulaire;
    private JTextField champNumeroCarte;
    private JTextField champDateValidite;
    private JTextField champCVV;
    private JButton boutonValider;

    public paiementVue() {
        setTitle("Paiement");
        setSize(450, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 🟦 Bande bleue en haut
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 25));
        headerPanel.setBackground(new Color(30, 144, 255));
        headerPanel.setPreferredSize(new Dimension(0, 80));

        JLabel titleLabel = new JLabel("Paiement sécurisé");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        headerPanel.add(titleLabel);

        // 🟫 Zone centrale
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(255, 255, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        champNomTitulaire = new JTextField();
        champNumeroCarte = new JTextField();
        champDateValidite = new JTextField(); // MM/AA
        champCVV = new JTextField();
        boutonValider = new JButton("Valider le paiement");

        // Champs
        contentPanel.add(new JLabel("Nom du titulaire :"), gbc); gbc.gridy++;
        contentPanel.add(champNomTitulaire, gbc); gbc.gridy++;

        contentPanel.add(new JLabel("Numéro de carte :"), gbc); gbc.gridy++;
        contentPanel.add(champNumeroCarte, gbc); gbc.gridy++;

        contentPanel.add(new JLabel("Date de validité (MM/AA) :"), gbc); gbc.gridy++;
        contentPanel.add(champDateValidite, gbc); gbc.gridy++;

        contentPanel.add(new JLabel("CVV :"), gbc); gbc.gridy++;
        contentPanel.add(champCVV, gbc); gbc.gridy++;

        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(boutonValider, gbc);

        // Mise en page
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(headerPanel, BorderLayout.NORTH);
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        // Action sur le bouton
        boutonValider.addActionListener(this::insererPaiementEnBase);

        setVisible(true);
    }

    private void insererPaiementEnBase(ActionEvent e) {
        String nomTitulaire = champNomTitulaire.getText();
        String numeroCarte = champNumeroCarte.getText();
        String dateValidite = champDateValidite.getText();
        String cvv = champCVV.getText();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/shopping", "root", "");
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO paiement (numero_carte, date_validite, cvv, nom_carte) VALUES (?, ?, ?, ?)")) {

            stmt.setString(1, numeroCarte);
            stmt.setString(2, dateValidite);
            stmt.setString(3, cvv);
            stmt.setString(4, nomTitulaire); // Ce champ est affiché comme "Nom du titulaire"

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Paiement enregistré avec succès !");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement du paiement.");
        }
    }
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets
    // tets







    public static void main(String[] args) {
        SwingUtilities.invokeLater(paiementVue::new);
    }
}
