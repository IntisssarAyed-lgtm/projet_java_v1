package modele;

public class Plat {
	private int id;
    private String nom;
    private String description;
    private double prix;
 // Relation
    private Menu menu;
    public Plat() {}

    public Plat(int id, String nom, String description, double prix, Menu menu) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.menu = menu;
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

	public double getPrix() {
		return prix;
	}

	public void setPrix(double prix) {
		this.prix = prix;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	@Override
	public String toString() {
		return "Plat [id=" + id + ", nom=" + nom + ", description=" + description + ", prix=" + prix + ", menu=" +(menu != null ? menu.getNom() : "null") + "]";
	}

    // Getters & Setters

}
