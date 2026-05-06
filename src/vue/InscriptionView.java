package vue;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

public class InscriptionView extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JComboBox<String> cmbRole;
    private JButton btnInscrire;
    private JButton btnRetourLogin;
    private JLabel lblErreur;

    public InscriptionView() {
        setTitle("Restaurant App - Inscription");
        setSize(500, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(30, 50, 30, 50));

        // ── Titre ────────────────────────────────────────────
        JLabel titre = new JLabel("Créer un compte");
        titre.setFont(new Font("SansSerif", Font.BOLD, 26));
        titre.setForeground(new Color(179, 89, 0));
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sousTitre = new JLabel("Rejoignez notre système");
        sousTitre.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sousTitre.setForeground(Color.GRAY);
        sousTitre.setAlignmentX(Component.CENTER_ALIGNMENT);

        formPanel.add(titre);
        formPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        formPanel.add(sousTitre);
        formPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(350, 2));
        sep.setForeground(new Color(224, 193, 161));
        formPanel.add(sep);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // ── Champ Username ───────────────────────────────────
        formPanel.add(creerLabel("Nom d'utilisateur"));
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        txtUsername = new JTextField();
        styliserChamp(txtUsername);
        formPanel.add(txtUsername);
        formPanel.add(Box.createRigidArea(new Dimension(0, 14)));

        // ── Champ Password ───────────────────────────────────
        formPanel.add(creerLabel("Mot de passe"));
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        txtPassword = new JPasswordField();
        styliserChamp(txtPassword);
        formPanel.add(txtPassword);
        formPanel.add(Box.createRigidArea(new Dimension(0, 14)));

        // ── Confirmer Password ───────────────────────────────
        formPanel.add(creerLabel("Confirmer le mot de passe"));
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        txtConfirmPassword = new JPasswordField();
        styliserChamp(txtConfirmPassword);
        formPanel.add(txtConfirmPassword);
        formPanel.add(Box.createRigidArea(new Dimension(0, 14)));

        // ── Choix du Rôle ────────────────────────────────────
        formPanel.add(creerLabel("Votre rôle"));
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cmbRole = new JComboBox<>(new String[]{"CLIENT", "SERVEUSE", "CUISINIER"});
        cmbRole.setMaximumSize(new Dimension(350, 42));
        cmbRole.setPreferredSize(new Dimension(350, 42));
        cmbRole.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cmbRole.setBackground(Color.WHITE);
        cmbRole.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(cmbRole);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // ── Label Erreur ─────────────────────────────────────
        lblErreur = new JLabel("");
        lblErreur.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblErreur.setForeground(new Color(200, 0, 0));
        lblErreur.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(lblErreur);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // ── Bouton Inscrire ──────────────────────────────────
        btnInscrire = new JButton("S'inscrire");
        btnInscrire.setMaximumSize(new Dimension(350, 44));
        btnInscrire.setPreferredSize(new Dimension(350, 44));
        btnInscrire.setBackground(new Color(179, 89, 0));
        btnInscrire.setForeground(Color.WHITE);
        btnInscrire.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnInscrire.setFocusPainted(false);
        btnInscrire.setBorderPainted(false);
        btnInscrire.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnInscrire.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnInscrire.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnInscrire.setBackground(new Color(230, 138, 0));
            }
            public void mouseExited(MouseEvent e) {
                btnInscrire.setBackground(new Color(179, 89, 0));
            }
        });
        formPanel.add(btnInscrire);
        formPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // ── Bouton Retour Login ──────────────────────────────
        btnRetourLogin = new JButton("Déjà un compte ? Se connecter");
        btnRetourLogin.setMaximumSize(new Dimension(350, 36));
        btnRetourLogin.setPreferredSize(new Dimension(350, 36));
        btnRetourLogin.setBackground(Color.WHITE);
        btnRetourLogin.setForeground(new Color(179, 89, 0));
        btnRetourLogin.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnRetourLogin.setFocusPainted(false);
        btnRetourLogin.setBorderPainted(false);
        btnRetourLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRetourLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        formPanel.add(btnRetourLogin);

        mainPanel.add(formPanel);
        setContentPane(mainPanel);
    }

    // ── Méthodes utilitaires ──────────────────────────────────

    private JLabel creerLabel(String texte) {
        JLabel lbl = new JLabel(texte);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lbl.setForeground(Color.DARK_GRAY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private void styliserChamp(JComponent champ) {
        champ.setMaximumSize(new Dimension(350, 42));
        champ.setPreferredSize(new Dimension(350, 42));
        champ.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(224, 193, 161), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        ((JComponent) champ).setFont(new Font("SansSerif", Font.PLAIN, 14));
        champ.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    // ── Getters ───────────────────────────────────────────────

    public String getUsername()     { return txtUsername.getText().trim(); }
    public String getPassword()     { return new String(txtPassword.getPassword()); }
    public String getConfirmPassword() { return new String(txtConfirmPassword.getPassword()); }
    public String getRole()         { return (String) cmbRole.getSelectedItem(); }
    public JButton getBtnInscrire() { return btnInscrire; }
    public JButton getBtnRetourLogin() { return btnRetourLogin; }

    public void afficherErreur(String msg) { lblErreur.setText("⚠ " + msg); }
    public void effacerErreur()            { lblErreur.setText(""); }

    public void reinitialiser() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
        cmbRole.setSelectedIndex(0);
        effacerErreur();
    }
}