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

    @Override
    public List<Paiement> trouverCartesParClient(int idClient) {
        return getCartesEnregistrees(idClient);
    }

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

    @Override
    public boolean debiterCarte(int numeroCarte, float montant) {
        return debiterCarte(String.valueOf(numeroCarte), (double)montant);
    }

    @Override
    public boolean carteExiste(int numeroCarte) {
        String sql = "SELECT 1 FROM paiement WHERE numero = ?";

        try (Connection connection = daoFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, numeroCarte);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Erreur vérification existence carte: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean ajouterCarte(Paiement carte) {
        String sql = "INSERT INTO paiement (nom_carte, numero, date_validite, cvv, IDClient, solde) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = daoFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, carte.getNomCarte());
            ps.setInt(2, carte.getNumero());
            ps.setDate(3, new java.sql.Date(carte.getDateValidite().getTime()));
            ps.setInt(4, carte.getCvv());
            ps.setInt(5, carte.getIdClient());
            ps.setFloat(6, carte.getSolde());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur ajout carte: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean debiterCarte(String numeroCarte, double montant) {
        String sql = "UPDATE paiement SET solde = solde - ? WHERE numero = ? AND solde >= ?";

        try (Connection connection = daoFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setDouble(1, montant);
            ps.setString(2, numeroCarte);
            ps.setDouble(3, montant);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur débit carte: " + e.getMessage());
            return false;
        }
    }

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

    @Override
    public boolean carteExistePourAutreClient(int numeroCarte, int idClient) {
        String sql = "SELECT 1 FROM paiement WHERE numero = ? AND IDClient != ?";

        try (Connection connection = daoFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, numeroCarte);
            ps.setInt(2, idClient);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Erreur vérification carte autre client: " + e.getMessage());
            return false;
        }
    }

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