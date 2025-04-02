package vue;

import javax.swing.*;
import java.awt.*;

public class produitVue extends JFrame {

    public produitVue(String nomProduit) {
        setTitle(nomProduit);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(30, 144, 255));
        headerPanel.setPreferredSize(new Dimension(0, 250));
        headerPanel.setLayout(null);

        JLabel titleLabel = new JLabel(nomProduit);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        titleLabel.setBounds(300, 30, 1000, 40);
        headerPanel.add(titleLabel);

        String imagePath = "";
        String prix = "";
        String stock = "";
        String avis = "";

        switch (nomProduit) {
            case "Nike Alphafly":
                imagePath = "images/Alphafly.png";
                prix = "250 €";
                stock = "10";
                avis = "Très confortable et parfait pour le marathon.";
                break;
            case "Brassière Femme":
                imagePath = "images/brassiere_femme.png";
                prix = "35 €";
                stock = "20";
                avis = "Bonne tenue, même après plusieurs lavages.";
                break;
            case "Chaussettes Sport":
                imagePath = "images/chaussettes.png";
                prix = "12 €";
                stock = "30";
                avis = "Très bon rapport qualité/prix.";
                break;
            case "Chaussures Nike":
                imagePath = "images/nike_chaussures.png";
                prix = "180 €";
                stock = "12";
                avis = "Design moderne et agréable à porter.";
                break;
            case "Short Sport":
                imagePath = "images/short_sport.png";
                prix = "40 €";
                stock = "8";
                avis = "Tissu léger, idéal pour l’été.";
                break;
            case "T-shirt Homme":
                imagePath = "images/teeshirt_homme.png";
                prix = "25 €";
                stock = "15";
                avis = "Simple, efficace et bien taillé.";
                break;
        }

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(245, 245, 220));
        contentPanel.setLayout(null);

        ImageIcon icon = new ImageIcon(imagePath);
        Image scaledImg = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImg));
        imageLabel.setBounds(100, 30, 200, 200);
        contentPanel.add(imageLabel);

        JLabel prixLabel = new JLabel("Prix : " + prix);
        prixLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        prixLabel.setBounds(350, 30, 300, 30);
        contentPanel.add(prixLabel);

        JLabel stockLabel = new JLabel("Quantité disponible : " + stock);
        stockLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        stockLabel.setBounds(350, 70, 300, 30);
        contentPanel.add(stockLabel);

        JLabel avisLabel = new JLabel("Avis client : " + avis);
        avisLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        avisLabel.setBounds(350, 110, 500, 60);
        contentPanel.add(avisLabel);

        JLabel tailleLabel = new JLabel("Choisir une taille :");
        tailleLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        tailleLabel.setBounds(350, 170, 150, 30);
        contentPanel.add(tailleLabel);

        JComboBox<String> tailleCombo = new JComboBox<>(new String[] {"S", "M", "L"});
        tailleCombo.setBounds(500, 170, 60, 30);
        contentPanel.add(tailleCombo);

        JButton panierButton = new JButton("Ajouter au panier");
        panierButton.setBounds(350, 220, 160, 40);
        panierButton.addActionListener(e -> {
            String tailleChoisie = (String) tailleCombo.getSelectedItem();
            JOptionPane.showMessageDialog(this, "Ajouté au panier : " + nomProduit + " - Taille " + tailleChoisie);
        });
        contentPanel.add(panierButton);

        JButton retourButton = new JButton("Retour à la boutique");
        retourButton.setBounds(530, 220, 180, 40);
        retourButton.addActionListener(e -> {
            new magasinVue();
            dispose();
        });
        contentPanel.add(retourButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(headerPanel, BorderLayout.NORTH);
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }
}