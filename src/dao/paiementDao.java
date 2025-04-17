package dao;

import modele.Paiement;

import java.util.List;

public interface paiementDao {
    List<Paiement> trouverCartesParClient(int idClient);
    boolean verifierCVV(int numero, int cvv);
    boolean debiterCarte(int numeroCarte, float montant);
    boolean carteExiste(int numeroCarte);
    boolean ajouterCarte(Paiement carte);
    boolean debiterCarte(String numeroCarte, double montant);

    List<Paiement> getCartesEnregistrees(int idClient);

    Paiement trouverParNumero(int numeroCarte);

    boolean carteExistePourAutreClient(int numeroCarte, int idClient);

    boolean lierCarteExistanteAuClient(int numeroCarte, int idClient);
}