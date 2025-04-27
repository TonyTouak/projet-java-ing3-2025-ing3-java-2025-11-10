package modele;

public abstract class Utilisateur {
    protected int id;
    protected String nom;
    protected String email;
    protected String motDePasse;

    /**
     * Constructeur d'un utilisateur de la plateforme.
     *
     * @param id L'identifiant unique de l'utilisateur.
     * @param nom Le nom de l'utilisateur.
     * @param email L'adresse email de l'utilisateur.
     * @param motDePasse Le mot de passe de l'utilisateur.
     */

    public Utilisateur(int id, String nom, String email, String motDePasse) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
    }
    public int getId() {
        return id;
    }
    public String getNom() {
        return nom;
    }
    public String getEmail() {
        return email;
    }
    public String getMotDePasse() {
        return motDePasse;
    }

    public void setId(int anInt) {
        this.id = anInt;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Utilisateur() {
    }

}