package modele;

public class ArticleCommande {
    private int idArticle;
    private int idCommande;
    private int quantite;
    private float prix;

    /**
     * Constructeur d'un ArticleCommande.
     *
     * Initialise une ligne d'une commande, associant un article, une commande, une quantité et un prix.
     *
     * @param idArticle L'identifiant de l'article commandé.
     * @param idCommande L'identifiant de la commande à laquelle l'article appartient.
     * @param quantite Le nombre d'unités de l'article commandées.
     * @param prix Le prix total payé pour cette quantité d'article.
     */

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