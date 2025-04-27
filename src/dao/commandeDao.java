package dao;

import modele.Article;
import modele.Client;
import modele.Commande;
import modele.Panier;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public interface commandeDao {
    ArrayList<Commande> getAll() throws SQLException;

    int ajouter(Commande commande)throws SQLException ;

    List<Commande> getCommandesParClient(int idClient);

    Commande chercher(int idCommande) throws SQLException;

    void supprimer(Commande commande) throws SQLException;

    void finaliserCommande(Client client, Panier panier, articleCommandeDao articleCmdDao, articleDao articleDao);

    Commande creerCommandeDepuisResultSet(ResultSet rs) throws SQLException;

    int creerNouvelleCommande(int idClient, float prixTotal) throws SQLException;

    List<Commande> getCommandesParClientID(int idClient);

    void mettreAJour(Commande commande) throws SQLException;
}
