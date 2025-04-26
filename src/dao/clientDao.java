package dao;

import modele.Client;

import java.util.ArrayList;

public interface clientDao {
    ArrayList<Client> getAll();

    void ajouter(Client client);

    Client chercher(int id);

    Client chercherIDCLient(int idClient);

    Client modifier(Client client);

    void supprimer(Client client);


    boolean supprimerID(int idClient);
}
