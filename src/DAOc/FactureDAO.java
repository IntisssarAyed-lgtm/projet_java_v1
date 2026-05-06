package DAOc;
import modele.Facture;
import modele.Commande;
import java.sql.*;
import java.util.List;

public class FactureDAO extends BaseDAO<Facture> {

    @Override
    protected Facture mapRow(ResultSet rs) throws SQLException {
        Commande commande = new Commande();
        commande.setId(rs.getInt("commande_id"));

        Facture f = new Facture();
        f.setId(rs.getInt("id"));
        f.setTotal(rs.getDouble("total"));
        f.setDateCommande(rs.getTimestamp("date_commande").toLocalDateTime());
        f.setCommande(commande);
        return f;
    }

    @Override
    public boolean create(Facture f) {
        String sql = "INSERT INTO facture(total, dateCommande, idCommande) VALUES(?, ?, ?)";
        return executeUpdate(sql,
            f.getTotal(),
            Timestamp.valueOf(f.getDateCommande()),
            f.getCommande().getId()
        );
    }

    @Override
    public boolean update(Facture f) {
        String sql = "UPDATE facture SET total=?, dateCommande=?, idCommande=? WHERE id=?";
        return executeUpdate(sql,
            f.getTotal(),
            Timestamp.valueOf(f.getDateCommande()),
            f.getCommande().getId(),
            f.getId()
        );
    }

    @Override
    public boolean delete(int id) {
        return executeUpdate("DELETE FROM facture WHERE id=?", id);
    }

    @Override
    public Facture findById(int id) {
        return executeQuerySingle("SELECT * FROM facture WHERE id=?", id);
    }

    @Override
    public List<Facture> findAll() {
        return executeQuery("SELECT * FROM facture");
    }

    // Facture liée à une commande
    public Facture findByCommande(int commandeId) {
        return executeQuerySingle(
            "SELECT * FROM facture WHERE idCommande=?", commandeId
        );
    }

    // Factures entre deux dates
    public List<Facture> findByPeriode(String dateDebut, String dateFin) {
        String sql = "SELECT * FROM facture WHERE dateCommande BETWEEN ? AND ?";
        return executeQuery(sql, dateDebut, dateFin);
    }
}