package modele;
import java.util.*;

public class Menu {
	private int id;
    private String nom;
    private String description;

    // Relation
    private List<Plat> plats = new ArrayList<>();

	public Menu(int id, String nom, String description) {
		super();
		this.id = id;
		this.nom = nom;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Plat> getPlats() {
		return plats;
	}

	public void setPlats(List<Plat> plats) {
		this.plats = plats;
	}

	@Override
	public String toString() {
	    return "Menu [id=" + id + 
	           ", nom=" + nom + 
	           ", description=" + description + "]";
	}

	
    

}
