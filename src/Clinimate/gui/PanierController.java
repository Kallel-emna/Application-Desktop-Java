/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Clinimate.gui;

import Clinimate.entities.Produit;
import Clinimate.service.ServiceProduit;
import Clinimate.utils.DataSource;
import com.mysql.cj.xdevapi.Statement;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author kalle
 */
public class PanierController implements Initializable {

    @FXML
    private AnchorPane card_form;
    @FXML
    private Label prod_name;
    @FXML
    private Label prod_price;
    @FXML
    private Spinner<Integer> prod_spinner;
    @FXML
    private Button prod_add;

    private Produit prodData;
    private String prodID;
    private String quantite;

    ServiceProduit pp = new ServiceProduit();
    PreparedStatement prepare;
    Statement statement;
    ResultSet result;
    private Alert alert;

    private SpinnerValueFactory<Integer> spin;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setQuantity();
    }

    public void setData(Produit prodData) {
        this.prodData = prodData;

        prodID = String.valueOf(prodData.getId());
        prod_name.setText(prodData.getNom_produit());
        prod_price.setText("TND" + String.valueOf(prodData.getPrix()));

        pr = prodData.getPrix();
    }

    private int qty;

    public void setQuantity() {
        spin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
        prod_spinner.setValueFactory(spin);
    }

    private double totalP;
    private double pr;

    @FXML
    public void addBtn() {

    CardProductController mForm = new CardProductController();
    mForm.customerID();

    int qty = prod_spinner.getValue();

    Connection cnx = DataSource.getInstance().getCnx();

    try {
        // Get current stock quantity of the product
        int currentStock = 0;
        String checkStockQuery = "SELECT quantite FROM produit WHERE id = ?";
        PreparedStatement checkStockStatement = cnx.prepareStatement(checkStockQuery);
        checkStockStatement.setString(1, prodID);
        ResultSet stockResult = checkStockStatement.executeQuery();
        if (stockResult.next()) {
            currentStock = stockResult.getInt("quantite");
        }

        if (qty == 0) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Invalid quantity");
            alert.showAndWait();
        } else if (currentStock == 0) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("This product is out of stock");
            alert.showAndWait();
        } else if (qty > currentStock) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Invalid. Only " + currentStock + " " + prod_name.getText() + " available");
            alert.showAndWait();
        } else {
            
            // Insert new cart item
            String insertData = "INSERT INTO panier (customer_id, prod_name, quantite, prix) VALUES (?, ?, ?, ?)";
            PreparedStatement insertStatement = cnx.prepareStatement(insertData);
            insertStatement.setString(1, String.valueOf(data.cID));
            insertStatement.setString(2, prod_name.getText());
            insertStatement.setInt(3, qty);
            double totalP = qty * pr;
            insertStatement.setDouble(4, totalP);
            insertStatement.executeUpdate();

            // Update product stock quantity
            int newStock = currentStock - qty;
            String updateStock = "UPDATE produit SET quantite = ?, prix = ? WHERE id = ?";
            PreparedStatement updateStatement = cnx.prepareStatement(updateStock);
            updateStatement.setInt(1, newStock);
            updateStatement.setDouble(2, pr);
            updateStatement.setString(3, prodID);
            updateStatement.executeUpdate();

            // Show success message and update cart total
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText("Successfully added to cart");
            alert.showAndWait();

            mForm.menuGetTotal();
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    
    
    
    
}
