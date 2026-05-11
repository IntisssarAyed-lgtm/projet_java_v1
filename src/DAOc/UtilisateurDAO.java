package DAOc;
import modele.Utilisateurs;
import java.sql.*;
import java.util.List;

public class UtilisateurDAO extends BaseDAO<Utilisateurs> {

    // Convertir ResultSet to Utilisateurs
    @Override
    protected Utilisateurs mapRow(ResultSet rs) throws SQLException {
        return new Utilisateurs(
            rs.getInt("id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("role")
        );
    }

    @Override
    public boolean create(Utilisateurs u) {
        String sql = "INSERT INTO utilisateurs(username, password, role) VALUES(?, ?, ?)";
        return executeUpdate(sql, u.getUsername(), u.getPassword(), u.getRole());
    }

    @Override
    public boolean update(Utilisateurs u) {
        String sql = "UPDATE utilisateurs SET username=?, password=?, role=? WHERE id=?";
        return executeUpdate(sql, u.getUsername(), u.getPassword(), u.getRole(), u.getId());
    }

    @Override
    public boolean delete(int id) {
        return executeUpdate("DELETE FROM utilisateurs WHERE id=?", id);
    }

    @Override
    public Utilisateurs findById(int id) {
        return executeQuerySingle("SELECT * FROM utilisateurs WHERE id=?", id);
    }

    @Override
    public List<Utilisateurs> findAll() {
        return executeQuery("SELECT * FROM utilisateurs");
    }

    // Méthode spécifique à Utilisateurs
    public Utilisateurs findByUsername(String username) {
        return executeQuerySingle(
            "SELECT * FROM utilisateurs WHERE username=?", username
        );
    }

    // Pour la connexion login
    public Utilisateurs login(String username, String password) {
        return executeQuerySingle(
            "SELECT * FROM utilisateurs WHERE username=? AND password=?",
            username, password
        );
    }
}