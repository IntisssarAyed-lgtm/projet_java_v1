package controlleur;



import vue.InscriptionView;
import vue.LoginView;

import javax.swing.JOptionPane;

import DAOc.UtilisateurDAO;
import modele.Utilisateurs;

public class InscriptionControleur {

    private InscriptionView vue;
    private UtilisateurDAO dao;

    public InscriptionControleur(InscriptionView vue) {
        this.vue = vue;
        this.dao = new UtilisateurDAO();

        // Bouton S'inscrire
        this.vue.getBtnInscrire().addActionListener(e -> inscrire());

        // Bouton Retour Login
        this.vue.getBtnRetourLogin().addActionListener(e -> retourLogin());
    }

    private void inscrire() {
        String username = vue.getUsername();
        String password = vue.getPassword();
        String confirm  = vue.getConfirmPassword();
        String role     = vue.getRole();

        // ── Validations ───────────────────────────────────────

        // Champs vides
        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            vue.afficherErreur("Veuillez remplir tous les champs.");
            return;
        }

        // Username trop court
        if (username.length() < 3) {
            vue.afficherErreur("Le nom d'utilisateur doit avoir au moins 3 caractères.");
            return;
        }

        // Mot de passe trop court
        if (password.length() < 4) {
            vue.afficherErreur("Le mot de passe doit avoir au moins 4 caractères.");
            return;
        }

        // Confirmation mot de passe
        if (!password.equals(confirm)) {
            vue.afficherErreur("Les mots de passe ne correspondent pas.");
            return;
        }

        // Username déjà pris
        if (dao.findByUsername(username) != null) {
            vue.afficherErreur("Ce nom d'utilisateur est déjà utilisé.");
            return;
        }

        // ── Insertion en BD ───────────────────────────────────
        Utilisateurs nouvelUser = new Utilisateurs(0, username, password, role);
        boolean succes = dao.create(nouvelUser);

        if (succes) {
            JOptionPane.showMessageDialog(vue,
                "Compte créé avec succès !\nVous pouvez maintenant vous connecter.",
                "Inscription réussie",
                JOptionPane.INFORMATION_MESSAGE
            );
            retourLogin();
        } else {
            vue.afficherErreur("Erreur lors de la création du compte.");
        }
    }

    private void retourLogin() {
        vue.dispose();
        LoginView loginVue = new LoginView();
        new LoginControleur(loginVue);
        loginVue.setVisible(true);
    }
}