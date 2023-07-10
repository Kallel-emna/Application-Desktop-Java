package Clinimate.gui;

import Clinimate.utils.DataSource;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javax.swing.JOptionPane;
import org.mindrot.jbcrypt.BCrypt;

/**
 * FXML Controller class
 *
 * @author moham
 */
public class LoginController implements Initializable {

    Connection cnx = DataSource.getInstance().getCnx();
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private Button LoginB;
    @FXML
    private Button passss;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void LoginButton(ActionEvent event) throws IOException {
        String Email = email.getText();
        String Password = password.getText();
        String userRole = null;
        int banU;
        String req = "SELECT * FROM User where email='" + Email + "'and password='" + Password + "'";
        String hashedPassword = hashPassword(Password);
        if (Email.isEmpty() || Password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Veuillez entrer l'email et le mot de passe ");
        } else {
            try {
                String query = "SELECT * FROM User WHERE email=?";
                PreparedStatement statement = cnx.prepareStatement(query);
                statement.setString(1, Email);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    userRole = resultSet.getString("roles");
                    banU = resultSet.getInt("ban");
                    getData.Email = Email;
                    // Compare the hashed password with the stored hashed password
                    String storedHashedPassword = resultSet.getString("password");
                    if (BCrypt.checkpw(Password, storedHashedPassword)) {
                        if (banU == 1) {JOptionPane.showMessageDialog(null, "Votre compte est bloquer ");
                        } else {
                            if (userRole != null && userRole.equals("[\"ROLE_ADMIN\"]")) {
                                Preferences prefs = Preferences.userNodeForPackage(getClass());
                                prefs.put("email", Email);
                                prefs.put("password", storedHashedPassword);
                                prefs.put("roles", userRole);
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("Dash.fxml"));
                                Parent root = loader.load();
                                email.getScene().setRoot(root);
                            } else if (userRole != null && userRole.equals("[\"ROLE_PATIENT\"]")) {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("Dash.fxml"));
                                Parent root = loader.load();
                                email.getScene().setRoot(root);
                            } else if (userRole != null && userRole.equals("[\"ROLE_Doctor\"]")) {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
                                Parent root = loader.load();
                                email.getScene().setRoot(root);
                            } else if (userRole != null && userRole.equals("[\"ROLE_RECEPT\"]")) {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("Secretairepage.fxml"));
                                Parent root = loader.load();
                                email.getScene().setRoot(root);
                            } else if (userRole != null && userRole.equals("[\"ROLE_PHARMACIEN\"]")) {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("Pharmacien.fxml"));
                                Parent root = loader.load();
                                email.getScene().setRoot(root);
                            } else {
                                System.out.println("wait until activate your account ");
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Verifier votre Mot de passe ");
                    }
                }

            } catch (SQLException ex) {

                System.out.println(ex.getMessage());
            }
        }
    }

    /*private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            return bytesToHex(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    @FXML
    private void InscriptionP(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("InscriptionP.fxml"));
        Parent root = loader.load();
        email.getScene().setRoot(root);
    }

    @FXML
    private void forgotPass(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ForgotPassword.fxml"));
        Parent root = loader.load();
        email.getScene().setRoot(root);
    }

    private String hashPassword(String password) {
        String salt = BCrypt.gensalt(10);
        String hashedPassword = BCrypt.hashpw(password, salt);
        return hashedPassword;
    }

    @FXML
    private void LoginOk(KeyEvent event) throws IOException {
         if(event.getCode() == KeyCode.ENTER) {
        String Email = email.getText();
        String Password = password.getText();
        String userRole = null;
        int banU;
        String req = "SELECT * FROM User where email='" + Email + "'and password='" + Password + "'";
        String hashedPassword = hashPassword(Password);
        
        if (Email.isEmpty() || Password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Veuillez entrer l'email et le mot de passe ");
        } else {
            try {
                String query = "SELECT * FROM User WHERE email=?";
                PreparedStatement statement = cnx.prepareStatement(query);
                statement.setString(1, Email);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    userRole = resultSet.getString("roles");
                    banU = resultSet.getInt("ban");
                    getData.Email = Email;
                    // Compare the hashed password with the stored hashed password
                    String storedHashedPassword = resultSet.getString("password");
                    if (BCrypt.checkpw(Password, storedHashedPassword)) {
                        if (banU == 1) {JOptionPane.showMessageDialog(null, "Votre compte est bloquer ");
                        } else {
                            if (userRole != null && userRole.equals("[\"ROLE_ADMIN\"]")) {
                                Preferences prefs = Preferences.userNodeForPackage(getClass());
                                prefs.put("email", Email);
                                prefs.put("password", storedHashedPassword);
                                prefs.put("roles", userRole);
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("Dash.fxml"));
                                Parent root = loader.load();
                                email.getScene().setRoot(root);
                            } else if (userRole != null && userRole.equals("[\"ROLE_PATIENT\"]")) {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("Dash.fxml"));
                                Parent root = loader.load();
                                email.getScene().setRoot(root);
                            } else if (userRole != null && userRole.equals("[\"ROLE_Doctor\"]")) {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
                                Parent root = loader.load();
                                email.getScene().setRoot(root);
                            } else if (userRole != null && userRole.equals("[\"ROLE_RECEPT\"]")) {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("Secretairepage.fxml"));
                                Parent root = loader.load();
                                email.getScene().setRoot(root);
                            } else if (userRole != null && userRole.equals("[\"ROLE_PHARMACIEN\"]")) {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("Pharmacien.fxml"));
                                Parent root = loader.load();
                                email.getScene().setRoot(root);
                            } else {
                                System.out.println("wait until activate your account ");
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Verifier votre Mot de passe ");
                    }
                }

            } catch (SQLException ex) {

                System.out.println(ex.getMessage());
            }
        }
    }}
}
