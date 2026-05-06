package dao;
import java.util.List;
import modele.*;

public interface CommandeDAO extends IDAO<Commande> {
    List<Commande> findByClient(int clientId);
    List<Commande> findByStatut(String statut);
}
