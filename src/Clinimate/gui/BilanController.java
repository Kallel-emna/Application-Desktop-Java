/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Clinimate.gui;

import Clinimate.entities.BilanMedical;
import Clinimate.entities.DossierMedical;
import Clinimate.entities.User;
import Clinimate.service.ServiceBilan;
import Clinimate.utils.DataSource;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.ByteMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javax.swing.JOptionPane;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
//import controlsfx.control.Notifications ; 

/**
 * FXML Controller class
 *
 * @author rania
 */
public class BilanController implements Initializable {

    ServiceBilan b = new ServiceBilan();
    ObservableList<BilanMedical> dataListe;
    private Connection conn;

    Connection cnx = DataSource.getInstance().getCnx();
    @FXML
    private TableView<BilanMedical> TableViewBilan;
    @FXML
    private TableColumn<BilanMedical, String> bilanAntecedent;
    @FXML
    private TableColumn<BilanMedical, String> BilanTaille;
    //  private TableColumn<BilanMedical, String> BilanPoid;
    @FXML
    private TableColumn<BilanMedical, String> BilanExamen;
    @FXML
    private TableColumn<BilanMedical, String> BilanImage;
    @FXML
    private TableColumn<User, String> BilanPatient;
    @FXML
    private TableColumn<DossierMedical, String> dossier;
    @FXML
    private TableColumn<BilanMedical, Void> btnS;

    @FXML
    private TableColumn<BilanMedical, String> Poid;
    //  private TextField ID;
    @FXML
    private TextField ANTECEDENTS;
    @FXML
    private TextField TAILLE;
    @FXML
    private TextField POIDS;
    @FXML
    private TextField EXAMENS;
    @FXML
    private TextField IMAGERIE;
    @FXML
    private TextField filterField;
    @FXML
    private Button buttonTrie;
    @FXML
    private ComboBox<User> cle;
    PreparedStatement pst;
    @FXML
    private ImageView image;
    @FXML
    private ComboBox<DossierMedical> dossier_combo;
    @FXML
    private Button retour;
    //    PreparedStatement pst;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //   selectLine();
        //    Afficher_Bilan();
        table();

