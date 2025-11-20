package br.com.market.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.market.db.DB;
import br.com.market.db.DbException;
import java.sql.PreparedStatement;
import br.com.market.model.Product;

public class ProductRepository {

    public boolean hasProduct(String name) {
        for (Product p : getProducts()) {
            if (p.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public List<Product> getProducts() {
        try {
            List<Product> products = new ArrayList<>();
            Connection conn = DB.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM products;");

            while (rs.next()) {
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                Double price = rs.getDouble("price");
                Integer quantity = rs.getInt("quantity");

                products.add(new Product(id, name, price, quantity));
            }

            return products;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    public Product getProductByName(String name) {
        try {
            Connection conn = DB.getConnection();
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM products WHERE name = ?");
            pst.setString(1, name);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String productName = rs.getString("name");
                Double price = rs.getDouble("price");
                Integer quantity = rs.getInt("quantity");
                long id = rs.getLong("id");
                return new Product(id, productName, price, quantity);
            }
            throw new NullPointerException();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    public int removeQuantity(String name, int quantity) {
        try {
            int userQuantity = quantity;
            int DBquantity = 0;
            Connection conn = DB.getConnection();
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM products WHERE name = ?");
            pst.setString(1, name);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                DBquantity = rs.getInt("quantity");
            } else {
                throw new NullPointerException();
            }

            //Se ambos os valores forem iguais ou menores
            if (DBquantity <= quantity) {
                pst = conn.prepareStatement("UPDATE products SET quantity = 0 WHERE name = ?");

                pst.setString(1, name);

                pst.executeUpdate();

                userQuantity = DBquantity;
            }
            //Se o valor do banco de dados for maior
            if (DBquantity > quantity) {
                int newQuantity = DBquantity - quantity;

                pst = conn.prepareStatement("UPDATE products SET quantity = ? WHERE name = ?");

                pst.setInt(1, newQuantity);
                pst.setString(2, name);

                pst.executeUpdate();
            }
            return userQuantity;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    public void addQuantity(String name, int quantity) {
        try {
            if (name == null) {
                throw new NullPointerException();
            }

            Connection conn = DB.getConnection();
            PreparedStatement pst;

            pst = conn.prepareStatement("SELECT * FROM products WHERE name = ?");
            pst.setString(1, name);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int DBquantity = rs.getInt("quantity");

                int newQuantity = quantity + DBquantity;

                pst = conn.prepareStatement("UPDATE products SET quantity = ? WHERE name = ?");

                pst.setInt(1, newQuantity);
                pst.setString(2, name);

                pst.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    public void addProduct(String name, Double price, Integer quantity) {
        Connection conn = DB.getConnection();
        String sql = "INSERT INTO products (name, price, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, name);
            pst.setDouble(2, price);
            pst.setInt(3, quantity);

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    public void removeProduct(String name) {
        Connection conn = DB.getConnection();
        String sql = "DELETE FROM products WHERE name = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, name);

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }
}
