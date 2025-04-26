package modele;

import java.util.Date;

public class Commande {
    private int id;
    private Client client;
    private Date date;
    private float prix;

    public Commande(int id, Client client, Date date, float prix) {
        this.id = id;
        this.client = client;
        this.date = date;
        this.prix = prix;
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

    public void setId(int anInt) {
        this.id = anInt;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPrix(float v) {
        this.prix = v;
    }

    public void setClient(Client client) {
        this.client = client;
    }

}