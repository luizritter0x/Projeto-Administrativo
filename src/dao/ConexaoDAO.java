package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/dbluiz";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public Connection conectaBD() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Erro ao conectar no banco de dados: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // SE QUISER manter compatibilidade
    public static Connection getConnection() {
        return new ConexaoDAO().conectaBD();
    }
}
