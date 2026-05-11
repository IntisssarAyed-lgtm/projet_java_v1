package util;
import vue.LoginView;
import controlleur.LoginControleur;

public class Test {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            LoginView vue = new LoginView();
            new LoginControleur(vue);
            vue.setVisible(true);
            
        });
    }
}