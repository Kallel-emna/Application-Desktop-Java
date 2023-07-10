/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Clinimate.gui;

import Clinimate.entities.Consultation;
import Clinimate.entities.Rendez_vous;
import Clinimate.entities.User;
import Clinimate.utils.DataSource;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import javax.swing.JOptionPane;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author medam
 */
public class Rendez_vousController implements Initializable {

    Connection cnx = DataSource.getInstance().getCnx();

    @FXML
    private TableColumn<Rendez_vous, String> coldate;
    @FXML
    private Button btnajoutRDV;
    @FXML
    private Button btnmodifRDV;
    @FXML
    private Button btnsupprRDV;
    @FXML
    private DatePicker datepicker;
    @FXML
    private Button btnretour;
    @FXML
    private TableView<Rendez_vous> tvrdv;
    @FXML
    private TableColumn<Rendez_vous, String> coltime;
    @FXML
    private ComboBox<User> patient;
    @FXML
    private TableColumn<User, String> coluser;
    @FXML
    private Button stat;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //  notifierReservationProche();
        choixuser();
        afficherrdv();
    }


public ObservableList<Rendez_vous> CrudRendez_vousListe() {
        ObservableList<Rendez_vous> liste = FXCollections.observableArrayList();
        String selectData = "SELECT Rendez_vous.id, Rendez_vous.date_rendezvous, User.email "
                + "FROM Rendez_vous "
                + "INNER JOIN User ON Rendez_vous.user_id = User.id";
        try {
            PreparedStatement pst = cnx.prepareStatement(selectData);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
             int id = rs.getInt("id");
           
              Date date  = rs.getDate("date_rendezvous") ; 
            
           
              
      String  nom = rs.getString("email");             
                
                Rendez_vous rdv = new Rendez_vous();
                rdv.setId(id);
                rdv.setDate_rendezvous(date);
              
                
                
                User us = new User();
                us.setEmail(nom);
                
             rdv.setU(us);
             
            
                liste.add(rdv);
             
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return liste;
    }

    @FXML
    private void retour(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
        Parent root = loader.load();
        btnretour.getScene().setRoot(root);
    }
    private ObservableList<Rendez_vous> CrudRendez_vous;

    public void afficherrdv() {
        CrudRendez_vous = CrudRendez_vousListe();
        coldate.setCellValueFactory(new PropertyValueFactory<>("date_rendezvous"));
        coltime.setCellValueFactory(new PropertyValueFactory<>("time"));
        coluser.setCellValueFactory(new PropertyValueFactory<>("u"));
        tvrdv.setItems(CrudRendez_vous);
    }

    @FXML
    private void ajouterrdv(ActionEvent event) throws IOException, SQLException {

        Date date = Date.valueOf(datepicker.getValue());

        if (event.getSource() == btnajoutRDV) {
            Rendez_vous U = new Rendez_vous();
            Rendez_vous Us = new Rendez_vous(date);
            String req = "INSERT INTO `Rendez_vous` ( `user_id` ,  `date_rendezvous`) VALUES (?,?)";
            try {
                System.out.println();
                PreparedStatement pst = cnx.prepareStatement(req);
                pst.setInt(1, patient.getValue().getId());
                pst.setDate(2, (Date) Us.getDate_rendezvous());

                pst.executeUpdate();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Rendez_vous.fxml"));
            Parent root = loader.load();
            btnajoutRDV.getScene().setRoot(root);
            notifier();
        }
    }

    @FXML
    private void suprimer(ActionEvent event) {
        try {
            Rendez_vous del = tvrdv.getSelectionModel().getSelectedItem();
            String deleteQuery = "DELETE FROM rendez_vous WHERE id = ?";
            PreparedStatement pst = cnx.prepareStatement(deleteQuery);
            pst.setInt(1, del.getId());
            pst.executeUpdate();
            afficherrdv();
            System.out.println("Successful DELETE");
            notifier1();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    PreparedStatement pst;

    public void choixuser() {
        try {
            pst = cnx.prepareStatement("SELECT id, email FROM user");
            ResultSet rs = pst.executeQuery();

            // Create a list of Service objects
            List<User> User = new ArrayList<>();

            // Loop through the results of the query and add each service to the list
            while (rs.next()) {
                int id = rs.getInt("id");
                String email = rs.getString("email");
                User user = new User(id, email);
                User.add(user);
            }

            // Set the items of the ComboBox to the list of Service objects
            patient.setItems(FXCollections.observableList(User));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void notifier() {
        Notifications notifications = Notifications.create();
        notifications.text("Merci d'avoir pris un rendez-vous!");
        notifications.title("Rendez-vous pris!");
        notifications.hideAfter(Duration.seconds(5));
        notifications.show();
    }

    public void notifier1() {
        Notifications notifications = Notifications.create();
        notifications.text("Vous avez supprimé le rendez-vous!");
        notifications.title("Rendez-vous supprimé !");
        notifications.hideAfter(Duration.seconds(5));
        notifications.show();
    }

    @FXML
    private void stat(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Statistique.fxml"));
        Parent root = loader.load();
        stat.getScene().setRoot(root);
    }
}
