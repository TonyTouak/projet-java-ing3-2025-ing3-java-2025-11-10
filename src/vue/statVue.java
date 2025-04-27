package vue;

import dao.statistiqueDao;
import modele.Article;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class statVue extends JFrame {

    /**
     * Constructeur de statVue.
     *
     * @param statDao : DAO pour accéder aux statistiques
     */
    public statVue(statistiqueDao statDao) {
        setTitle("Statistiques des Commandes");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 144, 255));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("STATISTIQUES DES VENTES", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        JButton btnFermer = new JButton("Fermer");
        btnFermer.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnFermer.setBackground(new Color(70, 130, 180));
        btnFermer.setForeground(Color.WHITE);
        btnFermer.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnFermer.addActionListener(e -> dispose());
        headerPanel.add(btnFermer, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        statsPanel.setBackground(Color.WHITE);

        JPanel totalCommandesPanel = Bandeau("Total Commandes",
                String.valueOf(statDao.getNombreTotalCommandes()),
                new Color(70, 130, 180));

        JPanel totalVentesPanel = Bandeau("Chiffre d'Affaires",
                statDao.getMontantTotalVentes() + " €",
                new Color(0, 100, 0));

        statsPanel.add(totalCommandesPanel);
        statsPanel.add(totalVentesPanel);

        // Tableau des articles
        String[] colonnes = {"Article", "Quantité Vendue", "Recette Totale (€)"};
        DefaultTableModel model = new DefaultTableModel(colonnes, 0) {
            // de même ici c'est non modifiable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        Map<Article, Integer> ventesParArticle = statDao.getQuantitesVenduesParArticle();
        ventesParArticle.entrySet().stream()
                //on trie les ventes par quantité vendue
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .forEach(entry -> {
                    //on affiche chaque article
                    Article article = entry.getKey();
                    int quantite = entry.getValue();
                    //et on calcule la recette générée (on aurait pu directement le récupérer via la commande en faisant une recherche également)
                    double recette = quantite * article.getPrixUnique();
                    model.addRow(new Object[]{
                            article.getNom(),
                            quantite,
                            String.format("%.2f", recette)
                    });
                });

        JTable table = new JTable(model);
        Table(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(headerPanel, BorderLayout.NORTH);
        add(statsPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Crée un bandeau pour afficher un chiffre clé.
     *
     * @param title Titre du bandeau
     * @param value Valeur à afficher
     * @param color Couleur de la valeur
     * @return Un JPanel configuré
     */
    private JPanel Bandeau(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        titleLabel.setForeground(Color.GRAY);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(color);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private void Table(JTable table) {
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(30, 144, 255));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));

        // On fait de même ici une alternance de couleur pour un rendu plus joli
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));
                if (!isSelected) {
                    c.setForeground(Color.BLACK);
                }
                setBorder(noFocusBorder);
                setHorizontalAlignment(column == 2 ? SwingConstants.RIGHT : SwingConstants.LEFT);
                return c;
            }
        });
    }
}