package util;
import java.util.List;
import modele.*;
import DAOc.*;



public class Test {

	public static void main(String[] args) {
		// pour tester le travial de singletonConnection 
		
		/*UtilisateurDAO d=new UtilisateurDAO();
		
		List <Utilisateurs>cat=d.findAll();
		for(Utilisateurs ca:cat) {
			System.out.println(ca);
		
	}*/
		PlatDAO p =new PlatDAO();
		MenuDAO m=new MenuDAO();
		System.out.println("les listes de plat ");
		for(Plat p1:p.findAll()) {
			System.out.println(p1);
		}
		System.out.println("les listes de plat de menu d'entrés ");
		Menu menuAP=m.findWithPlats(1);
		System.out.println("Menu : " + menuAP.getNom());
		System.out.println("Plats :");
		for(Plat p2:menuAP.getPlats()) {
			System.out.println("  → " + p2.getNom() + " (" + p2.getPrix() + " DT)");
		}
	}}
