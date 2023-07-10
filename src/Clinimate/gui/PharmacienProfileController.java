/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Clinimate.gui;

import Clinimate.utils.DataSource;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

/**
 * FXML Controller class
 *
 * @author kalle
 */
public class PharmacienProfileController implements Initializable {

    Connection cnx = DataSource.getInstance().getCnx();
    @FXML
    private TextField emailPr;
    @FXML
    private TextField firstnamePr;
    @FXML
    private TextField LastnamePr;
    @FXML
    private TextField AddressPr;
    @FXML
    private TextField TelephonePr;
    @FXML
    private Button validerPr;
    @FXML
    private Button Dashboard;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            GetProfile();
        } catch (SQLException ex) {
            Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void GetProfile() throws SQLException {
        String selectData = "SELECT * FROM User WHERE email=?";

        try {
            PreparedStatement pst = cnx.prepareStatement(selectData);
            pst.setString(1, getData.Email);
            System.out.println(selectData);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String id = rs.getString("id");
                getData.Id = id;
                String email = rs.getString("email");
                String firstname = rs.getString("firstname");
                String lastname = rs.getString("lastname");
                String address = rs.getString("address");
                int telephone = rs.getInt("telephone");

                // Populate text fields with user data
                emailPr.setText(email);
                firstnamePr.setText(firstname);
                LastnamePr.setText(lastname);
                AddressPr.setText(address);
                TelephonePr.setText(String.valueOf(telephone));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void BackDashboard(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Pharmacien.fxml"));
        Parent root = loader.load();
        emailPr.getScene().setRoot(root);
    }

    @FXML
    private void UpdatePr(ActionEvent event
    ) {
        try {
            String UpdateData = "UPDATE User SET email = ?, firstname = ?, lastname = ?, address = ?, telephone = ? WHERE id = ?";
            PreparedStatement pst = cnx.prepareStatement(UpdateData);
            pst.setString(1, emailPr.getText());
            pst.setString(2, firstnamePr.getText());
            pst.setString(3, LastnamePr.getText());
            pst.setString(4, AddressPr.getText());
            pst.setString(5, TelephonePr.getText());
            pst.setString(6, getData.Id);
            pst.executeUpdate();

            System.out.println("Successful update");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    

}
