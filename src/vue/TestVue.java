package vue;

import controlleur.LoginControleur;

public class TestVue {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            LoginView vue = new LoginView();
            new LoginControleur(vue);
            vue.setVisible(true);
            // ← rien d'autre ici !
            // ClientControleur est appelé automatiquement
            // dans LoginControleur quand le rôle est CLIENT
        });
    }
}