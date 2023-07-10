/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Clinimate.gui;

import Clinimate.utils.DataSource;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author moham
 */
public class ForgotPasswordController implements Initializable {

    @FXML
    private TextField EmailF;
    @FXML
    private TextField reponse;
    @FXML
    private Button suivant;
    @FXML
    private Button retourL;
    Connection cnx = DataSource.getInstance().getCnx();
    @FXML
    private Button src;
    @FXML
    private TextField questiontxt;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void newPassword(ActionEvent event) throws IOException {
        String Email = EmailF.getText();
        String Reponse = reponse.getText();
        if (Email.isEmpty() || Reponse.isEmpty()) {
            JOptionPane.showMessageDialog(null, "entrer svp votre EMAIL ET NOM ");
        } else {
            try {
                String req = "SELECT animal FROM User WHERE email=?";

                PreparedStatement statement = cnx.prepareStatement(req);
                statement.setString(1, Email);
                getData.Email = Email;
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    if (Reponse.equals(resultSet.getString("animal"))) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("newPass.fxml"));
                        Parent root = loader.load();
                        EmailF.getScene().setRoot(root);
                    } else {
                        JOptionPane.showMessageDialog(null, "Verifier votre nom");
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(ForgotPasswordController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void BackLogin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();
        EmailF.getScene().setRoot(root);
    }

    @FXML
    private void src(ActionEvent event) {
        try {
            if (EmailF.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please insert username");
            } else {
                String sql = "select question from User where email=?";
                PreparedStatement ps = cnx.prepareStatement(sql);
                ps.setString(1, EmailF.getText());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    questiontxt.setText(rs.getString(1));
                }
            }}catch (SQLException ex) {
            Logger.getLogger(ForgotPasswordController.class.getName()).log(Level.SEVERE, null, ex);
        }
        

    }
}