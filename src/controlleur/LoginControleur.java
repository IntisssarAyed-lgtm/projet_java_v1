package controlleur;

import vue.InscriptionView;

import vue.LoginView;
import DAOc.UtilisateurDAO;
import modele.Utilisateurs;


public class LoginControleur {

    private LoginView vue;
    private UtilisateurDAO dao;

    public LoginControleur(LoginView vue) {
        this.vue = vue;
        this.dao = new UtilisateurDAO();

        // Action du bouton
        this.vue.getBtnLogin().addActionListener(e -> seConnecter());
    }

    private void seConnecter() {
        String username = vue.getUsername();
        String password = vue.getPassword();

        // Validation champs vides
        if (username.isEmpty() || password.isEmpty()) {
            vue.afficherErreur("Veuillez remplir tous les champs.");
            return;
        }

        // Vérification en base
        Utilisateurs user = dao.login(username, password);

        if (user == null) {
            vue.afficherErreur("Identifiants incorrects.");
            vue.reinitialiser();
            return;
        }

        // Redirection selon le rôle
        vue.effacerErreur();
        vue.dispose();

        /*switch (user.getRole()) {
            case "CLIENT"   -> new ClientView(user).setVisible(true);
            case "SERVEUSE" -> new ServeauseView(user).setVisible(true);
            case "CUISINIER"-> new CuisinierView(user).setVisible(true);
            default -> vue.afficherErreur("Rôle inconnu : " + user.getRole());
        }*/
    
 // Dans le constructeur de LoginControleur
    this.vue.getBtnInscription().addActionListener(e -> {
        vue.dispose();
        InscriptionView inscVue = new InscriptionView();
        new InscriptionControleur(inscVue);
        inscVue.setVisible(true);
    });
}}