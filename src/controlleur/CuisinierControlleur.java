package controlleur;

import vue.CuisinierView;
import vue.LoginView;
import DAOc.*;
import modele.*;
import java.util.List;

import javax.swing.JOptionPane;

import java.util.ArrayList;

public class CuisinierControlleur {

    private CuisinierView vue;
    private Utilisateurs cuisinier;
    private MenuDAO menuDAO;
    private PlatDAO platDAO;
    private CommandeDAO commandeDAO;
    private LigneCommandeDAO ligneDAO;

    // Listes en mémoire pour retrouver l'id par ligne sélectionnée
    private List<Plat>    plats          = new ArrayList<>();
    private List<Commande> commandesAttente = new ArrayList<>();
    private List<Commande> commandesEnCours = new ArrayList<>();
    private List<Commande> commandesServies = new ArrayList<>();

    public CuisinierControlleur(CuisinierView vue, Utilisateurs cuisinier) {
        this.vue        = vue;
        this.cuisinier  = cuisinier;
        this.menuDAO    = new MenuDAO();
        this.platDAO    = new PlatDAO();
        this.commandeDAO = new CommandeDAO();
        this.ligneDAO   = new LigneCommandeDAO();

        chargerMenusDansCombo();
        chargerTousLesPlats();
        chargerCommandesAttente();
        chargerCommandesEnCours();
        chargerCommandesServies();

        // ── Onglet Plats ──────────────────────────────────────
        vue.getBtnAjouterPlat().addActionListener(e -> ajouterPlat());
        vue.getBtnModifierPlat().addActionListener(e -> modifierPlat());
        vue.getBtnSupprimerPlat().addActionListener(e -> supprimerPlat());
        vue.getBtnEnregistrerPlat().addActionListener(e -> enregistrerPlat());
        vue.getBtnFermerPlat().addActionListener(e -> vue.viderFormulaire());

        // Clic sur une ligne → remplir le formulaire
        vue.getTablePlats().getSelectionModel()
            .addListSelectionListener(e -> remplirFormulaire());

        // ── Onglet Attente ────────────────────────────────────
        vue.getBtnCommencer().addActionListener(e -> commencerCommande());
        vue.getBtnAnnuler().addActionListener(e -> annulerCommande());
        vue.getBtnRafraichirAttente().addActionListener(e -> chargerCommandesAttente());

        // ── Onglet En cours ───────────────────────────────────
        vue.getBtnMarquerPret().addActionListener(e -> marquerPret());
        vue.getBtnRafraichirEnCours().addActionListener(e -> chargerCommandesEnCours());

        // ── Onglet Servi ──────────────────────────────────────
        vue.getBtnRafraichirServi().addActionListener(e -> chargerCommandesServies());

        // ── Déconnexion ───────────────────────────────────────
        vue.getBtnDeconnexion().addActionListener(e -> deconnexion());
    }

    // ── Gestion Plats ─────────────────────────────────────────

    private void chargerMenusDansCombo() {
        vue.getCmbMenuPlat().removeAllItems();
        for (Menu m : menuDAO.findAll()) {
            vue.getCmbMenuPlat().addItem(m);
        }
    }

    private void chargerTousLesPlats() {
        vue.getModelePlats().setRowCount(0);
        plats = platDAO.findAll();
        for (Plat p : plats) {
            vue.getModelePlats().addRow(new Object[]{
                p.getId(),
                p.getNom(),
                p.getDescription(),
                String.format("%.2f DT", p.getPrix()),
                p.getMenu() != null ? p.getMenu().getNom() : ""
            });
        }
    }

    private void remplirFormulaire() {
        int ligne = vue.getLignePlatSelectionnee();
        if (ligne == -1 || ligne >= plats.size()) return;
        Plat p = plats.get(ligne);
        vue.getTxtNomPlat().setText(p.getNom());
        vue.getTxtDescriptionPlat().setText(p.getDescription());
        vue.getTxtPrixPlat().setText(String.valueOf(p.getPrix()));
        // Sélectionne le bon menu dans le combo
        for (int i = 0; i < vue.getCmbMenuPlat().getItemCount(); i++) {
            if (vue.getCmbMenuPlat().getItemAt(i).getId() == p.getMenu().getId()) {
                vue.getCmbMenuPlat().setSelectedIndex(i);
                break;
            }
        }
    }

    private void ajouterPlat() {
        vue.viderFormulaire();
        vue.getTxtNomPlat().requestFocus();
    }

    private void enregistrerPlat() {
        String nom         = vue.getTxtNomPlat().getText().trim();
        String description = vue.getTxtDescriptionPlat().getText().trim();
        String prixStr     = vue.getTxtPrixPlat().getText().trim();
        Menu menu          = (Menu) vue.getCmbMenuPlat().getSelectedItem();

        if (nom.isEmpty() || prixStr.isEmpty() || menu == null) {
            vue.afficherErreur("Veuillez remplir tous les champs obligatoires.");
            return;
        }

        double prix;
        try {
            prix = Double.parseDouble(prixStr);
        } catch (NumberFormatException e) {
            vue.afficherErreur("Le prix doit être un nombre (ex: 12.50)");
            return;
        }

        int ligne = vue.getLignePlatSelectionnee();

        if (ligne == -1) {
            // Nouveau plat
            Plat nouveau = new Plat(0, nom, description, prix, menu);
            if (platDAO.create(nouveau)) {
                vue.afficherMessage("✅ Plat ajouté avec succès !");
            } else {
                vue.afficherErreur("Erreur lors de l'ajout du plat.");
                return;
            }
        } else {
            // Modifier plat existant
            Plat existant = plats.get(ligne);
            existant.setNom(nom);
            existant.setDescription(description);
            existant.setPrix(prix);
            existant.setMenu(menu);
            if (platDAO.update(existant)) {
                vue.afficherMessage("✅ Plat modifié avec succès !");
            } else {
                vue.afficherErreur("Erreur lors de la modification.");
                return;
            }
        }

        vue.viderFormulaire();
        chargerTousLesPlats();
    }

