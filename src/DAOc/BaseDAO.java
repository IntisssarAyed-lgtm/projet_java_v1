package DAOc;
import util.SingletonConnect;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public abstract class BaseDAO<T> implements IDao<T> {

    // Connexion partagée via  Singleton
    protected Connection getConnection() {
        return SingletonConnect.getInstance();
    }

    // Exécute INSERT / UPDATE / DELETE
    protected boolean executeUpdate(String sql, Object... params) {
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            setParams(ps, params);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Exécute SELECT et retourne une liste
    protected List<T> executeQuery(String sql, Object... params) {
        List<T> list = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            setParams(ps, params);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs)); // chaque DAO implémente mapRow
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Exécute SELECT et retourne un seul objet
    protected T executeQuerySingle(String sql, Object... params) {
        List<T> list = executeQuery(sql, params);
        return list.isEmpty() ? null : list.get(0);
    }

    // Place les paramètres dans le PreparedStatement automatiquement
    private void setParams(PreparedStatement ps, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }

    // Chaque DAO définit comment convertir un ResultSet en objet
    protected abstract T mapRow(ResultSet rs) throws SQLException;
}