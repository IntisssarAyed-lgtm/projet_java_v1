package vue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import modele.Utilisateurs;
import modele.Plat;
import modele.Menu;

public class SeurveuseView extends JFrame {

    private static final long serialVersionUID = 1L;

    // Header
    private JLabel lblBienvenue;
    private JButton btnDeconnexion;

    // Onglets
    private JTabbedPane tabbedPane;

    // ── Onglet 1 : Commander ─────────────────────────────────
    private JComboBox<Menu> cmbMenus;
    private JList<Plat> listePlats;
    private DefaultListModel<Plat> modelePlats;
    private JSpinner spinQuantite;
    private JButton btnAjouter;
    private JButton btnSupprimer;
    private JButton btnCommander;
    private javax.swing.table.DefaultTableModel modeleCommande;
    private JTable tableCommande;
    private JLabel lblTotal;

    // ── Onglet 2 : Commandes en cours ────────────────────────
    private javax.swing.table.DefaultTableModel modeleEnCours;
    private JTable tableEnCours;
    private JButton btnRafraichirEnCours;

    // ── Onglet 3 : Commandes reçues ──────────────────────────
    private javax.swing.table.DefaultTableModel modeleRecues;
    private JTable tableRecues;
    private JButton btnGenererFacture;
    private JButton btnRafraichirRecues;

    public SeurveuseView(Utilisateurs serveuse) {
        setTitle("Restaurant App - Espace Serveuse");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(247, 239, 230));

