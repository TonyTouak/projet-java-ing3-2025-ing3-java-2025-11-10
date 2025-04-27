package modele;


public class Administrateur extends Utilisateur {

    private int idAdmin;

    /**
     * Constructeur de l'administrateur.
     *
     * Initialise un administrateur avec ses informations personnelles et son identifiant spécifique d'administrateur.
     *
     * @param id L'identifiant de l'utilisateur.
     * @param nom Le nom de l'administrateur.
     * @param email L'email de l'administrateur.
     * @param motDePasse Le mot de passe de l'administrateur.
     * @param idAdmin L'identifiant spécifique de l'administrateur.
     */

    public Administrateur(int id, String nom, String email, String motDePasse, int idAdmin) {
        super(id, nom, email, motDePasse);
        this.idAdmin = idAdmin;
    }

    public int getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
    }
}
