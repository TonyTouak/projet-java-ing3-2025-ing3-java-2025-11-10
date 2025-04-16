package dao;

import modele.Commande;

import java.util.ArrayList;
import java.util.List;

public interface commandeDao {
    ArrayList<Commande> getAll();

    void ajouter(Commande commande);

    Commande chercher(int idCommande);

    void supprimer(Commande commande);

    List<Commande> getCommandesParClientID(int idClient);
}
