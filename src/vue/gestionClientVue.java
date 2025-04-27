package vue;

import dao.clientDaoImpl;
import modele.Client;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;


public class gestionClientVue extends JFrame {
    private clientDaoImpl clientDao;
    private JTable tableClients;
    private DefaultTableModel tableModel;

    /**
     * Initialise la vue de gestion des clients.
     *
     * @param clientDao : DAO pour accéder aux données des clients
     */
    public gestionClientVue(clientDaoImpl clientDao) {
        this.clientDao = clientDao;
        initialisationAffichage();
        setVisible(true);
    }

    private void initialisationAffichage() {
        setTitle("Gestion des Clients");
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));

        JPanel header = Header();
        add(header, BorderLayout.NORTH);

        affichageClients();
        JScrollPane scrollPane = new JScrollPane(tableClients);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBoutons = Boutons();
        add(panelBoutons, BorderLayout.SOUTH);

        chargerClients();
    }

    private JPanel Header() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(30, 144, 255));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titre = new JLabel("Gestion des clients", SwingConstants.CENTER);
        titre.setForeground(Color.WHITE);
        titre.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titre.setHorizontalAlignment(SwingConstants.CENTER);

        JButton btnFermer = new JButton("Fermer");
        btnFermer.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnFermer.setBackground(new Color(70, 130, 180));
        btnFermer.setForeground(Color.WHITE);
        btnFermer.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnFermer.addActionListener(e -> dispose());

        header.add(Box.createHorizontalStrut(100), BorderLayout.WEST);
        header.add(titre, BorderLayout.CENTER);
        header.add(btnFermer, BorderLayout.EAST);

        return header;

    }

    /**
     * Initialise l'affichage du tableau des clients.
     */
    private void affichageClients() {
        String[] colonnes = {"ID", "Nom", "Email", "Adresse", "Téléphone"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            //on rend les cellules non modifiables
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableClients = new JTable(tableModel);
        Table();
    }

    /**
     * Configure le tableau affichant les clients.
     */
    private void Table() {
        tableClients.setRowHeight(35);
        tableClients.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableClients.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableClients.setShowGrid(false);
        tableClients.setIntercellSpacing(new Dimension(0, 0));

        tableClients.setSelectionBackground(new Color(52, 152, 219));
        tableClients.setSelectionForeground(Color.WHITE);

        JTableHeader header = tableClients.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 40));

        // on personalise notre affichage pour tous nos objets pour un rendu visuel meilleur
        tableClients.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            //avec cette méthode on alterne les couleurs de fond des cellules et du texte
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (isSelected) {
                    c.setBackground(new Color(52, 152, 219));
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));
                    c.setForeground(Color.BLACK);
                }
                setBorder(noFocusBorder);
                return c;
            }
        });
    }


    private JPanel Boutons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));
        panel.setBackground(Color.WHITE);

        JButton btnActualiser = styleBoutons("Actualiser", new Color(46, 204, 113));
        JButton btnModifier = styleBoutons("Modifier", new Color(52, 152, 219));
        JButton btnSupprimer = styleBoutons("Supprimer", new Color(231, 76, 60));

        btnActualiser.addActionListener(e -> chargerClients());
        btnModifier.addActionListener(e -> modifierClient());
        btnSupprimer.addActionListener(e -> supprimerClient());

        panel.add(btnActualiser);
        panel.add(btnModifier);
        panel.add(btnSupprimer);

        return panel;
    }

    private JButton styleBoutons(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return button;
    }

    /**
     * Charge la liste des clients et l'affiche dans le tableau.
     */
    private void chargerClients() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                tableModel.setRowCount(0);
                ArrayList<Client> clients = clientDao.getAll();
                for (Client c : clients) {
                    tableModel.addRow(new Object[]{
                            c.getIDClient(),
                            c.getNom(),
                            c.getEmail(),
                            c.getAdresse(),
                            c.getTelephone()
                    });
                }
                return null;
            }
        };
        worker.execute();
    }

    /**
     * Permet de modifier les informations d'un client sélectionné.
     */
    private void modifierClient() {
        int row = tableClients.getSelectedRow();
        if (row == -1) {
            showMessage("Veuillez sélectionner un client à modifier.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        //on vérifie que le client existe bien
        int idClient = (int) tableModel.getValueAt(row, 0);
        Client client = clientDao.chercherIDCLient(idClient);
        if (client == null) {
            showMessage("Client introuvable.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField nomField = new JTextField(client.getNom());
        JTextField emailField = new JTextField(client.getEmail());
        JTextField adresseField = new JTextField(client.getAdresse());
        JTextField telField = new JTextField(client.getTelephone());

        panel.add(new JLabel("Nom:"));
        panel.add(nomField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Adresse:"));
        panel.add(adresseField);
        panel.add(new JLabel("Téléphone:"));
        panel.add(telField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Modifier Client",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            client.setNom(nomField.getText());
            client.setEmail(emailField.getText());
            client.setAdresse(adresseField.getText());
            client.setTelephone(telField.getText());

            Client client_modifie = clientDao.modifier(client);

            if (client_modifie != null) {
                showMessage("Client modifié avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
                chargerClients();
            } else {
                showMessage("Erreur lors de la modification.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Permet de supprimer un client sélectionné.
     */
    private void supprimerClient() {
        int row = tableClients.getSelectedRow();
        // on regarde si l'admin a bien sélectionné un client
        if (row == -1) {
            showMessage("Veuillez sélectionner un client à supprimer.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer ce client ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            int idClient = (int) tableModel.getValueAt(row, 0);
            if (clientDao.supprimerID(idClient)) {
                showMessage("Client supprimé avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
                chargerClients();
            } else {
                showMessage("Erreur lors de la suppression.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Affichage d'un message d'information ou d'erreur.
     *
     * @param message : le message à afficher
     * @param title : le titre de la boîte de dialogue
     * @param messageType : le type de message (information, erreur)
     */
    private void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
}