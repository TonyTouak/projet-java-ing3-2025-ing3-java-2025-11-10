package modele;

public class ArticleCommande {
    private int idArticle;
    private int idCommande;
    private int quantite;
    private float prix;

    public ArticleCommande(int idArticle, int idCommande, int quantite, float prix) {
        this.idArticle = idArticle;
        this.idCommande = idCommande;
        this.quantite = quantite;
        this.prix = prix;
    }

    public int getIdArticle() { return idArticle; }
    public int getIdCommande() { return idCommande; }
    public int getQuantite() { return quantite; }
    public float getPrix() { return prix; }

    public void setIdCommande(int idCommande) { this.idCommande = idCommande; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    public void setPrix(float prix) { this.prix = prix; }
}