package vue;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import modele.Utilisateurs;
import modele.Plat;
import modele.Menu;

public class ClientView extends JFrame {

    private static final long serialVersionUID = 1L;

    // Composants principaux
    private JLabel lblBienvenue;
    private JComboBox<Menu> cmbMenus;
    private JList<Plat> listePlats;
    private DefaultListModel<Plat> modelePlats;
    private JTable tableCommande;
    private javax.swing.table.DefaultTableModel modeleCommande;
    private JLabel lblTotal;
    private JButton btnAjouter;
    private JButton btnSupprimer;
    private JButton btnCommander;
    private JButton btnDeconnexion;
    private JSpinner spinQuantite;

    public ClientView(Utilisateurs client) {
        setTitle("Restaurant App - Espace Client");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // ── Panel Principal ───────────────────────────────────
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(247, 239, 230));

        // ── Header ────────────────────────────────────────────
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(179, 89, 0));
        headerPanel.setPreferredSize(new Dimension(900, 60));
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        lblBienvenue = new JLabel("🍽  Bienvenue, " + client.getUsername() + " !");
        lblBienvenue.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblBienvenue.setForeground(Color.WHITE);

        btnDeconnexion = new JButton("Déconnexion");
        btnDeconnexion.setBackground(new Color(230, 138, 0));
        btnDeconnexion.setForeground(Color.WHITE);
        btnDeconnexion.setFocusPainted(false);
        btnDeconnexion.setBorderPainted(false);
        btnDeconnexion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDeconnexion.setFont(new Font("SansSerif", Font.BOLD, 13));

        headerPanel.add(lblBienvenue, BorderLayout.WEST);
        headerPanel.add(btnDeconnexion, BorderLayout.EAST);

        // ── Panel Gauche (Menu + Plats) ───────────────────────
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setPreferredSize(new Dimension(420, 500));
        leftPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Titre section menu
        JLabel lblMenuTitre = new JLabel("📋 Parcourir le Menu");
        lblMenuTitre.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblMenuTitre.setForeground(new Color(179, 89, 0));
        lblMenuTitre.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ComboBox Menus
        JLabel lblChoixMenu = new JLabel("Choisir un menu :");
        lblChoixMenu.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblChoixMenu.setAlignmentX(Component.LEFT_ALIGNMENT);

        cmbMenus = new JComboBox<>();
        cmbMenus.setMaximumSize(new Dimension(380, 36));
        cmbMenus.setPreferredSize(new Dimension(380, 36));
        cmbMenus.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cmbMenus.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Liste des plats
        JLabel lblPlats = new JLabel("Plats disponibles :");
        lblPlats.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblPlats.setAlignmentX(Component.LEFT_ALIGNMENT);

        modelePlats = new DefaultListModel<>();
        listePlats = new JList<>(modelePlats);
        listePlats.setFont(new Font("SansSerif", Font.PLAIN, 13));
        listePlats.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listePlats.setCellRenderer(new PlatCellRenderer());

        JScrollPane scrollPlats = new JScrollPane(listePlats);
        scrollPlats.setMaximumSize(new Dimension(380, 200));
        scrollPlats.setPreferredSize(new Dimension(380, 200));
        scrollPlats.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPlats.setBorder(BorderFactory.createLineBorder(new Color(224, 193, 161)));

        // Quantité + Bouton Ajouter
        JPanel panelAjouter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelAjouter.setBackground(Color.WHITE);
        panelAjouter.setMaximumSize(new Dimension(380, 50));
        panelAjouter.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblQte = new JLabel("Quantité :");
        lblQte.setFont(new Font("SansSerif", Font.PLAIN, 13));

        spinQuantite = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        spinQuantite.setPreferredSize(new Dimension(60, 32));

        btnAjouter = new JButton("➕ Ajouter au panier");
        btnAjouter.setBackground(new Color(179, 89, 0));
        btnAjouter.setForeground(Color.WHITE);
        btnAjouter.setFocusPainted(false);
        btnAjouter.setBorderPainted(false);
        btnAjouter.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAjouter.setFont(new Font("SansSerif", Font.BOLD, 13));

        panelAjouter.add(lblQte);
        panelAjouter.add(spinQuantite);
        panelAjouter.add(btnAjouter);

        leftPanel.add(lblMenuTitre);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        leftPanel.add(lblChoixMenu);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPanel.add(cmbMenus);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        leftPanel.add(lblPlats);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPanel.add(scrollPlats);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(panelAjouter);

        // ── Panel Droit (Panier / Commande) ──────────────────
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(new Color(247, 239, 230));
        rightPanel.setPreferredSize(new Dimension(450, 500));
        rightPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblPanierTitre = new JLabel("🛒 Mon Panier");
        lblPanierTitre.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblPanierTitre.setForeground(new Color(179, 89, 0));
        lblPanierTitre.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Table du panier
        String[] colonnes = {"Plat", "Prix unitaire", "Quantité", "Sous-total"};
        modeleCommande = new javax.swing.table.DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tableCommande = new JTable(modeleCommande);
        tableCommande.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tableCommande.setRowHeight(28);
        tableCommande.getTableHeader().setBackground(new Color(179, 89, 0));
        tableCommande.getTableHeader().setForeground(Color.WHITE);
        tableCommande.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tableCommande.setSelectionBackground(new Color(255, 220, 180));

        JScrollPane scrollPanier = new JScrollPane(tableCommande);
        scrollPanier.setMaximumSize(new Dimension(420, 250));
        scrollPanier.setPreferredSize(new Dimension(420, 250));
        scrollPanier.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPanier.setBorder(BorderFactory.createLineBorder(new Color(224, 193, 161)));

        // Total
        lblTotal = new JLabel("Total : 0.00 DT");
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTotal.setForeground(new Color(179, 89, 0));
        lblTotal.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Boutons panier
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBoutons.setBackground(new Color(247, 239, 230));
        panelBoutons.setMaximumSize(new Dimension(420, 50));
        panelBoutons.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnSupprimer = new JButton("🗑 Supprimer");
        btnSupprimer.setBackground(new Color(200, 60, 60));
        btnSupprimer.setForeground(Color.WHITE);
        btnSupprimer.setFocusPainted(false);
        btnSupprimer.setBorderPainted(false);
        btnSupprimer.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSupprimer.setFont(new Font("SansSerif", Font.BOLD, 13));

        btnCommander = new JButton("✅ Passer la commande");
        btnCommander.setBackground(new Color(34, 139, 34));
        btnCommander.setForeground(Color.WHITE);
        btnCommander.setFocusPainted(false);
        btnCommander.setBorderPainted(false);
        btnCommander.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCommander.setFont(new Font("SansSerif", Font.BOLD, 13));

        panelBoutons.add(btnSupprimer);
        panelBoutons.add(btnCommander);

        rightPanel.add(lblPanierTitre);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(scrollPanier);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(lblTotal);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(panelBoutons);

        // ── Assemblage final ──────────────────────────────────
        JSeparator separateur = new JSeparator(JSeparator.VERTICAL);
        separateur.setForeground(new Color(224, 193, 161));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(leftPanel, BorderLayout.WEST);
        centerPanel.add(separateur, BorderLayout.CENTER);
        centerPanel.add(rightPanel, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    // ── Renderer personnalisé pour afficher les plats ─────────
    private class PlatCellRenderer extends DefaultListCellRenderer {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Component getListCellRendererComponent(JList<?> list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Plat) {
                Plat p = (Plat) value;
                setText(p.getNom() + "  —  " + String.format("%.2f DT", p.getPrix()));
            }
            setBorder(new EmptyBorder(5, 10, 5, 10));
            if (isSelected) {
                setBackground(new Color(255, 220, 180));
                setForeground(new Color(179, 89, 0));
            }
            return this;
        }
    }

    // ── Getters pour le contrôleur ────────────────────────────

    public JComboBox<Menu> getCmbMenus()             { return cmbMenus; }
    public JList<Plat> getListePlats()               { return listePlats; }
    public DefaultListModel<Plat> getModelePlats()   { return modelePlats; }
    public javax.swing.table.DefaultTableModel getModeleCommande() { return modeleCommande; }
    public JTable getTableCommande()                 { return tableCommande; }
    public JButton getBtnAjouter()                   { return btnAjouter; }
    public JButton getBtnSupprimer()                 { return btnSupprimer; }
    public JButton getBtnCommander()                 { return btnCommander; }
    public JButton getBtnDeconnexion()               { return btnDeconnexion; }
    public int getQuantite()                         { return (int) spinQuantite.getValue(); }
    public Plat getPlatSelectionne()                 { return listePlats.getSelectedValue(); }
    public int getLigneSelectionnee()                { return tableCommande.getSelectedRow(); }

    public void setTotal(double total) {
        lblTotal.setText(String.format("Total : %.2f DT", total));
    }

    public void afficherMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public void afficherErreur(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}