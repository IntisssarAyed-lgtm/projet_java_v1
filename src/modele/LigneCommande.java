package modele;

public class LigneCommande {

    private Commande commande;
    private Plat plat;
    private int quantite;

    public LigneCommande() {}

    public LigneCommande(Commande commande, Plat plat, int quantite) {
        this.commande = commande;
        this.plat = plat;
        this.quantite = quantite;
    }

	public Commande getCommande() {
		return commande;
	}

	public void setCommande(Commande commande) {
		this.commande = commande;
	}

	public Plat getPlat() {
		return plat;
	}

	public void setPlat(Plat plat) {
		this.plat = plat;
	}

	public int getQuantite() {
		return quantite;
	}

	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}

    // Getters & Setters
}