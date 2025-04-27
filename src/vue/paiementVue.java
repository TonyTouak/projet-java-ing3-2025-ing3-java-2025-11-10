package vue;

import controleur.paiementControleur;
import dao.DaoFactory;
import dao.paiementDaoImpl;
import modele.Client;
import modele.Paiement;
import modele.Panier;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Consumer;

public class paiementVue extends JFrame {
    private JTextField champNomTitulaire;
    private JTextField champNumeroCarte;
    private JTextField champDateValidite;
    private JTextField champCVV;
    private JTextField champCVVNouvelleCarte;
    private JCheckBox caseEnregistrerCarte;
    private JButton boutonPayer;
    private JPanel cartesPanel;
    private ButtonGroup groupeCartes;
    private float montantTotal;
    private Client client;
    private Panier panier;
    private paiementControleur controleur;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yy");
    private final Consumer<Boolean> callback;
    private ButtonGroup groupeMethodes;
    private JRadioButton existingCardRadio;
    private JRadioButton newCardRadio;

    /**
     * Constructeur de paiementVue.
     *
     * @param client : le client en cours de paiement
     * @param panier : le panier associé au client
     * @param montantTotal : le montant total à payer
     * @param callback : callback pour notifier du succès du paiement
     */
    public paiementVue(Client client, Panier panier, float montantTotal, Consumer<Boolean> callback) {
        this.client = client;
        this.panier = panier;
        this.montantTotal = montantTotal;
        this.callback = callback;



        this.controleur = new paiementControleur(
                this,
                new paiementDaoImpl(DaoFactory.getInstance("shopping", "root", "")),
                client,
                panier
        );

        initialisationVue();
        chargerCartesEnregistrees();
    }

    /**
     * Configure l'interface graphique de la fenêtre de paiement.
     */
    private void initialisationVue() {
        setTitle("Paiement sécurisé");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel header = new JPanel();
        header.setBackground(new Color(30, 144, 255));
        header.setPreferredSize(new Dimension(0, 80));
        JLabel titre = new JLabel("Paiement - " + String.format("%.2f€", montantTotal));
        titre.setForeground(Color.WHITE);
        titre.setFont(new Font("SansSerif", Font.BOLD, 20));
        header.add(titre);
        add(header, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel choixPaiementPanel = new JPanel();
        choixPaiementPanel.setLayout(new BoxLayout(choixPaiementPanel, BoxLayout.Y_AXIS));
        choixPaiementPanel.setBorder(BorderFactory.createTitledBorder("Méthode de paiement"));

        groupeMethodes = new ButtonGroup();

        existingCardRadio = new JRadioButton("Utiliser une carte enregistrée");
        existingCardRadio.setActionCommand("existante");
        groupeMethodes.add(existingCardRadio);
        choixPaiementPanel.add(existingCardRadio);

        cartesPanel = new JPanel();
        cartesPanel.setLayout(new BoxLayout(cartesPanel, BoxLayout.Y_AXIS));
        cartesPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 10));
        cartesPanel.setVisible(false);

        groupeCartes = new ButtonGroup();
        List<Paiement> cartes = controleur.getCartesClient();
        for (Paiement carte : cartes) {
            JRadioButton radioCarte = new JRadioButton(
                    carte.getNomCarte() + " - ****" + carte.getNumero() % 10000 +
                            " Exp: " + dateFormat.format(carte.getDateValidite()) +
                            " | Solde: " + carte.getSolde() + "€"
            );
            radioCarte.setActionCommand(String.valueOf(carte.getNumero()));
            groupeCartes.add(radioCarte);
            cartesPanel.add(radioCarte);
        }

        choixPaiementPanel.add(cartesPanel);

        newCardRadio = new JRadioButton("Utiliser une nouvelle carte");
        newCardRadio.setActionCommand("nouvelle");
        groupeMethodes.add(newCardRadio);
        choixPaiementPanel.add(newCardRadio);

        JPanel newCardPanel = new JPanel();
        newCardPanel.setLayout(new BoxLayout(newCardPanel, BoxLayout.Y_AXIS));
        newCardPanel.setBorder(BorderFactory.createTitledBorder("Détails de la carte"));
        newCardPanel.setVisible(false);

