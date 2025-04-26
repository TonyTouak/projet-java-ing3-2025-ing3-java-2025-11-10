package modele;

public class Article {
    private int id;
    private double prixUnique;
    private double prixVrac;
    private String marque;
    private int quantiteVrac;
    private String taille;
    private String type;
    private String nom;
    private String image;
    private String sexe;
    private int quantite;
    private double reduction;

    public Article(int id, double prixUnique, double prixVrac, String marque, int quantiteVrac, String taille, String type, String nom, String image, String sexe, int quantite) {
        this.id = id;
        this.prixUnique = prixUnique;
        this.prixVrac = prixVrac;
        this.marque = marque;
        this.quantiteVrac = quantiteVrac;
        this.taille = taille;
        this.type = type;
        this.nom = nom;
        this.image = image;
        this.sexe = sexe;
        this.quantite = quantite;
        this.reduction = 0.0;
    }

    public double getReduction() {
        return reduction;
    }

    public void setReduction(double reduction) {
        this.reduction = reduction;
    }

    public double getPrixReduit() {
        return prixUnique * (1 - reduction / 100.0);
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

    public String getTaille() {
        return taille;
    }

    public String getType() {
        return type;
    }

    public String getNom() {
        return nom;
    }

    public String getImage() {
        return image;
    }

    public String getSexe() {
        return sexe;
    }

    public int getQuantite() {
        return quantite;
    }

    /**
     * Calcule le prix total en fonction des réductions en prenant en compte les deux prix (prix en vrac et prix unitaire)
     *
     * @param quantite = la quantité achetée
     * @return : Le prix total de la commande
     */
    public float calculerPrix(int quantite) {
        double prixUnitaire = getPrixReduit();
        if (quantiteVrac != 0) {
            int paquet = quantite / quantiteVrac;
            int quantiteUnique = quantite % quantiteVrac;
            return (float) ((paquet * prixVrac) + (quantiteUnique * prixUnitaire));
        } else {
            return (float) (prixUnitaire * quantite);
        }
    }

    public void setId(int anInt) {
        this.id = anInt;
    }

    public void setPrixUnique(double prixUnique) {
        this.prixUnique = prixUnique;
    }

    public void setPrixVrac(double prixVrac) {
        this.prixVrac = prixVrac;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public void setTaille(String taille) {
        this.taille = taille;
    }

    public void setQuantiteVrac(int quantiteVrac) {
        this.quantiteVrac = quantiteVrac;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
