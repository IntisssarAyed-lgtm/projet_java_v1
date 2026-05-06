package controlleur;

import vue.ClientView;
import vue.LoginView;
import DAOc.*;
import modele.*;
import java.util.ArrayList;
import java.util.List;

public class ClientControleur {

    private ClientView vue;
    private Utilisateurs client;
    private MenuDAO menuDAO;
    private PlatDAO platDAO;
    private CommandeDAO commandeDAO;
    private LigneCommandeDAO ligneDAO;

    // Panier en mémoire
    private List<LigneCommande> panier = new ArrayList<>();

    public ClientControleur(ClientView vue, Utilisateurs client) {
        this.vue        = vue;
        this.client     = client;
        this.menuDAO    = new MenuDAO();
        this.platDAO    = new PlatDAO();
        this.commandeDAO = new CommandeDAO();
        this.ligneDAO   = new LigneCommandeDAO();

        chargerMenus();

        // Quand on change de menu → charger ses plats
        vue.getCmbMenus().addActionListener(e -> chargerPlats());

        // Boutons
        vue.getBtnAjouter().addActionListener(e -> ajouterAuPanier());
        vue.getBtnSupprimer().addActionListener(e -> supprimerDuPanier());
        vue.getBtnCommander().addActionListener(e -> passerCommande());
        vue.getBtnDeconnexion().addActionListener(e -> deconnexion());
    }

    // Charge tous les menus dans le ComboBox
    private void chargerMenus() {
    	vue.getCmbMenus().removeAllItems();
        
        List<Menu> menus = menuDAO.findAll();
        
        // LOG — affiche dans la console Eclipse
        System.out.println("Nombre de menus trouvés : " + menus.size());
        
        for (Menu m : menus) {
            System.out.println("Menu : " + m.getId() + " - " + m.getNom());
            vue.getCmbMenus().addItem(m);
        }
        
        chargerPlats();
    }

    // Charge les plats du menu sélectionné
    private void chargerPlats() {
        vue.getModelePlats().clear();
        Menu menuSelectionne = (Menu) vue.getCmbMenus().getSelectedItem();
        
        // LOG
        System.out.println("Menu sélectionné : " + menuSelectionne);
        
        if (menuSelectionne != null) {
            List<Plat> plats = platDAO.findByMenu(menuSelectionne.getId());
            
            // LOG
            System.out.println("Nombre de plats trouvés : " + plats.size());
            
            for (Plat p : plats) {
                System.out.println("Plat : " + p.getNom() + " - " + p.getPrix());
                vue.getModelePlats().addElement(p);
            }
        }
    }
    // Ajoute un plat au panier
    private void ajouterAuPanier() {
        Plat plat = vue.getPlatSelectionne();
        if (plat == null) {
            vue.afficherErreur("Veuillez sélectionner un plat.");
            return;
        }

        int quantite = vue.getQuantite();

        // Vérifie si le plat est déjà dans le panier
        for (LigneCommande lc : panier) {
            if (lc.getPlat().getId() == plat.getId()) {
                lc.setQuantite(lc.getQuantite() + quantite);
                rafraichirTableau();
                return;
            }
        }

        // Nouveau plat dans le panier
        panier.add(new LigneCommande(null, plat, quantite));
        rafraichirTableau();
    }

    // Supprime une ligne du panier
    private void supprimerDuPanier() {
        int ligne = vue.getLigneSelectionnee();
        if (ligne == -1) {
            vue.afficherErreur("Veuillez sélectionner une ligne à supprimer.");
            return;
        }
        panier.remove(ligne);
        rafraichirTableau();
    }

    // Met à jour le tableau et le total
    private void rafraichirTableau() {
        vue.getModeleCommande().setRowCount(0);
        double total = 0;
        for (LigneCommande lc : panier) {
            double sousTotal = lc.getPlat().getPrix() * lc.getQuantite();
            total += sousTotal;
            vue.getModeleCommande().addRow(new Object[]{
                lc.getPlat().getNom(),
                String.format("%.2f DT", lc.getPlat().getPrix()),
                lc.getQuantite(),
                String.format("%.2f DT", sousTotal)
            });
        }
        vue.setTotal(total);
    }

    // Passe la commande en base
    
    	private void passerCommande() {
    	    if (panier.isEmpty()) {
    	        vue.afficherErreur("Votre panier est vide !");
    	        return;
    	    }

    	    // ── Vérification commande déjà en cours ──────────────────
    	    List<Commande> commandesEnCours = commandeDAO.findByClient(client.getId());
    	    for (Commande c : commandesEnCours) {
    	        if (c.getStatut().equals("EN_ATTENTE") || c.getStatut().equals("EN_COURS")) {
    	            vue.afficherErreur(
    	                "Vous avez déjà une commande en cours !\n" +
    	                "Attendez qu'elle soit servie avant d'en passer une nouvelle."
    	            );
    	            return;
    	        }
    	    }

    	    // ── Créer la commande ─────────────────────────────────────
    	    Commande commande = new Commande();
    	    commande.setClient(client);
    	    commande.setStatut("EN_ATTENTE");
    	    commande.setDateCommande(java.time.LocalDateTime.now());

    	    boolean ok = commandeDAO.create(commande);
    	    if (!ok) {
    	        vue.afficherErreur("Erreur lors de la création de la commande.");
    	        return;
    	    }

    	    // ── Récupère l'id de la commande créée ───────────────────
    	    List<Commande> commandes = commandeDAO.findByClient(client.getId());
    	    Commande derniereCommande = commandes.get(commandes.size() - 1);

    	    System.out.println("Commande créée avec id : " + derniereCommande.getId());

    	    // ── Insérer les lignes de commande ────────────────────────
    	    boolean toutesLignesOk = true;
    	    for (LigneCommande lc : panier) {
    	        lc.setCommande(derniereCommande);
    	        boolean ligneOk = ligneDAO.create(lc);
    	        System.out.println("Ligne insérée : " + lc.getPlat().getNom() + " → " + ligneOk);
    	        if (!ligneOk) toutesLignesOk = false;
    	    }

    	    if (!toutesLignesOk) {
    	        vue.afficherErreur("Commande créée mais certains plats n'ont pas été enregistrés.");
    	        return;
    	    }

    	    // ── Vider le panier ───────────────────────────────────────
    	    panier.clear();
    	    rafraichirTableau(); // ← tableau vide + total = 0.00 DT

    	    vue.afficherMessage(
    	        "✅ Commande passée avec succès !\n" +
    	        "Elle est en attente de traitement par le cuisinier."
    	    );
    	}

    // Déconnexion
    private void deconnexion() {
        vue.dispose();
        LoginView loginVue = new LoginView();
        new LoginControleur(loginVue);
        loginVue.setVisible(true);
    }
}