        champNomTitulaire = new JTextField(20);
        champNumeroCarte = new JTextField(20);
        champDateValidite = new JTextField(5);
        champCVVNouvelleCarte = new JTextField(3);
        caseEnregistrerCarte = new JCheckBox("Enregistrer cette carte pour les paiements futurs");

        newCardPanel.add(creerChamp("Nom titulaire:", champNomTitulaire));
        newCardPanel.add(creerChamp("Numéro de carte:", champNumeroCarte));
        newCardPanel.add(creerChamp("Date d'expiration (MM/AA):", champDateValidite));
        newCardPanel.add(creerChamp("Code de sécurité (CVV):", champCVVNouvelleCarte));
        newCardPanel.add(caseEnregistrerCarte);

        ItemListener methodListener = e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String choix = ((JRadioButton)e.getSource()).getActionCommand();
                cartesPanel.setVisible("existante".equals(choix));
                newCardPanel.setVisible("nouvelle".equals(choix));
            }
        };
        existingCardRadio.addItemListener(methodListener);
        newCardRadio.addItemListener(methodListener);

        boutonPayer = new JButton("Payer maintenant");
        boutonPayer.setBackground(new Color(30, 144, 255));
        boutonPayer.setForeground(Color.WHITE);
        boutonPayer.addActionListener(e -> validerPaiement());

        content.add(choixPaiementPanel);
        content.add(Box.createVerticalStrut(15));
        content.add(newCardPanel);
        content.add(Box.createVerticalGlue());
        content.add(boutonPayer);

        add(content, BorderLayout.CENTER);

        newCardRadio.setSelected(true);
        newCardPanel.setVisible(true);
    }

    public void validerPaiement() {
        try {
            if (existingCardRadio.isSelected()) {
                boolean carteSelectionnee = false;
                int numeroCarte = -1;

                for (Enumeration<AbstractButton> buttons = groupeCartes.getElements(); buttons.hasMoreElements();) {
                    AbstractButton button = buttons.nextElement();
                    if (button.isSelected()) {
                        carteSelectionnee = true;
                        numeroCarte = Integer.parseInt(button.getActionCommand());
                        break;
                    }
                }

                if (!carteSelectionnee) {
                    throw new IllegalArgumentException("Veuillez sélectionner une carte enregistrée");
                }

                String cvv = JOptionPane.showInputDialog(this, "Veuillez entrer le CVV pour la carte sélectionnée", "Validation CVV", JOptionPane.PLAIN_MESSAGE);

                if (cvv == null || cvv.trim().isEmpty()) {
                    throw new IllegalArgumentException("Le code CVV est requis pour la carte enregistrée");
                }

                controleur.traiterPaiementAvecCarteExistante(numeroCarte, Integer.parseInt(cvv), montantTotal);

            } else {
                if (champNomTitulaire.getText().trim().isEmpty()) {
                    throw new IllegalArgumentException("Le nom du titulaire est requis");
                }

                if (champNumeroCarte.getText().trim().isEmpty()) {
                    throw new IllegalArgumentException("Le numéro de carte est requis");
                }

                if (champDateValidite.getText().trim().isEmpty()) {
                    throw new IllegalArgumentException("La date d'expiration est requise");
                }

                try {
                    if (!estDateValide(champDateValidite.getText())) {
                        throw new IllegalArgumentException("La date d'expiration est invalide ou la carte a expiré");
                    }
                } catch (ParseException e) {
                    throw new IllegalArgumentException("Format de date invalide (MM/AA attendu)");
                }

                if (champCVVNouvelleCarte.getText().trim().isEmpty()) {
                    throw new IllegalArgumentException("Le code CVV est requis pour la nouvelle carte");
                }

                String expirationText = champDateValidite.getText();
                Date dateValidite = new SimpleDateFormat("MM/yy").parse(expirationText);

                SimpleDateFormat formatPourBDD = new SimpleDateFormat("yyyy-MM-dd");
                String datePourBddString = formatPourBDD.format(dateValidite);
                java.sql.Date datePourBdd = java.sql.Date.valueOf(datePourBddString);

                Paiement nouvelleCarte = new Paiement(
                        champNomTitulaire.getText().trim(),
                        Integer.parseInt(champNumeroCarte.getText().trim()),
                        datePourBdd,
                        Integer.parseInt(champCVVNouvelleCarte.getText().trim()),
                        1000.0f
                );

                controleur.traiterPaiementAvecNouvelleCarte(
                        nouvelleCarte,
                        caseEnregistrerCarte.isSelected(),
                        montantTotal
                );
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Format numérique invalide pour le numéro de carte ou CVV",
                    "Erreur de paiement",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Erreur de paiement",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du paiement: " + e.getMessage(),
                    "Erreur de paiement",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Vérifie si la date de validité de la carte n'est pas expirée
     *
     * @param dateString la date au format String
     * @return true si la date est valide, false si elle est expirée
     * @throws ParseException si le format de date est invalide
     */

    private boolean estDateValide(String dateString) throws ParseException {
        Date dateCarte = dateFormat.parse(dateString);
        Date aujourdHui = dateFormat.parse(dateFormat.format(new Date()));
        return !dateCarte.before(aujourdHui);
    }

    /**
     * Crée un panneau avec un label et un champ de saisie.
     *
     * @param label : le texte du label
     * @param field : le champ associé
     * @return : un JPanel contenant le label et le champ
     */
    private JPanel creerChamp(String label, JComponent field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel(label));
        field.setPreferredSize(new Dimension(200, 25));
        panel.add(field);
        return panel;
    }

    /**
     * Charge et affiche les cartes de paiement déjà enregistrées pour le client.
     */
    private void chargerCartesEnregistrees() {
        cartesPanel.removeAll();
        List<Paiement> cartes = controleur.getCartesClient();

        if (!cartes.isEmpty()) {
            cartesPanel.setBorder(BorderFactory.createTitledBorder("Mes cartes"));



            for (Paiement carte : cartes) {
                JPanel cardPanel = new JPanel(new BorderLayout());
                JRadioButton radio = new JRadioButton(
                        carte.getNomCarte() + " - ****" + carte.getNumero() % 10000
                );

                // On gère l'affichage pour les cartes ayant expirées
                boolean estExpiree = carte.getDateValidite().before(new Date());

                if (estExpiree) {
                    radio.setForeground(Color.RED);
                    radio.setText(radio.getText() + " (EXPIRÉE)");
                    radio.setEnabled(false);
                }

                radio.setActionCommand(String.valueOf(carte.getNumero()));
                groupeCartes.add(radio);

                JLabel details = new JLabel(
                        "Exp: " + dateFormat.format(carte.getDateValidite()) +
                                " | Solde: " + carte.getSolde() + "€"
                );

                if (estExpiree) {
                    details.setForeground(Color.RED);
                }

                cardPanel.add(radio, BorderLayout.WEST);
                cardPanel.add(details, BorderLayout.CENTER);
                cartesPanel.add(cardPanel);
            }

            cartesPanel.add(Box.createVerticalStrut(10));
        } else {
            cartesPanel.setVisible(false);
        }

        cartesPanel.revalidate();
        cartesPanel.repaint();
    }


    /**
     * Affiche une fenêtre d'information indiquant que des cartes sont enregistrées.
     *
     * @param cartes : liste de paiements existants
     */
    public void afficherCartes(List<Paiement> cartes) {
        JOptionPane.showMessageDialog(this, "Vous avez des cartes déjà enregistrées");
    }

    /**
     * Affiche une fenêtre d'information indiquant l'absence de cartes enregistrées.
     *
     * @param cartes : liste de paiements existants
     */
    public void pasdecartes(List<Paiement> cartes) {
        JOptionPane.showMessageDialog(this, "Aucune carte enregistrée");
    }

    /**
     * Affiche une erreur dans une fenêtre d'alerte.
     *
     * @param message : message d'erreur à afficher
     */
    public void afficherErreur(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Affiche une confirmation quand le paiement est réussi et ferme la fenêtre.
     */
    public void paiementReussi() {
        JOptionPane.showMessageDialog(this, "Paiement effectué avec succès !");
        this.callback.accept(true);
        this.dispose();
    }


}