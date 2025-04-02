package modele;

public class Administrateur extends Utilisateur {
    private int idAdmin;

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
