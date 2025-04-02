package dao;

import modele.Administrateur;

import java.util.ArrayList;

public interface administrateurDao {
    ArrayList<Administrateur> getAll();

    void ajouter(Administrateur admin);

    Administrateur chercher(int idAdmin);

    void supprimer(Administrateur admin);
}
