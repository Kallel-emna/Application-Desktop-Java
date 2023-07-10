/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Clinimate.gui;

import Clinimate.utils.DataSource;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.mindrot.jbcrypt.BCrypt;

/**
 * FXML Controller class
 *
 * @author moham
 */
public class NewPassController implements Initializable {

    @FXML
    private TextField password;
    @FXML
    private Button suivant;
    @FXML
    private Button retourL;
    Connection cnx = DataSource.getInstance().getCnx();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void newPassword(ActionEvent event) throws IOException {
        String UpdateData = "UPDATE User SET password = ? WHERE email = ?";
        try {
            if (password.getText().isEmpty()) {
            } else {
                PreparedStatement upd;

                upd = cnx.prepareStatement(UpdateData);

                upd.setString(2, getData.Email);

                upd.setString(1, hashPassword(password.getText()));
                upd.executeUpdate();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
                Parent root = loader.load();
                retourL.getScene().setRoot(root);
            }
        } catch (SQLException ex) {
            Logger.getLogger(NewPassController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void BackLogin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();
        retourL.getScene().setRoot(root);
    }

    private String hashPassword(String password) {
        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(password, salt);
        return hashedPassword;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
