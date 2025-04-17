package modele;

import java.util.Date;

public class Commande {
    private int id;
    private Client client;
    private Date date;
    private float prix;
    private int quantite;

    public Commande(int id, Client client, Date date, float prix, int quantite) {
        this.id = id;
        this.client = client;
        this.date = date;
        this.prix = prix;
        this.quantite = quantite;
    }

    public Commande() {
    }

    public int getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public Date getDate() {
        return date;
    }

    public float getPrix() {
        return prix;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setId(int anInt) {
        this.id = anInt;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPrix(float v) {
        this.prix = v;
    }

    public void setQuantite(int i) {
        this.quantite = i;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}