        // ── Header ────────────────────────────────────────────
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(179, 89, 0));
        headerPanel.setPreferredSize(new Dimension(950, 60));
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        lblBienvenue = new JLabel("👩‍💼  Bienvenue, " + serveuse.getUsername() + " !");
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

        // ── TabbedPane ────────────────────────────────────────
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 13));
        tabbedPane.setBackground(Color.WHITE);

        tabbedPane.addTab("🍽 Commander",        creerOngletCommander());
        tabbedPane.addTab("⏳ En cours",          creerOngletEnCours());
        tabbedPane.addTab("✅ Commandes reçues",  creerOngletRecues());

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    // ═══════════════════════════════════════════════════════════
    // ONGLET 1 — Commander (même logique que ClientView)
    // ═══════════════════════════════════════════════════════════
    private JPanel creerOngletCommander() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Panel gauche — menu et plats
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setPreferredSize(new Dimension(400, 500));

        JLabel lblTitre = new JLabel("📋 Parcourir le Menu");
        lblTitre.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTitre.setForeground(new Color(179, 89, 0));
        lblTitre.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblChoix = new JLabel("Choisir un menu :");
        lblChoix.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblChoix.setAlignmentX(Component.LEFT_ALIGNMENT);

        cmbMenus = new JComboBox<>();
        cmbMenus.setMaximumSize(new Dimension(370, 36));
        cmbMenus.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cmbMenus.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblPlats = new JLabel("Plats disponibles :");
        lblPlats.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblPlats.setAlignmentX(Component.LEFT_ALIGNMENT);

        modelePlats = new DefaultListModel<>();
        listePlats = new JList<>(modelePlats);
        listePlats.setFont(new Font("SansSerif", Font.PLAIN, 13));
        listePlats.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listePlats.setCellRenderer(new PlatCellRenderer());

        JScrollPane scrollPlats = new JScrollPane(listePlats);
        scrollPlats.setMaximumSize(new Dimension(370, 180));
        scrollPlats.setPreferredSize(new Dimension(370, 180));
        scrollPlats.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPlats.setBorder(BorderFactory.createLineBorder(new Color(224, 193, 161)));

        JPanel panelAjouter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelAjouter.setBackground(Color.WHITE);
        panelAjouter.setMaximumSize(new Dimension(370, 50));
        panelAjouter.setAlignmentX(Component.LEFT_ALIGNMENT);

        spinQuantite = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        spinQuantite.setPreferredSize(new Dimension(60, 32));

        btnAjouter = new JButton("➕ Ajouter");
        styliserBouton(btnAjouter, new Color(179, 89, 0));

        panelAjouter.add(new JLabel("Quantité :"));
        panelAjouter.add(spinQuantite);
        panelAjouter.add(btnAjouter);

        leftPanel.add(lblTitre);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(lblChoix);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPanel.add(cmbMenus);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(lblPlats);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPanel.add(scrollPlats);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(panelAjouter);

        // Panel droit — panier
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(new Color(247, 239, 230));
        rightPanel.setBorder(new EmptyBorder(0, 15, 0, 0));

        JLabel lblPanier = new JLabel("🛒 Panier");
        lblPanier.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblPanier.setForeground(new Color(179, 89, 0));
        lblPanier.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] colonnes = {"Plat", "Prix", "Qté", "Sous-total"};
        modeleCommande = new javax.swing.table.DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableCommande = new JTable(modeleCommande);
        styleTable(tableCommande);

        JScrollPane scrollPanier = new JScrollPane(tableCommande);
        scrollPanier.setPreferredSize(new Dimension(460, 220));
        scrollPanier.setMaximumSize(new Dimension(460, 220));
        scrollPanier.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPanier.setBorder(BorderFactory.createLineBorder(new Color(224, 193, 161)));

        lblTotal = new JLabel("Total : 0.00 DT");
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTotal.setForeground(new Color(179, 89, 0));
        lblTotal.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel panelBtns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBtns.setBackground(new Color(247, 239, 230));
        panelBtns.setMaximumSize(new Dimension(460, 50));
        panelBtns.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnSupprimer = new JButton("🗑 Supprimer");
        styliserBouton(btnSupprimer, new Color(200, 60, 60));

        btnCommander = new JButton("✅ Passer la commande");
        styliserBouton(btnCommander, new Color(34, 139, 34));

        panelBtns.add(btnSupprimer);
        panelBtns.add(btnCommander);

        rightPanel.add(lblPanier);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(scrollPanier);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(lblTotal);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(panelBtns);

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.CENTER);
        return panel;
    }

    // ═══════════════════════════════════════════════════════════
    // ONGLET 2 — Commandes en cours de traitement
    // ═══════════════════════════════════════════════════════════
    private JPanel creerOngletEnCours() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblTitre = new JLabel("⏳ Commandes en cours de traitement");
        lblTitre.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTitre.setForeground(new Color(179, 89, 0));

        String[] colonnes = {"ID", "Client", "Date", "Statut"};
        modeleEnCours = new javax.swing.table.DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableEnCours = new JTable(modeleEnCours);
        styleTable(tableEnCours);

        JScrollPane scroll = new JScrollPane(tableEnCours);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(224, 193, 161)));

        btnRafraichirEnCours = new JButton("🔄 Rafraîchir");
        styliserBouton(btnRafraichirEnCours, new Color(179, 89, 0));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setBackground(Color.WHITE);
        bottom.add(btnRafraichirEnCours);

        panel.add(lblTitre, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    // ═══════════════════════════════════════════════════════════
    // ONGLET 3 — Commandes reçues + Facture
    // ═══════════════════════════════════════════════════════════
    private JPanel creerOngletRecues() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblTitre = new JLabel("✅ Commandes reçues (PRET)");
        lblTitre.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTitre.setForeground(new Color(179, 89, 0));

        String[] colonnes = {"ID", "Client", "Date", "Statut", "Total"};
        modeleRecues = new javax.swing.table.DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableRecues = new JTable(modeleRecues);
        styleTable(tableRecues);

        JScrollPane scroll = new JScrollPane(tableRecues);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(224, 193, 161)));

        btnGenererFacture = new JButton("🧾 Générer Facture");
        styliserBouton(btnGenererFacture, new Color(34, 139, 34));

        btnRafraichirRecues = new JButton("🔄 Rafraîchir");
        styliserBouton(btnRafraichirRecues, new Color(179, 89, 0));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setBackground(Color.WHITE);
        bottom.add(btnRafraichirRecues);
        bottom.add(btnGenererFacture);

        panel.add(lblTitre, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    // ── Renderer plats ────────────────────────────────────────
    private class PlatCellRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList<?> list,
                Object value, int index, boolean isSelected, boolean hasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, hasFocus);
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

    // ── Méthodes utilitaires ──────────────────────────────────
    private void styliserBouton(JButton btn, Color couleur) {
        btn.setBackground(couleur);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.getTableHeader().setBackground(new Color(179, 89, 0));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.setSelectionBackground(new Color(255, 220, 180));
        table.setSelectionForeground(Color.BLACK);
    }

    // ── Getters ───────────────────────────────────────────────
    public JComboBox<Menu> getCmbMenus()           { return cmbMenus; }
    public JList<Plat> getListePlats()             { return listePlats; }
    public DefaultListModel<Plat> getModelePlats() { return modelePlats; }
    public JSpinner getSpinQuantite()              { return spinQuantite; }
    public JButton getBtnAjouter()                 { return btnAjouter; }
    public JButton getBtnSupprimer()               { return btnSupprimer; }
    public JButton getBtnCommander()               { return btnCommander; }
    public JButton getBtnDeconnexion()             { return btnDeconnexion; }
    public JButton getBtnRafraichirEnCours()       { return btnRafraichirEnCours; }
    public JButton getBtnRafraichirRecues()        { return btnRafraichirRecues; }
    public JButton getBtnGenererFacture()          { return btnGenererFacture; }
    public JTable getTableEnCours()                { return tableEnCours; }
    public JTable getTableRecues()                 { return tableRecues; }
    public javax.swing.table.DefaultTableModel getModeleCommande() { return modeleCommande; }
    public javax.swing.table.DefaultTableModel getModeleEnCours()  { return modeleEnCours; }
    public javax.swing.table.DefaultTableModel getModeleRecues()   { return modeleRecues; }
    public int getQuantite()          { return (int) spinQuantite.getValue(); }
    public Plat getPlatSelectionne()  { return listePlats.getSelectedValue(); }
    public int getLigneSelectionnee() { return tableCommande.getSelectedRow(); }
    public int getLigneRecueSelectionnee() { return tableRecues.getSelectedRow(); }

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