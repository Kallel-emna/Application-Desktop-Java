/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Clinimate.gui;

import Clinimate.entities.BilanMedical;
import Clinimate.entities.Consultation;
import Clinimate.entities.Ordonnance;
import Clinimate.entities.User;
import Clinimate.utils.DataSource;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.ByteMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.mysql.cj.xdevapi.Statement;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author medam
 */
public class AfficherOrdoController implements Initializable {

    Connection cnx = DataSource.getInstance().getCnx();
    @FXML
    private TableView<Ordonnance> tvordonnance;
    @FXML
    private TableColumn<Ordonnance, String> nompatient;
    @FXML
    private TableColumn<Ordonnance, String> date;
    @FXML
    private TableColumn<Ordonnance, String> medicament;
    @FXML
      private TableColumn<Consultation, String> consul;
    private TextField tfid;
    @FXML
    private TextField tfnom1;
    @FXML
    private DatePicker tfdate;
    @FXML
    private TextField tfmedicament;
    @FXML
    private Button btnajoutordo;
    @FXML
    private Button btnsupprordo;
    @FXML
    private ComboBox<Consultation> cbcons;
    PreparedStatement pst;
    @FXML
    private Button btnretour;
    @FXML
    private Button btnsupprordo1;
    @FXML
    private TextField recherche;
    @FXML
    private Button generateQR;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        selectLine();

