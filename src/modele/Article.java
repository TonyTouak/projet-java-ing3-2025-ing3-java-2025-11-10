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
    private String image; // correspond au chemin dans la base
    private String sexe;
    private int quantite;

    public Article(int id, double prixUnique, double prixVrac, String marque, int quantiteVrac,
                   String taille, String type, String nom, String image, String sexe, int quantite) {
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
    }

    // --- Getters ---

    public int getId() { return id; }
    public double getPrixUnique() { return prixUnique; }
    public double getPrixVrac() { return prixVrac; }
    public String getMarque() { return marque; }
    public int getQuantiteVrac() { return quantiteVrac; }
    public String getTaille() { return taille; }
    public String getType() { return type; }
    public String getNom() { return nom; }
    public String getImage() { return image; }
    public String getSexe() { return sexe; }
    public int getQuantite() { return quantite; }

    // Permet d'afficher l'image dans l'interface
    public String getImagePath() {
        return "Images/" + image;
    }

    // --- Setters ---

    public void setId(int id) { this.id = id; }
    public void setPrixUnique(double prixUnique) { this.prixUnique = prixUnique; }
    public void setPrixVrac(double prixVrac) { this.prixVrac = prixVrac; }
    public void setMarque(String marque) { this.marque = marque; }
    public void setQuantiteVrac(int quantiteVrac) { this.quantiteVrac = quantiteVrac; }
    public void setTaille(String taille) { this.taille = taille; }
    public void setType(String type) { this.type = type; }
    public void setNom(String nom) { this.nom = nom; }
    public void setImage(String image) { this.image = image; }
    public void setSexe(String sexe) { this.sexe = sexe; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    /**
     * Calcule le prix total selon quantité et prix unitaire ou prix vrac
     */
    public double calculerPrix(int quantite) {
        if (quantiteVrac != 0) {
            int paquet = quantite / quantiteVrac;
            int quantiteUnique = quantite % quantiteVrac;
            return (paquet * prixVrac) + (quantiteUnique * prixUnique);
        } else {
            return prixUnique * quantite;
        }
    }
}
