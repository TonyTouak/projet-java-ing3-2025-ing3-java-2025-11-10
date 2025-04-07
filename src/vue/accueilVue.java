package vue;

import modele.Client;

import javax.swing.*;

public class accueilVue extends JFrame {
    private Client client;

    public accueilVue(Client client) {
        this.client = client;
        setTitle("Accueil");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        menuVue menuVue = new menuVue(client, this);
        setJMenuBar(menuVue.creerMenuBar());

        setVisible(true);
    }
}

