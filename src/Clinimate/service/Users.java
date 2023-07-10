/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinimate.service;

import Clinimate.entities.BilanMedical;
import Clinimate.entities.User;
import Clinimate.gui.PasswordGenerator;
import Clinimate.utils.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author moham
 */
public class Users implements UserAbstract<User> {

    Connection cnx = DataSource.getInstance().getCnx();

    @Override
    public List<User> afficher() {
        List<User> list = new ArrayList();

        String req = "SELECT * FROM User";
        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new User(rs.getString("email"), rs.getString("password")));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return list;
    }
    private String email;
    private String password;
    private String role;

    public void setUserInfo(String username, String password, String role) {
        this.email = username;
        this.password = password;
        this.role = role;
    }

    public void SignUp(User U) throws SQLException {
        String req = "INSERT INTO `User` (`email`, `roles`,`password`,`firstname`,`lastname`,`address`,`telephone`, `animal`, `question`) VALUES (?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setString(1, U.getEmail());
            pst.setString(2, U.getRole());
            pst.setString(3, PasswordGenerator.generatePassword(U.getPassword()));
            pst.setString(4, U.getFirstname());
            pst.setString(5, U.getLastname());
            pst.setString(6, U.getAddress());
            pst.setInt(7, U.getTelephone());
            pst.setString(8, U.getAnimal());
            System.out.println(U.getQuestion());
            pst.setString(9, U.getQuestion());
            pst.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public boolean Login(String email, String password) throws SQLException, NoSuchAlgorithmException {
        String hashedPassword = hashPassword(password);
        try {
            String query = "SELECT * FROM User WHERE email=?";
            PreparedStatement statement = cnx.prepareStatement(query);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Compare the hashed password with the stored hashed password
                String storedHashedPassword = resultSet.getString("password");
                if (hashedPassword.equals(storedHashedPassword)) {
                    return true; // Authentication successful
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            return bytesToHex(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public ObservableList<User> searchUser(String searchTerm) {
        {

            ObservableList<User> liste = FXCollections.observableArrayList();
            String selectData = "SELECT * FROM User";
            try {
                PreparedStatement pst = cnx.prepareStatement(selectData);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    liste.add(new User(rs.getString("email"), rs.getString("firstname"),rs.getString("lastname"), rs.getString("roles"),rs.getString("address"), rs.getInt("telephone")));

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return liste;
        }
    }
}
