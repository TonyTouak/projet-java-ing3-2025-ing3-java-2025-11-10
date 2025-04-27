package dao;

import modele.Paiement;

import java.util.List;

public interface paiementDao {
    boolean verifierCVV(int numero, int cvv);

    boolean debiterCarte(int numeroCarte, float montant);

    List<Paiement> getCartesEnregistrees(int idClient);

    Paiement trouverParNumero(int numeroCarte);

    boolean lierCarteExistanteAuClient(int numeroCarte, int idClient);
}