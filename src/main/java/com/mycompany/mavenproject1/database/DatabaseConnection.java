package com.mycompany.mavenproject1.database;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Gerenciador Singleton de Conexão com o PostgreSQL via JDBC.
 * Projeto: Mestre da Cozinha
 * Autor: Henrique Jara Palma
 */
public class DatabaseConnection {

    private static Connection connection;
    private static String host = "localhost";
    private static String port = "5432";
    private static String dbName = "mestre_cozinha_db";
    private static String user = "postgres";
    private static String password = "postgres";
    private static boolean driverCarregado = false;

    static {
        carregarConfiguracoes();
        try {
            Class.forName("org.postgresql.Driver");
            driverCarregado = true;
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC do PostgreSQL não encontrado no classpath: " + e.getMessage());
        }
    }

    private static void carregarConfiguracoes() {
        try (InputStream input = DatabaseConnection.class.getResourceAsStream("/com/mycompany/mavenproject1/db.properties")) {
            if (input != null) {
                Properties prop = new Properties();
                prop.load(input);
                host = prop.getProperty("db.host", host);
                port = prop.getProperty("db.port", port);
                dbName = prop.getProperty("db.name", dbName);
                user = prop.getProperty("db.user", user);
                password = prop.getProperty("db.password", password);
            }
        } catch (Exception e) {
            System.out.println("Arquivo db.properties não carregado, usando padrões.");
        }
    }

    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String url = String.format("jdbc:postgresql://%s:%s/%s", host, port, dbName);
            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }

    public static boolean testarConexao() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao PostgreSQL: " + e.getMessage());
            return false;
        }
    }

    public static void fecharConexao() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão: " + e.getMessage());
        }
    }

    // Getters e Setters para ajuste em tempo de execução se necessário
    public static void setConfig(String novoHost, String novaPorta, String novoBanco, String novoUser, String novaSenha) {
        fecharConexao();
        host = novoHost;
        port = novaPorta;
        dbName = novoBanco;
        user = novoUser;
        password = novaSenha;
    }

    public static String getDbName() {
        return dbName;
    }

    public static String getUser() {
        return user;
    }
}
