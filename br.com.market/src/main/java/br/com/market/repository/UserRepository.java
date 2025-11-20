package br.com.market.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.market.db.DB;
import br.com.market.db.DbException;
import br.com.market.enums.Position;
import java.sql.PreparedStatement;
import br.com.market.model.Admin;
import br.com.market.model.Shopper;
import br.com.market.model.User;

public class UserRepository {
    private Connection conn = null;
    private Statement st = null;
    private ResultSet rs = null;

    public List<User> getUsers() {
        try {
            List<User> users = new ArrayList<>();
            conn = DB.getConnection();
            st = conn.createStatement();
            rs = st.executeQuery("SELECT * FROM users;");

            while (rs.next()) {
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                Position position = Position.valueOf(rs.getString("position"));
                Double money = rs.getDouble("money");

                switch (position) {
                    case Position.ADMIN ->
                        users.add(new Admin(name, password, id));
                    case Position.SHOPPER ->
                        users.add(new Shopper(name, password, id, money));
                }
            }

            return users;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    public void updateUsuario(Shopper user){
        conn = DB.getConnection();
        String sql = "UPDATE users SET money = ? WHERE id = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setDouble(1, user.getMoney());
            pst.setLong(2, user.getId());

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    public boolean hasUser(String name) {
        for (User p : getUsers()) {
            if (p.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
