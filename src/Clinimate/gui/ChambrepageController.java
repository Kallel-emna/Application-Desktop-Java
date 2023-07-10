/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Clinimate.gui;

import Clinimate.entities.Chambre;
import Clinimate.entities.Service;
import Clinimate.entities.User;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javax.swing.JOptionPane;
import Clinimate.utils.DataSource;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class ChambrepageController implements Initializable {

    @FXML
    private Button ajouterchambre;

    @FXML
    private Button btnretourchambre;

    @FXML
    private TableColumn<Chambre, Integer> colcapacite;

    @FXML
    private TableColumn<Chambre, Integer> coletage;

    @FXML
    private TableColumn<Service, Integer> colnomservice;

    @FXML
    private TableColumn<Chambre, Double> colprix;
    @FXML
    private TableView<Chambre> tablechambre;
    @FXML
    private ComboBox<Service> combonomservice;

    @FXML
    private TableColumn<Chambre, String> collibelle;

    @FXML
    private TableColumn<Chambre, Void> colbtn;

    @FXML
    private TableColumn<Chambre, Void> colsupprimer;

    @FXML
    private TextField txtcapacitechambre;

    @FXML
    private TextField txtetagechambre;

    @FXML
    private TextField txtlibellechambre;

    @FXML
    private TextField txtprixchambre;
    Connection con;
    PreparedStatement pst;

    Connection conn = DataSource.getInstance().getCnx();

    @FXML
    void retourchambre(ActionEvent event) {
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

    public void table() {
        ObservableList<Chambre> chambres = FXCollections.observableArrayList();
        String sql = "SELECT chambre.libelle_chambre, chambre.etage, chambre.capacite, chambre.prix, service.nom_service "
                + "FROM chambre "
                + "INNER JOIN service ON chambre.service_id = service.id";

        try {
            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String libelle = rs.getString("libelle_chambre");
                int etage = rs.getInt("etage");
                int capacite = rs.getInt("capacite");
                int prix = rs.getInt("prix");
                String nomService = rs.getString("nom_service");

                Chambre chambre = new Chambre();
                chambre.setLibelle_chambre(libelle);
                chambre.setEtage(etage);
                chambre.setCapacite(capacite);
                chambre.setPrix(prix);

                Service service = new Service();
                service.setNom_service(nomService);

                chambre.setService(service);

                chambres.add(chambre);
            }
            collibelle.setCellValueFactory(new PropertyValueFactory<>("libelle_chambre"));
            colnomservice.setCellValueFactory(new PropertyValueFactory<>("service"));
            coletage.setCellValueFactory(new PropertyValueFactory<>("etage"));
            colprix.setCellValueFactory(new PropertyValueFactory<>("prix"));
            colcapacite.setCellValueFactory(new PropertyValueFactory<>("capacite"));

            tablechambre.setItems(chambres);
            collibelle.setEditable(true);
            collibelle.setCellFactory(TextFieldTableCell.forTableColumn());
            collibelle.setOnEditCommit((TableColumn.CellEditEvent<Chambre, String> t) -> {
                ((Chambre) t.getTableView().getItems().get(t.getTablePosition().getRow())).setLibelle_chambre(t.getNewValue());
                updateChambre((Chambre) t.getTableView().getItems().get(t.getTablePosition().getRow()));
            });
            // set the listener to update the database when a cell is edited
            coletage.setEditable(true);
            coletage.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            coletage.setOnEditCommit((TableColumn.CellEditEvent<Chambre, Integer> t) -> {
                ((Chambre) t.getTableView().getItems().get(t.getTablePosition().getRow())).setEtage(t.getNewValue());
                updateChambre((Chambre) t.getTableView().getItems().get(t.getTablePosition().getRow()));
            });

            colcapacite.setEditable(true);
            colcapacite.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            colcapacite.setOnEditCommit((TableColumn.CellEditEvent<Chambre, Integer> t) -> {
                ((Chambre) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCapacite(t.getNewValue());
                updateChambre((Chambre) t.getTableView().getItems().get(t.getTablePosition().getRow()));
            });

            colprix.setEditable(true);
            colprix.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
            colprix.setOnEditCommit((TableColumn.CellEditEvent<Chambre, Double> t) -> {
                ((Chambre) t.getTableView().getItems().get(t.getTablePosition().getRow())).setPrix(t.getNewValue());
                updateChambre((Chambre) t.getTableView().getItems().get(t.getTablePosition().getRow()));
            });

            tablechambre.setEditable(true);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updateChambre(Chambre chambre) {
        try {
            int serviceId = 0;
            String serviceName = chambre.getService().getNom_service();
            String sql = "SELECT id FROM service WHERE nom_service = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, serviceName);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        serviceId = rs.getInt(1);
                    }
                }
            }
            PreparedStatement pst = conn.prepareStatement("UPDATE chambre  SET service_id = ?, etage = ?, prix = ?, capacite = ? WHERE libelle_chambre = ?");
            pst.setInt(1, serviceId);
            pst.setInt(2, chambre.getEtage());
            pst.setDouble(3, chambre.getPrix());

            pst.setInt(4, chambre.getCapacite());
            pst.setString(5, chambre.getLibelle_chambre());

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Modification réussie");
                table(); // update the table view
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    @FXML
    void ajouterchambre(ActionEvent event) {
        try {
            if (combonomservice.getValue() == null) {
                JOptionPane.showMessageDialog(null, "Veuillez sélectionner un service.");
                return;
            }

            String libelleChambre = txtlibellechambre.getText(); // Ajouter un suffixe numérique pour chaque chambre
            pst = conn.prepareStatement("SELECT * FROM chambre WHERE libelle_chambre=?");
            pst.setString(1, libelleChambre);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "La chambre " + libelleChambre + " existe déjà.");
            } else {
                pst = conn.prepareStatement("insert into chambre(libelle_chambre,service_id,etage,prix,capacite)values(?,?,?,?,?)");
                pst.setString(1, libelleChambre);
                pst.setInt(2, combonomservice.getValue().getId());
                pst.setString(3, txtetagechambre.getText());
                pst.setString(4, txtprixchambre.getText());
                pst.setString(5, txtcapacitechambre.getText());
                pst.execute();
            }

            JOptionPane.showMessageDialog(null, " chambre(s) ajoutée(s) avec succès.");
            table();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Veuillez entrer un nombre de chambres valide.");
        }
    }

    public void choixservice() {
        try {
            pst = conn.prepareStatement("SELECT id, nom_service FROM service");
            ResultSet rs = pst.executeQuery();

            // Create a list of Service objects
            List<Service> services = new ArrayList<>();

            // Loop through the results of the query and add each service to the list
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom_service = rs.getString("nom_service");
                Service service = new Service(id, nom_service);
                services.add(service);
            }

            // Set the items of the ComboBox to the list of Service objects
            combonomservice.setItems(FXCollections.observableList(services));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    void supprimerchambre(Chambre chambre) {
        try {
            // Get the ID of the selected chambre
            String id = chambre.getLibelle_chambre();

            // Delete the selected chambre from the database
            pst = conn.prepareStatement("DELETE FROM chambre WHERE libelle_chambre = ?");
            pst.setString(1, id);
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "Suppression réussite");
                table(); // update the table view
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    void getSelected(MouseEvent event) {
        if (tablechambre == null || collibelle == null || colnomservice == null || coletage == null || colprix == null || txtetagechambre == null) {
            // Error: one or more required objects are null
            return;
        }

        int index = tablechambre.getSelectionModel().getSelectedIndex();
        if (index < 0 || index >= tablechambre.getItems().size()) {
            // Error: selected index is out of bounds
            return;
        }

        Chambre chambre = tablechambre.getItems().get(index);

        Service service = chambre.getService();
        combonomservice.setValue(service);
        if (!combonomservice.getItems().contains(service)) {
            combonomservice.getItems().add(service);
        }
        Service selectedService = combonomservice.getSelectionModel().getSelectedItem();
        String nomService = selectedService.getNom_service();
        txtlibellechambre.setText(String.valueOf(chambre.getLibelle_chambre()));

        txtetagechambre.setText(String.valueOf(chambre.getEtage()));
        txtprixchambre.setText(String.valueOf(chambre.getPrix()));
        txtcapacitechambre.setText(String.valueOf(chambre.getCapacite()));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        table();
        choixservice();
        colsupprimer.setCellFactory(column -> {
            return new TableCell<Chambre, Void>() {
                private final Button btn = new Button();

                {
                    final Image image = new Image(getClass().getResource("c.png").toExternalForm());
                    final ImageView icon = new ImageView(image);
                    btn.setGraphic(icon);
                    btn.setOnAction((ActionEvent event) -> {
                        // Get the selected chambre from the table view
                        Chambre chambre = getTableView().getItems().get(getIndex());

                        // Call the supprimerchambre method with the selected chambre
                        supprimerchambre(chambre);
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
        colbtn.setCellFactory(column
                -> {
            return new TableCell<Chambre, Void>() {
                private final Button btn = new Button();

                {
                    final Image image = new Image(getClass().getResource("lit.png").toExternalForm());
                    final ImageView icon = new ImageView(image);
                    btn.setGraphic(icon);
                    btn.setOnAction((ActionEvent event) -> {

                        // Récupérer la chambre associée à la ligne du bouton cliqué
                        Chambre resa = getTableView().getItems().get(getIndex());
                        String libelleChambre = resa.getLibelle_chambre();

                        // Récupérer les patients actuels dans la chambre
                        try {
                            PreparedStatement chambreStmt = conn.prepareStatement("SELECT id FROM chambre WHERE libelle_chambre = ?");
                            chambreStmt.setString(1, libelleChambre);
                            ResultSet chambreRs = chambreStmt.executeQuery();
                            int chambreId = -1;
                            if (chambreRs.next()) {
                                chambreId = chambreRs.getInt("id");
                            }
                            PreparedStatement actuelsStmt = conn.prepareStatement("SELECT p.* FROM user p, reservation_chambre r WHERE r.chambre_id = ? AND r.patient_id = p.id AND r.date_admission <= ? AND (r.date_sortie IS NULL OR r.date_sortie > ?)");
                            actuelsStmt.setInt(1, chambreId);
                            actuelsStmt.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
                            actuelsStmt.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
                            ResultSet actuelsRs = actuelsStmt.executeQuery();

                            // Récupérer les patients futurs dans la chambre
                            PreparedStatement futursStmt = conn.prepareStatement("SELECT p.* FROM user p, reservation_chambre r WHERE r.chambre_id = ? AND r.patient_id = p.id AND r.date_admission > ?");
                            futursStmt.setInt(1, chambreId);
                            futursStmt.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
                            ResultSet futursRs = futursStmt.executeQuery();

                            // Créer une nouvelle fenêtre pour afficher les patients
                            // Créer une nouvelle fenêtre pour afficher les patients
                            Stage stage = new Stage();
                            stage.setTitle("Patients dans la chambre " + libelleChambre);

// Créer un GridPane pour organiser les tables
                            GridPane gridPane = new GridPane();
                            Label actuelsLabel = new Label("Patients actuels");
                            Label futursLabel = new Label("Patients futurs");

// Ajouter les libellés à la grille avant les tables correspondantes
                            gridPane.add(actuelsLabel, 0, 1);
                            gridPane.add(futursLabel, 1, 1);
                            gridPane.setHgap(10); // Espace horizontal entre les cellules
                            gridPane.setVgap(10); // Espace vertical entre les cellules
                            gridPane.setPadding(new Insets(10, 10, 10, 10)); // Espace autour de la grille

// Créer une table pour afficher les patients actuels
                            TableView<User> actuelsTable = new TableView<>();
                            actuelsTable.setId("actuelsTable");

                            TableColumn<User, String> nomActuelsCol = new TableColumn<>("Nom");
                            nomActuelsCol.setCellValueFactory(new PropertyValueFactory<>("lastname"));
                            TableColumn<User, String> prenomActuelsCol = new TableColumn<>("Prénom");
                            prenomActuelsCol.setCellValueFactory(new PropertyValueFactory<>("firstname"));
                            actuelsTable.getColumns().addAll(nomActuelsCol, prenomActuelsCol);

// Ajouter les patients actuels à la table
                            ObservableList<User> actuelsPatients = FXCollections.observableArrayList();
                            while (actuelsRs.next()) {
                                String nom = actuelsRs.getString("lastname");
                                String prenom = actuelsRs.getString("firstname");
                                actuelsPatients.add(new User(nom, prenom));
                            }
                            actuelsTable.setItems(actuelsPatients);

// Ajouter la table des patients actuels à la grille
                            gridPane.add(actuelsTable, 0, 0); // La table est placée dans la première cellule (colonne 0, ligne 0)

// Créer une table pour afficher les patients futurs
                            TableView<User> futursTable = new TableView<>();
                            futursTable.setId("futursTable");

                            TableColumn<User, String> nomFutursCol = new TableColumn<>("Nom");
                            nomFutursCol.setCellValueFactory(new PropertyValueFactory<>("lastname"));
                            TableColumn<User, String> prenomFutursCol = new TableColumn<>("Prénom");
                            prenomFutursCol.setCellValueFactory(new PropertyValueFactory<>("firstname"));
                            futursTable.getColumns().addAll(nomFutursCol, prenomFutursCol);

// Ajouter les patients futurs à la table
                            ObservableList<User> futursPatients = FXCollections.observableArrayList();
                            while (futursRs.next()) {
                                String nom = futursRs.getString("lastname");
                                String prenom = futursRs.getString("firstname");
                                futursPatients.add(new User(nom, prenom));
                            }
                            futursTable.setItems(futursPatients);

// Ajouter la table des patients futurs à la grille
                            gridPane.add(futursTable, 1, 0); // La table est placée dans la deuxième cellule (colonne 1, ligne 0)

// Créer une scène pour afficher la grille
                            Scene scene = new Scene(gridPane, 600, 400);
                            stage.setScene(scene);
                            stage.show();

                        } catch (SQLException ex) {
                            System.out.println(ex);
                        }
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
        }
        );
    }

}
