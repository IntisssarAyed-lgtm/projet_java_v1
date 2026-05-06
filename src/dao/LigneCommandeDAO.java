package dao;
import java.util.*;
import modele.*;

public interface LigneCommandeDAO {
	void addLigne(LigneCommande ligne);
    void updateLigne(LigneCommande ligne);
    void deleteLigne(int commandeId, int platId);
    List<LigneCommande> findByCommande(int commandeId);
}
