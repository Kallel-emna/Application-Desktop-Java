/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Clinimate.gui;

import Clinimate.entities.Calendar;
import Clinimate.entities.Produit;
import Clinimate.service.ServiceProduit;
import Clinimate.utils.DataSource;
import com.mysql.cj.xdevapi.Statement;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.geometry.Insets;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import java.sql.ResultSet;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
//import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Element;

import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.zxing.WriterException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.ByteMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.EnumMap;
import java.util.Map;
import javax.imageio.ImageIO;

//import com.google.zxing.common.ByteMatrix;
/**
 * FXML Controller class
 *
 * @author kalle
 */
public class CardProductController implements Initializable {

    @FXML
    private ScrollPane menu_scrollPane;
    @FXML
    private GridPane menu_gridPane;

    @FXML
    private Label total;
    @FXML
    private TextField montant;
    @FXML
    private Label change;
    @FXML
    private Button payer;

    private Alert alert;
    Connection cnx = DataSource.getInstance().getCnx();
    ServiceProduit pp = new ServiceProduit();
    PreparedStatement prepare;
    Statement statement;
    ResultSet result;

    private ObservableList<Produit> cardListData = FXCollections.observableArrayList();
    @FXML
    private Button retour;
    @FXML
    private Button QR_code;
    @FXML
    private Button PDF;
    @FXML
    private TableView<Produit> menu_tableview;
    @FXML
    private TableColumn<Produit, String> nom_produit;
    @FXML
    private TableColumn<Produit, String> quantite;
    @FXML
    private TableColumn<Produit, String> prix;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        menuDisplayCard();
        menuGetOrder();
        menuShowOrderData();
        //Prod_Show();
        menuDisplayTotal();

    }

    private int getid;

    public void menuSelectOrder() {
        Produit prod = menu_tableview.getSelectionModel().getSelectedItem();
        int num = menu_tableview.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }
        getid = prod.getId();
    }

    public ObservableList<Produit> menuGetData() {

        String sql = "SELECT * FROM produit";

        ObservableList<Produit> listData = FXCollections.observableArrayList();
        //cnx = database.connectDB();

        try {
            prepare = cnx.prepareStatement(sql);
            result = prepare.executeQuery();

            Produit prod;

            while (result.next()) {
                prod = new Produit(result.getInt("id"),
                        result.getString("nom_produit"),
                        result.getInt("quantite"),
                        result.getInt("prix"),
                        result.getInt("categorie_id"));

                listData.add(prod);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    public void menuDisplayCard() {

        cardListData.clear();
        cardListData.addAll(menuGetData());

        int row = 0;
        int column = 0;

        menu_gridPane.getChildren().clear();
        menu_gridPane.getRowConstraints().clear();
        menu_gridPane.getColumnConstraints().clear();

        for (int q = 0; q < cardListData.size(); q++) {

            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("panier.fxml"));
                AnchorPane pane = load.load();
                PanierController cardC = load.getController();
                cardC.setData(cardListData.get(q));

                if (column == 3) {
                    column = 0;
                    row += 1;
                }

                menu_gridPane.add(pane, column++, row);
                GridPane.setMargin(pane, new Insets(10));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void Retour(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Produiit.fxml"));
        Parent root = loader.load();
        retour.getScene().setRoot(root);
    }

    public ObservableList<Produit> menuGetOrder() {
        customerID();
        ObservableList<Produit> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM produit ";
        try {
            prepare = cnx.prepareStatement(sql);
            result = prepare.executeQuery();

            Produit prod;
            while (result.next()) {
                prod = new Produit(result.getInt("id"),
                        result.getString("nom_produit"),
                        result.getInt("quantite"),
                        result.getInt("prix"),
                        result.getInt("categorie_id"));
                listData.add(prod);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<Produit> menuOrderListData;

    public void menuShowOrderData() {
        menuOrderListData = menuGetOrder();

        nom_produit.setCellValueFactory(new PropertyValueFactory<>("nom_produit"));
        quantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        prix.setCellValueFactory(new PropertyValueFactory<>("prix"));

        menu_tableview.setItems(menuOrderListData);
    }

    private double totalP;

    public void menuGetTotal() {
        customerID();
        String total = "SELECT SUM(prix) FROM panier WHERE customer_id = " + cID;

        try {

            prepare = cnx.prepareStatement(total);
            result = prepare.executeQuery();

            if (result.next()) {
                totalP = result.getInt("SUM(prix)");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void menuDisplayTotal() {
        menuGetTotal();
        total.setText("TND" + totalP);
    }

    private double amount;
    private double echange;

    @FXML
    public void menuAmount() {
        menuGetTotal();
        if (montant.getText().isEmpty() || totalP == 0) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Invalid ");
            alert.showAndWait();
        } else {
            amount = Double.parseDouble(montant.getText());
            if (amount < totalP) {
                montant.setText("");
            } else {
                echange = (amount - totalP);
                change.setText("TND" + echange);
            }
        }
    }

    @FXML
    public void menuPayBtn() {

        if (totalP == 0) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please choose your order first!");
            alert.showAndWait();
        } else {
            menuGetTotal();
            String insertPay = "INSERT INTO receipt (customer_id, total) "
                    + "VALUES(?,?)";

            try {

                if (amount == 0) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Messaged");
                    alert.setHeaderText(null);
                    alert.setContentText("Something wrong");
                    alert.showAndWait();
                } else {
                    alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure?");
                    Optional<ButtonType> option = alert.showAndWait();

                    if (option.get().equals(ButtonType.OK)) {
                        customerID();
                        menuGetTotal();
                        prepare = cnx.prepareStatement(insertPay);
                        prepare.setString(1, String.valueOf(cID));
                        prepare.setString(2, String.valueOf(totalP));

                        prepare.executeUpdate();

                        alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Infomation Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successful.");
                        alert.showAndWait();

                        menuShowOrderData();
                        //Prod_Show();
                        menuRestart();

                    } else {
                        alert = new Alert(AlertType.WARNING);
                        alert.setTitle("Infomation Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Cancelled.");
                        alert.showAndWait();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void menuRestart() {
        totalP = 0;
        echange = 0;
        amount = 0;
        total.setText("TND0.0");
        montant.setText("");
        change.setText("TND0.0");
    }

    private int cID;

    public void customerID() {

        String sql = "SELECT MAX(customer_id) FROM panier";

        try {
            prepare = cnx.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                cID = result.getInt("MAX(customer_id)");
            }

            String checkCID = "SELECT MAX(customer_id) FROM receipt";
            prepare = cnx.prepareStatement(checkCID);
            result = prepare.executeQuery();
            int checkID = 0;
            if (result.next()) {
                checkID = result.getInt("MAX(customer_id)");
            }

            if (cID == 0) {
                cID += 1;
            } else if (cID == checkID) {
                cID += 1;
            }

            data.cID = cID;

        } catch (Exception e) {
            e.printStackTrace();
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
            Produit qr = menu_tableview.getSelectionModel().getSelectedItem();
            ByteMatrix matrix = null;
            String data = "Nom Produit =" + qr.getNom_produit() + "\n Quantité = " + qr.getQuantite() + "\n Prix = " + qr.getPrix();
            matrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height, new Hashtable<EncodeHintType, Object>(hintMap));
            BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
            String fileName = file.getName().concat(".png");
            ImageIO.write(image, "png", new File(file.getParent(), fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void PDF(ActionEvent event) {

        menu_tableview.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                Produit L = menu_tableview.getItems().get(menu_tableview.getSelectionModel().getSelectedIndex());
                try {
                    downloadPDF(L);

                } catch (MalformedURLException ex) {
                    Logger.getLogger(CardProductController.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public void downloadPDF(Produit produit) throws MalformedURLException {
        // Créer un document PDF
        Document document = new Document();
        try {
            // Ouvrir le sélecteur de fichiers pour sélectionner l'emplacement de téléchargement du PDF
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer le produit sous forme de PDF");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichiers PDF (.pdf)", ".pdf");
            fileChooser.getExtensionFilters().add(extFilter);
            File fileToSave = fileChooser.showSaveDialog(new Stage());

            if (fileToSave != null) {
                // Écrire les informations de l'ordonnance dans le document PDF
                PdfWriter.getInstance(document, new FileOutputStream(fileToSave));
                document.open();

                Paragraph titre = new Paragraph("Produit " + produit.getId());
                titre.setAlignment(Element.ALIGN_CENTER);
                titre.setSpacingAfter(10);
                document.add(titre);

                PdfPTable table = new PdfPTable(2);
                table.setWidthPercentage(100);
                table.setSpacingBefore(10);

                /*// add image cell
                Image image = new Image(ImageDataFactory.create("C:/xampp/htdocs/PI_Desktop/Clinimate_Desktop/src/Clinimate/gui/CliniMate.jpg"));
                image.setAutoScale(true);
                image.setWidth(UnitValue.createPercentValue(50));
                //image.setHorizontalAlignment(com.itextpdf.layout.property.HorizontalAlignment.CENTER);
                document.add((Element) image);*/
 /* // add image cell
                Image img = new Image(ImageDataFactory.create("example.jpg")).setWidth(UnitValue.createPercentValue(50))
                .setHeight(UnitValue.createPercentValue(50));

                // Create a new cell instance and add the image to it
                  Cell cell = new Cell().add(img).setBorder(Border.NO_BORDER); */
                PdfPCell cell1 = new PdfPCell(new Paragraph("Nom :"));
                cell1.setPadding(10);
                cell1.setBorderColor(BaseColor.WHITE);
                PdfPCell cell2 = new PdfPCell(new Paragraph(produit.getNom_produit()));
                cell2.setPadding(10);
                cell2.setBorderColor(BaseColor.WHITE);

                PdfPCell cell3 = new PdfPCell(new Paragraph("Quantité :"));
                cell3.setPadding(10);
                cell3.setBorderColor(BaseColor.WHITE);
                PdfPCell cell4 = new PdfPCell(new Paragraph(String.valueOf(produit.getQuantite())));
                cell4.setPadding(10);
                cell4.setBorderColor(BaseColor.WHITE);

                PdfPCell cell5 = new PdfPCell(new Paragraph("Prix :"));
                cell5.setPadding(10);
                cell5.setBorderColor(BaseColor.WHITE);
                PdfPCell cell6 = new PdfPCell(new Paragraph(String.valueOf(produit.getPrix())));
                cell6.setPadding(10);
                cell6.setBorderColor(BaseColor.WHITE);

                table.addCell(cell1);
                table.addCell(cell2);
                table.addCell(cell3);
                table.addCell(cell4);
                table.addCell(cell5);
                table.addCell(cell6);

                document.add(table);
            }
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
