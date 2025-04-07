package vue;

import modele.Administrateur;

import javax.swing.*;
import java.awt.*;

public class adminVue extends JFrame {

    public adminVue(Administrateur admin) {
        setTitle("Espace Administrateur - " + admin.getNom());
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel("Bienvenue admin " + admin.getNom() + " !");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);

        setVisible(true);
    }
}
