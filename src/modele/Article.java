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

    /**
     * Constructeur d'un article sans réduction.
     *
     * Initialise un article avec toutes ses caractéristiques principales (prix, marque, type, stock, etc.).
     *
     * @param id L'identifiant de l'article.
     * @param prixUnique Le prix unitaire de l'article.
     * @param prixVrac Le prix pour un lot (vrac) d'articles.
     * @param marque La marque de l'article.
     * @param quantiteVrac La quantité d'articles comprise dans un lot (vrac).
     * @param taille La taille de l'article.
     * @param type Le type de l'article (ex: t-shirt, pantalon...).
     * @param nom Le nom de l'article.
     * @param image Le chemin vers l'image de l'article.
     * @param sexe Le sexe associé à l'article (Homme/Femme).
     * @param quantite La quantité disponible en stock.
     */

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


    public Article(int id, double prixUnique, double prixVrac, String marque, int quantiteVrac, String taille, String type, String nom, String image, String sexe, int quantite, double reduction) {
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
        this.reduction = reduction;
    }

    public double getReduction() {
        return reduction;
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
     * Calcule le prix total en fonction des réductions et en prenant en compte les deux prix (prix en vrac et prix unitaire)
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
