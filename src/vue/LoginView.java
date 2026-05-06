
package vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
public class LoginView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblErreur;
    private JButton btnInscription; // ← déclaration en haut

    public LoginView() {
        setTitle("Restaurant App - Connexion");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(247, 239, 230));

        // ── Panel Gauche (Image) ──────────────────────────────
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(new Color(179, 89, 0));
        leftPanel.setPreferredSize(new Dimension(400, 500));

        try {
            ImageIcon img = new ImageIcon(
                new ImageIcon("src/food.jpg")
                    .getImage()
                    .getScaledInstance(400, 500, Image.SCALE_SMOOTH)
            );
            leftPanel.add(new JLabel(img), BorderLayout.CENTER);
        } catch (Exception e) {
            JPanel fallback = new JPanel(new GridBagLayout());
            fallback.setBackground(new Color(179, 89, 0));

            JLabel nom = new JLabel("🍽 Restaurant");
            nom.setFont(new Font("SansSerif", Font.BOLD, 32));
            nom.setForeground(Color.WHITE);

            JLabel sous = new JLabel("Bienvenue dans notre système");
            sous.setFont(new Font("SansSerif", Font.PLAIN, 14));
            sous.setForeground(new Color(255, 220, 180));

            JPanel txt = new JPanel();
            txt.setOpaque(false);
            txt.setLayout(new BoxLayout(txt, BoxLayout.Y_AXIS));
            nom.setAlignmentX(Component.CENTER_ALIGNMENT);
            sous.setAlignmentX(Component.CENTER_ALIGNMENT);
            txt.add(nom);
            txt.add(Box.createRigidArea(new Dimension(0, 10)));
            txt.add(sous);

            fallback.add(txt);
            leftPanel.add(fallback, BorderLayout.CENTER);
        }

        // ── Panel Droit (Formulaire) ──────────────────────────
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setPreferredSize(new Dimension(400, 500));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Titre
        JLabel titre = new JLabel("Connexion");
        titre.setFont(new Font("SansSerif", Font.BOLD, 30));
        titre.setForeground(new Color(179, 89, 0));
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Sous-titre
        JLabel sousTitre = new JLabel("Connectez-vous à votre espace");
        sousTitre.setFont(new Font("SansSerif", Font.PLAIN, 14));
        sousTitre.setForeground(Color.GRAY);
        sousTitre.setAlignmentX(Component.CENTER_ALIGNMENT);

        formPanel.add(titre);
        formPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        formPanel.add(sousTitre);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Séparateur
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(300, 2));
        sep.setForeground(new Color(224, 193, 161));
        formPanel.add(sep);
        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Champ Username
        JLabel lblUser = new JLabel("Nom d'utilisateur");
        lblUser.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblUser.setForeground(Color.DARK_GRAY);
        lblUser.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtUsername = new JTextField();
        txtUsername.setMaximumSize(new Dimension(300, 42));
        txtUsername.setPreferredSize(new Dimension(300, 42));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(224, 193, 161), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtUsername.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtUsername.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Champ Password
        JLabel lblPass = new JLabel("Mot de passe");
        lblPass.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblPass.setForeground(Color.DARK_GRAY);
        lblPass.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtPassword = new JPasswordField();
        txtPassword.setMaximumSize(new Dimension(300, 42));
        txtPassword.setPreferredSize(new Dimension(300, 42));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(224, 193, 161), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtPassword.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Label erreur
        lblErreur = new JLabel("");
        lblErreur.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblErreur.setForeground(new Color(200, 0, 0));
        lblErreur.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Bouton Login
        btnLogin = new JButton("Se connecter");
        btnLogin.setMaximumSize(new Dimension(300, 44));
        btnLogin.setPreferredSize(new Dimension(300, 44));
        btnLogin.setBackground(new Color(179, 89, 0));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLogin.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(new Color(230, 138, 0));
            }
            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(new Color(179, 89, 0));
            }
        });

        txtPassword.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnLogin.doClick();
                }
            }
        });

        // ── Bouton Inscription ────────────────────────────────
        // Initialisé dans le constructeur ← correction principale
        btnInscription = new JButton("Nouveau ? Créer un compte");
        btnInscription.setMaximumSize(new Dimension(300, 36));
        btnInscription.setPreferredSize(new Dimension(300, 36));
        btnInscription.setBackground(Color.WHITE);
        btnInscription.setForeground(new Color(179, 89, 0));
        btnInscription.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnInscription.setFocusPainted(false);
        btnInscription.setBorderPainted(false);
        btnInscription.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnInscription.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Info rôles en bas
        JLabel lblInfo = new JLabel("Serveuse · Client · Cuisinier");
        lblInfo.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lblInfo.setForeground(new Color(180, 180, 180));
        lblInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ── Ajout des composants au panel ────────────────────
        formPanel.add(lblUser);
        formPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        formPanel.add(txtUsername);
        formPanel.add(Box.createRigidArea(new Dimension(0, 16)));
        formPanel.add(lblPass);
        formPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        formPanel.add(txtPassword);
        formPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        formPanel.add(lblErreur);
        formPanel.add(Box.createRigidArea(new Dimension(0, 16)));
        formPanel.add(btnLogin);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(btnInscription); // ← ajouté au panel
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(lblInfo);

        rightPanel.add(formPanel);
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    // ── Getters ───────────────────────────────────────────────

    public String getUsername() { return txtUsername.getText().trim(); }
    public String getPassword() { return new String(txtPassword.getPassword()); }
    public JButton getBtnLogin() { return btnLogin; }
    public JButton getBtnInscription() { return btnInscription; } // ← corrigé

    // ── Méthodes utilitaires ──────────────────────────────────

    public void afficherErreur(String message) { lblErreur.setText("⚠ " + message); }
    public void effacerErreur() { lblErreur.setText(""); }

    public void reinitialiser() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtUsername.requestFocus();
        effacerErreur();
    }

    public void afficherMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}