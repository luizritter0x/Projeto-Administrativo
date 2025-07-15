package dao;

import model.KeyRegistro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KeyDAO {
    ConexaoDAO conexao = new ConexaoDAO();      

    // insere uma nova key na tabela keys_registro
    public boolean inserirKey(String chave) {
        try (Connection conn = conexao.conectaBD()) {
            String sql = "INSERT INTO keys_registro(chave, usada) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, chave);
            stmt.setBoolean(2, false); 
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro inserirKey: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // verifica se a chave existe e ainda não foi usada (mencionado a baixo)
    public boolean validarChave(String chave) {
        try (Connection conn = conexao.conectaBD()) {
            String sql = "SELECT usada FROM keys_registro WHERE chave = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, chave);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return !rs.getBoolean("usada"); // retorna true se a chave existe e NÃO foi usada
            }
            return false; 
        } catch (SQLException e) {
            System.err.println("Erro validarChave: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // marca uma chave como usada (so se ela existe)
    public boolean usarChave(String chave) {
        try (Connection conn = conexao.conectaBD()) {
            // verifica se a chave existe e não está usada
            if (!validarChave(chave)) {
                return false; 
            }

            String sql = "UPDATE keys_registro SET usada = ? WHERE chave = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setBoolean(1, true);
            stmt.setString(2, chave);
            int linhasAfetadas = stmt.executeUpdate();  // executa o sql e retorna quantas linhas foram afetadas
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.err.println("Erro usarChave: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // lista todas as chaves cadastradas no banco (usada ou nao)
    public List<KeyRegistro> listarKeys() {
        List<KeyRegistro> lista = new ArrayList<>();
        try (Connection conn = conexao.conectaBD()) {
            String sql = "SELECT chave, usada FROM keys_registro";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                KeyRegistro key = new KeyRegistro();
                key.setChave(rs.getString("chave"));
                key.setUsada(rs.getBoolean("usada"));
                lista.add(key);
            }
        } catch (SQLException e) {
            System.err.println("Erro listarKeys: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    // remove do banco uma chave específica
    public boolean removerKey(String chave) {
        try (Connection conn = conexao.conectaBD()) {
            String sql = "DELETE FROM keys_registro WHERE chave = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, chave);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.err.println("Erro removerKey: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