        try {
            pst = cnx.prepareStatement("SELECT id, lastname FROM user");
            ResultSet rs = pst.executeQuery();

            // Create a list of Service objects
            List<User> Consultation = new ArrayList<>();

            // Loop through the results of the query and add each service to the list
            while (rs.next()) {
                int id = rs.getInt("id");
                String lastname = rs.getString("Lastname");
                User consultation = new User(id, lastname);
                Consultation.add(consultation);

            }
            cle.setItems(FXCollections.observableList(Consultation));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        /**
         * ******************************************************************
         */
        try {
            pst = cnx.prepareStatement("SELECT id, certificat FROM dossier_medical");
            ResultSet rs = pst.executeQuery();

            // Create a list of Service objects
            List<DossierMedical> Consultatio = new ArrayList<>();

            // Loop through the results of the query and add each service to the list
            while (rs.next()) {
                int ID = rs.getInt("id");
                String nc = rs.getString("certificat");
                DossierMedical consultatio = new DossierMedical(ID, nc);
                Consultatio.add(consultatio);

            }

            // Set the items of the ComboBox to the list of Service objects
            dossier_combo.setItems(FXCollections.observableList(Consultatio));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        btnS.setCellFactory(column -> {
            return new TableCell<BilanMedical, Void>() {
                private final Button btn = new Button();

                {
                    //  btn.setStyle("-fx-background-color: #11998e; -fx-background-radius: 30px; -fx-text-fill: white;-fx-font-weight:Bold;");
                    final Image image = new Image(getClass().getResource("c.png").toExternalForm());
                    final ImageView icon = new ImageView(image);
                    btn.setGraphic(icon);
                    btn.setOnAction((ActionEvent event) -> {
                        // Get the selected chambre from the table view
                        BilanMedical bi = getTableView().getItems().get(getIndex());

                        // Call the supprimerchambre method with the selected chambre
                        supprimerBilan(bi);
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

    BilanMedical r = new BilanMedical();
    //  User c = new User();

    public void Afficher_Bilan() {

        ObservableList<BilanMedical> fle = new ServiceBilan().AfficherBilan();
        System.err.println(fle.size());
        bilanAntecedent.setCellValueFactory(new PropertyValueFactory<>("antecedents"));
        BilanTaille.setCellValueFactory(new PropertyValueFactory<>("taille"));
        Poid.setCellValueFactory(new PropertyValueFactory<>("poids"));
        BilanExamen.setCellValueFactory(new PropertyValueFactory<>("examens_biologiques"));
        BilanImage.setCellValueFactory(new PropertyValueFactory<>("imagerie_medicale"));
        BilanPatient.setCellValueFactory(new PropertyValueFactory<>("user_id"));
        dossier.setCellValueFactory(new PropertyValueFactory<>("dossier_id"));

        TableViewBilan.setItems(fle);

    }

    /*
    private void selectLine() {
        TableViewBilan.setOnMouseClicked((MouseEvent event) -> {
            BilanMedical L = TableViewBilan.getItems().get(TableViewBilan.getSelectionModel().getSelectedIndex());

            ANTECEDENTS.setText(L.getAntecedents());
            TAILLE.setText(L.getTaille());
            POIDS.setText(L.getPoids());
            EXAMENS.setText(L.getExamens_biologiques());
            IMAGERIE.setText(L.getImagerie_medicale());
            //    cle.getValue().setId(L.getUser_id()); 
        });
    }
     */
    @FXML

    private void Supprimer_Bilan(ActionEvent event) {
        BilanMedical selecteditem = (BilanMedical) TableViewBilan.getSelectionModel().getSelectedItem();
        b.SupprimerBilan(selecteditem);
        TableViewBilan.getItems().setAll(b.AfficherBilan());
    }

    @FXML

    private void Modifier_Bilan(ActionEvent event) {

        if (ANTECEDENTS.getText().isEmpty() || TAILLE.getText().isEmpty() || POIDS.getText().isEmpty()
                || EXAMENS.getText().isEmpty() || IMAGERIE.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs !");
            alert.showAndWait();
            return;
        }
        BilanMedical selecteditem = (BilanMedical) TableViewBilan.getSelectionModel().getSelectedItem();
        b.ModifierBilan(ANTECEDENTS.getText(), TAILLE.getText(), POIDS.getText(), EXAMENS.getText(), IMAGERIE.getText(), cle.getValue().getId(), dossier_combo.getValue().getId(), selecteditem.getId());
        TableViewBilan.getItems().setAll(b.AfficherBilan());
    }

    @FXML
    void Ajouter_Bilan(ActionEvent event) {

        if (ANTECEDENTS.getText().isEmpty() || TAILLE.getText().isEmpty() || POIDS.getText().isEmpty()
                || EXAMENS.getText().isEmpty() || IMAGERIE.getText().isEmpty()) {
            String title = "Veuillez remplir tous les champs !";
            TrayNotification tray = new TrayNotification();
            AnimationType type = AnimationType.POPUP;

            tray.setAnimationType(type);
            tray.setTitle(title);
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(5000));

            return;
        }
        String antecedent = "[a-zA-Z0-9 ]+";
        Pattern pa = Pattern.compile(antecedent, Pattern.CASE_INSENSITIVE);
        Matcher ma = pa.matcher(ANTECEDENTS.getText());
        if (!ma.matches()) {

            String title = "Veuillez saisir cet antecedent valide !";
            String message = ANTECEDENTS.getText();
            TrayNotification tray = new TrayNotification();
            AnimationType type = AnimationType.POPUP;

            tray.setAnimationType(type);
            tray.setTitle(title);
            tray.setMessage(message);
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(5000));

            /*    Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez saisir un antecedent valide !");
            alert.showAndWait();*/
            return;

        }
        String taille = "[a-zA-Z0-9 ]+";
        Pattern pattern = Pattern.compile(taille, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(TAILLE.getText());
        if (!matcher.matches()) {
            String title = "Veuillez saisir cette taille valide !";
            String message = TAILLE.getText();
            TrayNotification tray = new TrayNotification();
            AnimationType type = AnimationType.POPUP;

            tray.setAnimationType(type);
            tray.setTitle(title);
            tray.setMessage(message);
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(5000));

            return;
        }
        String poid = "[a-zA-Z0-9 ]+";
        Pattern p = Pattern.compile(poid, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(POIDS.getText());
        if (!m.matches()) {
            String title = "Veuillez saisir ce poids valide !";
            String message = POIDS.getText();
            TrayNotification tray = new TrayNotification();
            AnimationType type = AnimationType.POPUP;

            tray.setAnimationType(type);
            tray.setTitle(title);
            tray.setMessage(message);
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(5000));

            return;
        }
        r.setAntecedents(ANTECEDENTS.getText());
        r.setTaille(TAILLE.getText());
        r.setPoids(POIDS.getText());
        r.setExamens_biologiques(EXAMENS.getText());
        r.setImagerie_medicale(IMAGERIE.getText());
        r.setUser_id(cle.getValue().getId());
        r.setDossier_id(dossier_combo.getValue().getId());
        b.ajouterBilan(r);
        TableViewBilan.getItems().setAll(b.AfficherBilan());
        String title = "Votre bilan a étè ajouté avec success !";
        String message = ANTECEDENTS.getText();
        TrayNotification tray = new TrayNotification();
        AnimationType type = AnimationType.POPUP;

        tray.setAnimationType(type);
        tray.setTitle(title);
        tray.setMessage(message);
        tray.setNotificationType(NotificationType.SUCCESS);
        tray.showAndDismiss(Duration.millis(5000));

    }

    @FXML

    private void Uploader_Image(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();

//Définir le titre de la fenêtre de sélection de fichier
        fileChooser.setTitle("Choisir une image");

//Ajouter un filtre pour ne montrer que les fichiers d'images
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Images", ".jpg", ".jpeg", "*.png");
        fileChooser.getExtensionFilters().add(imageFilter);

//Afficher la boîte de dialogue de sélection de fichier et attendre que l'utilisateur sélectionne un fichier
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // Enregistrer le nom de fichier dans une variable String
//    String fileName = selectedFile.getName();
            //   IMAGERIE.setText(fileName);

            try {
                Image value = new Image(new FileInputStream(selectedFile));
                image.setImage(value);

                String imagePath = "C:\\\\\\\\xamppp\\\\\\\\htdocs\\\\\\\\" + selectedFile.getName(); // chemin d'accès complet au fichier
                File outputFile = new File(imagePath);
                Files.createDirectories(outputFile.getParentFile().toPath()); // créer le répertoire si nécessaire
                Files.copy(selectedFile.toPath(), outputFile.toPath());
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        IMAGERIE.setText(selectedFile.getAbsolutePath());

    }

    @FXML

    private void FiltreKeyRealsead() {

        ObservableList<BilanMedical> list = b.search2("");
        List<BilanMedical> list1;
        list1 = list.stream()
                .filter((BilanMedical u) -> u.getTaille().length() >= filterField.getText().length())
                .filter(u -> u.getTaille().substring(0, filterField.getText().length()).equalsIgnoreCase(filterField.getText()))
                .collect(Collectors.<BilanMedical>toList());

        ObservableList<BilanMedical> List = FXCollections.observableArrayList();
        list1.forEach((BilanMedical u) -> List.add(u));

        TableViewBilan.setItems(List);
    }

    @FXML
    private void Trie_desc() {
        ObservableList<BilanMedical> b = TableViewBilan.getItems();
        b.sort(Comparator.comparingInt(BilanMedical::getId).reversed());
        TableViewBilan.setItems(b);

    }

    @FXML
    private void Home(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
        Parent root = loader.load();
        retour.getScene().setRoot(root);
    }

    public void table() {

        ObservableList<BilanMedical> liste = FXCollections.observableArrayList();
        String selectData = "SELECT b.id, b.antecedents, b.taille,  b.poids  ,b.examens_biologiques,b.imagerie_medicale, d.certificat, b.user_id, u.lastname "
                + "FROM bilan_medical b "
                + "JOIN dossier_medical d ON b.dossier_medical_id = d.id "
                + "JOIN user u ON b.user_id = u.id";
        try {
            PreparedStatement pst = cnx.prepareStatement(selectData);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");

                String an = rs.getString("antecedents");
                String taille = rs.getString("taille");
                String poids = rs.getString("poids");
                String ex = rs.getString("examens_biologiques");
                String image = rs.getString("imagerie_medicale");

                String certificat = rs.getString("certificat");
                int patient_id = rs.getInt("user_id");
                String lastname = rs.getString("lastname");

                BilanMedical bilan = new BilanMedical();
                bilan.setId(id);
                bilan.setAntecedents(an);
                bilan.setTaille(taille);
                bilan.setPoids(poids);
                bilan.setExamens_biologiques(ex);
                bilan.setImagerie_medicale(image);

                DossierMedical dossier = new DossierMedical();
                dossier.setCertificat(certificat);

                User patient = new User();
                patient.setEmail(lastname);

                bilan.setDossier(dossier);
                bilan.setUser(patient);

                liste.add(bilan);

            }
            //   colnom.setCellValueFactory(new PropertyValueFactory<>("nom_service"));
            bilanAntecedent.setCellValueFactory(new PropertyValueFactory<>("antecedents"));
            BilanTaille.setCellValueFactory(new PropertyValueFactory<>("taille"));
            Poid.setCellValueFactory(new PropertyValueFactory<>("poids"));
            BilanExamen.setCellValueFactory(new PropertyValueFactory<>("examens_biologiques"));
            BilanImage.setCellValueFactory(new PropertyValueFactory<>("imagerie_medicale"));
            BilanPatient.setCellValueFactory(new PropertyValueFactory<>("user"));
            dossier.setCellValueFactory(new PropertyValueFactory<>("dossier"));

            TableViewBilan.setItems(liste);

            // Add a listener to the table to detect when a row is selected
            TableViewBilan.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    String selectedService = newSelection.getAntecedents();
                    System.out.println("bilan sélectionné : " + selectedService);
                }
            });

            // Add a cell factory to the table to make the cells editable
            bilanAntecedent.setCellFactory(TextFieldTableCell.forTableColumn());
            bilanAntecedent.setOnEditCommit((TableColumn.CellEditEvent<BilanMedical, String> t) -> {
                BilanMedical selectedService = t.getRowValue();
                selectedService.setAntecedents(t.getNewValue());
                updateBilan(selectedService);
            });
            TableViewBilan.setEditable(true);
            //BilanTaille.setCellValueFactory(new PropertyValueFactory<>("taille"));
            BilanTaille.setCellFactory(TextFieldTableCell.forTableColumn());
            BilanTaille.setOnEditCommit((TableColumn.CellEditEvent<BilanMedical, String> t) -> {
                BilanMedical selectedServic = t.getRowValue();
                selectedServic.setTaille(t.getNewValue());
                updateBilan(selectedServic);
            });
            TableViewBilan.setEditable(true);
            Poid.setCellFactory(TextFieldTableCell.forTableColumn());
            Poid.setOnEditCommit((TableColumn.CellEditEvent<BilanMedical, String> t) -> {
                BilanMedical selectedServi = t.getRowValue();
                selectedServi.setPoids(t.getNewValue());
                updateBilan(selectedServi);
            });
            TableViewBilan.setEditable(true);
            BilanExamen.setCellFactory(TextFieldTableCell.forTableColumn());
            BilanExamen.setOnEditCommit((TableColumn.CellEditEvent<BilanMedical, String> t) -> {
                BilanMedical selectedServ = t.getRowValue();
                selectedServ.setExamens_biologiques(t.getNewValue());
                updateBilan(selectedServ);
            });
            TableViewBilan.setEditable(true);
            BilanImage.setCellFactory(TextFieldTableCell.forTableColumn());
            BilanImage.setOnEditCommit((TableColumn.CellEditEvent<BilanMedical, String> t) -> {
                BilanMedical selectedSe = t.getRowValue();
                selectedSe.setImagerie_medicale(t.getNewValue());
                updateBilan(selectedSe);
            });
            TableViewBilan.setEditable(true);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updateBilan(BilanMedical service) {
        int id = service.getId();
        String an = service.getAntecedents();
        String t = service.getTaille();
        String p = service.getPoids();
        String e = service.getExamens_biologiques();
        String m = service.getImagerie_medicale();
        //int user =service.getUser_id();
        int d = service.getDossier_id();
        try {

            String req = " UPDATE bilan_medical SET  antecedents`='" + an + "',taille`='" + t + "',`poids`='" + p + "', examens_biologiques`='" + e + "', imagerie_medicale` ='" + m + "' WHERE `id`='" + id + "' ";
            Statement st = cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("Bilan modifie avec succes");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void supprimerBilan(BilanMedical b) {
        try {
            // Get the ID of the selected chambre
            int id = b.getId();

            // Delete the selected chambre from the database
            pst = cnx.prepareStatement("DELETE FROM bilan_medical WHERE id = ?");
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

    @FXML
    private void generateQR(ActionEvent event) throws WriterException {
        // Création de l'objet QRCodeWriter
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        // Paramètres de l'encodeur
        int width = 300;
        int height = 300;
        //String fileType = "png";
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le QR code");
        File file = fileChooser.showSaveDialog(null);

        // Configuration des paramètres de l'encodeur
        Map<EncodeHintType, Object> hintMap = new HashMap<>();
        hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        try {
            BilanMedical qr = TableViewBilan.getSelectionModel().getSelectedItem();
            ByteMatrix matrix = null;
            String data = "Les informations de ce Bilan Medical Sont : \n Antecedents =" + qr.getAntecedents() + "\n Taille = " + qr.getTaille() + "\n Poids = " + qr.getPoids() + "\n Examens Biologiques = " + qr.getExamens_biologiques() + "\n Imagerie Medicale = " + qr.getImagerie_medicale();
            matrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height, new Hashtable<EncodeHintType, Object>(hintMap));
            BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
            String fileName = file.getName().concat(".png");
            ImageIO.write(image, "png", new File(file.getParent(), fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
