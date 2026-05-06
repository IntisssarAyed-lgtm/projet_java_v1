package dao;
import modele.*;

public interface UtilisateurDAO extends IDAO<Utilisateurs> {
    Utilisateurs findByUsername(String username);
}
