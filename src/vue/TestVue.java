package vue;
import controlleur.LoginControleur;


public class TestVue {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
LoginView v =new LoginView();

new LoginControleur(v);
v.setVisible(true);
	}

}