    private void modifierPlat() {
        int ligne = vue.getLignePlatSelectionnee();
        if (ligne == -1) {
            vue.afficherErreur("Veuillez sélectionner un plat à modifier.");
            return;
        }
        vue.getTxtNomPlat().requestFocus();
        vue.afficherMessage("Modifiez les champs puis cliquez sur Enregistrer.");
    }

    private void supprimerPlat() {
        int ligne = vue.getLignePlatSelectionnee();
        if (ligne == -1) {
            vue.afficherErreur("Veuillez sélectionner un plat à supprimer.");
            return;
        }

        Plat plat = plats.get(ligne);
        int confirm = JOptionPane.showConfirmDialog(vue,
            "Supprimer le plat \"" + plat.getNom() + "\" ?",
            "Confirmation", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (platDAO.delete(plat.getId())) {
                vue.afficherMessage("✅ Plat supprimé !");
                chargerTousLesPlats();
            } else {
                vue.afficherErreur("Erreur lors de la suppression.");
            }
        }
    }

    // ── Gestion Commandes ─────────────────────────────────────

    private String getPlatsCommande(int commandeId) {
        List<LigneCommande> lignes = ligneDAO.findByCommande(commandeId);
        StringBuilder sb = new StringBuilder();
        for (LigneCommande lc : lignes) {
            sb.append(lc.getPlat().getNom())
              .append(" x").append(lc.getQuantite()).append(" | ");
        }
        return sb.length() > 0 ? sb.substring(0, sb.length() - 3) : "";
    }

    private void chargerCommandesAttente() {
        vue.getModeleAttente().setRowCount(0);
        commandesAttente = commandeDAO.findEnAttente();
        for (Commande c : commandesAttente) {
            vue.getModeleAttente().addRow(new Object[]{
                c.getId(),
                c.getClient().getUsername(),
                c.getDateCommande(),
                getPlatsCommande(c.getId())
            });
        }
    }

    private void chargerCommandesEnCours() {
        vue.getModeleEnCours().setRowCount(0);
        commandesEnCours = commandeDAO.findEnCours();
        for (Commande c : commandesEnCours) {
            vue.getModeleEnCours().addRow(new Object[]{
                c.getId(),
                c.getClient().getUsername(),
                c.getDateCommande(),
                getPlatsCommande(c.getId()),
                c.getStatut()
            });
        }
    }

    private void chargerCommandesServies() {
        vue.getModeleServi().setRowCount(0);
        commandesServies = commandeDAO.findServi();
        for (Commande c : commandesServies) {
            vue.getModeleServi().addRow(new Object[]{
                c.getId(),
                c.getClient().getUsername(),
                c.getDateCommande(),
                c.getStatut()
            });
        }
    }

    private void commencerCommande() {
        int ligne = vue.getLigneAttenteSelectionnee();
        if (ligne == -1) {
            vue.afficherErreur("Veuillez sélectionner une commande.");
            return;
        }
        Commande commande = commandesAttente.get(ligne);
        commandeDAO.changerStatut(commande.getId(), "EN_COURS");
        vue.afficherMessage("▶ Commande #" + commande.getId() + " en cours de préparation !");
        chargerCommandesAttente();
        chargerCommandesEnCours();
    }

    private void annulerCommande() {
        int ligne = vue.getLigneAttenteSelectionnee();
        if (ligne == -1) {
            vue.afficherErreur("Veuillez sélectionner une commande.");
            return;
        }
        Commande commande = commandesAttente.get(ligne);
        int confirm = JOptionPane.showConfirmDialog(vue,
            "Annuler la commande #" + commande.getId() + " ?",
            "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            commandeDAO.changerStatut(commande.getId(), "ANNULE");
            vue.afficherMessage("✖ Commande #" + commande.getId() + " annulée.");
            chargerCommandesAttente();
        }
    }

    private void marquerPret() {
        int ligne = vue.getLigneEnCoursSelectionnee();
        if (ligne == -1) {
            vue.afficherErreur("Veuillez sélectionner une commande.");
            return;
        }
        Commande commande = commandesEnCours.get(ligne);
        commandeDAO.changerStatut(commande.getId(), "PRET");
        vue.afficherMessage(
            "✅ Commande #" + commande.getId() + " prête !\n" +
            "La serveuse sera notifiée."
        );
        chargerCommandesEnCours();
    }

    private void deconnexion() {
        vue.dispose();
        LoginView loginVue = new LoginView();
        new LoginControleur(loginVue);
        loginVue.setVisible(true);
    }
}