/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Clinimate.gui;

import Clinimate.utils.DataSource;
import Clinimate.entities.Chambre;
import Clinimate.entities.Service;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableCell;
import javax.swing.JOptionPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class ServicepageController implements Initializable {

    @FXML
    private PieChart statcapacite;
    @FXML
    private AnchorPane panePieChart;
    @FXML
    private Button btnajouterservice;
    @FXML
    private Button btnretourservice;

    @FXML
    private TableColumn<Service, String> colnom;

    @FXML
    private TableView<Service> tableservice;

    @FXML
    private TextField txtnomservice;
    @FXML
    private TableColumn<Service, Void> colsupprimerservice;
    Connection con;
    PreparedStatement pst;
    int myIndex;
    int index = -1;
    int id;

    Connection conn = DataSource.getInstance().getCnx();

    public void displayOccupancyRate() {
        try {
            String sql = "SELECT s.nom_service, SUM(c.capacite) as total_capacite\n"
                    + "FROM service s \n"
                    + "JOIN chambre c ON c.service_id = s.id\n"
                    + "GROUP BY s.id";
            PreparedStatement stmt = conn.prepareStatement(sql);

            // Exécuter la requête SQL et récupérer les résultats
            ResultSet rs = stmt.executeQuery();

            // Créer la liste des données pour le PieChart
            ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
            while (rs.next()) {
                String nomService = rs.getString("nom_service");
                int totalCapacite = rs.getInt("total_capacite");
                data.add(new PieChart.Data(nomService, totalCapacite));
            }

            // Créer le PieChart
            PieChart pieChart = new PieChart();
            pieChart.setData(data);
            pieChart.setTitle("Occupation par service");

            // Ajouter le PieChart à la scène
            panePieChart.getChildren().add(pieChart);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void backservice(ActionEvent event) {
        try {
            // Charger la nouvelle vue à partir d'un fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Secretairepage.fxml"));
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
    void ajouter_service(ActionEvent event) {
        try {
            if (txtnomservice.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Le champ ne doit pas être vide.");
                return;
            }
            pst = conn.prepareStatement("SELECT * FROM service WHERE nom_service=?");
            pst.setString(1, txtnomservice.getText());
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "Le nom de service existe déjà.");
            } else {
                pst = conn.prepareStatement("INSERT INTO service (nom_service) VALUES (?)");
                pst.setString(1, txtnomservice.getText());
                pst.execute();
                JOptionPane.showMessageDialog(null, "Ajout réussi");
                table();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void table() {
        ObservableList<Service> service = FXCollections.observableArrayList();
        try {
            String sql = "SELECT id, nom_service FROM service";
            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                service.add(new Service(rs.getInt("id"), rs.getString("nom_service")));
            }
            colnom.setCellValueFactory(new PropertyValueFactory<>("nom_service"));
            tableservice.setItems(service);

            // Add a listener to the table to detect when a row is selected
            tableservice.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    String selectedService = newSelection.getNom_service();
                    System.out.println("Service sélectionné : " + selectedService);
                }
            });

            // Add a cell factory to the table to make the cells editable
            colnom.setCellFactory(TextFieldTableCell.forTableColumn());
            colnom.setOnEditCommit((TableColumn.CellEditEvent<Service, String> t) -> {
                Service selectedService = t.getRowValue();
                selectedService.setNom_service(t.getNewValue());
                updateService(selectedService);
            });

            // Enable editing of the table
            tableservice.setEditable(true);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updateService(Service service) {
        try {
            int serviceId = service.getId();
            String serviceName = service.getNom_service();

            // vérifier que le nouveau nom de service n'est pas null
            if (serviceName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Le nom du service ne peut pas être null");
                return;
            }

            // vérifier que le nouveau nom de service n'existe pas déjà pour un autre service
            int count = 0;
            String sqlCount = "SELECT COUNT(*) FROM service WHERE nom_service = ? AND id != ?";
            try (PreparedStatement stmtCount = conn.prepareStatement(sqlCount)) {
                stmtCount.setString(1, serviceName);
                stmtCount.setInt(2, serviceId);
                try (ResultSet rsCount = stmtCount.executeQuery()) {
                    if (rsCount.next()) {
                        count = rsCount.getInt(1);
                    }
                }
            }
            if (count > 0) {
                JOptionPane.showMessageDialog(null, "Le nom du service existe déjà pour un autre service");
                return;
            }

            PreparedStatement pst = conn.prepareStatement("UPDATE service SET nom_service = ? WHERE id = ?");
            pst.setString(1, serviceName);
            pst.setInt(2, serviceId);

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Modification réussie");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void getSelected(MouseEvent event) {
        if (tableservice == null || colnom == null || txtnomservice == null) {
            // Error: one or more required objects are null
            return;
        }

        int index = tableservice.getSelectionModel().getSelectedIndex();
        if (index < 0 || index >= tableservice.getItems().size()) {
            // Error: selected index is out of bounds
            return;
        }

        txtnomservice.setText(String.valueOf(colnom.getCellData(index)));
    }

    void supprimerservice(Service service) {
        try {

            String setviceId = service.getNom_service();
            // Create the PreparedStatement object for the DELETE statement
            PreparedStatement pst = conn.prepareStatement("DELETE FROM service WHERE nom_service = ?");
            pst.setString(1, setviceId);

            int rowsDeleted = pst.executeUpdate();

            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "Suppression réussite");
                table(); // update the table view
            }

            // Close the ResultSet, PreparedStatement, and Connection objects
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        table();
        try {
            // Préparer la requête SQL
            String sql = "SELECT s.nom_service, SUM(c.capacite) as total_capacite\n"
                    + "FROM service s \n"
                    + "JOIN chambre c ON c.service_id = s.id\n"
                    + "GROUP BY s.id";
            PreparedStatement stmt = conn.prepareStatement(sql);

            // Exécuter la requête SQL et récupérer les résultats
            ResultSet rs = stmt.executeQuery();

            // Créer la liste des données pour le PieChart
            ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
            while (rs.next()) {
                String nomService = rs.getString("nom_service");
                int totalCapacite = rs.getInt("total_capacite");
                data.add(new PieChart.Data(nomService, totalCapacite));
            }

            // Créer le PieChart
            PieChart pieChart = new PieChart();
            pieChart.setData(data);
            pieChart.setTitle("Taux d'occupation par service");
            String style = "-fx-pie-color: #0072C6;"; // blue color
            pieChart.setStyle(style);
            // Ajouter le PieChart à la scène
            panePieChart.getChildren().add(pieChart);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        colsupprimerservice.setCellFactory(column -> {
            return new TableCell<Service, Void>() {
                private final Button btn = new Button();

                {
                    // Set the button's graphic to an ImageView of the cross emoticon
                    final Image image = new Image(getClass().getResource("c.png").toExternalForm());
                    final ImageView icon = new ImageView(image);
                    btn.setGraphic(icon);

                    btn.setOnAction((ActionEvent event) -> {
                        // Get the selected service from the table view
                        Service service = getTableView().getItems().get(getIndex());

                        // Call the supprimerservice method with the selected service
                        supprimerservice(service);
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(btn);
                    }
                }

            };
        });
    }
}
