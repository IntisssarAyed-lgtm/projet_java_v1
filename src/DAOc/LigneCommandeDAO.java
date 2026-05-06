package DAOc;

import modele.*;
import java.sql.*;
import java.util.List;

public class LigneCommandeDAO extends BaseDAO<LigneCommande> {

    @Override
    protected LigneCommande mapRow(ResultSet rs) throws SQLException {
        Commande commande = new Commande();
        commande.setId(rs.getInt("commande_id"));

        Menu menu = new Menu(rs.getInt("menu_id"), rs.getString("menu_nom"), "");
        Plat plat = new Plat(
            rs.getInt("plat_id"),
            rs.getString("plat_nom"),
            rs.getString("plat_description"),
            rs.getDouble("plat_prix"),
            menu
        );

        return new LigneCommande(commande, plat, rs.getInt("quantite"));
    }

    @Override
    public boolean create(LigneCommande lc) {
        String sql = "INSERT INTO ligne_commande(commande_id, plat_id, quantite) VALUES(?, ?, ?)";
        return executeUpdate(sql,
            lc.getCommande().getId(),
            lc.getPlat().getId(),
            lc.getQuantite()
        );
    }

    @Override
    public boolean update(LigneCommande lc) {
        String sql = "UPDATE ligne_commande SET quantite=? WHERE commande_id=? AND plat_id=?";
        return executeUpdate(sql,
            lc.getQuantite(),
            lc.getCommande().getId(),
            lc.getPlat().getId()
        );
    }

    @Override
    public boolean delete(int id) {
        // id = commande_id ici
        return executeUpdate("DELETE FROM ligne_commande WHERE commande_id=?", id);
    }

    @Override
    public LigneCommande findById(int id) {
        String sql = "SELECT lc.*, p.nom as plat_nom, p.description as plat_description, " +
                     "p.prix as plat_prix, p.id as plat_id, " +
                     "m.id as menu_id, m.nom as menu_nom " +
                     "FROM ligne_commande lc " +
                     "JOIN plat p ON lc.plat_id = p.id " +
                     "JOIN menu m ON p.menu_id = m.id " +
                     "WHERE lc.commande_id=? LIMIT 1";
        return executeQuerySingle(sql, id);
    }

    @Override
    public List<LigneCommande> findAll() {
        String sql = "SELECT lc.*, p.nom as plat_nom, p.description as plat_description, " +
                     "p.prix as plat_prix, p.id as plat_id, " +
                     "m.id as menu_id, m.nom as menu_nom " +
                     "FROM ligne_commande lc " +
                     "JOIN plat p ON lc.plat_id = p.id " +
                     "JOIN menu m ON p.menu_id = m.id";
        return executeQuery(sql);
    }

    // Toutes les lignes d'une commande (la plus utile)
    public List<LigneCommande> findByCommande(int commandeId) {
        String sql = "SELECT lc.*, p.nom as plat_nom, p.description as plat_description, " +
                     "p.prix as plat_prix, p.id as plat_id, " +
                     "m.id as menu_id, m.nom as menu_nom " +
                     "FROM ligne_commande lc " +
                     "JOIN plat p ON lc.plat_id = p.id " +
                     "JOIN menu m ON p.menu_id = m.id " +
                     "WHERE lc.commande_id=?";
        return executeQuery(sql, commandeId);
    }

    // Calcule le total d'une commande
    public double calculerTotal(int commandeId) {
        String sql = "SELECT SUM(p.prix * lc.quantite) as total " +
                     "FROM ligne_commande lc " +
                     "JOIN plat p ON lc.plat_id = p.id " +
                     "WHERE lc.commande_id=?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, commandeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("total");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}