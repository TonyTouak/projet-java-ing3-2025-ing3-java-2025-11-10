package modele;

import java.util.Date;

public class Paiement {
    private String nomCarte;
    private int numero;
    private Date dateValidite;
    private int cvv;
    private int idClient;
    private float solde;

    public Paiement(String nomCarte, int numero, Date dateValidite,
                    int cvv, float solde) {
        this.nomCarte = nomCarte;
        this.numero = numero;
        this.dateValidite = dateValidite;
        this.cvv = cvv;
        this.solde = solde;
    }

    public Paiement(String nomCarte, int numero, Date dateValidite,
                    int cvv, int ID_Client, float solde) {
        this.nomCarte = nomCarte;
        this.numero = numero;
        this.dateValidite = dateValidite;
        this.cvv = cvv;
        this.idClient = ID_Client;
        this.solde = solde;
    }

    public String getNomCarte() {
        return nomCarte;
    }

    public int getNumero() {
        return numero;
    }

    public Date getDateValidite() {
        return dateValidite;
    }

    public int getCvv() {
        return cvv;
    }

    public int getIdClient() {
        return idClient;
    }

    public float getSolde() {
        return solde;
    }

    public void setIdClient(int id) {
        idClient = id;
    }
}