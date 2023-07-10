/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Clinimate.gui;

import Clinimate.entities.Consultation;
import Clinimate.entities.Rendez_vous;
import Clinimate.utils.DataSource;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author medam
 */
public class ConsultationController implements Initializable {
 Connection cnx = DataSource.getInstance().getCnx();

    @FXML
    private TableView<Consultation> tvConsultation;
    @FXML
    private TableColumn<Consultation, String> id;
    @FXML
    private TableColumn<Consultation, String> colnotes;
    @FXML
    private TableColumn<Consultation, String> colprix;
    @FXML
    private TableColumn<Rendez_vous, String> colidrdv;
    @FXML
    private Button btnajoutConsultation;
    @FXML
    private Button btnmodifConsultation;
    @FXML
    private Button btnsupprConsultation;
    @FXML
    private TextField tfid;
    @FXML
    private TextField tfnotes;
    @FXML
    private TextField tfprix;
  PreparedStatement pst;
    @FXML
    private ComboBox<Rendez_vous> cbrdv;
    @FXML
    private Button btnretour;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        selectLine();
        choixrendezvous();
        table();
    // afficherconsultation();
    }    
 /* public ObservableList<Consultation> CrudConsultationListe() {
        ObservableList<Consultation> liste = FXCollections.observableArrayList();
        String selectData = "SELECT * FROM Consultation";
        try {
            PreparedStatement pst = cnx.prepareStatement(selectData);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Consultation sData;
                sData = new Consultation(rs.getInt("id"), rs.getString("notes"), rs.getString("prix"),rs.getInt("id_rendezvous_id"));
                liste.add(sData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return liste;
    }
    private ObservableList<Consultation> CrudConsultation;
     public void afficherconsultation(){
        CrudConsultation =  CrudConsultationListe();
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        colnotes.setCellValueFactory(new PropertyValueFactory<>("notes"));
        colprix.setCellValueFactory(new PropertyValueFactory<>("prix"));
       colidrdv.setCellValueFactory(new PropertyValueFactory<>("id_rendezvous_id"));
        tvConsultation.setItems(CrudConsultation);
    }*/
  public void table() {
    ObservableList<Consultation> consultation = FXCollections.observableArrayList();
    String selectData = "SELECT consultation.id, consultation.notes, consultation.prix, Rendez_vous.date_rendezvous "
                + "FROM consultation "
                + "INNER JOIN Rendez_vous ON consultation.id_rendezvous_id = Rendez_vous.id";
        try {
            PreparedStatement pst = cnx.prepareStatement(selectData);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
             int id = rs.getInt("id");
           
              String notes  = rs.getString("notes") ; 
              String prix = rs.getString("prix") ; 
           
              
      Date date = rs.getDate("date_rendezvous");             
                
                Consultation cons = new Consultation();
                cons.setId(id);
                cons.setNotes(notes);
            cons.setPrix(prix);
              
                
                
                Rendez_vous cn = new Rendez_vous();
                cn.setDate_rendezvous(date);
                
             cons.setR(cn);
             
            
                consultation.add(cons);
             
            }
        colnotes.setCellValueFactory(new PropertyValueFactory<>("notes"));
        colprix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colidrdv.setCellValueFactory(new PropertyValueFactory<>("r"));
        tvConsultation.setItems(consultation);
        
        // Add a listener to the table to detect when a row is selected
        tvConsultation.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                String selectedcons = newSelection.getNotes();
                System.out.println("consultation sélectionné : " + selectedcons);
            }
        });
        
        // Add a cell factory to the table to make the cells editable
        colnotes.setCellFactory(TextFieldTableCell.forTableColumn());
        colnotes.setOnEditCommit((TableColumn.CellEditEvent<Consultation, String> t) -> {
            Consultation selectedcons = t.getRowValue();
            selectedcons.setNotes(t.getNewValue());
        updatecons(selectedcons);
        });
         colprix.setCellFactory(TextFieldTableCell.forTableColumn());
        colprix.setOnEditCommit((TableColumn.CellEditEvent<Consultation, String> t) -> {
            Consultation selectedcons = t.getRowValue();
            selectedcons.setPrix(t.getNewValue());
        updatecons(selectedcons);
        });
       
        // Enable editing of the table
        tvConsultation.setEditable(true);
        
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}
      @FXML
    private void retour(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
        Parent root = loader.load();
        btnretour.getScene().setRoot(root);
    }
     private void selectLine() {
        tvConsultation.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Consultation L = tvConsultation.getItems().get(tvConsultation.getSelectionModel().getSelectedIndex());
               tfid.setText(Integer.toString(L.getId()));
                tfnotes.setText(L.getNotes());
                tfprix.setText(L.getPrix());

            }
        }
        );}
      @FXML
    private void ajouterconsul(ActionEvent event) throws IOException, SQLException {
  if (tfnotes.getText().isEmpty() || tfprix.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs !");
            alert.showAndWait();
            return;
        }
        String notes = tfnotes.getText();
      
        String prix = tfprix.getText();
        if (event.getSource() == btnajoutConsultation) {
            Consultation U = new Consultation();
            Consultation Us = new Consultation( tfnotes.getText(),tfprix.getText());
         String req = "INSERT INTO `Consultation` ( `id_rendezvous_id`, `notes`,`prix`) VALUES (?,?,?)";
        try {
            System.out.println();
            PreparedStatement pst = cnx.prepareStatement(req);
                    pst.setInt(1, cbrdv.getValue().getId());
            pst.setString(2, Us.getNotes());
            pst.setString(3, Us.getPrix());

            pst.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Consultation.fxml"));
            Parent root = loader.load();
            btnajoutConsultation.getScene().setRoot(root);

        }
    }
  
     private void updatecons(Consultation consultation) {
    try {
        int consId = consultation.getId();
        String notes = consultation.getNotes();
          String prix = consultation.getPrix();
        PreparedStatement pst = cnx.prepareStatement("UPDATE Consultation SET notes = ?, prix = ? WHERE id = ?");
        pst.setString(1, notes);
        pst.setString(2, prix);
        pst.setInt(3, consId);

        int rowsUpdated = pst.executeUpdate();
        if (rowsUpdated > 0) {
            JOptionPane.showMessageDialog(null, "Modification réussie");
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e);
    }
}
  
     @FXML
    private void suprimer(ActionEvent event) {
        try {
            Consultation del = tvConsultation.getSelectionModel().getSelectedItem();
            String deleteQuery = "DELETE FROM Consultation WHERE id = ?";
            PreparedStatement pst = cnx.prepareStatement(deleteQuery);
            pst.setInt(1, del.getId());
            pst.executeUpdate();
            table();
            System.out.println("Successful DELETE");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void choixrendezvous() {
    try {
        pst = cnx.prepareStatement("SELECT id, date_rendezvous FROM rendez_vous");
        ResultSet rs = pst.executeQuery();

        // Create a list of Service objects
        List<Rendez_vous> Rendez_vous = new ArrayList<>();

        // Loop through the results of the query and add each service to the list
        while (rs.next()) {
            int id = rs.getInt("id");
            Date date_rendezvous = rs.getDate("date_rendezvous");
            Rendez_vous rendez_vous = new Rendez_vous(id, date_rendezvous);
            Rendez_vous.add(rendez_vous);
        }

        // Set the items of the ComboBox to the list of Service objects
     cbrdv.setItems(FXCollections.observableList(Rendez_vous));
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, ex);
    }
}
    
}
