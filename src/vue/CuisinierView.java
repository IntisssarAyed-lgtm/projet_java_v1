package vue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import modele.Utilisateurs;
import modele.Plat;
import modele.Menu;

public class CuisinierView extends JFrame {

    private static final long serialVersionUID = 1L;

    // Header
    private JButton btnDeconnexion;

    // TabbedPane
    private JTabbedPane tabbedPane;

    // ── Onglet 1 : Gestion des Plats ─────────────────────────
    private JComboBox<Menu> cmbMenuPlat;
    private JTextField txtNomPlat;
    private JTextField txtDescriptionPlat;
    private JTextField txtPrixPlat;
    private JButton btnAjouterPlat;
    private JButton btnModifierPlat;
    private JButton btnSupprimerPlat;
    private JButton btnEnregistrerPlat;
    private JButton btnFermerPlat;
    private javax.swing.table.DefaultTableModel modelePlats;
    private JTable tablePlats;

    // ── Onglet 2 : Commandes EN_ATTENTE ──────────────────────
    private javax.swing.table.DefaultTableModel modeleAttente;
    private JTable tableAttente;
    private JButton btnCommencer;
    private JButton btnAnnuler;
    private JButton btnRafraichirAttente;

    // ── Onglet 3 : Commandes EN_COURS ────────────────────────
    private javax.swing.table.DefaultTableModel modeleEnCours;
    private JTable tableEnCours;
    private JButton btnMarquerPret;
    private JButton btnRafraichirEnCours;

    // ── Onglet 4 : Commandes SERVI ───────────────────────────
    private javax.swing.table.DefaultTableModel modeleServi;
    private JTable tableServi;
    private JButton btnRafraichirServi;

    public CuisinierView(Utilisateurs cuisinier) {
        setTitle("Restaurant App - Espace Cuisinier");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // ── Header ────────────────────────────────────────────
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(40, 40, 40));
        headerPanel.setPreferredSize(new Dimension(950, 60));
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel lblBienvenue = new JLabel("👨‍🍳  Bienvenue, " + cuisinier.getUsername() + " !");
        lblBienvenue.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblBienvenue.setForeground(Color.WHITE);

        btnDeconnexion = new JButton("Déconnexion");
        styliserBouton(btnDeconnexion, new Color(179, 89, 0));

        headerPanel.add(lblBienvenue, BorderLayout.WEST);
        headerPanel.add(btnDeconnexion, BorderLayout.EAST);

