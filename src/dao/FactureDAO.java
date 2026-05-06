package dao;

import modele.*;

public interface FactureDAO extends IDAO<Facture> {
    Facture findByCommande(int commandeId);
}