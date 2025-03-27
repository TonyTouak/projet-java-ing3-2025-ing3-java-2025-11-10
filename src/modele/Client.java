package modele;

public class Client extends Utilisateur {
    protected String adresse;
    protected String telephone;

    public Client(int id, String nom, String email, String motDePasse, String adresse, String telephone) {
        super(id, nom, email, motDePasse);
        this.adresse = adresse;
        this.telephone = telephone;
    }
    public String getAdresse() {
        return adresse;
    }
    public String getTelephone() {
        return telephone;
    }
}