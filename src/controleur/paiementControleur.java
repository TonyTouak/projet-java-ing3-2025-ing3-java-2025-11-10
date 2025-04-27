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

    /**
     * Constructeur du contrôleur de paiement.
     *
     * Initialise le contrôleur avec la vue de paiement, le DAO pour les paiements,
     * le client connecté et son panier. Lance l'initialisation de la vue pour afficher
     * les cartes enregistrées du client.
     *
     * @param vue La vue utilisée pour afficher et gérer le paiement.
     * @param dao Le DAO pour accéder aux données de paiement.
     * @param client Le client réalisant le paiement.
     * @param panier Le panier contenant les articles à payer.
     */

    public paiementControleur(paiementVue vue, paiementDao dao, Client client, Panier panier) {
        this.vue = vue;
        this.dao = dao;
        this.client = client;
        this.panier = panier;
        initialiserVue();
    }

    /**
     * Initialise la vue en chargeant les cartes enregistrées du client depuis la base de données.
     * Affiche une erreur si le chargement échoue.
     */
    private void initialiserVue() {
        List<Paiement> cartes = dao.getCartesEnregistrees(client.getId());
        if (cartes == null) {
            vue.afficherErreur("Impossible de charger les cartes enregistrées");
        } else {
            vue.afficherCartes(cartes);
        }
    }

    /**
     * Traite un paiement en utilisant une carte existante enregistrée.
     * Vérifie le CVV, le solde disponible, et débite la carte si toutes les vérifications sont réussies.
     *
     * @param numeroCarte numéro de la carte utilisée
     * @param cvv code de sécurité CVV fourni par l'utilisateur
     * @param montant montant à débiter
     */
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

    /**
     * Traite un paiement en utilisant une nouvelle carte, avec option d'enregistrement de la carte pour le client.
     * Vérifie la validité de la carte, le solde disponible, et débite la carte.
     *
     * @param nouvelleCarte objet Paiement représentant la carte
     * @param enregistrer true si l'utilisateur souhaite enregistrer la carte, false sinon
     * @param montant montant à débiter
     */
    public void traiterPaiementAvecNouvelleCarte(Paiement nouvelleCarte, boolean enregistrer, float montant) {
        if (nouvelleCarte.getDateValidite().before(new Date())) {
            vue.afficherErreur("La carte a expiré");
            return;
        }

        if (nouvelleCarte.getSolde() < montant) {
            vue.afficherErreur("Solde insuffisant");
            return;
        }

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