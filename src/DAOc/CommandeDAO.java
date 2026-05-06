package DAOc;
import modele.*;
import java.sql.*;
import java.util.List;

public class CommandeDAO extends BaseDAO<Commande> {

    @Override
    protected Commande mapRow(ResultSet rs) throws SQLException {
        Utilisateurs client = new Utilisateurs();
        client.setId(rs.getInt("client_id"));
        client.setUsername(rs.getString("client_username"));

        Utilisateurs serveuse = new Utilisateurs();
        serveuse.setId(rs.getInt("serveuse_id"));
        serveuse.setUsername(rs.getString("serveuse_username"));

        Commande c = new Commande();
        c.setId(rs.getInt("id"));
        c.setClient(client);
        c.setServeuse(serveuse);
        c.setStatut(rs.getString("statut"));
        c.setDateCommande(rs.getTimestamp("date_commande").toLocalDateTime());
        return c;
    }

    @Override
    public boolean create(Commande c) {
        String sql = "INSERT INTO commandes(client_id, serveuse_id, statut, date_commande) " +
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
        String sql = "UPDATE commandes SET statut=?, serveuse_id=? WHERE id=?";
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
                     "sv.username as serveuse_username, " +
                     "sv.id as serveuse_id " +
                     "FROM commandes c " +
                     "JOIN utilisateurs cl ON c.client_id = cl.id " +
                     "LEFT JOIN utilisateurs sv ON c.serveuse_id = sv.id " +
                     "WHERE c.id=?";
        return executeQuerySingle(sql, id);
    }

    @Override
    public List<Commande> findAll() {
        String sql = "SELECT c.*, " +
                     "cl.username as client_username, " +
                     "sv.username as serveuse_username, " +
                     "sv.id as serveuse_id " +
                     "FROM commandes c " +
                     "JOIN utilisateurs cl ON c.client_id = cl.id " +
                     "LEFT JOIN utilisateurs sv ON c.serveuse_id = sv.id";
        return executeQuery(sql);
    }

    // ── Méthodes par statut ──────────────────────────────────

    // Commandes EN_ATTENTE → cuisinier les voit
    public List<Commande> findEnAttente() {
        return findByStatut("EN_ATTENTE");
    }

    // Commandes EN_COURS → cuisinier + serveuse les voient
    public List<Commande> findEnCours() {
        return findByStatut("EN_COURS");
    }

    // Commandes PRET → serveuse notifiée
    public List<Commande> findPret() {
        return findByStatut("PRET");
    }

    // Commandes SERVI → serveuse peut générer facture
    public List<Commande> findServi() {
        return findByStatut("SERVI");
    }

    // Méthode générique par statut
    public List<Commande> findByStatut(String statut) {
        String sql = "SELECT c.*, " +
                     "cl.username as client_username, " +
                     "sv.username as serveuse_username, " +
                     "sv.id as serveuse_id " +
                     "FROM commandes c " +
                     "JOIN utilisateurs cl ON c.client_id = cl.id " +
                     "LEFT JOIN utilisateurs sv ON c.serveuse_id = sv.id " +
                     "WHERE c.statut=?";
        return executeQuery(sql, statut);
    }

    // Commandes d'un client spécifique
    public List<Commande> findByClient(int clientId) {
        String sql = "SELECT c.*, " +
                     "cl.username as client_username, " +
                     "sv.username as serveuse_username, " +
                     "sv.id as serveuse_id " +
                     "FROM commandes c " +
                     "JOIN utilisateurs cl ON c.client_id = cl.id " +
                     "LEFT JOIN utilisateurs sv ON c.serveuse_id = sv.id " +
                     "WHERE c.client_id=?";
        return executeQuery(sql, clientId);
    }

    // Changer uniquement le statut d'une commande
    public boolean changerStatut(int commandeId, String nouveauStatut) {
        return executeUpdate(
            "UPDATE commandes SET statut=? WHERE id=?",
            nouveauStatut, commandeId
        );
    }
}