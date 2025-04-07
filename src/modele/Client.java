package modele;

import java.time.LocalDate;

public class Client extends Utilisateur {
    protected int IDClient;
    protected String adresse;
    protected String telephone;


    public Client(int id, String nom, String email, String motDePasse,int IDClient,
                  String adresse, String telephone) {
        super(id, nom, email, motDePasse);
        this.IDClient = IDClient;
        this.adresse = adresse;
        this.telephone = telephone;

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


}