        choixconsultation();
        table();
    }

    /* public ObservableList<Ordonnance> data() {
        ObservableList<Ordonnance> liste = FXCollections.observableArrayList();
        String selectData = "SELECT * FROM Ordonnance";
        try {
            PreparedStatement pst = cnx.prepareStatement(selectData);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Ordonnance sData;
                sData = new Ordonnance(rs.getInt("id"), rs.getString("nompatient"), rs.getDate("date"), rs.getString("medicament"),rs.getInt("consultation_id"));
                liste.add(sData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return liste;
    }
    private ObservableList<Ordonnance> CrudOrdonnance;

   /* public void afficherordonnance(){
        CrudOrdonnance =  CrudOrdonnanceListe();
        nompatient.setCellValueFactory(new PropertyValueFactory<>("nompatient"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        medicament.setCellValueFactory(new PropertyValueFactory<>("medicament"));
       consul.setCellValueFactory(new PropertyValueFactory<>("consultation_id"));
        tvordonnance.setItems(CrudOrdonnance);
    }*/
   public void table() {
        ObservableList<Ordonnance> ordonnance = FXCollections.observableArrayList();
     String selectData = "SELECT ordonnance.id, ordonnance.nompatient, ordonnance.date,ordonnance.medicament, consultation.notes "
                + "FROM ordonnance "
                + "INNER JOIN consultation ON ordonnance.consultation_id = consultation.id";
        try {
            PreparedStatement pst = cnx.prepareStatement(selectData);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
             int id = rs.getInt("id");
           
              String nom  = rs.getString("nompatient") ; 
              Date date = rs.getDate("date");
              String medi = rs.getString("medicament") ; 
           
              
      String nom1 = rs.getString("notes");             
                
                Ordonnance ordo = new Ordonnance();
                ordo.setId(id);
                ordo.setNompatient(nom);
            ordo.setDate(date);
                ordo.setMedicament(medi);
              
                
                
                Consultation cn = new Consultation();
                cn.setNotes(nom1);
                
             ordo.setC(cn);
             
            
                ordonnance.add(ordo);
             
            }
            nompatient.setCellValueFactory(new PropertyValueFactory<>("nompatient"));
            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            medicament.setCellValueFactory(new PropertyValueFactory<>("medicament"));
            consul.setCellValueFactory(new PropertyValueFactory<>("c"));
            tvordonnance.setItems(ordonnance);

            // Add a listener to the table to detect when a row is selected
            tvordonnance.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    String selectedcons = newSelection.getNompatient();
                    System.out.println("ordonnance sélectionné : " + selectedcons);
                }
            });

            // Add a cell factory to the table to make the cells editable
            nompatient.setCellFactory(TextFieldTableCell.forTableColumn());
            nompatient.setOnEditCommit((TableColumn.CellEditEvent<Ordonnance, String> t) -> {
                Ordonnance selectedcons = t.getRowValue();
                selectedcons.setNompatient(t.getNewValue());
                updateordo(selectedcons);
            });
            tvordonnance.setEditable(true);

            medicament.setCellFactory(TextFieldTableCell.forTableColumn());
            medicament.setOnEditCommit((TableColumn.CellEditEvent<Ordonnance, String> t) -> {
                Ordonnance selectedcons = t.getRowValue();
                selectedcons.setMedicament(t.getNewValue());
                updateordo(selectedcons);
            });

            // Enable editing of the table
            tvordonnance.setEditable(true);
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
        tvordonnance.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Ordonnance L = tvordonnance.getItems().get(tvordonnance.getSelectionModel().getSelectedIndex());
                tfid.setText(Integer.toString(L.getId()));
                tfnom1.setText(L.getNompatient());
                // Date date = Date.valueOf(tfdate.getValue()); 
                tfmedicament.setText(L.getMedicament());
                // Télécharger le PDF pour l'objet Ordonnance sélectionné
                downloadPDF(L);
            }

        }
        );
    }

    @FXML
    private void selectLinepdf() {
        tvordonnance.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Ordonnance L = tvordonnance.getItems().get(tvordonnance.getSelectionModel().getSelectedIndex());

                // Télécharger le PDF pour l'objet Ordonnance sélectionné
                downloadPDF(L);
            }

        }
        );
    }

    @FXML
    private void ajouterordo(ActionEvent event) throws IOException, SQLException {
        if (tfnom1.getText().isEmpty() || tfmedicament.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs !");
            alert.showAndWait();
            return;
        }

        Date date = Date.valueOf(tfdate.getValue());

        if (event.getSource() == btnajoutordo) {
            Ordonnance U = new Ordonnance();
            Ordonnance Us = new Ordonnance(tfnom1.getText(), date, tfmedicament.getText());
            String req = "INSERT INTO `Ordonnance` ( `consultation_id`,`nompatient`,`date`,`medicament`) VALUES (?,?,?,?)";
            try {
                System.out.println();
                PreparedStatement pst = cnx.prepareStatement(req);
                pst.setInt(1, cbcons.getValue().getId());
                pst.setString(2, Us.getNompatient());
                pst.setDate(3, (Date) Us.getDate());
                pst.setString(4, Us.getMedicament());

                pst.executeUpdate();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("AfficherOrdo.fxml"));
            Parent root = loader.load();
            btnajoutordo.getScene().setRoot(root);

        }
    }

    private void updateordo(Ordonnance ordonnance) {
        try {
            int ordId = ordonnance.getId();
            String nompatient = ordonnance.getNompatient();
            String medicament = ordonnance.getMedicament();
            PreparedStatement pst = cnx.prepareStatement("UPDATE Ordonnance SET nompatient = ?, medicament= ? WHERE id = ?");
            pst.setString(1, nompatient);
            pst.setString(2, medicament);
            pst.setInt(3, ordId);

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
            Ordonnance del = tvordonnance.getSelectionModel().getSelectedItem();
            String deleteQuery = "DELETE FROM Ordonnance WHERE id = ?";
            PreparedStatement pst = cnx.prepareStatement(deleteQuery);
            pst.setInt(1, del.getId());
            pst.executeUpdate();
            table();
            System.out.println("Successful DELETE");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void choixconsultation() {
        try {
            pst = cnx.prepareStatement("SELECT id, notes FROM Consultation");
            ResultSet rs = pst.executeQuery();

            // Create a list of Service objects
            List<Consultation> Consultation = new ArrayList<>();

            // Loop through the results of the query and add each service to the list
            while (rs.next()) {
                int id = rs.getInt("id");
                String notes = rs.getString("notes");
                Consultation consultation = new Consultation(id, notes);
                Consultation.add(consultation);
            }

            // Set the items of the ComboBox to the list of Service objects
            cbcons.setItems(FXCollections.observableList(Consultation));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void downloadPDF(Ordonnance ordonnance) {
        // Créer un document PDF
        Document document = new Document();
        try {
            // Ouvrir le sélecteur de fichiers pour sélectionner l'emplacement de téléchargement du PDF
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer l'ordonnance sous forme de PDF");
            // Ajouter l'extension ".pdf" au nom de fichier par défaut
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichiers PDF (.pdf)", ".pdf");
            fileChooser.getExtensionFilters().add(extFilter);
            File fileToSave = fileChooser.showSaveDialog(new Stage());

            if (fileToSave != null) {
                // Écrire les informations de l'ordonnance dans le document PDF
                PdfWriter.getInstance(document, new FileOutputStream(fileToSave));
                document.open();

                // Ajouter le nom de la clinique avec une police et une couleur spécifiques
                Paragraph nomClinique = new Paragraph("Clinimate", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLACK));
                nomClinique.setAlignment(Element.ALIGN_CENTER);
                nomClinique.setSpacingAfter(10);
                document.add(nomClinique);

// Ajouter un titre personnalisé avec une police et une couleur spécifiques
                Paragraph titre = new Paragraph("Ordonnance #" + ordonnance.getId(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLUE));
                titre.setAlignment(Element.ALIGN_CENTER);
                titre.setSpacingAfter(20);
                document.add(titre);

                // Créer une table personnalisée avec des bordures et des couleurs de fond spécifiques
                PdfPTable table = new PdfPTable(2);
                table.setWidthPercentage(100);
                table.setSpacingBefore(10);
                table.getDefaultCell().setPadding(5);
                table.getDefaultCell().setBorderWidth(1);
                table.getDefaultCell().setBorderColor(BaseColor.BLACK);
                table.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);

                PdfPCell cell1 = new PdfPCell(new Paragraph("Patient :", FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
                cell1.setPadding(5);
                cell1.setBorderColor(BaseColor.BLACK);
                cell1.setBackgroundColor(BaseColor.WHITE);

                PdfPCell cell2 = new PdfPCell(new Paragraph(ordonnance.getNompatient(), FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
                cell2.setPadding(5);
                cell2.setBorderColor(BaseColor.BLACK);
                cell2.setBackgroundColor(BaseColor.WHITE);

                PdfPCell cell3 = new PdfPCell(new Paragraph("Médicaments :", FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
                cell3.setPadding(5);
                cell3.setBorderColor(BaseColor.BLACK);
                cell3.setBackgroundColor(BaseColor.WHITE);

                PdfPCell cell4 = new PdfPCell(new Paragraph(ordonnance.getMedicament(), FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
                cell4.setPadding(5);
                cell4.setBorderColor(BaseColor.BLACK);
                cell4.setBackgroundColor(BaseColor.WHITE);

                table.addCell(cell1);
                table.addCell(cell2);
                table.addCell(cell3);
                table.addCell(cell4);

                document.add(table);

                // Ajouter une place pour la signature du docteur en bas du document
                Paragraph signature = new Paragraph("Signature du docteur :", FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK));
                signature.setAlignment(Element.ALIGN_RIGHT);
                signature.setSpacingBefore(20);
                document.add(signature);
// Ajouter une section avec les informations supplémentaires en bas du document PDF
                Paragraph infoSupplémentaires = new Paragraph();
                infoSupplémentaires.setAlignment(Element.ALIGN_BOTTOM | Element.ALIGN_LEFT);  // Aligner en bas à gauche
                infoSupplémentaires.setSpacingBefore(20);

                Chunk adresse = new Chunk("Adresse : 13 Cité Ettaamir\n", FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK));
                Chunk telephone = new Chunk("Téléphone : +216 99999999\n", FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK));
                Chunk email = new Chunk("E-mail : clinimate@esprit.tn\n", FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK));

                infoSupplémentaires.add(adresse);
                infoSupplémentaires.add(telephone);
                infoSupplémentaires.add(email);

                document.add(infoSupplémentaires);

            }

            // Fermer le document PDF
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /*private ObservableList<Ordonnance> data;
    @FXML
    private void rechercher(ActionEvent event) throws SQLException {
        String sql = "SELECT * FROM Ordonnance where nompatient = ?";
            pst = cnx.prepareStatement(sql);
            pst.setString(1, recherche.getText());
            ResultSet rs = pst.executeQuery();
          tvordonnance.setItems(data);

           
    }*/
    private ObservableList<Ordonnance> data;

    @FXML
    private void rechercher(ActionEvent event) throws SQLException {
        String sql = "SELECT * FROM Ordonnance where nompatient=?";
        try {
            pst = cnx.prepareStatement(sql);
            pst.setString(1, recherche.getText());
            ResultSet rs = pst.executeQuery();

            // Créer une nouvelle liste observable pour stocker les résultats de la recherche
            ObservableList<Ordonnance> resultatsRecherche = FXCollections.observableArrayList();

            // Ajouter chaque élément de ResultSet à la liste observable des résultats
            while (rs.next()) {
                Ordonnance ordonnance = new Ordonnance(rs.getInt("id"), rs.getString("nompatient"), rs.getDate("date"), rs.getString("medicament"), rs.getInt("consultation_id"));
                resultatsRecherche.add(ordonnance);
            }

            // Lier la liste observable des résultats à votre TableView
            tvordonnance.setItems(resultatsRecherche);
            ObservableList<Ordonnance> observableListEntitesOrdonnance = FXCollections.observableArrayList();

// Définir le prédicat de filtrage pour le FilteredList
            FilteredList<Ordonnance> filteredListEntitesOrdonnance = new FilteredList<>(observableListEntitesOrdonnance);
            recherche.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredListEntitesOrdonnance.setPredicate(entite -> {
                    // Si la saisie utilisateur est vide, afficher toutes les entités d'ordonnance
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    // Sinon, filtrer les entités d'ordonnance en fonction de la saisie utilisateur
                    String lowerCaseFilter = newValue.toLowerCase();
                    return entite.getNompatient().toLowerCase().contains(lowerCaseFilter);
                });
            });

// Lié la liste observable au FilteredList
            filteredListEntitesOrdonnance.addAll(data);
// Lier le FilteredList à votre TableView
            tvordonnance.setItems(filteredListEntitesOrdonnance);

// Définir les valeurs des colonnes
            nompatient.setCellValueFactory(new PropertyValueFactory<>("nompatient"));
            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            medicament.setCellValueFactory(new PropertyValueFactory<>("medicament"));
            consul.setCellValueFactory(new PropertyValueFactory<>("consultation_id"));

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
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
            Ordonnance qr = tvordonnance.getSelectionModel().getSelectedItem();
            ByteMatrix matrix = null;
            String data = "User Nom Patient  =" + qr.getNompatient() + "\n Medicament = " + qr.getMedicament();
            matrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height, new Hashtable<EncodeHintType, Object>(hintMap));
            BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
            String fileName = file.getName().concat(".png");
            ImageIO.write(image, "png", new File(file.getParent(), fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void Trie_desc() {
        ObservableList<Ordonnance> b = tvordonnance.getItems();
        b.sort(Comparator.comparingInt(Ordonnance::getId).reversed());
        tvordonnance.setItems(b);

    }

    @FXML
    public void table1() {
        ObservableList<Ordonnance> ordonnance = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM Ordonnance ORDER BY medicament";
            pst = cnx.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                ordonnance.add(new Ordonnance(rs.getInt("id"), rs.getString("nompatient"), rs.getDate("date"), rs.getString("medicament"), rs.getInt("consultation_id")));
            }
            nompatient.setCellValueFactory(new PropertyValueFactory<>("nompatient"));
            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            medicament.setCellValueFactory(new PropertyValueFactory<>("medicament"));
            consul.setCellValueFactory(new PropertyValueFactory<>("consultation_id"));
            tvordonnance.setItems(ordonnance);

            // Add a listener to the table to detect when a row is selected
            tvordonnance.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    String selectedcons = newSelection.getNompatient();
                    System.out.println("ordonnance sélectionné : " + selectedcons);
                }
            });

            // Add a cell factory to the table to make the cells editable
            nompatient.setCellFactory(TextFieldTableCell.forTableColumn());
            nompatient.setOnEditCommit((TableColumn.CellEditEvent<Ordonnance, String> t) -> {
                Ordonnance selectedcons = t.getRowValue();
                selectedcons.setNompatient(t.getNewValue());
                updateordo(selectedcons);
            });
            tvordonnance.setEditable(true);

            medicament.setCellFactory(TextFieldTableCell.forTableColumn());
            medicament.setOnEditCommit((TableColumn.CellEditEvent<Ordonnance, String> t) -> {
                Ordonnance selectedcons = t.getRowValue();
                selectedcons.setMedicament(t.getNewValue());
                updateordo(selectedcons);
            });

            // Enable editing of the table
            tvordonnance.setEditable(true);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ObservableList<Ordonnance> search2(String searchTerm) {
        {

            ObservableList<Ordonnance> liste = FXCollections.observableArrayList();
            String selectData = "SELECT * FROM Ordonnance";
            try {
                PreparedStatement pst = cnx.prepareStatement(selectData);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    Ordonnance sData = new Ordonnance(rs.getInt("id"), rs.getString("nompatient"), rs.getDate("date"), rs.getString("medicament"), rs.getInt("consultation_id"));
                    liste.add(sData);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return liste;
        }
    }

    @FXML

    private void FiltreKeyRealsead() {

        ObservableList<Ordonnance> list = search2("");
        List<Ordonnance> list1;
        list1 = list.stream()
                .filter((Ordonnance u) -> u.getNompatient().length() >= recherche.getText().length())
                .filter(u -> u.getNompatient().substring(0, recherche.getText().length()).equalsIgnoreCase(recherche.getText()))
                .collect(Collectors.<Ordonnance>toList());

        ObservableList<Ordonnance> List = FXCollections.observableArrayList();
        list1.forEach((Ordonnance u) -> List.add(u));

        tvordonnance.setItems(List);
    }
}
