package controller;

import dao.ConexaoDAO;
import model.Chamado;
import model.MensagemChamado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChamadoController {

    public boolean criarChamado(String assunto, String usuario, String mensagemInicial) {
        Connection conn = null;
        PreparedStatement stmtChamado = null;
        PreparedStatement stmtMensagem = null;
        ResultSet generatedKeys = null;

        try {
            conn = ConexaoDAO.getConnection();

            // Cria o chamado
            String sqlChamado = "INSERT INTO chamados (titulo, descricao, status, usuario) VALUES (?, ?, ?, ?)";
            stmtChamado = conn.prepareStatement(sqlChamado, Statement.RETURN_GENERATED_KEYS);
            stmtChamado.setString(1, assunto);
            stmtChamado.setString(2, mensagemInicial);
            stmtChamado.setString(3, "Aberto");
            stmtChamado.setString(4, usuario);
            stmtChamado.executeUpdate();

            generatedKeys = stmtChamado.getGeneratedKeys();
            if (generatedKeys.next()) {
                int chamadoId = generatedKeys.getInt(1);

                // Cria mensagem inicial vinculada ao chamado
                String sqlMensagem = "INSERT INTO mensagens_chamado (chamado_id, autor, texto, data_hora) VALUES (?, ?, ?, ?)";
                stmtMensagem = conn.prepareStatement(sqlMensagem);
                stmtMensagem.setInt(1, chamadoId);
                stmtMensagem.setString(2, usuario);
                stmtMensagem.setString(3, mensagemInicial);
                stmtMensagem.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                stmtMensagem.executeUpdate();

                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (generatedKeys != null) generatedKeys.close(); } catch (Exception ignored) {}
            try { if (stmtChamado != null) stmtChamado.close(); } catch (Exception ignored) {}
            try { if (stmtMensagem != null) stmtMensagem.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
    }

    public List<Chamado> listarChamadosPorUsuario(String usuario) {
        List<Chamado> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexaoDAO.getConnection();
            String sql = "SELECT * FROM chamados WHERE usuario = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Chamado c = new Chamado();
                c.setId(rs.getInt("id"));
                c.setTitulo(rs.getString("titulo"));
                c.setDescricao(rs.getString("descricao"));
                c.setStatus(rs.getString("status"));
                lista.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
        return lista;
    }

    public Chamado buscarChamadoPorId(int id) {
        Chamado chamado = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexaoDAO.getConnection();
            String sql = "SELECT * FROM chamados WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                chamado = new Chamado();
                chamado.setId(rs.getInt("id"));
                chamado.setTitulo(rs.getString("titulo"));
                chamado.setDescricao(rs.getString("descricao"));
                chamado.setStatus(rs.getString("status"));

                chamado.setMensagens(buscarMensagensPorChamado(id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
        return chamado;
    }

    public List<MensagemChamado> buscarMensagensPorChamado(int chamadoId) {
        List<MensagemChamado> mensagens = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexaoDAO.getConnection();
            String sql = "SELECT * FROM mensagens_chamado WHERE chamado_id = ? ORDER BY data_hora ASC";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, chamadoId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                MensagemChamado m = new MensagemChamado();
                m.setId(rs.getInt("id"));
                m.setAutor(rs.getString("autor"));
                m.setTexto(rs.getString("texto"));
                m.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
                mensagens.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
        return mensagens;
    }

    public void salvarChamado(Chamado chamado) {
        String sqlUpdateStatus = "UPDATE chamados SET status = ? WHERE id = ?";
        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlUpdateStatus)) {

            stmt.setString(1, chamado.getStatus());
            stmt.setInt(2, chamado.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Salvar mensagens novas
        for (MensagemChamado m : chamado.getMensagens()) {
            if (m.getId() == 0) {
                String sql = "INSERT INTO mensagens_chamado (chamado_id, autor, texto, data_hora) VALUES (?, ?, ?, ?)";
                try (Connection conn = ConexaoDAO.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {

                    stmt.setInt(1, chamado.getId());
                    stmt.setString(2, m.getAutor());
                    stmt.setString(3, m.getTexto());
                    stmt.setTimestamp(4, Timestamp.valueOf(m.getDataHora()));
                    stmt.executeUpdate();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Chamado> listarChamados() {
        List<Chamado> lista = new ArrayList<>();
        String sql = "SELECT id, titulo, descricao, status, usuario FROM chamados";

        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Chamado chamado = new Chamado();
                chamado.setId(rs.getInt("id"));
                chamado.setTitulo(rs.getString("titulo"));
                chamado.setDescricao(rs.getString("descricao"));
                chamado.setStatus(rs.getString("status"));
                chamado.setUsuario(rs.getString("usuario"));
                lista.add(chamado);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean atualizarStatusChamado(int idChamado, String novoStatus) {
        String sql = "UPDATE chamados SET status = ? WHERE id = ?";

        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novoStatus);
            stmt.setInt(2, idChamado);

            int linhas = stmt.executeUpdate();
            return linhas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean removerChamado(int idChamado) {
        // Primeiro remove as mensagens
        String sqlDelMensagens = "DELETE FROM mensagens_chamado WHERE chamado_id = ?";
        String sqlDelChamado = "DELETE FROM chamados WHERE id = ?";

        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmtDelMsg = conn.prepareStatement(sqlDelMensagens);
             PreparedStatement stmtDelChamado = conn.prepareStatement(sqlDelChamado)) {

            conn.setAutoCommit(false);

            stmtDelMsg.setInt(1, idChamado);
            stmtDelMsg.executeUpdate();

            stmtDelChamado.setInt(1, idChamado);
            int linhas = stmtDelChamado.executeUpdate();

            conn.commit();
            return linhas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            try (Connection conn = ConexaoDAO.getConnection()) {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }
}
