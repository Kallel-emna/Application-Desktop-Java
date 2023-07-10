/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Clinimate.gui;

import Clinimate.entities.Categorie;
import Clinimate.entities.DossierMedical;
import Clinimate.entities.Ordonnance;
import Clinimate.entities.Produit;
import Clinimate.service.ServiceCategorie;
import Clinimate.service.ServiceProduit;
import Clinimate.utils.DataSource;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
import java.util.stream.Collectors;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import javax.swing.JOptionPane;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author kalle
 */
public class ProduiitController implements Initializable {

    @FXML
    private TableView<Produit> tableview_prod;
    @FXML
    private TableColumn<Produit, String> nom_produit;
    @FXML
    private TableColumn<Produit, String> quantite;
    @FXML
    private TableColumn<Produit, String> prix;
    @FXML
    private TableColumn<Categorie, String> categorie_id;
    @FXML
    private TextField nomprod;
    @FXML
    private Button ajouter_prod;
    @FXML
    private Button supp_prod;
    @FXML
    private TextField Quantite;
    @FXML
    private TextField prixx;
    @FXML
    private ComboBox<Categorie> cat;

    Connection cnx = DataSource.getInstance().getCnx();
    ServiceProduit pp = new ServiceProduit();
    PreparedStatement pst;
    Produit r = new Produit();
    PreparedStatement ste;
    @FXML
    private Button retour;
    @FXML
    private Button panier;
    @FXML
    private Button tri;
    @FXML
    private TextField recherche;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     //   Prod_Show();
     //   table();
     
     ObservableList<Produit> liste = FXCollections.observableArrayList();
        String selectData = "SELECT produit.id, produit.nom_produit, produit.quantite   , produit.prix, categorie.nom_cat "
                + "FROM produit "
                + "INNER JOIN categorie ON produit.categorie_id = categorie.id";

