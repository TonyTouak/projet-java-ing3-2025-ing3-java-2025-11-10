package vue;

import modele.Client;

import javax.swing.*;

public class panierVue extends JFrame {

    private Client client;

    public panierVue(Client client) {

        menuVue menuVue = new menuVue(client, this);
        setJMenuBar(menuVue.creerMenuBar());

        setVisible(true);
    }

}
