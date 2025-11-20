package br.com.market.repository;

import br.com.market.db.DB;
import br.com.market.db.DbException;
import br.com.market.enums.Position;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LoginRepository {
    public void createAccount(String name, String password) {
        if(name == null && password == null){
            throw new NullPointerException();
        }
        
        Connection conn = DB.getConnection();
        String sql = "INSERT INTO users (name, password, position, money) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, name);
            pst.setString(2, password);
            pst.setString(3, String.valueOf(Position.SHOPPER));
            pst.setDouble(4, 10000);

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }
}