        try {
            PreparedStatement pst = cnx.prepareStatement(selectData);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");

                String nom = rs.getString("nom_produit");
                int q = rs.getInt("quantite");
                int prix = rs.getInt("prix");

                String nom_cat = rs.getString("nom_cat");

                Produit dossier = new Produit();
                dossier.setId(id);
                dossier.setNom_produit(nom);
                dossier.setQuantite(q);
                dossier.setPrix(prix);

                Categorie or = new Categorie();
                or.setNom_cat(nom_cat);

                dossier.setCat(or);

                liste.add(dossier);

            }  } catch (Exception e) {
            e.printStackTrace();
        }
       
            

         

                nom_produit.setCellValueFactory(new PropertyValueFactory<>("nom_produit"));
                quantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
                prix.setCellValueFactory(new PropertyValueFactory<>("prix"));
                categorie_id.setCellValueFactory(new PropertyValueFactory<>("cat"));
                tableview_prod.setItems(liste);

        nom_produit.setEditable(true);
        nom_produit.setCellFactory(TextFieldTableCell.forTableColumn());
        nom_produit.setOnEditCommit((TableColumn.CellEditEvent<Produit, String> t) -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setNom_produit(t.getNewValue());
            modifier((Produit) t.getTableView().getItems().get(t.getTablePosition().getRow()));
        });
        /*
        quantite.setEditable(true);
        quantite.setCellFactory(TextFieldTableCell.forTableColumn());
        quantite.setOnEditCommit((TableColumn.CellEditEvent<Produit, String> t) -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setNom_produit(t.getNewValue());
            modifier((Produit) t.getTableView().getItems().get(t.getTablePosition().getRow()));
        });
        
        prix.setEditable(true);
        prix.setCellFactory(TextFieldTableCell.forTableColumn());
        prix.setOnEditCommit((TableColumn.CellEditEvent<Produit, String> t) -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setNom_produit(t.getNewValue());
            modifier((Produit) t.getTableView().getItems().get(t.getTablePosition().getRow()));
        });*/

        try {
            pst = cnx.prepareStatement("SELECT id, nom_cat FROM Categorie");
            ResultSet rs = pst.executeQuery();
            List<Categorie> u = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom_cat = rs.getString("nom_cat");
                Categorie categorie = new Categorie(id, nom_cat);
                u.add(categorie);

            }
            cat.setItems(FXCollections.observableList(u));

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    Produit mod = null;

    private void modifier(Produit produit) {
        try {

            mod = tableview_prod.getSelectionModel().getSelectedItem();
            String UpdateData = "UPDATE produit SET nom_produit = ?, quantite = ?, prix = ? WHERE id = ?";
            PreparedStatement upd = cnx.prepareStatement(UpdateData);
            upd.setString(1, produit.getNom_produit());
            upd.setInt(2, produit.getQuantite());
            upd.setInt(3, produit.getPrix());
            upd.setInt(4, mod.getId());
            upd.executeUpdate();
            Prod_Show();
            System.out.println("Successful update");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Prod_Show() {
        nom_produit.setCellValueFactory(new PropertyValueFactory<>("nom_produit"));
        quantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        prix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        categorie_id.setCellValueFactory(new PropertyValueFactory<>("categorie_id"));

        tableview_prod.getItems().setAll(pp.afficherProduit());
    }

    @FXML
    private void Ajouter_prod(javafx.event.ActionEvent event) {
        if (nomprod.getText().isEmpty() || Quantite.getText().isEmpty() || prixx.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs !");
            alert.showAndWait();

            return;
        }

        String nom = "[a-zA-Z]+";
        Pattern pa = Pattern.compile(nom, Pattern.CASE_INSENSITIVE);
        Matcher ma = pa.matcher(nomprod.getText());
        if (!ma.matches()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez saisir un nom valide !");
            alert.showAndWait();

            return;
        }
        String qtite = "[a-zA-Z4-9]+";
        Pattern pattern = Pattern.compile(qtite, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(Quantite.getText());
        if (!matcher.matches()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez saisir une quantité valide !");
            alert.showAndWait();

            String title = "Veuillez saisir quatite > 3 !";
            String message = Quantite.getText();
            TrayNotification tray = new TrayNotification();
            AnimationType type = AnimationType.POPUP;

            tray.setAnimationType(type);
            tray.setTitle(title);
            tray.setMessage(message);
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(5000));

            return;
        }
        String prx = "[a-zA-Z0-9 ]+";
        Pattern p = Pattern.compile(prx, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(prixx.getText());
        if (!m.matches()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez saisir un prix valide !");
            alert.showAndWait();
            return;
        }
        r.setNom_produit(nomprod.getText());
        r.setQuantite(Integer.parseInt(Quantite.getText()));
        r.setPrix(Integer.parseInt(prixx.getText()));

        r.setCategorie_id(cat.getValue().getId());

        pp.ajouterProduit(r);
        tableview_prod.getItems().setAll(pp.afficherProduit());

    }

    @FXML
    private void Supprimer_prod(javafx.event.ActionEvent event) {
        Produit selecteditem = (Produit) tableview_prod.getSelectionModel().getSelectedItem();
        pp.supprimerProduit(selecteditem);
        tableview_prod.getItems().setAll(pp.afficherProduit());
    }

/*
    public void table() {
        ObservableList<Produit> liste = FXCollections.observableArrayList();
        String selectData = "SELECT produit.id, produit.nom_produit, produit.quantite   , produit.prix, categorie.nom_cat "
                + "FROM produit "
                + "INNER JOIN categorie ON produit.categorie_id = categorie.id";

        try {
            PreparedStatement pst = cnx.prepareStatement(selectData);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");

                String nom = rs.getString("nom_produit");
                int q = rs.getInt("quantite");
                int prix = rs.getInt("prix");

                String nom_cat = rs.getString("nom_cat");

                Produit dossier = new Produit();
                dossier.setId(id);
                dossier.setNom_produit(nom_cat);
                dossier.setQuantite(q);
                dossier.setPrix(prix);

                Categorie or = new Categorie();
                or.setNom_cat(nom_cat);

                dossier.setCat(or);

                liste.add(dossier);

            }

         

                nom_produit.setCellValueFactory(new PropertyValueFactory<>("nom_produit"));
                quantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
                prix.setCellValueFactory(new PropertyValueFactory<>("prix"));
                categorie_id.setCellValueFactory(new PropertyValueFactory<>("cat"));
                tableview_prod.setItems(liste);

                // Add a listener to the table to detect when a row is selected
                tableview_prod.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        String selectedprod = newSelection.getNom_produit();
                        System.out.println("Catégorie sélectionné : " + selectedprod);
                    }
                });

                // Add a cell factory to the table to make the cells editable
                nom_produit.setCellFactory(TextFieldTableCell.forTableColumn());
                nom_produit.setOnEditCommit((TableColumn.CellEditEvent<Produit, String> t) -> {
                    Produit selectedCat = t.getRowValue();
                    selectedCat.setNom_produit(t.getNewValue());
                    updateProduit(selectedCat);
                });

                tableview_prod.setEditable(true);
                
                quantite.setCellFactory(TextFieldTableCell.forTableColumn());
                quantite.setOnEditCommit((TableColumn.CellEditEvent<Produit, String> t) -> {
                    Produit selectedCat = t.getRowValue();
                    selectedCat.setQuantite(Integer.valueOf(t.getNewValue()));
                    updateProduit(selectedCat);
                });
                tableview_prod.setEditable(true);
                
                prix.setCellFactory(TextFieldTableCell.forTableColumn());
                prix.setOnEditCommit((TableColumn.CellEditEvent<Produit, String> t) -> {
                    Produit selectedCat = t.getRowValue();
                    selectedCat.setQuantite(Integer.valueOf(t.getNewValue()));
                    updateProduit(selectedCat);
                });
                // Enable editing of the table
                tableview_prod.setEditable(true);

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

 

    private void updateProduit(Produit produit) {
        int ProdId = produit.getId();
        String ProdName = produit.getNom_produit();
        int Prodqtte = produit.getQuantite();
        int Prodprix = produit.getPrix();

        try {
            String req = " UPDATE produit SET  nom_produit`='" + ProdName + "',quantite`='" + Prodqtte + "',`prix`='" + Prodprix + "' WHERE `id`='" + ProdId + "' ";
            Statement st = cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("Bilan modifie avec succes");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
*/
    @FXML
    private void Retour(javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Pharmacien.fxml"));
        Parent root = loader.load();
        retour.getScene().setRoot(root);
    }

    @FXML
    private void panier(javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("cardProduct.fxml"));
        Parent root = loader.load();
        panier.getScene().setRoot(root);
    }

    @FXML
    private void Trie(javafx.event.ActionEvent event) {
        ObservableList<Produit> b = tableview_prod.getItems();
        b.sort(Comparator.comparingInt(Produit::getId).reversed());
        tableview_prod.setItems(b);
    }

  

    @FXML
    private void FiltreKeyRealsead(KeyEvent event) {
        
          ObservableList<Produit> list = pp.search2("");
        List<Produit> list1;
        list1 = list.stream()
                .filter((Produit u) -> u.getNom_produit().length() >= recherche.getText().length())
                .filter(u -> u.getNom_produit().substring(0, recherche.getText().length()).equalsIgnoreCase(recherche.getText()))
                .collect(Collectors.<Produit>toList());

        ObservableList<Produit> List = FXCollections.observableArrayList();
        list1.forEach((Produit u) -> List.add(u));

        tableview_prod.setItems(List);
    }

}
