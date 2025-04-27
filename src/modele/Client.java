package modele;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class Client extends Utilisateur {
    protected int IDClient;
    protected String adresse;
    protected String telephone;
    private List<Commande> commandes;


    public Client(int id, String nom, String email, String motDePasse,int IDClient,
                  String adresse, String telephone) {
        super(id, nom, email, motDePasse);
        this.IDClient = IDClient;
        this.adresse = adresse;
        this.telephone = telephone;
        this.commandes = new ArrayList<>();

    }

    //on ajoute un constructeur vide utilisé notamment pour l'inventaireVue
    public Client() {
        super();
    }


    public int getIDClient() {
        return IDClient;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setIDClient(int IDClient) {
        this.IDClient = IDClient;
    }

    public List<Commande> getCommandes() {
        return commandes;
    }

    public void ajouterCommande(Commande commande) {
        this.commandes.add(commande);
    }


    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
}