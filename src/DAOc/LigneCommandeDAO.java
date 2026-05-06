package DAOc;
import modele.*;
import java.sql.*;
import java.util.List;

public class LigneCommandeDAO extends BaseDAO<LigneCommande> {

    @Override
    protected LigneCommande mapRow(ResultSet rs) throws SQLException {
        Commande commande = new Commande();
        commande.setId(rs.getInt("idCommande"));

        Menu menu = new Menu(rs.getInt("idMenu"), rs.getString("menu_nom"), "");
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
        String sql = "INSERT INTO lignescommande(idCommande, idPlat, quantite) VALUES(?, ?, ?)";
        return executeUpdate(sql,
            lc.getCommande().getId(),
            lc.getPlat().getId(),
            lc.getQuantite()
        );
    }

    @Override
    public boolean update(LigneCommande lc) {
        String sql = "UPDATE lignescommande SET quantite=? WHERE idCommande=? AND idPlat=?";
        return executeUpdate(sql,
            lc.getQuantite(),
            lc.getCommande().getId(),
            lc.getPlat().getId()
        );
    }

    @Override
    public boolean delete(int id) {
        return executeUpdate("DELETE FROM lignescommande WHERE idCommande=?", id);
    }

    @Override
    public LigneCommande findById(int id) {
        String sql = "SELECT lc.*, p.nom as plat_nom, p.description as plat_description, " +
                     "p.prix as plat_prix, p.id as plat_id, " +
                     "m.id as idMenu, m.nom as menu_nom " +
                     "FROM lignescommande lc " +
                     "JOIN plats p ON lc.idPlat = p.id " +
                     "JOIN menus m ON p.idMenu = m.id " +
                     "WHERE lc.idCommande=? LIMIT 1";
        return executeQuerySingle(sql, id);
    }

    @Override
    public List<LigneCommande> findAll() {
        String sql = "SELECT lc.*, p.nom as plat_nom, p.description as plat_description, " +
                     "p.prix as plat_prix, p.id as plat_id, " +
                     "m.id as idMenu, m.nom as menu_nom " +
                     "FROM lignescommande lc " +
                     "JOIN plats p ON lc.idPlat = p.id " +
                     "JOIN menus m ON p.idMenu = m.id"; // ← menus (pas menu)
        return executeQuery(sql);
    }

    public List<LigneCommande> findByCommande(int commandeId) {
        String sql = "SELECT lc.*, p.nom as plat_nom, p.description as plat_description, " +
                     "p.prix as plat_prix, p.id as plat_id, " +
                     "m.id as idMenu, m.nom as menu_nom " +
                     "FROM lignescommande lc " +  // ← lignescommande
                     "JOIN plats p ON lc.idPlat = p.id " + // ← plats, idPlat
                     "JOIN menus m ON p.idMenu = m.id " +  // ← menus, idMenu
                     "WHERE lc.idCommande=?";              // ← idCommande
        return executeQuery(sql, commandeId);
    }

    public double calculerTotal(int commandeId) {
        String sql = "SELECT SUM(p.prix * lc.quantite) as total " +
                     "FROM lignescommande lc " +   // ← lignescommande
                     "JOIN plats p ON lc.idPlat = p.id " + // ← plats, idPlat
                     "WHERE lc.idCommande=?";              // ← idCommande
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