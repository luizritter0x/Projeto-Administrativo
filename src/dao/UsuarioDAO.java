package dao;

import model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    ConexaoDAO conexao = new ConexaoDAO();

    public boolean inserirUsuario(Usuario usuario) {
        try (Connection conn = conexao.conectaBD()) {
            String sql = "INSERT INTO usuarios(nome, usuario, senha, is_admin) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getUsuario());
            stmt.setString(3, usuario.getSenha());
            stmt.setBoolean(4, usuario.isAdmin());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro inserirUsuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean autenticarUsuario(String usuario, String senha) {
        try (Connection conn = conexao.conectaBD()) {
            String sql = "SELECT * FROM usuarios WHERE usuario = ? AND senha = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Erro autenticarUsuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean verificarAdmin(String usuario, String senha) {
        try (Connection conn = conexao.conectaBD()) {
            String sql = "SELECT is_admin FROM usuarios WHERE usuario = ? AND senha = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("is_admin");
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Erro verificarAdmin: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Usuario> listarTodosUsuarios(String adminUsuarioLogado) {
        List<Usuario> lista = new ArrayList<>();
        try (Connection conn = conexao.conectaBD()) {
            String sql = "SELECT nome, usuario, is_admin FROM usuarios WHERE usuario != ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, adminUsuarioLogado);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Usuario user = new Usuario();
                user.setNome(rs.getString("nome"));
                user.setUsuario(rs.getString("usuario"));
                user.setAdmin(rs.getBoolean("is_admin"));
                lista.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Erro listarTodosUsuarios: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    public boolean verificarUsuarioExiste(String usuario) {
        try (Connection conn = conexao.conectaBD()) {
            String sql = "SELECT COUNT(*) FROM usuarios WHERE usuario = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Erro verificarUsuarioExiste: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean redefinirSenha(String usuario, String novaSenha) {
        try (Connection conn = conexao.conectaBD()) {
            String sql = "UPDATE usuarios SET senha = ? WHERE usuario = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, novaSenha);
            stmt.setString(2, usuario);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.err.println("Erro redefinirSenha: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean trocarSenha(String usuario, String senhaAtual, String novaSenha) {
        try (Connection conn = conexao.conectaBD()) {
            String sql = "SELECT senha FROM usuarios WHERE usuario = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String senhaBD = rs.getString("senha");
                if (!senhaBD.equals(senhaAtual)) {
                    return false;
                }
            } else {
                return false;
            }

            sql = "UPDATE usuarios SET senha = ? WHERE usuario = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, novaSenha);
            stmt.setString(2, usuario);
            int linhas = stmt.executeUpdate();
            return linhas > 0;
        } catch (SQLException e) {
            System.err.println("Erro trocarSenha: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean atualizarAdminStatus(String usuario, boolean isAdmin) {
        try (Connection conn = conexao.conectaBD()) {
            String sql = "UPDATE usuarios SET is_admin = ? WHERE usuario = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setBoolean(1, isAdmin);
            stmt.setString(2, usuario);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.err.println("Erro atualizarAdminStatus: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean removerUsuario(String usuario) {
        try (Connection conn = conexao.conectaBD()) {
            String sql = "DELETE FROM usuarios WHERE usuario = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.err.println("Erro removerUsuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
