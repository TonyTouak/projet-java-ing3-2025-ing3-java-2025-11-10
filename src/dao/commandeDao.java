package dao;

import modele.Article;
import modele.Client;
import modele.Commande;
import modele.Panier;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public interface commandeDao {
    public ArrayList<Commande> getAll() throws SQLException;

    int ajouter(Commande commande)throws SQLException ;

    Commande chercher(int idCommande) throws SQLException;

    void supprimer(Commande commande) throws SQLException;

    boolean validerCommandeComplete(Client client, Map<Article, Integer> articlesPanier);


    void finaliserCommande(Client client, Panier panier, articleCommandeDao articleCmdDao, articleDao articleDao);

    Commande creerCommandeDepuisResultSet(ResultSet rs) throws SQLException;

    int creerNouvelleCommande(int idClient, float prixTotal) throws SQLException;

}
