/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Clinimate.gui;

import Clinimate.utils.DataSource;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingWorker;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class SecretairepageController implements Initializable {

    @FXML
    private Button btnchambre;

    @FXML
    private Button btnreservation;

    @FXML
    private Button btnservice;

    PreparedStatement pst;

    Connection conn = DataSource.getInstance().getCnx();
    @FXML
    private TextField CnxEmail;
    @FXML
    private Button Profile;
    @FXML
    private Button Logoutt;

    @FXML
    void redirigerchambre(ActionEvent event) {
        try {
            // Charger la nouvelle vue à partir d'un fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("chambrepage.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec la nouvelle vue chargée
            Scene chambreScene = new Scene(root);

            // Obtenir la fenêtre principale et changer la scène
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.setScene(chambreScene);
            primaryStage.show();
        } catch (IOException e) {
        }
    }

    @FXML
    void redirigerservice(ActionEvent event) throws IOException {
        try {
            // Charger la nouvelle vue à partir d'un fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("servicepage.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec la nouvelle vue chargée
            Scene chambreScene = new Scene(root);

            // Obtenir la fenêtre principale et changer la scène
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.setScene(chambreScene);
            primaryStage.show();
        } catch (IOException e) {
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CnxEmail.setText(getData.Email);
    }

    public void notifierReservationProche() {
        try {
            LocalDate now = LocalDate.now();
            LocalDate tomorrow = now.plusDays(1);
            pst = conn.prepareStatement("SELECT * FROM reservation_chambre WHERE date_admission BETWEEN ? AND ?");
            pst.setObject(1, now);
            pst.setObject(2, tomorrow);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int patientId = rs.getInt("patient_id");
                pst = conn.prepareStatement("SELECT firstname, lastname FROM user WHERE id=?");
                pst.setInt(1, patientId);
                ResultSet patientRs = pst.executeQuery();
                if (patientRs.next()) {
                    String patientName = patientRs.getString("firstname") + " " + patientRs.getString("lastname");
                    String message = "Le patient " + patientName + " doit arriver demain.";

                    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() throws Exception {
                            JWindow notification = new JWindow();
                            notification.setSize(300, 100);
                            // Set the location to the bottom-right corner of the screen
                            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                            java.awt.Rectangle screenRect = ge.getDefaultScreenDevice().getDefaultConfiguration().getBounds();
                            int x = screenRect.width - (notification.getWidth());
                            int y = screenRect.height - (notification.getHeight() + 50);
                            notification.setLocation(x, y);
                            JPanel contentPane = new JPanel();
                            contentPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                            contentPane.setLayout(new BorderLayout());

                            JLabel messageLabel = new JLabel(message);
                            messageLabel.setHorizontalAlignment(JLabel.CENTER);
                            contentPane.add(messageLabel, BorderLayout.CENTER);

                            notification.setContentPane(contentPane);
                            notification.setVisible(true);

                            Thread.sleep(5000); // Wait for 5 seconds

                            notification.dispose(); // Hide the window

                            return null;
                        }
                    };

                    worker.execute(); // Start the worker thread
                }

            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    @FXML
    private void ProfileButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("profileS.fxml"));
        Parent root = loader.load();
        CnxEmail.getScene().setRoot(root);
    }

    @FXML
    private void redirectionreservation(ActionEvent event) {
        try {
            // Charger la nouvelle vue à partir d'un fichier FXML

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Reservationpage.fxml"));
            Parent root = loader.load();
            CnxEmail.getScene().setRoot(root);
            notifierReservationProche();

        } catch (IOException e) {
        }
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
