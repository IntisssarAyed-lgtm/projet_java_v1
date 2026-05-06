package controlleur;



import vue.SeurveuseView;
import vue.LoginView;
import DAOc.*;
import modele.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import java.time.LocalDateTime;

public class ServeuseControlleur {

    private SeurveuseView vue;
    private Utilisateurs serveuse;
    private MenuDAO menuDAO;
    private PlatDAO platDAO;
    private CommandeDAO commandeDAO;
    private LigneCommandeDAO ligneDAO;
    private FactureDAO factureDAO;

    private List<LigneCommande> panier = new ArrayList<>();
    private List<Commande> commandesRecues = new ArrayList<>();

    public ServeuseControlleur(SeurveuseView vue, Utilisateurs serveuse) {
        this.vue         = vue;
        this.serveuse    = serveuse;
        this.menuDAO     = new MenuDAO();
        this.platDAO     = new PlatDAO();
        this.commandeDAO = new CommandeDAO();
        this.ligneDAO    = new LigneCommandeDAO();
        this.factureDAO  = new FactureDAO();

        chargerMenus();
        chargerCommandesEnCours();
        chargerCommandesRecues();

        // Onglet Commander
        vue.getCmbMenus().addActionListener(e -> chargerPlats());
        vue.getBtnAjouter().addActionListener(e -> ajouterAuPanier());
        vue.getBtnSupprimer().addActionListener(e -> supprimerDuPanier());
        vue.getBtnCommander().addActionListener(e -> passerCommande());

        // Onglet En cours
        vue.getBtnRafraichirEnCours().addActionListener(e -> chargerCommandesEnCours());

        // Onglet Reçues
        vue.getBtnRafraichirRecues().addActionListener(e -> chargerCommandesRecues());
        vue.getBtnGenererFacture().addActionListener(e -> genererFacture());

        // Déconnexion
        vue.getBtnDeconnexion().addActionListener(e -> deconnexion());
    }

    // ── Onglet Commander ─────────────────────────────────────

    private void chargerMenus() {
        vue.getCmbMenus().removeAllItems();
        for (Menu m : menuDAO.findAll()) {
            vue.getCmbMenus().addItem(m);
        }
        chargerPlats();
    }

    private void chargerPlats() {
        vue.getModelePlats().clear();
        Menu menu = (Menu) vue.getCmbMenus().getSelectedItem();
        if (menu != null) {
            for (Plat p : platDAO.findByMenu(menu.getId())) {
                vue.getModelePlats().addElement(p);
            }
        }
    }

    private void ajouterAuPanier() {
        Plat plat = vue.getPlatSelectionne();
        if (plat == null) {
            vue.afficherErreur("Veuillez sélectionner un plat.");
            return;
        }
        int quantite = vue.getQuantite();
        for (LigneCommande lc : panier) {
            if (lc.getPlat().getId() == plat.getId()) {
                lc.setQuantite(lc.getQuantite() + quantite);
                rafraichirTableau();
                return;
            }
        }
        panier.add(new LigneCommande(null, plat, quantite));
        rafraichirTableau();
    }

    private void supprimerDuPanier() {
        int ligne = vue.getLigneSelectionnee();
        if (ligne == -1) {
            vue.afficherErreur("Veuillez sélectionner une ligne.");
            return;
        }
        panier.remove(ligne);
        rafraichirTableau();
    }

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

    private void passerCommande() {
        if (panier.isEmpty()) {
            vue.afficherErreur("Le panier est vide !");
            return;
        }

        Commande commande = new Commande();
        commande.setClient(serveuse); // la serveuse passe la commande
        commande.setServeuse(serveuse);
        commande.setStatut("EN_ATTENTE");
        commande.setDateCommande(LocalDateTime.now());

        boolean ok = commandeDAO.create(commande);
        if (!ok) {
            vue.afficherErreur("Erreur lors de la création de la commande.");
            return;
        }

        List<Commande> commandes = commandeDAO.findByClient(serveuse.getId());
        Commande derniereCommande = commandes.get(commandes.size() - 1);

        for (LigneCommande lc : panier) {
            lc.setCommande(derniereCommande);
            ligneDAO.create(lc);
        }

        panier.clear();
        rafraichirTableau();
        vue.afficherMessage("✅ Commande passée avec succès !");
    }

    // ── Onglet En cours ──────────────────────────────────────

    private void chargerCommandesEnCours() {
        vue.getModeleEnCours().setRowCount(0);
        List<Commande> enCours = commandeDAO.findEnCours();
        for (Commande c : enCours) {
            vue.getModeleEnCours().addRow(new Object[]{
                c.getId(),
                c.getClient().getUsername(),
                c.getDateCommande(),
                c.getStatut()
            });
        }
    }

    // ── Onglet Reçues ─────────────────────────────────────────

    private void chargerCommandesRecues() {
        vue.getModeleRecues().setRowCount(0);
        commandesRecues = commandeDAO.findPret();
        for (Commande c : commandesRecues) {
            double total = ligneDAO.calculerTotal(c.getId());
            vue.getModeleRecues().addRow(new Object[]{
                c.getId(),
                c.getClient().getUsername(),
                c.getDateCommande(),
                c.getStatut(),
                String.format("%.2f DT", total)
            });
        }
    }

    private void genererFacture() {
        int ligne = vue.getLigneRecueSelectionnee();
        if (ligne == -1) {
            vue.afficherErreur("Veuillez sélectionner une commande.");
            return;
        }

        Commande commande = commandesRecues.get(ligne);
        double total = ligneDAO.calculerTotal(commande.getId());

        // Créer la facture
        Facture facture = new Facture();
        facture.setCommande(commande);
        facture.setTotal(total);
        facture.setDateCommande(LocalDateTime.now());
        factureDAO.create(facture);

        // Changer statut → SERVI
        commandeDAO.changerStatut(commande.getId(), "SERVI");

        // Afficher la facture
        List<LigneCommande> lignes = ligneDAO.findByCommande(commande.getId());
        StringBuilder sb = new StringBuilder();
        sb.append("════════════════════════\n");
        sb.append("      🧾 FACTURE\n");
        sb.append("════════════════════════\n");
        sb.append("Client : ").append(commande.getClient().getUsername()).append("\n");
        sb.append("Date   : ").append(commande.getDateCommande()).append("\n");
        sb.append("────────────────────────\n");
        for (LigneCommande lc : lignes) {
            sb.append(lc.getPlat().getNom())
              .append(" x").append(lc.getQuantite())
              .append("  →  ")
              .append(String.format("%.2f DT", lc.getPlat().getPrix() * lc.getQuantite()))
              .append("\n");
        }
        sb.append("────────────────────────\n");
        sb.append("TOTAL : ").append(String.format("%.2f DT", total)).append("\n");
        sb.append("════════════════════════");

        JOptionPane.showMessageDialog(vue, sb.toString(),
            "Facture générée", JOptionPane.INFORMATION_MESSAGE);

        chargerCommandesRecues();
    }

    private void deconnexion() {
        vue.dispose();
        LoginView loginVue = new LoginView();
        new LoginControleur(loginVue);
        loginVue.setVisible(true);
    }
}