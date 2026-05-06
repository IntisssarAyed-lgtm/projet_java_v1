package DAOc;
import modele.Plat;
import modele.Menu;
import java.sql.*;
import java.util.List;

public class PlatDAO extends BaseDAO<Plat> {

    @Override
    protected Plat mapRow(ResultSet rs) throws SQLException {
        Menu menu = new Menu(
            rs.getInt("idMenu"),
            rs.getString("menu_nom"),
            rs.getString("menu_description")
        );
        return new Plat(
            rs.getInt("id"),
            rs.getString("nom"),
            rs.getString("description"),
            rs.getDouble("prix"),
            menu
        );
    }

    @Override
    public boolean create(Plat p) {
        String sql = "INSERT INTO plats(nom, description, prix, idMenu) VALUES(?, ?, ?, ?)";
        return executeUpdate(sql, p.getNom(), p.getDescription(), 
                             p.getPrix(), p.getMenu().getId());
    }

    @Override
    public boolean update(Plat p) {
        String sql = "UPDATE plats SET nom=?, description=?, prix=?,idMenu=? WHERE id=?";
        return executeUpdate(sql, p.getNom(), p.getDescription(), 
                             p.getPrix(), p.getMenu().getId(), p.getId());
    }

    @Override
    public boolean delete(int id) {
        return executeUpdate("DELETE FROM plats WHERE id=?", id);
    }

    @Override
    public Plat findById(int id) {
        String sql = "SELECT p.*, m.nom as menu_nom, m.description as menu_description " +
                     "FROM plats p JOIN menus m ON p.idMenu = m.id WHERE p.id=?";
        return executeQuerySingle(sql, id);
    }

    @Override
    public List<Plat> findAll() {
        String sql = "SELECT p.*, m.nom as menu_nom, m.description as menu_description " +
                     "FROM plats p JOIN menus m ON p.idMenu = m.id";
        return executeQuery(sql);
    }

    // Plats par menu
    public List<Plat> findByMenu(int menuId) {
        String sql = "SELECT p.*, m.nom as menu_nom, m.description as menu_description " +
                     "FROM plats p JOIN menus m ON p.idMenu = m.id WHERE p.idMenu=?";
        return executeQuery(sql, menuId);
    }
}

