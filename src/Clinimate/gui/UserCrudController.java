/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Clinimate.gui;

import Clinimate.entities.BilanMedical;
import Clinimate.entities.User;
import Clinimate.service.Users;
import Clinimate.utils.DataSource;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.cell.TextFieldTableCell;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.ByteMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.mysql.cj.MysqlConnection;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import javax.imageio.ImageIO;
import javax.lang.model.util.Types;

/**
 * FXML Controller class
 *
 * @author moham
 */
public class UserCrudController implements Initializable {

    Connection cnx = DataSource.getInstance().getCnx();

    @FXML
    private TableColumn<User, String> crud_email;
    @FXML
    private TableColumn<User, String> crud_nom;
    @FXML
    private TableColumn<User, String> crud_prenom;
    @FXML
    private TableColumn<User, String> crud_role;
    @FXML
    private TableColumn<User, String> crud_address;
    @FXML
    private TableColumn<User, Integer> crud_telephone;
    @FXML
    private TableView<User> table_show;
    @FXML
    private Button dashBoard;

    @FXML
    private ComboBox<String> RoleC;
    @FXML
    private Button generateQR;
    private int idc;
    User del = null;
    User mod = null;
    User bloq = null;
    private TextField seearchbar;
    @FXML
    private Button suppp;
    @FXML
    private Button bloque;
    @FXML
    private Button debloque;
    @FXML
    private TextField searchR;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        selectLine();
        ObservableList<String> list = FXCollections.observableArrayList("[\"ROLE_ADMIN\"]", "[\"ROLE_PATIENT\"]");
        RoleC.setItems(list);
        ComboBox<String> role_box = new ComboBox<>();
        role_box.getItems().add("[\"ROLE_ADMIN\"]");
        role_box.getItems().add("[\"ROLE_PATIENT\"]");
        User_Show();
        searchUsers();
    }

    public void searchUsers() {
        String keyword = searchR.getText();
        FilteredList<User> filteredList = new FilteredList<>(CrudUser, p -> true);
        filteredList.setPredicate(user -> {
            if (keyword == null || keyword.isEmpty()) {
                return true;
            }
            String lowerCaseKeyword = keyword.toLowerCase();
            if (String.valueOf(user.getTelephone()).contains(lowerCaseKeyword)) {
                return true;
            }
            if (user.getFirstname().toLowerCase().contains(lowerCaseKeyword)) {
                return true;
            }
            if (user.getLastname().toLowerCase().contains(lowerCaseKeyword)) {
                return true;
            }
            if (user.getRole().toLowerCase().contains(lowerCaseKeyword)) {
                return true;
            }
            if (user.getAddress().toLowerCase().contains(lowerCaseKeyword)) {
                return true;
            }
            if (user.getEmail().toLowerCase().contains(lowerCaseKeyword)) {
                return true;
            }
            return false;
        });
        SortedList<User> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(table_show.comparatorProperty());
        table_show.setItems(sortedList);
    }

    private void selectLine() {
        table_show.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                User L = table_show.getItems().get(table_show.getSelectionModel().getSelectedIndex());

                RoleC.setValue(L.getRole());

            }
        }
        );
    }

    public ObservableList<User> CrudUserListe() {
        ObservableList<User> liste = FXCollections.observableArrayList();
        String selectData = "SELECT * FROM User";
        try {
            PreparedStatement pst = cnx.prepareStatement(selectData);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                User sData;
                sData = new User(rs.getInt("id"), rs.getString("email"), rs.getString("roles"), rs.getString("firstname"), rs.getString("lastname"), rs.getString("address"), rs.getInt("telephone"));
                liste.add(sData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return liste;
    }
    private ObservableList<User> CrudUser;

    public void User_Show() {
        ObservableList<String> list = FXCollections.observableArrayList("[\"ROLE_ADMIN\"]", "[\"ROLE_PATIENT\"]");

        CrudUser = CrudUserListe();
        crud_email.setCellValueFactory(new PropertyValueFactory<>("Email"));
        crud_email.setEditable(true);
        crud_email.setCellFactory(TextFieldTableCell.forTableColumn());
        crud_email.setOnEditCommit((TableColumn.CellEditEvent<User, String> t) -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setEmail(t.getNewValue());
            modifier((User) t.getTableView().getItems().get(t.getTablePosition().getRow()));
        });

        crud_nom.setCellValueFactory(new PropertyValueFactory<>("Firstname"));
        crud_nom.setEditable(true);
        crud_nom.setCellFactory(TextFieldTableCell.forTableColumn());
        crud_nom.setOnEditCommit((TableColumn.CellEditEvent<User, String> t) -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setFirstname(t.getNewValue());
            modifier((User) t.getTableView().getItems().get(t.getTablePosition().getRow()));
        });

        crud_prenom.setCellValueFactory(new PropertyValueFactory<>("Lastname"));
        crud_prenom.setEditable(true);
        crud_prenom.setCellFactory(TextFieldTableCell.forTableColumn());
        crud_prenom.setOnEditCommit((TableColumn.CellEditEvent<User, String> t) -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setLastname(t.getNewValue());
            modifier((User) t.getTableView().getItems().get(t.getTablePosition().getRow()));
        });

        crud_role.setCellValueFactory(new PropertyValueFactory<>("Role"));
        crud_role.setCellFactory(col -> {
            TableCell<User, String> cell = new TableCell<User, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {

                        if (item.equals("[\"ROLE_ADMIN\"]")) {
                            setText("Admin");
                        } else if (item.equals("[\"ROLE_PATIENT\"]")) {
                            setText("Patient");
                        } else if (item.equals("[\"ROLE_PHARMACIEN\"]")) {
                            setText("Pharmacien");
                        } else if (item.equals("[\"ROLE_Doctor\"]")) {
                            setText("Medecin");
                        } else {
                            setText(item);
                        }
                    }
                }
            };
            return cell;
        });

        crud_address.setCellValueFactory(new PropertyValueFactory<>("Address"));
        crud_address.setEditable(true);
        crud_address.setCellFactory(TextFieldTableCell.forTableColumn());
        crud_address.setOnEditCommit((TableColumn.CellEditEvent<User, String> t) -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setAddress(t.getNewValue());
            modifier((User) t.getTableView().getItems().get(t.getTablePosition().getRow()));
        });

        crud_telephone.setCellValueFactory(new PropertyValueFactory<>("Telephone"));
        crud_telephone.setEditable(true);
        crud_telephone.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        crud_telephone.setOnEditCommit((TableColumn.CellEditEvent<User, Integer> t) -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setTelephone(t.getNewValue());
            modifier((User) t.getTableView().getItems().get(t.getTablePosition().getRow()));
        });

    }

    @FXML

    private void backB(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dash.fxml"));
        Parent root = loader.load();
        dashBoard.getScene().setRoot(root);
    }

    private void modifier(User user) {
        try {

            mod = table_show.getSelectionModel().getSelectedItem();
            String UpdateData = "UPDATE User SET email = ?, roles = ?, firstname = ?, lastname = ?, address = ?, telephone = ? WHERE id = ?";
            PreparedStatement upd = cnx.prepareStatement(UpdateData);
            upd.setString(1, user.getEmail());
            upd.setString(2, user.getRole());
            upd.setString(3, user.getFirstname());
            upd.setString(4, user.getLastname());
            
            upd.setString(5, user.getAddress());
            upd.setInt(6, user.getTelephone());
            upd.setInt(7, mod.getId());
            upd.executeUpdate();
            User_Show();

            System.out.println("Successful update");

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
            User qr = table_show.getSelectionModel().getSelectedItem();
            ByteMatrix matrix = null;
            String data = "User email =" + qr.getEmail() + "\n Nom = " + qr.getLastname() + "\n Prenom = " + qr.getFirstname() + "\nAddresse = " + qr.getAddress();
            matrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height, new Hashtable<EncodeHintType, Object>(hintMap));
            BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
            String fileName = file.getName().concat(".png");
            ImageIO.write(image, "png", new File(file.getParent(), fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void DeleteB(ActionEvent event) {
        try {
            del = table_show.getSelectionModel().getSelectedItem();
            String deleteQuery = "DELETE FROM User WHERE id = ? ";
            PreparedStatement pst = cnx.prepareStatement(deleteQuery);
            pst.setInt(1, del.getId());
            pst.executeUpdate();
            User_Show();
        searchUsers();

            System.out.println("Successful DELETE");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void bloqueB(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Message de Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Etes-vous sur de vouloir bloquer ?");
        Optional<ButtonType> option = alert.showAndWait();

        // Check if the user clicked the OK button
        if (option.isPresent() && option.get() == ButtonType.OK) {
            try {
                bloq = table_show.getSelectionModel().getSelectedItem();
                String Updatebloq = "UPDATE User SET ban = ? WHERE id = ?";
                PreparedStatement bl = cnx.prepareStatement(Updatebloq);
                bl.setInt(1, 1);

                bl.setInt(2, bloq.getId());
                bl.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void debloqueB(ActionEvent event
    ) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Message de Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Etes-vous sur de vouloir debloquer ?");
        Optional<ButtonType> option = alert.showAndWait();

        // Check if the user clicked the OK button
        if (option.isPresent() && option.get() == ButtonType.OK) {
            try {
                bloq = table_show.getSelectionModel().getSelectedItem();
                String Updatebloq = "UPDATE User SET ban = ? WHERE id = ?";
                PreparedStatement bl = cnx.prepareStatement(Updatebloq);
                bl.setNull(1, 0);
                bl.setInt(2, bloq.getId());
                bl.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    Users u = new Users();

    @FXML
    private void scR() {

        ObservableList<User> list = u.searchUser("");
        List<User> list1;
        list1 = list.stream()
                .filter((User us) -> us.getFirstname().length() >= searchR.getText().length())
                .filter(u -> u.getFirstname().substring(0, searchR.getText().length()).equalsIgnoreCase(searchR.getText()))
                .collect(Collectors.<User>toList());

        ObservableList<User> List = FXCollections.observableArrayList();
        list1.forEach((User us) -> List.add(us));

        table_show.setItems(List);

    }

}
