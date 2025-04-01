package modele;

import java.util.Date;

public class Commande {
    private int id;
    private Client client;
    private Article article;
    private Date date;
    private double prixFinal;
    private int quantite;

    public Commande(int id, Client client, Article article, Date date, double prixFinal, int quantite) {
        this.id = id;
        this.client = client;
        this.article = article;
        this.date = date;
        this.prixFinal = prixFinal;
        this.quantite = quantite;
    }
    public int getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public Article getArticle() {
        return article;
    }

    public Date getDate() {
        return date;
    }

    public double getPrixFinal() {
        return prixFinal;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setId(int anInt) {
        this.id = anInt;
    }
}