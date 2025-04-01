package vue;

import javax.swing.*;
import java.awt.*;

public class magasinVue extends JFrame {

    public magasinVue() {
        setTitle("Bienvenue");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(30, 144, 255));
        headerPanel.setPreferredSize(new Dimension(0, 250));
        headerPanel.setLayout(null);

        JLabel titleLabel = new JLabel("Bienvenue sur notre magasin de shopping ");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        titleLabel.setBounds(500, 30, 1000, 40); // x, y, largeur, hauteur
        headerPanel.add(titleLabel);

        JButton boutonBoutique = new JButton("Boutique");
        JButton boutonInscription = new JButton("Connexion");
        boutonBoutique.setBounds(600, 150, 140, 45);
        boutonInscription.setBounds(800, 150, 140, 45);
        headerPanel.add(boutonBoutique);
        headerPanel.add(boutonInscription);
        boutonInscription.addActionListener(e -> {
            new loginVue();
            dispose();
        });


        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(245, 245, 220));
        contentPanel.setLayout(new GridBagLayout());


        contentPanel.setLayout(null);
        contentPanel.setBackground(new Color(245, 245, 220));




        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(headerPanel, BorderLayout.NORTH);
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(magasinVue::new);
    }
}
