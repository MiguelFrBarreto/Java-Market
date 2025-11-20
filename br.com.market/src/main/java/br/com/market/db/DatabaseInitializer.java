package br.com.market.db;

import java.sql.Statement;

public class DatabaseInitializer {

    public static void initializeDatabase() {
        DB.closeConnection();

        try (Statement st = DB.getConnection().createStatement()) {
            String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255) NOT NULL UNIQUE,
                    password VARCHAR(255) NOT NULL,
                    position ENUM('ADMIN', 'SHOPPER') NOT NULL,
                    money DOUBLE DEFAULT 10000.0
                )
            """;

            // Criação da tabela de produtos
            String createProductsTable = """
                CREATE TABLE IF NOT EXISTS products (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255) NOT NULL UNIQUE,
                    price DOUBLE NOT NULL,
                    quantity INT NOT NULL
                )
            """;

            st.executeUpdate(createUsersTable);
            st.executeUpdate(createProductsTable);

            String insertAdmin = """
                INSERT IGNORE INTO users (name, password, position) 
                VALUES ('admin', 'admin123', 'ADMIN')
            """;
            st.executeUpdate(insertAdmin);

            insertSampleProducts(st);
        } catch (Exception e) {
            System.out.println("Error: database initialization failed - " + e.getMessage());
        }
    }

    private static void insertSampleProducts(Statement st) throws Exception {
        String insertProducts = """
            INSERT IGNORE INTO products (name, price, quantity) VALUES
            ('Notebook', 2899.99, 10),
            ('iPhone 14', 4599.99, 15),
            ('Samsung Galaxy', 3299.99, 20),
            ('Tablet', 1899.99, 8),
            ('Smart TV', 2799.99, 5),
            ('Fone', 1299.99, 25),
            ('Mouse', 399.99, 30),
            ('Teclado', 299.99, 12),
            ('Monitor', 899.99, 7),
            ('Impressora', 499.99, 9)
        """;
        
        st.executeUpdate(insertProducts);
    }
}

