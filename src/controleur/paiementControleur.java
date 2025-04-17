package controleur;

import dao.paiementDao;
import modele.Client;
import modele.Paiement;
import modele.Panier;
import vue.paiementVue;
import java.util.Date;
import java.util.List;

public class paiementControleur {
    private final paiementVue vue;
    private final paiementDao dao;
    private final Client client;
    private final Panier panier;

    public paiementControleur(paiementVue vue, paiementDao dao, Client client, Panier panier) {
        this.vue = vue;
        this.dao = dao;
        this.client = client;
        this.panier = panier;
        initialiserVue();
    }

    private void initialiserVue() {
        List<Paiement> cartes = dao.getCartesEnregistrees(client.getId());
        if (cartes == null) {
            vue.afficherErreur("Impossible de charger les cartes enregistrées");
        } else {
            vue.afficherCartes(cartes);
        }
    }

    public void traiterPaiementAvecCarteExistante(int numeroCarte, int cvv, float montant) {
        if (!dao.verifierCVV(numeroCarte, cvv)) {
            vue.afficherErreur("Code CVV incorrect");
            return;
        }

        Paiement carte = dao.trouverParNumero(numeroCarte);
        if (carte == null || carte.getSolde() < montant) {
            vue.afficherErreur("Solde insuffisant");
            return;
        }

        if (!dao.debiterCarte(numeroCarte, montant)) {
            vue.afficherErreur("Erreur lors du débit");
            return;
        }

        vue.paiementReussi();
    }

    public void traiterPaiementAvecNouvelleCarte(Paiement nouvelleCarte, boolean enregistrer, float montant) {
        // On commence par vérifier que la carte n'a pas expiré
        if (nouvelleCarte.getDateValidite().before(new Date())) {
            vue.afficherErreur("La carte a expiré");
            return;
        }

        // On vérifie qu'il y a ensuite un solde suffisant
        if (nouvelleCarte.getSolde() < montant) {
            vue.afficherErreur("Solde insuffisant");
            return;
        }


        // on regarde si l'utilisateur peut l'enregistrer
        if (enregistrer) {
            Paiement carteex = dao.trouverParNumero(nouvelleCarte.getNumero());

            if (carteex == null) {
                vue.afficherErreur("Cette carte n'existe pas");
                return;
            }

            if (carteex.getIdClient() != 0) {
                vue.afficherErreur("Cette carte est déjà liée à un autre client");
                return;
            }

            if (!dao.lierCarteExistanteAuClient(nouvelleCarte.getNumero(), client.getId())) {
                vue.afficherErreur("Nous ne pouvons pas associer cette carte à votre compte");
                return;
            }
        }


        // on débite du solde de la carte le montant du paiement
        if (!dao.debiterCarte(nouvelleCarte.getNumero(), montant)) {
            vue.afficherErreur("Échec du paiement");
            return;
        }

        vue.paiementReussi();
    }

    public List<Paiement> getCartesClient() {
        return dao.getCartesEnregistrees(client.getId());
    }
}