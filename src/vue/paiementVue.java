package vue;

import controleur.paiementControleur;
import dao.DaoFactory;
import dao.paiementDaoImpl;
import modele.Client;
import modele.Paiement;
import modele.Panier;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.function.Consumer;

public class paiementVue extends JFrame {
    private JTextField champNomTitulaire;
    private JTextField champNumeroCarte;
    private JTextField champDateValidite;
    private JTextField champCVV;
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

        configurerUI();
        chargerCartesEnregistrees();
    }

    /**
     * Configure l'interface graphique de la fenêtre de paiement.
     */
    private void configurerUI() {
        setTitle("Paiement sécurisé");
        setSize(600, 500);
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

        cartesPanel = new JPanel();
        cartesPanel.setLayout(new BoxLayout(cartesPanel, BoxLayout.Y_AXIS));
        groupeCartes = new ButtonGroup();

        JPanel newCardPanel = new JPanel();
        newCardPanel.setLayout(new BoxLayout(newCardPanel, BoxLayout.Y_AXIS));
        newCardPanel.setBorder(BorderFactory.createTitledBorder("Nouvelle carte"));

        champNomTitulaire = new JTextField(20);
        champNumeroCarte = new JTextField(20);
        champDateValidite = new JTextField(5);
        champCVV = new JTextField(3);
        caseEnregistrerCarte = new JCheckBox("Enregistrer cette carte");

        newCardPanel.add(creerChamp("Nom titulaire:", champNomTitulaire));
        newCardPanel.add(creerChamp("Numéro:", champNumeroCarte));
        newCardPanel.add(creerChamp("Expiration (MM/AA):", champDateValidite));
        newCardPanel.add(creerChamp("CVV:", champCVV));
        newCardPanel.add(caseEnregistrerCarte);

        boutonPayer = new JButton("Payer maintenant");
        boutonPayer.setBackground(new Color(30, 144, 255));
        boutonPayer.setForeground(Color.WHITE);
        boutonPayer.addActionListener(e -> validerPaiement());

        content.add(cartesPanel);
        content.add(Box.createVerticalStrut(20));
        content.add(newCardPanel);
        content.add(Box.createVerticalStrut(20));
        content.add(boutonPayer);

        add(content, BorderLayout.CENTER);
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
                radio.setActionCommand(String.valueOf(carte.getNumero()));
                groupeCartes.add(radio);

                JLabel details = new JLabel(
                        "Exp: " + dateFormat.format(carte.getDateValidite()) +
                                " | Solde: " + carte.getSolde() + "€"
                );

                cardPanel.add(radio, BorderLayout.WEST);
                cardPanel.add(details, BorderLayout.CENTER);
                cartesPanel.add(cardPanel);
            }

            cartesPanel.add(Box.createVerticalStrut(10));
            cartesPanel.add(creerChamp("CVV:", champCVV));
        } else {
            cartesPanel.setVisible(false);
        }

        cartesPanel.revalidate();
        cartesPanel.repaint();
    }

    /**
     * Valide et traite le paiement selon les informations saisies ou sélectionnées.
     */
    public void validerPaiement() {
        try {
            if (groupeCartes.getSelection() != null) {
                int numeroCarte = Integer.parseInt(groupeCartes.getSelection().getActionCommand());
                int cvv = Integer.parseInt(champCVV.getText());
                controleur.traiterPaiementAvecCarteExistante(numeroCarte, cvv, montantTotal);
            } else {
                Paiement nouvelleCarte = new Paiement(
                        champNomTitulaire.getText(),
                        Integer.parseInt(champNumeroCarte.getText()),
                        dateFormat.parse(champDateValidite.getText()),
                        Integer.parseInt(champCVV.getText()),
                        1000.0f
                );
                controleur.traiterPaiementAvecNouvelleCarte(
                        nouvelleCarte,
                        caseEnregistrerCarte.isSelected(),
                        montantTotal
                );
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur: " + e.getMessage(),
                    "Erreur de paiement",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Affiche une fenêtre d'information indiquant l'absence de cartes enregistrées.
     *
     * @param cartes : liste de paiements existants
     */
    public void afficherCartes(List<Paiement> cartes) {
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