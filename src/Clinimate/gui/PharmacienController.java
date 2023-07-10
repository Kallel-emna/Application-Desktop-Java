/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Clinimate.gui;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author kalle
 */
public class PharmacienController implements Initializable {

    @FXML
    private Button Profile;
    @FXML
    private Button cat;
    @FXML
    private Button prod;
    @FXML
    private Button Logoutt;
    @FXML
    private TextField CnxEmail;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CnxEmail();
    }

    public void CnxEmail() {
        CnxEmail.setText(getData.Email);
    }

    @FXML
    private void Catégorie(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("testest.fxml"));
        Parent root = loader.load();
        cat.getScene().setRoot(root);
    }

    @FXML
    private void Produit(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Produiit.fxml"));
        Parent root = loader.load();
        prod.getScene().setRoot(root);
    }

    @FXML
    private void ProfileButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("pharmacienProfile.fxml"));
        Parent root = loader.load();
        cat.getScene().setRoot(root);
    }

    @FXML
    private void logoutB(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sur de vouloir vous déconnecter?");
        Optional<ButtonType> option = alert.showAndWait();

        // Check if the user clicked the OK button
        if (option.isPresent() && option.get() == ButtonType.OK) {
            // Redirect the user to the login page
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
