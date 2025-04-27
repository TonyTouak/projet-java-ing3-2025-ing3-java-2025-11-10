package dao;

import modele.Paiement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class paiementDaoImpl implements paiementDao {
    private final DaoFactory daoFactory;

    public paiementDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /**
     * Vérifie si le CVV est bon pour les clients qui ont enregistré une carte
     *
     * @param numeroCarte : Le numéro de la carte bancaire (la clé)
     * @param cvv : Le code de vérification
     * @return : true si les informations sont correctes, sinon false
     */
    @Override
    public boolean verifierCVV(int numeroCarte, int cvv) {
        String sql = "SELECT 1 FROM paiement WHERE numero = ? AND cvv = ?";

        try (Connection connection = daoFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, numeroCarte);
            ps.setInt(2, cvv);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Erreur vérification CVV: " + e.getMessage());
            return false;
        }
    }


    /**
     * Débite un montant à un compte (carte)
     *
     * @param numeroCarte : Le numéro de la carte (clé)
     * @param montant : Le montant à débiter
     * @return : true si le débit est réussi, sinon false
     */
    @Override
    public boolean debiterCarte(int numeroCarte, float montant) {
        String sql = "UPDATE paiement SET solde = solde - ? WHERE numero = ? AND solde >= ?";

        try (Connection connection = daoFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setFloat(1, montant);
            ps.setInt(2, numeroCarte);
            ps.setFloat(3, montant);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur débit carte: " + e.getMessage());
            return false;
        }
    }

    /**
     * Récupère toutes les cartes enregistrées pour un client
     *
     * @param idClient : L'identifiant du client
     * @return : Une liste des cartes enregistrées
     */
    @Override
    public List<Paiement> getCartesEnregistrees(int idClient) {
        String sql = "SELECT nom_carte, numero, date_validite, cvv, IDClient, solde FROM paiement WHERE IDClient = ?";
        List<Paiement> cartes = new ArrayList<>();

        try (Connection connection = daoFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, idClient);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    cartes.add(new Paiement(
                            rs.getString("nom_carte"),
                            rs.getInt("numero"),
                            rs.getDate("date_validite"),
                            rs.getInt("cvv"),
                            rs.getInt("IDClient"),
                            rs.getFloat("solde")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération cartes: " + e.getMessage());
        }
        return cartes;
    }

    /**
     * Cherche une carte via son numéro
     *
     * @param numeroCarte : Le numéro de la carte
     * @return : Un objet `Paiement` représentant la carte, un objet null si la carte n'est pas trouvée
     */
    @Override
    public Paiement trouverParNumero(int numeroCarte) {
        String sql = "SELECT nom_carte, numero, date_validite, cvv, IDClient, solde FROM paiement WHERE numero = ?";

        try (Connection connection = daoFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, numeroCarte);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Paiement(
                            rs.getString("nom_carte"),
                            rs.getInt("numero"),
                            rs.getDate("date_validite"),
                            rs.getInt("cvv"),
                            rs.getInt("IDClient"),
                            rs.getFloat("solde")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur recherche carte: " + e.getMessage());
        }
        return null;
    }

    /**
     * Associe une carte bancaire  à un client si elle n'est pas déjà liée à un autre compte.
     *
     * @param numeroCarte : Le numéro de la carte bancaire
     * @param idClient : L'identifiant du client auquel la carte doit être associée.
     * @return : true si la mise à jour est réussie, sinon false
     */
    @Override
    public boolean lierCarteExistanteAuClient(int numeroCarte, int idClient) {
        String sql = "UPDATE paiement SET IDClient = ? WHERE numero = ? AND IDClient IS NULL";

        try (Connection connection = daoFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, idClient);
            ps.setInt(2, numeroCarte);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur liaison carte au client: " + e.getMessage());
            return false;
        }
    }

}