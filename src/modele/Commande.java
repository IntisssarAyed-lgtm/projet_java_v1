package modele;
import java.time.LocalDateTime;
import java.util.List;

public class Commande {
	

	
	    private int id;

	    // Relations
	    private Utilisateurs client;
	    private Utilisateurs serveuse;

	    public enum StatutCommande {
	        EN_ATTENTE,   // commande passée, pas encore prise
	        EN_COURS,     // cuisinier a commencé
	        PRET,         // cuisinier a terminé → notification serveuse
	        SERVI,        // serveuse a livré
	        ANNULE        // annulé par cuisinier
	    }
	    private LocalDateTime dateCommande;

	    private List<LigneCommande> lignes;

		private String statut;

	    public Commande() {}

	    public Commande(int id, Utilisateurs client, Utilisateurs serveuse,
	                    String statut, LocalDateTime dateCommande) {
	        this.id = id;
	        this.client = client;
	        this.serveuse = serveuse;
	        this.statut = statut;
	        this.dateCommande = dateCommande;
	    }

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getStatut() {
			return statut;
		}

		public Utilisateurs getClient() {
			return client;
		}

		public void setClient(Utilisateurs client) {
			this.client = client;
		}

		public Utilisateurs getServeuse() {
			return serveuse;
		}

		public void setServeuse(Utilisateurs serveuse) {
			this.serveuse = serveuse;
		}

		public void setStatut(String statut) {
			this.statut = statut;
		}

		public LocalDateTime getDateCommande() {
			return dateCommande;
		}

		public void setDateCommande(LocalDateTime dateCommande) {
			this.dateCommande = dateCommande;
		}

	    // Getters & Setters
	}


