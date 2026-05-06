package DAOc;
import modele.Menu;
import modele.Plat;
import java.sql.*;
import java.util.List;

public class MenuDAO extends BaseDAO<Menu> {

    @Override
    protected Menu mapRow(ResultSet rs) throws SQLException {
        return new Menu(
            rs.getInt("id"),
            rs.getString("nom"),
            rs.getString("description")
        );
    }

    @Override
    public boolean create(Menu m) {
        String sql = "INSERT INTO menus(nom, description) VALUES(?, ?)";
        return executeUpdate(sql, m.getNom(), m.getDescription());
    }

    @Override
    public boolean update(Menu m) {
        String sql = "UPDATE menus SET nom=?, description=? WHERE id=?";
        return executeUpdate(sql, m.getNom(), m.getDescription(), m.getId());
    }

    @Override
    public boolean delete(int id) {
        return executeUpdate("DELETE FROM menus WHERE id=?", id);
    }

    @Override
    public Menu findById(int id) {
        return executeQuerySingle("SELECT * FROM menus WHERE id=?", id);
    }

    @Override
    public List<Menu> findAll() {
        return executeQuery("SELECT * FROM menus");
    }

    // Récupère un menu avec tous ses plats
    public Menu findWithPlats(int menuId) {
        Menu menu = findById(menuId);
        if (menu != null) {
            PlatDAO platDAO = new PlatDAO();
            List<Plat> plats = platDAO.findByMenu(menuId);
            menu.setPlats(plats); // ajoute setPlats() dans ta classe Menu
        }
        return menu;
    }
}