package dao;

import model.Aviso;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AvisoDAO {

    public List<Aviso> listarTodos() {
        List<Aviso> avisos = new ArrayList<>();
        String sql = "SELECT * FROM avisos ORDER BY data_publicacao DESC";

        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Aviso aviso = new Aviso();
                aviso.setId(rs.getInt("id"));
                aviso.setTitulo(rs.getString("titulo"));
                aviso.setMensagem(rs.getString("mensagem"));
                aviso.setDataPublicacao(rs.getTimestamp("data_publicacao").toLocalDateTime());
                aviso.setCriadoPor(rs.getString("criado_por"));
                avisos.add(aviso);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return avisos;
    }

    public void salvar(Aviso aviso) {
        String sql = "INSERT INTO avisos (titulo, mensagem, criado_por, data_publicacao) VALUES (?, ?, ?, NOW())";
        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, aviso.getTitulo());
            stmt.setString(2, aviso.getMensagem());
            stmt.setString(3, aviso.getCriadoPor());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM avisos WHERE id = ?";
        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
