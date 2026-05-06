package modele;

import java.time.LocalDateTime;

public class Facture {

    private int id;
    private double total;
    private LocalDateTime dateCommande;

    // Relation
    private Commande commande;

    public Facture() {}

    public Facture(int id, double total, LocalDateTime dateCommande, Commande commande) {
        this.id = id;
        this.total = total;
        this.dateCommande = dateCommande;
        this.commande = commande;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public LocalDateTime getDateCommande() {
		return dateCommande;
	}

	public void setDateCommande(LocalDateTime dateCommande) {
		this.dateCommande = dateCommande;
	}

	public Commande getCommande() {
		return commande;
	}

	public void setCommande(Commande commande) {
		this.commande = commande;
	}

    // Getters & Setters
}
