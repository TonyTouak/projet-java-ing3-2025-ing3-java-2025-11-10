package modele;

import java.time.LocalDate;

public class Client extends Utilisateur { //test
    protected String prenom;
    protected String adresse;
    protected String telephone;
    protected String numeroCarte;
    protected LocalDate dateInscription;

    public Client(int id, String nom, String prenom, String email, String motDePasse,
                  String adresse, String telephone, String numeroCarte, LocalDate dateInscription) {
        super(id, nom, email, motDePasse);
        this.prenom = prenom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.numeroCarte = numeroCarte;
        this.dateInscription = dateInscription;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getNumeroCarte() {
        return numeroCarte;
    }

    public LocalDate getDateInscription() {
        return dateInscription;
    }
}
