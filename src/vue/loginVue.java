package vue;

import javax.swing.*;
import java.awt.*;

public class loginVue extends JFrame {

    public loginVue() {
        setTitle("Bienvenue");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 🟦 Bande bleue flexible
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 25));
        headerPanel.setBackground(new Color(30, 144, 255));
        headerPanel.setPreferredSize(new Dimension(0, 250)); // 0 en largeur = s'étend automatiquement

        JLabel titleLabel = new JLabel("Bienvenue sur ShoppingApp");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        headerPanel.add(titleLabel);

        // 🟫 Zone centrale beige
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(245, 245, 220));
        contentPanel.setLayout(new GridBagLayout());

        JButton boutonConnexion = new JButton("Se connecter");
        JButton boutonInscription = new JButton("S'inscrire");

        boutonConnexion.setPreferredSize(new Dimension(140, 45));
        boutonInscription.setPreferredSize(new Dimension(140, 45));

        JPanel boutonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        boutonsPanel.setBackground(new Color(245, 245, 220));
        boutonsPanel.add(boutonConnexion);
        boutonsPanel.add(boutonInscription);

        contentPanel.add(boutonsPanel);

        // 🧱 Assemblage avec BorderLayout
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(headerPanel, BorderLayout.NORTH);
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(loginVue::new);
    }
}
