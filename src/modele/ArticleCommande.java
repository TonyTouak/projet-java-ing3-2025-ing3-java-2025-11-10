package modele;

public class ArticleCommande {
    private int idArticle;
    private int idCommande;
    private int quantite;

    public ArticleCommande(int idArticle, int idCommande, int quantite) {
        this.idArticle = idArticle;
        this.idCommande = idCommande;
        this.quantite = quantite;
    }

    public int getIdArticle() { return idArticle; }
    public int getIdCommande() { return idCommande; }
    public int getQuantite() { return quantite; }

    public void setIdArticle(int idArticle) { this.idArticle = idArticle; }
    public void setIdCommande(int idCommande) { this.idCommande = idCommande; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
}