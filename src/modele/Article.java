package modele;

public class Article {
    private int id;
    private double prixUnique;
    private double prixVrac;
    private String marque;
    private int quantiteVrac;

    public Article(int id, double prixUnique, double prixVrac, String marque,int quantiteVrac) {
        this.id = id;
        this.prixUnique = prixUnique;
        this.prixVrac = prixVrac;
        this.marque = marque;
        this.quantiteVrac = quantiteVrac;
    }

    public int getId() {
        return id;
    }

    public double getPrixUnique() {
        return prixUnique;
    }

    public double getPrixVrac() {
        return prixVrac;
    }

    public String getMarque() {
        return marque;
    }

    public int getQuantiteVrac() {
        return quantiteVrac;
    }


    /**
     * Calcule le prix total en fonction des réductions en prenant en compte les deux prix (prix en vrac et prix unitaire)
     *
     * @param quantite = la quantité achetée
     * @return : Le prix total de la commande
     */
    public double calculerPrix(int quantite) {
        int paquet,quantiteUnique;
        paquet = quantite / quantiteVrac;
        quantiteUnique = quantite % quantiteVrac;
        return (paquet * prixVrac) + (quantiteUnique * prixUnique);
    }
}