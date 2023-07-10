/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Clinimate.gui;

import Clinimate.utils.DataSource;
import Clinimate.entities.Chambre;
import Clinimate.entities.ReservationChambre;
import Clinimate.entities.User;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class ReservationpageController implements Initializable {

    @FXML
    private Button retourrese;
    @FXML
    private Button searche;

    @FXML
    private TextField seearchbar;

    @FXML
    private Button btnajouterrese;

    @FXML
    private Button btnmodifierreser;

    @FXML
    private Button btnsupprimerreser;

    @FXML
    private DatePicker choixdate_admission;

    @FXML
    private DatePicker choixdate_sortie;

    @FXML
    private TableColumn<ReservationChambre, Date> coladmission;
    @FXML
    private TableColumn<ReservationChambre, Void> colfacture;
    @FXML
    private TableColumn<Chambre, String> colchambre;

    @FXML
    private TableColumn<ReservationChambre, Integer> colidres;

    @FXML
    private TableColumn<User, String> colpatient;

    @FXML
    private TableColumn<ReservationChambre, Date> colsortie;
    @FXML
    private TableColumn<ReservationChambre, Void> colsupprimerrese;

    @FXML
    private ComboBox<Chambre> combochambre;

    @FXML
    private ComboBox<User> combopatient;

    @FXML
    private TableView<ReservationChambre> tablereservation;

    PreparedStatement pst;

    Connection conn = DataSource.getInstance().getCnx();

    @FXML
    void backRese(ActionEvent event) {
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
    void chercherreservation(String recherche) {
        // Get the reservation ID from the text field
        int reservationId = Integer.parseInt(recherche);

        // Prepare the SQL query to retrieve the reservation with the specified ID
        String sql = "SELECT rc.id, rc.date_admission, rc.date_sortie, c.libelle_chambre, rc.patient_id, u.lastname "
                + "FROM reservation_chambre rc "
                + "JOIN chambre c ON rc.chambre_id = c.id "
                + "JOIN user u ON rc.patient_id = u.id "
                + "WHERE rc.id = ?";

        try {
            // Create a PreparedStatement with the SQL query and set the parameter
            pst = conn.prepareStatement(sql);
            pst.setInt(1, reservationId);

            // Execute the query and get the result set
            ResultSet rs = pst.executeQuery();

            // Create an ObservableList to hold the reservations
            ObservableList<ReservationChambre> reservations = FXCollections.observableArrayList();

            // Loop through the result set and add the reservations to the list
            while (rs.next()) {
                int id = rs.getInt("id");
                Date admission = rs.getDate("date_admission");
                Date sortie = rs.getDate("date_sortie");
                String libelle_chambre = rs.getString("libelle_chambre");
                int patient_id = rs.getInt("patient_id");
                String lastname = rs.getString("lastname");

                ReservationChambre reservation = new ReservationChambre();
                reservation.setId(id);
                reservation.setDate_admission(admission);
                reservation.setDate_sortie(sortie);

                Chambre chambre = new Chambre();
                chambre.setLibelle_chambre(libelle_chambre);

                User patient = new User();
                patient.setLastname(lastname);

                reservation.setChambre(chambre);
                reservation.setPatient(patient);

                reservations.add(reservation);
            }

            // Set the cell value factories for the table columns
            colidres.setCellValueFactory(new PropertyValueFactory<>("id"));
            colchambre.setCellValueFactory(new PropertyValueFactory<>("chambre"));
            colpatient.setCellValueFactory(new PropertyValueFactory<>("patient"));
            coladmission.setCellValueFactory(new PropertyValueFactory<>("date_admission"));
            colsortie.setCellValueFactory(new PropertyValueFactory<>("date_sortie"));

            // Set the table items to the reservations list
            tablereservation.setItems(reservations);
            coladmission.setEditable(true);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void table() {
        ObservableList<ReservationChambre> reservations = FXCollections.observableArrayList();
        String sql = "SELECT rc.id, rc.date_admission, rc.date_sortie, c.libelle_chambre, rc.patient_id, u.lastname "
                + "FROM reservation_chambre rc "
                + "JOIN chambre c ON rc.chambre_id = c.id "
                + "JOIN user u ON rc.patient_id = u.id";

        try {
            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                Date admission = rs.getDate("date_admission");
                Date sortie = rs.getDate("date_sortie");
                String libelle_chambre = rs.getString("libelle_chambre");
                int patient_id = rs.getInt("patient_id");
                String lastname = rs.getString("lastname");

                ReservationChambre reservation = new ReservationChambre();
                reservation.setId(id);
                reservation.setDate_admission(admission);
                reservation.setDate_sortie(sortie);

                Chambre chambre = new Chambre();
                chambre.setLibelle_chambre(libelle_chambre);

                User patient = new User();
                patient.setLastname(lastname);

                reservation.setChambre(chambre);
                reservation.setPatient(patient);

                reservations.add(reservation);
            }

            colidres.setCellValueFactory(new PropertyValueFactory<>("id"));
            colchambre.setCellValueFactory(new PropertyValueFactory<>("chambre"));

            colpatient.setCellValueFactory(new PropertyValueFactory<>("patient"));
            coladmission.setCellValueFactory(new PropertyValueFactory<>("date_admission"));
            colsortie.setCellValueFactory(new PropertyValueFactory<>("date_sortie"));

            tablereservation.setItems(reservations);
            coladmission.setEditable(true);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void ajouter_rese(ActionEvent event) {
        try {
            LocalDate admissionDate = choixdate_admission.getValue();
            LocalDate sortieDate = choixdate_sortie.getValue();
            int libelle = combochambre.getValue().getId();
            // Vérifier que les champs obligatoires sont remplis
            if (combochambre.getValue() == null || combopatient.getValue() == null || admissionDate == null) {
                JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs obligatoires.");
                return;
            }

            // Vérifier que la date d'admission est postérieure ou égale à la date actuelle
            if (admissionDate.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(null, "La date d'admission doit être postérieure ou égale à la date actuelle.");
                return;
            }

            // Vérifier que la date d'admission est antérieure à la date de sortie
            if (sortieDate != null && admissionDate.isAfter(sortieDate)) {
                JOptionPane.showMessageDialog(null, "La date d'admission doit être antérieure à la date de sortie.");
                return;
            }

            pst = conn.prepareStatement("insert into reservation_chambre(chambre_id,patient_id,date_admission,date_sortie) values(?,?,?,?)");
            pst.setInt(1, libelle);
            pst.setInt(2, combopatient.getValue().getId());
            pst.setDate(3, java.sql.Date.valueOf(admissionDate));
            if (sortieDate != null) {
                pst.setDate(4, java.sql.Date.valueOf(sortieDate));
            } else {
                // Vérifier que la capacité de la chambre est supérieure à zéro avant de décrémenter
                PreparedStatement pst1 = conn.prepareStatement("SELECT capacite FROM chambre WHERE id = ?");
                pst1.setInt(1, libelle);
                ResultSet rs = pst1.executeQuery();
                if (rs.next()) {
                    int currentCapacity = rs.getInt("capacite");
                    if (currentCapacity == 0) {
                        JOptionPane.showMessageDialog(null, "La capacité de cette chambre est déjà à zéro.");
                        return;
                    } else if (LocalDate.now().isBefore(admissionDate)) {
                        pst.setNull(4, java.sql.Types.DATE);
                    } else {
                        // Décrémenter la capacité de la chambre et mettre à jour dans la base de données
                        updateCapacity(libelle, -1);
                        pst.setNull(4, java.sql.Types.DATE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "La chambre spécifiée n'existe pas.");
                    return;
                }
            }

            pst.execute();
            JOptionPane.showMessageDialog(null, "Ajout réussi.");
            table();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void updateCapacity(int chambreId, int increment) throws SQLException {
        // récupérer la capacité actuelle de la chambre
        PreparedStatement pst1 = conn.prepareStatement("SELECT capacite FROM chambre WHERE id = ?");
        pst1.setInt(1, chambreId);
        ResultSet rs = pst1.executeQuery();
        if (rs.next()) {
            int currentCapacity = rs.getInt("capacite");
            // si la capacité est déjà à zéro, on ne peut pas la décrémenter davantage
            if (currentCapacity == 0 && increment < 0) {
                JOptionPane.showMessageDialog(null, "La capacité de cette chambre est déjà à zéro.");
                return;
            }
            // ajouter la capacité à incrémenter ou soustraire la capacité à décrémenter
            int newCapacity = currentCapacity + increment;
            // mettre à jour la capacité de

            // mettre à jour la capacité de la chambre dans la base de données
            PreparedStatement pst2 = conn.prepareStatement("UPDATE chambre SET capacite = ? WHERE id = ?");
            pst2.setInt(1, newCapacity);
            pst2.setInt(2, chambreId);
            pst2.executeUpdate();
        }
    }

    private int findChambreId(String libelle_chambre) throws SQLException {
        int chambreId = -1;
        String query = "SELECT id FROM chambre WHERE libelle_chambre = ?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1, libelle_chambre);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            chambreId = rs.getInt("id");
        }
        return chambreId;
    }

    public int getPatientIdByLastName(String lastName) throws SQLException {
        int patientId = -1; // initialiser à -1 pour le cas où aucun patient n'est trouvé
        PreparedStatement pst = conn.prepareStatement("SELECT id FROM user WHERE lastname = ?");
        pst.setString(1, lastName);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            patientId = rs.getInt("id");
        }
        return patientId;
    }

    @FXML
    void modifier(ActionEvent event) {
        try {
            LocalDate admissionDate = choixdate_admission.getValue();
            LocalDate sortieDate = choixdate_sortie.getValue();

            // Vérifier que la date d'admission est supérieure ou égale à la date actuelle
            LocalDate currentDate = LocalDate.now();
            if (admissionDate.isBefore(currentDate)) {
                JOptionPane.showMessageDialog(null, "La date d'admission doit être supérieure ou égale à la date actuelle.");
                return;
            }

            // Vérifier que tous les champs sont remplis
            if (combochambre.getValue() == null || combopatient.getValue() == null || admissionDate == null) {
                JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs.");
                return;
            }

            if (admissionDate.isAfter(sortieDate)) {
                JOptionPane.showMessageDialog(null, "La date d'admission doit être antérieure à la date de sortie.");
                return;
            }

            String chambreLibelle = combochambre.getValue().getLibelle_chambre();
            int chambreId = findChambreId(chambreLibelle);

            PreparedStatement pst = conn.prepareStatement("UPDATE reservation_chambre SET chambre_id = ?, patient_id = ?, date_admission = ?, date_sortie = ? WHERE id = ?");
            pst.setInt(1, chambreId);
            pst.setInt(2, getPatientIdByLastName(combopatient.getValue().getLastname()));
            pst.setDate(3, java.sql.Date.valueOf(admissionDate));
            if (sortieDate != null) {
                pst.setDate(4, java.sql.Date.valueOf(sortieDate));
            } else {
                pst.setNull(4, java.sql.Types.DATE);
            }

            // Check if we need to increment the capacity of the room
            ReservationChambre selectedRes = tablereservation.getSelectionModel().getSelectedItem();
            boolean hadNullSortie = selectedRes.getDate_sortie() == null;
            boolean hasNonNullSortie = sortieDate != null;
            if (hadNullSortie && hasNonNullSortie) {
                updateCapacity(chambreId, 1);
            }

            int selectedResId = selectedRes.getId();
            pst.setInt(5, selectedResId);

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
    void selected(MouseEvent event
    ) {
        if (tablereservation == null || colidres == null || coladmission == null || colchambre == null || colpatient == null) {
            // Error: one or more required objects are null
            return;
        }

        int index = tablereservation.getSelectionModel().getSelectedIndex();
        if (index < 0 || index >= tablereservation.getItems().size()) {
            // Error: selected index is out of bounds
            return;
        }

        ReservationChambre res = tablereservation.getItems().get(index);
        Chambre chambre = res.getChambre();
        combochambre.setValue(chambre);
        Chambre selectedChambre = combochambre.getSelectionModel().getSelectedItem();
        int nomService = selectedChambre.getId();

        User patient = res.getPatient();
        combopatient.setValue(patient);
        User selectedPatient = combopatient.getSelectionModel().getSelectedItem();
        String nompatient = selectedPatient.getLastname();
        //textidreser.setText(String.valueOf(colidres.getCellData(index)));

        Date date = res.getDate_admission();
        if (date != null) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = format.format(date);
            LocalDate localDate = LocalDate.parse(formattedDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            choixdate_admission.setValue(localDate);
        }

        Date date_sortie = res.getDate_sortie();
        if (date_sortie != null) {
            SimpleDateFormat aa = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDatesortie = aa.format(date_sortie);
            LocalDate aaa = LocalDate.parse(formattedDatesortie, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            choixdate_sortie.setValue(aaa);
        }
    }

    @FXML
    void supprimerservice(ReservationChambre rese) {
        try {
            int selectedResId = rese.getId();
            Date dateSortie = rese.getDate_sortie();

            // Récupérer la chambre associée à la réservation
            PreparedStatement pst = conn.prepareStatement("SELECT chambre_id FROM reservation_chambre WHERE id = ?");
            pst.setInt(1, selectedResId);
            ResultSet rs = pst.executeQuery();
            rs.next();
            int chambreId = rs.getInt("chambre_id");

            // Supprimer la réservation
            pst = conn.prepareStatement("DELETE FROM reservation_chambre WHERE id = ?");
            pst.setInt(1, selectedResId);
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "Suppression réussite");

                // Si la date de sortie est null, incrémenter la capacité de la chambre
                if (dateSortie == null) {
                    updateCapacity(chambreId, 1);
                }

                table(); // update the table view
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void choixpatient() {
        try {
            pst = conn.prepareStatement("SELECT id, lastname FROM user WHERE roles = '[\"ROLE_PATIENT\"]'");
            ResultSet rs = pst.executeQuery();

            List<User> patients = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("id");
                String lastname = rs.getString("lastname");
                User patient = new User(id, lastname);
                patients.add(patient);
            }

            combopatient.setItems(FXCollections.observableList(patients));

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void choixchambre() {
        try {
            pst = conn.prepareStatement("SELECT id, libelle_chambre FROM chambre");
            ResultSet rs = pst.executeQuery();

            // Create a list of Chambre objects
            List<Chambre> chambres = new ArrayList<>();

            // Loop through the results of the query and add each chambre to the list
            while (rs.next()) {
                int id = rs.getInt("id");

                String libelle = rs.getString("libelle_chambre");
                Chambre chambre = new Chambre(id, libelle);
                chambres.add(chambre);
            }

            // Set the items of the ComboBox to the list of Chambre objects
            combochambre.setItems(FXCollections.observableList(chambres));

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        choixchambre();
        choixpatient();
        table();
        seearchbar.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                chercherreservation(newValue);
            }
        });
        colfacture.setCellFactory(column -> {
            return new TableCell<ReservationChambre, Void>() {
                private final Button btn = new Button();

                {final Image image = new Image(getClass().getResource("pdf.png").toExternalForm());
                    final ImageView icon = new ImageView(image);
                    btn.setGraphic(icon);
                    btn.setOnAction((ActionEvent event) -> {
                        // Get the selected chambre from the table view
                        ReservationChambre rese = getTableView().getItems().get(getIndex());
                        PDFReservation a = new PDFReservation();
                        try {
                            a.createPDF(rese);
                            // Call the createPDF method with the selected chambre
                        } catch (SQLException ex) {
                            Logger.getLogger(ReservationpageController.class.getName()).log(Level.SEVERE, null, ex);
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

        colsupprimerrese.setCellFactory(column -> {
            return new TableCell<ReservationChambre, Void>() {
                private final Button btn = new Button();

                {
                    final Image image = new Image(getClass().getResource("c.png").toExternalForm());
                    final ImageView icon = new ImageView(image);
                    btn.setGraphic(icon);
                    btn.setOnAction((ActionEvent event) -> {
                        // Get the selected chambre from the table view
                        ReservationChambre rese = getTableView().getItems().get(getIndex());

                        // Call the supprimerchambre method with the selected chambre
                        supprimerservice(rese);
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
