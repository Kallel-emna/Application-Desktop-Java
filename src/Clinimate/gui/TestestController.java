/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Clinimate.gui;

import Clinimate.entities.Categorie;
import Clinimate.service.ServiceCategorie;
import Clinimate.utils.DataSource;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author kalle
 */
public class TestestController implements Initializable {

    @FXML
    private TableView<Categorie> tableview_cat;
    @FXML
    private TableColumn<Categorie, String> nom_cat;

    Connection cnx = DataSource.getInstance().getCnx();
    ServiceCategorie c = new ServiceCategorie();
    PreparedStatement stmt;
    PreparedStatement ste;

    @FXML
    private TextField nomcat;
    @FXML
    private Button ajouter_cat;

    @FXML
    private Button supp_cat;
    @FXML
    private Button retour;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Cat_Show();
        table();
    }

    public void Cat_Show() {
        nom_cat.setCellValueFactory(new PropertyValueFactory<>("nom_cat"));

        tableview_cat.getItems().setAll(c.afficherCategorie());
    }

    @FXML
    private void Ajouter_cat(javafx.event.ActionEvent event) {
        if (nomcat.getText().isEmpty() ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs !");
            alert.showAndWait();
            return;
        }
        String nom = "[a-zA-Z]+";
        Pattern pa = Pattern.compile(nom, Pattern.CASE_INSENSITIVE);
        Matcher ma = pa.matcher(nomcat.getText());
        if (!ma.matches()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez saisir un nom valide !");
            alert.showAndWait();

            return;
        }
        Categorie C;
        C = new Categorie(nomcat.getText());
        
        c.ajouterCategorie(C);

        tableview_cat.getItems().setAll(c.afficherCategorie());
    }

    public void table() {
        ObservableList<Categorie> categorie = FXCollections.observableArrayList();
        try {
            String sql = "SELECT id, nom_cat FROM categorie";
            ste = cnx.prepareStatement(sql);
            ResultSet rs = ste.executeQuery();
            while (rs.next()) {
                categorie.add(new Categorie(rs.getInt("id"), rs.getString("nom_cat")));
            }
            nom_cat.setCellValueFactory(new PropertyValueFactory<>("nom_cat"));
            tableview_cat.setItems(categorie);

            // Add a listener to the table to detect when a row is selected
            tableview_cat.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    String selectedCat = newSelection.getNom_cat();
                    System.out.println("Catégorie sélectionné : " + selectedCat);
                }
            });

            // Add a cell factory to the table to make the cells editable
            nom_cat.setCellFactory(TextFieldTableCell.forTableColumn());
            nom_cat.setOnEditCommit((TableColumn.CellEditEvent<Categorie, String> t) -> {
                Categorie selectedCat = t.getRowValue();
                selectedCat.setNom_cat(t.getNewValue());
                updateCategorie(selectedCat);
            });

            // Enable editing of the table
            tableview_cat.setEditable(true);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updateCategorie(Categorie categorie) {
        try {
            int CategorieId = categorie.getId();
            String CategorieName = categorie.getNom_cat();
            PreparedStatement pst = cnx.prepareStatement("UPDATE categorie SET nom_cat = ? WHERE id = ?");
            pst.setString(1, CategorieName);
            pst.setInt(2, CategorieId);

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Modification réussie");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    @FXML
    private void Supprimer_cat(javafx.event.ActionEvent event) {
        Categorie selecteditem = (Categorie) tableview_cat.getSelectionModel().getSelectedItem();
        c.supprimerCategorie(selecteditem);
        tableview_cat.getItems().setAll(c.afficherCategorie());
    }

    @FXML
    private void Retour(javafx.event.ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Pharmacien.fxml"));
        Parent root = loader.load();
        retour.getScene().setRoot(root);
    }

}
