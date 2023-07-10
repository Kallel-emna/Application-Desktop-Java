/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Clinimate.gui;

import Clinimate.entities.BilanMedical;
import Clinimate.entities.DossierMedical;
import Clinimate.entities.Ordonnance;
import Clinimate.service.ServiceDossier;
import Clinimate.utils.DataSource;
import java.io.IOException;
import javafx.scene.chart.PieChart;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.AnchorPane;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.JOptionPane;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author rania
 */
public class DossierController implements Initializable {

    ServiceDossier b = new ServiceDossier() {
    };

    Connection con;
    PreparedStatement pst;

    Connection cnx = DataSource.getInstance().getCnx();

    @FXML
    private PieChart statcapacite;
    @FXML
    private TextField certificat;
    @FXML
    private TextField groupe_sanguin;
    @FXML
    private Button ajouterD;
    //private AnchorPane pane;
    @FXML
    private TableView<DossierMedical> TableViewDossier;
    @FXML
    private TableColumn<DossierMedical, String> certificat_Dossier;
    @FXML
    private TableColumn<DossierMedical, Void> btnB;
    @FXML
    private TableColumn<DossierMedical, Void> btnsup;
    @FXML
    private TableColumn<DossierMedical, String> GS_Dossier;
    @FXML
    private TextField search;
    @FXML
    private TableColumn<Ordonnance, String> ordonnance;
    @FXML
    private ComboBox<Ordonnance> ordonnance_combo;
    //   PreparedStatement pst;
    @FXML
    private Button retour;
    @FXML
    private AnchorPane panePieChart;
    @FXML
    private TableColumn<DossierMedical, Void> nbr;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //     selectLine();
        //   Afficher_Dossier();
        table();
        try {
            pst = cnx.prepareStatement("SELECT id, nompatient FROM ordonnance");
            ResultSet rs = pst.executeQuery();

            // Create a list of Service objects
            List<Ordonnance> or = new ArrayList<>();

            // Loop through the results of the query and add each service to the list
            while (rs.next()) {
                int id = rs.getInt("id");
                String notes = rs.getString("nompatient");
                Ordonnance ordonnannce = new Ordonnance(id, notes);
                or.add(ordonnannce);

            }

            // Set the items of the ComboBox to the list of Service objects
            ordonnance_combo.setItems(FXCollections.observableList(or));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        btnsup.setCellFactory(column -> {
            return new TableCell<DossierMedical, Void>() {
                private final Button btn = new Button();

                {
                    final Image image = new Image(getClass().getResource("c.png").toExternalForm());
                    final ImageView icon = new ImageView(image);
                    btn.setGraphic(icon);
                    btn.setOnAction((ActionEvent event) -> {
                        // Get the selected chambre from the table view
                        DossierMedical d = getTableView().getItems().get(getIndex());

                        // Call the supprimerchambre method with the selected chambre
                        supprimerDossier(d);
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

        btnB.setCellFactory(column
                -> {
            return new TableCell<DossierMedical, Void>() {
                private final Button btn = new Button();

                {
                    final Image image = new Image(getClass().getResource("bilan.png").toExternalForm());
                    final ImageView icon = new ImageView(image);
                    btn.setGraphic(icon);
                    btn.setOnAction((ActionEvent event) -> {

                        // Récupérer la chambre associée à la ligne du bouton cliqué
                        DossierMedical resa = getTableView().getItems().get(getIndex());
                        String certificatt = resa.getCertificat();

                        // Récupérer les patients actuels dans la chambre
                        try {
                            PreparedStatement dossierStmt = cnx.prepareStatement("SELECT id FROM dossier_medical WHERE certificat = ?");
                            dossierStmt.setString(1, certificatt);
                            ResultSet dossierRs = dossierStmt.executeQuery();
                            int dossierId = -1;
                            if (dossierRs.next()) {
                                dossierId = dossierRs.getInt("id");
                            }

                            PreparedStatement actuelsStmt = cnx.prepareStatement("SELECT * FROM bilan_medical WHERE dossier_medical_id = ? ");
                            actuelsStmt.setInt(1, dossierId);

                            ResultSet actuelsRs = actuelsStmt.executeQuery();

                            // Créer une nouvelle fenêtre pour afficher les patients
                            // Créer une nouvelle fenêtre pour afficher les patients
                            Stage stage = new Stage();
                            stage.setTitle("Bilan dans le dossier " + certificatt);

// Créer un GridPane pour organiser les tables
                            GridPane gridPane = new GridPane();
                            Label actuelsLabel = new Label("Les bilans actuels ");

// Ajouter les libellés à la grille avant les tables correspondantes
                            gridPane.add(actuelsLabel, 0, 1);

                            gridPane.setHgap(10); // Espace horizontal entre les cellules
                            gridPane.setVgap(10); // Espace vertical entre les cellules
                            gridPane.setPadding(new Insets(10, 10, 10, 10)); // Espace autour de la grille

// Créer une table pour afficher les patients actuels
                            TableView<BilanMedical> actuelsTable = new TableView<>();
                            actuelsTable.setId("actuelsTable");

                            TableColumn<BilanMedical, String> a = new TableColumn<>("Antecedent");
                            a.setCellValueFactory(new PropertyValueFactory<>("antecedents"));
                            TableColumn<BilanMedical, String> t = new TableColumn<>("Taille");
                            t.setCellValueFactory(new PropertyValueFactory<>("taille"));

                            TableColumn<BilanMedical, String> poid = new TableColumn<>("Poids");
                            poid.setCellValueFactory(new PropertyValueFactory<>("poids"));

                            TableColumn<BilanMedical, String> e = new TableColumn<>("Examens Biol");
                            e.setCellValueFactory(new PropertyValueFactory<>("examens_biologiques"));

                            TableColumn<BilanMedical, String> imageP = new TableColumn<>("Imagerie M");
                            imageP.setCellValueFactory(new PropertyValueFactory<>("imagerie_medicale"));

                            actuelsTable.getColumns().addAll(a, t, poid, e, imageP);

// Ajouter les patients actuels à la table
                            ObservableList<BilanMedical> actuelsPatients = FXCollections.observableArrayList();
                            while (actuelsRs.next()) {
                                String an = actuelsRs.getString("antecedents");
                                String tai = actuelsRs.getString("taille");
                                String p = actuelsRs.getString("poids");
                                String examen = actuelsRs.getString("examens_biologiques");
                                String imagerie = actuelsRs.getString("imagerie_medicale");

                                actuelsPatients.add(new BilanMedical(an, tai, p, examen, imagerie));
                            }
                            actuelsTable.setItems(actuelsPatients);

// Ajouter la table des patients actuels à la grille
                            gridPane.add(actuelsTable, 0, 0); // La table est placée dans la première cellule (colonne 0, ligne 0)

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
        });

        /*
          try {
            // Préparer la requête SQL
            String sql = "SELECT dossier_medical.certificat, COUNT(bilan_medical.id) as nb_bilans FROM dossier_medical LEFT JOIN bilan_medical ON dossier_medical.id = bilan_medical.dossier_medical_id GROUP BY dossier_medical.id" ; 
            PreparedStatement stmt = cnx.prepareStatement(sql);

            // Exécuter la requête SQL et récupérer les résultats
            ResultSet rs = stmt.executeQuery();

            // Créer la liste des données pour le PieChart
            ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
            while (rs.next()) {
                String nomService = rs.getString("certificat");
                int totalCapacite = rs.getInt("nb_bilans");
                data.add(new PieChart.Data(nomService, totalCapacite));
            }

            // Créer le PieChart
            PieChart statcapacite = new PieChart();
            statcapacite.setData(data);
            statcapacite.setTitle("Occupancy rate by service");

            // Ajouter le PieChart à la scène
            panePieChart.getChildren().add(statcapacite);

        } catch (SQLException e) {
            e.printStackTrace();
        } 
        
         */
        nbr.setCellFactory(column
                -> {
            return new TableCell<DossierMedical, Void>() {
                private final Button btn = new Button();

                {
                    final Image image = new Image(getClass().getResource("number.png").toExternalForm());
                    final ImageView icon = new ImageView(image);
                    btn.setGraphic(icon);
                    btn.setOnAction((ActionEvent event) -> {

                        // Récupérer la chambre associée à la ligne du bouton cliqué
                        DossierMedical resa = getTableView().getItems().get(getIndex());
                        String certificatt = resa.getCertificat();

                        // Récupérer les patients actuels dans la chambre
                        try {
                            PreparedStatement dossierStmt = cnx.prepareStatement("SELECT id FROM dossier_medical WHERE certificat = ?");
                            dossierStmt.setString(1, certificatt);
                            ResultSet dossierRs = dossierStmt.executeQuery();
                            int dossierId = -1;
                            if (dossierRs.next()) {
                                dossierId = dossierRs.getInt("id");
                            }

                            PreparedStatement actuelsStmt = cnx.prepareStatement("SELECT COUNT(bilan_medical.id) AS nb_bilans  FROM bilan_medical WHERE dossier_medical_id = ? ");
                            actuelsStmt.setInt(1, dossierId);

                            ResultSet rs = actuelsStmt.executeQuery();
                            rs.next();
                            int nbrBilan = rs.getInt("nb_bilans");

                            // Créer une nouvelle fenêtre pour afficher les patients
                            // Créer une nouvelle fenêtre pour afficher les patients
                            Stage stage = new Stage();
                            stage.setTitle("Nbr de bilans de ce dossier " + certificatt);

// Créer un GridPane pour organiser les tables
                            GridPane gridPane = new GridPane();
                            Label actuelsLabel = new Label("Nombre de bilans de ce dossier est  " + nbrBilan);

// Ajouter les libellés à la grille avant les tables correspondantes
                            gridPane.add(actuelsLabel,
                                    38, 1);

                            gridPane.setHgap(
                                    5); // Espace horizontal entre les cellules
                            gridPane.setVgap(
                                    5); // Espace vertical entre les cellules
                            gridPane.setPadding(
                                    new Insets(5, 5, 5, 5)); // Espace autour de la grille

// Créer une table pour afficher les patients actuels
                            //    Label label = new Label("Nombre de bilans : " + nbrBilan);
// Ajouter les patients actuels à la table
// Ajouter la table des patients actuels à la grille
// Créer une scène pour afficher la grille
                            Scene scene = new Scene(gridPane, 600, 100);

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
        });

    }

    private ObservableList<DossierMedical> bilan = b.AfficherDossier();

    public void Afficher_Dossier() {

        //    ID_Dossier.setCellValueFactory(new PropertyValueFactory<>("id"));
        certificat_Dossier.setCellValueFactory(new PropertyValueFactory<>("certificat"));
        GS_Dossier.setCellValueFactory(new PropertyValueFactory<>("groupe_sanguin"));
        ordonnance.setCellValueFactory(new PropertyValueFactory<>("ordonnance_id"));

        TableViewDossier.setItems(bilan);
    }

    /*

       private void selectLine() {
        TableViewDossier.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                DossierMedical L = TableViewDossier.getItems().get(TableViewDossier.getSelectionModel().getSelectedIndex());
             //   ID.setText(Integer.toString(L.getId()));
                certificat.setText(L.getCertificat());
                groupe_sanguin.setText(L.getGroupe_sanguin());
           //    ordonnance_combo.setId(L.getOrdonnance_id());
             //   TelephoneC.setText(Integer.toString(L.getTelephone()));

            }
        }
        );
    }
    @FXML

    private void Supprimer_Dossier(ActionEvent event) {
        DossierMedical selecteditem = (DossierMedical) TableViewDossier.getSelectionModel().getSelectedItem();
        b.SupprimerDossier(selecteditem);
        TableViewDossier.getItems().setAll(b.AfficherDossier());
    }*/
    void supprimerDossier(DossierMedical d) {
        try {
            // Get the ID of the selected chambre
            int id = d.getId();

            // Delete the selected chambre from the database
            pst = cnx.prepareStatement("DELETE FROM dossier_medical WHERE id = ?");
            pst.setInt(1, id);
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "Suppression réussite");
                table(); // update the table view
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void Modifier_Dossier(ActionEvent event) {

        if (certificat.getText().isEmpty() || groupe_sanguin.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs !");
            alert.showAndWait();
            return;
        }
        DossierMedical selecteditem = (DossierMedical) TableViewDossier.getSelectionModel().getSelectedItem();
        b.ModifierDossier(certificat.getText(), groupe_sanguin.getText(), ordonnance_combo.getValue().getId(), selecteditem.getId());
        TableViewDossier.getItems().setAll(b.AfficherDossier());
    }

    @FXML

    private void Ajouter_Dossier(ActionEvent event) {
        if (certificat.getText().isEmpty() || groupe_sanguin.getText().isEmpty()) {
            String title = " Veuillez remplir tous les champs !";
            TrayNotification tray = new TrayNotification();
            AnimationType type = AnimationType.POPUP;

            tray.setAnimationType(type);
            tray.setTitle(title);
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));

            return;
        }
        DossierMedical D;
        D = new DossierMedical(certificat.getText(), groupe_sanguin.getText(), ordonnance_combo.getValue().getId());
        b.ajouterDossier(D);
        TableViewDossier.getItems().setAll(b.AfficherDossier());
        String title = "Votre dossier a étè ajouté avec success ! ";

        TrayNotification tray = new TrayNotification();
        AnimationType type = AnimationType.POPUP;

        tray.setAnimationType(type);
        tray.setTitle(title);

        tray.setNotificationType(NotificationType.SUCCESS);
        tray.showAndDismiss(Duration.millis(3000));

    }

    @FXML

    private void FiltreKeyRealsead() {

        ObservableList<DossierMedical> list = b.search("");
        List<DossierMedical> list1;
        list1 = list.stream()
                .filter((DossierMedical u) -> u.getGroupe_sanguin().length() >= search.getText().length())
                .filter(u -> u.getGroupe_sanguin().substring(0, search.getText().length()).equalsIgnoreCase(search.getText()))
                .collect(Collectors.<DossierMedical>toList());

        ObservableList<DossierMedical> List = FXCollections.observableArrayList();
        list1.forEach((DossierMedical u) -> List.add(u));

        TableViewDossier.setItems(List);
    }

    @FXML
    private void Home(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
        Parent root = loader.load();
        retour.getScene().setRoot(root);
    }

    public void table() {

        ObservableList<DossierMedical> liste = FXCollections.observableArrayList();
        String selectData = "SELECT dossier_medical.id, dossier_medical.certificat, dossier_medical.groupe_sanguin, ordonnance.nompatient "
                + "FROM dossier_medical "
                + "INNER JOIN ordonnance ON dossier_medical.ordonnance_id = ordonnance.id";

        try {
            PreparedStatement pst = cnx.prepareStatement(selectData);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");

                String cer = rs.getString("certificat");
                String gr = rs.getString("groupe_sanguin");

                String nom = rs.getString("nompatient");

                DossierMedical dossier = new DossierMedical();
                dossier.setId(id);
                dossier.setCertificat(cer);
                dossier.setGroupe_sanguin(gr);

                Ordonnance or = new Ordonnance();
                or.setNompatient(nom);

                dossier.setOrdonnance(or);

                liste.add(dossier);

            }
            //   colnom.setCellValueFactory(new PropertyValueFactory<>("nom_service"));
            //    ID_Dossier.setCellValueFactory(new PropertyValueFactory<>("id"));
            certificat_Dossier.setCellValueFactory(new PropertyValueFactory<>("certificat"));
            GS_Dossier.setCellValueFactory(new PropertyValueFactory<>("groupe_sanguin"));
            ordonnance.setCellValueFactory(new PropertyValueFactory<>("ordonnance"));

            TableViewDossier.setItems(liste);

            // Add a listener to the table to detect when a row is selected
            TableViewDossier.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    String selectedService = newSelection.getCertificat();
                    System.out.println("dossier sélectionné : " + selectedService);
                }
            });

            //BilanTaille.setCellValueFactory(new PropertyValueFactory<>("taille"));
            certificat_Dossier.setCellFactory(TextFieldTableCell.forTableColumn());
            certificat_Dossier.setOnEditCommit((TableColumn.CellEditEvent<DossierMedical, String> t) -> {
                DossierMedical selectedService = t.getRowValue();
                selectedService.setCertificat(t.getNewValue());
                updateBilan(selectedService);
            });
            TableViewDossier.setEditable(true);

            GS_Dossier.setCellFactory(TextFieldTableCell.forTableColumn());
            GS_Dossier.setOnEditCommit((TableColumn.CellEditEvent<DossierMedical, String> t) -> {
                DossierMedical selectedService = t.getRowValue();
                selectedService.setGroupe_sanguin(t.getNewValue());
                updateBilan(selectedService);
            });
            TableViewDossier.setEditable(true);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updateBilan(DossierMedical service) {
        int id = service.getId();
        String cer = service.getCertificat();
        String gr = service.getGroupe_sanguin();

        //int user =service.getUser_id();
        // int d = service.getDossier_id();
        try {

            String req = " UPDATE `dossier_medical` SET  `certificat`='" + cer + "',`groupe_sanguin`='" + gr + "' WHERE `id`='" + id + "' ";
            Statement st = cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("Bilan modifie avec succes");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
