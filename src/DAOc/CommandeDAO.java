package DAOc;
import modele.*;
import java.sql.*;
import java.util.List;

public class CommandeDAO extends BaseDAO<Commande> {

    @Override
    protected Commande mapRow(ResultSet rs) throws SQLException {
        Utilisateurs client = new Utilisateurs();
        client.setId(rs.getInt("idClient"));
        client.setUsername(rs.getString("client_username"));

        Utilisateurs serveuse = new Utilisateurs();
        serveuse.setId(rs.getInt("idServeuse"));
        serveuse.setUsername(rs.getString("serveuse_username"));

        Commande c = new Commande();
        c.setId(rs.getInt("id"));
        c.setClient(client);
        c.setServeuse(serveuse);
        c.setStatut(rs.getString("statut"));
        c.setDateCommande(rs.getTimestamp("dateCommande").toLocalDateTime());
        return c;
    }

    @Override
    public boolean create(Commande c) {
        // ← colonne dateCommande (pas date_commande)
        String sql = "INSERT INTO commandes(idClient, idServeuse, statut, dateCommande) " +
                     "VALUES(?, ?, ?, ?)";
        return executeUpdate(sql,
            c.getClient().getId(),
            c.getServeuse() != null ? c.getServeuse().getId() : null,
            c.getStatut(),
            Timestamp.valueOf(c.getDateCommande())
        );
    }

    @Override
    public boolean update(Commande c) {
        String sql = "UPDATE commandes SET statut=?, idServeuse=? WHERE id=?";
        return executeUpdate(sql,
            c.getStatut(),
            c.getServeuse() != null ? c.getServeuse().getId() : null,
            c.getId()
        );
    }

    @Override
    public boolean delete(int id) {
        return executeUpdate("DELETE FROM commandes WHERE id=?", id);
    }

    @Override
    public Commande findById(int id) {
        String sql = "SELECT c.*, " +
                     "cl.username as client_username, " +
                     "sv.username as serveuse_username " +
                     "FROM commandes c " +
                     "JOIN utilisateurs cl ON c.idClient = cl.id " +
                     "LEFT JOIN utilisateurs sv ON c.idServeuse = sv.id " +
                     "WHERE c.id=?";
        return executeQuerySingle(sql, id);
    }

    @Override
    public List<Commande> findAll() {
        String sql = "SELECT c.*, " +
                     "cl.username as client_username, " +
                     "sv.username as serveuse_username " +
                     "FROM commandes c " +
                     "JOIN utilisateurs cl ON c.idClient = cl.id " +
                     "LEFT JOIN utilisateurs sv ON c.idServeuse = sv.id";
        return executeQuery(sql);
    }

    public List<Commande> findEnAttente()  { return findByStatut("EN_ATTENTE"); }
    public List<Commande> findEnCours()    { return findByStatut("EN_COURS"); }
    public List<Commande> findPret()       { return findByStatut("PRET"); }
    public List<Commande> findServi()      { return findByStatut("SERVI"); }

    public List<Commande> findByStatut(String statut) {
        String sql = "SELECT c.*, " +
                     "cl.username as client_username, " +
                     "sv.username as serveuse_username " +
                     "FROM commandes c " +
                     "JOIN utilisateurs cl ON c.idClient = cl.id " +
                     "LEFT JOIN utilisateurs sv ON c.idServeuse = sv.id " +
                     "WHERE c.statut=?";
        return executeQuery(sql, statut);
    }

    public List<Commande> findByClient(int clientId) {
        String sql = "SELECT c.*, " +
                     "cl.username as client_username, " +
                     "sv.username as serveuse_username " +
                     "FROM commandes c " +
                     "JOIN utilisateurs cl ON c.idClient = cl.id " +
                     "LEFT JOIN utilisateurs sv ON c.idServeuse = sv.id " +
                     "WHERE c.idClient=?";
        return executeQuery(sql, clientId);
    }

    public boolean changerStatut(int commandeId, String nouveauStatut) {
        return executeUpdate(
            "UPDATE commandes SET statut=? WHERE id=?",
            nouveauStatut, commandeId
        );
    }
}