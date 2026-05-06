package controlleur;

import vue.LoginView;
import vue.SeurveuseView;
import vue.InscriptionView;
import DAOc.UtilisateurDAO;
import modele.Utilisateurs;
import vue.ClientView;
import vue.CuisinierView;

import javax.swing.JOptionPane;

public class LoginControleur {

    private LoginView vue;
    private UtilisateurDAO dao;

    public LoginControleur(LoginView vue) {
        this.vue = vue;
        this.dao = new UtilisateurDAO();

        // Bouton Se connecter
        this.vue.getBtnLogin().addActionListener(e -> seConnecter());

        // Bouton Inscription
        this.vue.getBtnInscription().addActionListener(e -> allerInscription());
    }

    private void seConnecter() {
        String username = vue.getUsername();
        String password = vue.getPassword();

        // Champs vides
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

        // Redirection temporaire selon rôle
        // (remplacer plus tard par les vraies vues)
        vue.effacerErreur();
        vue.dispose();

        JOptionPane.showMessageDialog(null,
            "✓ Bienvenue " + user.getUsername() + " !\nRôle : " + user.getRole(),
            "Connexion réussie",
            JOptionPane.INFORMATION_MESSAGE
        );

        // Décommenter quand les vues seront créées :
        switch (user.getRole()) {
           case "CLIENT"    -> {ClientView v=new ClientView(user);
           v.setVisible(true);
           new ClientControleur(v, user);}
         
               // ServeuseView plus tard
           case "SERVEUSE" -> {
        	    SeurveuseView serveuseVue = new SeurveuseView(user);
        	    new ServeuseControlleur(serveuseVue, user);
        	    serveuseVue.setVisible(true);
        	}
           
           case "CUISINIER" -> {
        	    CuisinierView cuiVue = new CuisinierView(user);
        	    new CuisinierControlleur(cuiVue, user);
        	    cuiVue.setVisible(true);
        	}
        //     case "SERVEUSE"  -> new ServeuseView(user).setVisible(true);
        //     case "CUISINIER" -> new CuisinierView(user).setVisible(true);
         }
    }

    private void allerInscription() {
        vue.dispose();
        InscriptionView inscVue = new InscriptionView();
        new InscriptionControleur(inscVue);
        inscVue.setVisible(true);
    
    
        }}