        // ── TabbedPane ────────────────────────────────────────
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 13));

        tabbedPane.addTab("🍲 Gestion Plats",    creerOngletPlats());
        tabbedPane.addTab("📋 En attente",        creerOngletAttente());
        tabbedPane.addTab("🔥 En cours",          creerOngletEnCours());
        tabbedPane.addTab("✅ Déjà servis",        creerOngletServi());

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    // ═══════════════════════════════════════════════════════════
    // ONGLET 1 — Gestion des Plats
    // ═══════════════════════════════════════════════════════════
    private JPanel creerOngletPlats() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // ── Formulaire ────────────────────────────────────────
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setPreferredSize(new Dimension(320, 400));
        formPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(224, 193, 161)),
            "Détails du plat",
            0, 0,
            new Font("SansSerif", Font.BOLD, 13),
            new Color(179, 89, 0)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Menu
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Menu :"), gbc);
        gbc.gridx = 1;
        cmbMenuPlat = new JComboBox<>();
        cmbMenuPlat.setPreferredSize(new Dimension(180, 32));
        formPanel.add(cmbMenuPlat, gbc);

        // Nom
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Nom du plat :"), gbc);
        gbc.gridx = 1;
        txtNomPlat = new JTextField();
        txtNomPlat.setPreferredSize(new Dimension(180, 32));
        formPanel.add(txtNomPlat, gbc);

        // Description
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Description :"), gbc);
        gbc.gridx = 1;
        txtDescriptionPlat = new JTextField();
        txtDescriptionPlat.setPreferredSize(new Dimension(180, 32));
        formPanel.add(txtDescriptionPlat, gbc);

        // Prix
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Prix (DT) :"), gbc);
        gbc.gridx = 1;
        txtPrixPlat = new JTextField();
        txtPrixPlat.setPreferredSize(new Dimension(180, 32));
        formPanel.add(txtPrixPlat, gbc);

        // Boutons formulaire
        JPanel panelBtnsForm = new JPanel(new GridLayout(3, 2, 8, 8));
        panelBtnsForm.setBackground(Color.WHITE);
        panelBtnsForm.setBorder(new EmptyBorder(10, 0, 0, 0));

        btnAjouterPlat    = new JButton("➕ Ajouter");
        btnModifierPlat   = new JButton("✏ Modifier");
        btnSupprimerPlat  = new JButton("🗑 Supprimer");
        btnEnregistrerPlat = new JButton("💾 Enregistrer");
        btnFermerPlat     = new JButton("❌ Fermer");

        styliserBouton(btnAjouterPlat,     new Color(34, 139, 34));
        styliserBouton(btnModifierPlat,    new Color(179, 89, 0));
        styliserBouton(btnSupprimerPlat,   new Color(200, 60, 60));
        styliserBouton(btnEnregistrerPlat, new Color(0, 120, 215));
        styliserBouton(btnFermerPlat,      new Color(100, 100, 100));

        panelBtnsForm.add(btnAjouterPlat);
        panelBtnsForm.add(btnModifierPlat);
        panelBtnsForm.add(btnSupprimerPlat);
        panelBtnsForm.add(btnEnregistrerPlat);
        panelBtnsForm.add(btnFermerPlat);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(panelBtnsForm, gbc);

        // ── Table des plats ───────────────────────────────────
        String[] colonnes = {"ID", "Nom", "Description", "Prix", "Menu"};
        modelePlats = new javax.swing.table.DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablePlats = new JTable(modelePlats);
        styleTable(tablePlats);

        JScrollPane scroll = new JScrollPane(tablePlats);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(224, 193, 161)));

        panel.add(formPanel, BorderLayout.WEST);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ═══════════════════════════════════════════════════════════
    // ONGLET 2 — Commandes EN_ATTENTE
    // ═══════════════════════════════════════════════════════════
    private JPanel creerOngletAttente() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblTitre = new JLabel("📋 Commandes en attente");
        lblTitre.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTitre.setForeground(new Color(179, 89, 0));
        lblTitre.setBorder(new EmptyBorder(0, 0, 10, 0));

        String[] colonnes = {"ID", "Client", "Date", "Plats commandés"};
        modeleAttente = new javax.swing.table.DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableAttente = new JTable(modeleAttente);
        styleTable(tableAttente);

        JScrollPane scroll = new JScrollPane(tableAttente);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(224, 193, 161)));

        btnCommencer         = new JButton("▶ Commencer");
        btnAnnuler           = new JButton("✖ Annuler");
        btnRafraichirAttente = new JButton("🔄 Rafraîchir");

        styliserBouton(btnCommencer,         new Color(34, 139, 34));
        styliserBouton(btnAnnuler,           new Color(200, 60, 60));
        styliserBouton(btnRafraichirAttente, new Color(179, 89, 0));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setBackground(Color.WHITE);
        bottom.add(btnRafraichirAttente);
        bottom.add(btnCommencer);
        bottom.add(btnAnnuler);

        panel.add(lblTitre,  BorderLayout.NORTH);
        panel.add(scroll,    BorderLayout.CENTER);
        panel.add(bottom,    BorderLayout.SOUTH);
        return panel;
    }

    // ═══════════════════════════════════════════════════════════
    // ONGLET 3 — Commandes EN_COURS
    // ═══════════════════════════════════════════════════════════
    private JPanel creerOngletEnCours() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblTitre = new JLabel("🔥 Commandes en cours de préparation");
        lblTitre.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTitre.setForeground(new Color(179, 89, 0));
        lblTitre.setBorder(new EmptyBorder(0, 0, 10, 0));

        String[] colonnes = {"ID", "Client", "Date", "Plats commandés", "Statut"};
        modeleEnCours = new javax.swing.table.DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableEnCours = new JTable(modeleEnCours);
        styleTable(tableEnCours);

        JScrollPane scroll = new JScrollPane(tableEnCours);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(224, 193, 161)));

        btnMarquerPret       = new JButton("✅ Marquer comme PRÊT");
        btnRafraichirEnCours = new JButton("🔄 Rafraîchir");

        styliserBouton(btnMarquerPret,       new Color(34, 139, 34));
        styliserBouton(btnRafraichirEnCours, new Color(179, 89, 0));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setBackground(Color.WHITE);
        bottom.add(btnRafraichirEnCours);
        bottom.add(btnMarquerPret);

        panel.add(lblTitre, BorderLayout.NORTH);
        panel.add(scroll,   BorderLayout.CENTER);
        panel.add(bottom,   BorderLayout.SOUTH);
        return panel;
    }

    // ═══════════════════════════════════════════════════════════
    // ONGLET 4 — Commandes SERVI
    // ═══════════════════════════════════════════════════════════
    private JPanel creerOngletServi() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblTitre = new JLabel("✅ Commandes déjà servies");
        lblTitre.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTitre.setForeground(new Color(179, 89, 0));
        lblTitre.setBorder(new EmptyBorder(0, 0, 10, 0));

        String[] colonnes = {"ID", "Client", "Date", "Statut"};
        modeleServi = new javax.swing.table.DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableServi = new JTable(modeleServi);
        styleTable(tableServi);

        JScrollPane scroll = new JScrollPane(tableServi);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(224, 193, 161)));

        btnRafraichirServi = new JButton("🔄 Rafraîchir");
        styliserBouton(btnRafraichirServi, new Color(179, 89, 0));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setBackground(Color.WHITE);
        bottom.add(btnRafraichirServi);

        panel.add(lblTitre, BorderLayout.NORTH);
        panel.add(scroll,   BorderLayout.CENTER);
        panel.add(bottom,   BorderLayout.SOUTH);
        return panel;
    }

    // ── Utilitaires ───────────────────────────────────────────
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
        table.getTableHeader().setBackground(new Color(40, 40, 40));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.setSelectionBackground(new Color(255, 220, 180));
        table.setSelectionForeground(Color.BLACK);
    }

    // ── Getters ───────────────────────────────────────────────
    public JButton getBtnDeconnexion()       { return btnDeconnexion; }

    // Plats
    public JComboBox<Menu> getCmbMenuPlat()  { return cmbMenuPlat; }
    public JTextField getTxtNomPlat()        { return txtNomPlat; }
    public JTextField getTxtDescriptionPlat(){ return txtDescriptionPlat; }
    public JTextField getTxtPrixPlat()       { return txtPrixPlat; }
    public JButton getBtnAjouterPlat()       { return btnAjouterPlat; }
    public JButton getBtnModifierPlat()      { return btnModifierPlat; }
    public JButton getBtnSupprimerPlat()     { return btnSupprimerPlat; }
    public JButton getBtnEnregistrerPlat()   { return btnEnregistrerPlat; }
    public JButton getBtnFermerPlat()        { return btnFermerPlat; }
    public JTable getTablePlats()            { return tablePlats; }
    public javax.swing.table.DefaultTableModel getModelePlats() { return modelePlats; }
    public int getLignePlatSelectionnee()    { return tablePlats.getSelectedRow(); }

    // Attente
    public JTable getTableAttente()          { return tableAttente; }
    public javax.swing.table.DefaultTableModel getModeleAttente() { return modeleAttente; }
    public JButton getBtnCommencer()         { return btnCommencer; }
    public JButton getBtnAnnuler()           { return btnAnnuler; }
    public JButton getBtnRafraichirAttente() { return btnRafraichirAttente; }
    public int getLigneAttenteSelectionnee() { return tableAttente.getSelectedRow(); }

    // En cours
    public JTable getTableEnCours()          { return tableEnCours; }
    public javax.swing.table.DefaultTableModel getModeleEnCours() { return modeleEnCours; }
    public JButton getBtnMarquerPret()       { return btnMarquerPret; }
    public JButton getBtnRafraichirEnCours() { return btnRafraichirEnCours; }
    public int getLigneEnCoursSelectionnee() { return tableEnCours.getSelectedRow(); }

    // Servi
    public JTable getTableServi()            { return tableServi; }
    public javax.swing.table.DefaultTableModel getModeleServi() { return modeleServi; }
    public JButton getBtnRafraichirServi()   { return btnRafraichirServi; }

    public void afficherMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public void afficherErreur(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    public void viderFormulaire() {
        txtNomPlat.setText("");
        txtDescriptionPlat.setText("");
        txtPrixPlat.setText("");
        cmbMenuPlat.setSelectedIndex(0);
    }
}