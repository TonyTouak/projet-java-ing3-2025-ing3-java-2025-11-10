package dao;

import modele.Utilisateur;

import java.util.ArrayList;

public interface utilisateurDao {
    ArrayList<Utilisateur> getAll();
    Utilisateur chercher(String email, String motDePasse);
}
