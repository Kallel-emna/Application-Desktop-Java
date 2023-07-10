package Clinimate.gui;

import Clinimate.entities.Calendar;
import Clinimate.entities.User;
import Clinimate.utils.DataSource;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.ByteMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.controlsfx.control.Notifications;

public class CrudCalendarController implements Initializable {

    @FXML
    private TableColumn<Calendar, Boolean> all_day;

    @FXML
    private TableColumn<Calendar, String> description;

    @FXML
    private TableColumn<Calendar, LocalDate> end;

    @FXML
    private TableColumn<Calendar, LocalDate> start;

    @FXML
    private TableView<Calendar> tableCalendrier;

    @FXML
    private TableColumn<Calendar, String> title;

    Connection conn = DataSource.getInstance().getCnx();
    PreparedStatement pst;
    @FXML
    private DatePicker startField;
    @FXML
    private DatePicker endField;
    @FXML
    private CheckBox allDayField;
    @FXML
    private TextField titleField;
    @FXML
    private TextField descField;
    @FXML
    private ChoiceBox<String> userChoiceBox;

    @FXML
    private ChoiceBox<String> userChoiceBox1;

    int count = 0;
    @FXML
    private Button retour;
    @FXML
    private Button qr;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        table();
        description.setEditable(true);
        description.setCellFactory(TextFieldTableCell.forTableColumn());
        description.setOnEditCommit((TableColumn.CellEditEvent<Calendar, String> t) -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setDescription(t.getNewValue());
            modifier((Calendar) t.getTableView().getItems().get(t.getTablePosition().getRow()));
        });
        title.setEditable(true);
        title.setCellFactory(TextFieldTableCell.forTableColumn());
        title.setOnEditCommit((TableColumn.CellEditEvent<Calendar, String> t) -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setTitle(t.getNewValue());
            modifier((Calendar) t.getTableView().getItems().get(t.getTablePosition().getRow()));
        });

        start.setCellFactory(column -> {
            return new TableCell<Calendar, LocalDate>() {
                private final DatePicker datePicker = new DatePicker();

                {
                    // Update the date value when the DatePicker value changes
                    datePicker.setOnAction(event -> {
                        LocalDate localDate = datePicker.getValue();
                        if (localDate != null) {
                            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                            //java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                            //LocalDate dateLocal = date.toLocalDate();
                            commitEdit(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

                        }
                    });

                    // Show the DatePicker when the cell is clicked
                    this.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2) {
                            this.setGraphic(datePicker);
                            this.setText(null);
                        }
                    });
                }

                @Override
                protected void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);

                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(date.toString());
                        setGraphic(null);
                        start.setEditable(true);

                        start.setOnEditCommit((TableColumn.CellEditEvent<Calendar, LocalDate> t) -> {
                            System.out.println("inside start on edit");
                            t.getTableView().getItems().get(t.getTablePosition().getRow()).setStart(t.getNewValue());
                            modifier((Calendar) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        });
                    }
                }
            };
        });

        end.setCellFactory(column -> {
            return new TableCell<Calendar, LocalDate>() {
                private final DatePicker datePicker = new DatePicker();

                {
                    // Update the date value when the DatePicker value changes
                    datePicker.setOnAction(event -> {
                        LocalDate localDate = datePicker.getValue();
                        if (localDate != null) {
                            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                            //java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                            //LocalDate dateLocal = date.toLocalDate();
                            commitEdit(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

                        }
                    });

                    // Show the DatePicker when the cell is clicked
                    this.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2) {
                            this.setGraphic(datePicker);
                            this.setText(null);
                        }
                    });
                }

                @Override
                protected void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);

                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(date.toString());
                        setGraphic(null);
                        end.setEditable(true);

                        end.setOnEditCommit((TableColumn.CellEditEvent<Calendar, LocalDate> t) -> {
                            System.out.println("inside start on edit");
                            t.getTableView().getItems().get(t.getTablePosition().getRow()).setEnd(t.getNewValue());
                            modifier((Calendar) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        });
                    }
                }
            };
        });

    }

    public void selectUsers() {
        List userList = new ArrayList();
        String reqUser = "SELECT * FROM user WHERE roles LIKE '%[\"ROLE_Doctor\"]%';";
        try {
            pst = conn.prepareStatement(reqUser);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String userLast = rs.getString("lastname");
                userList.add(userLast);
            }
            userChoiceBox.getItems().addAll(userList);
            userChoiceBox1.getItems().addAll("Congé", "Rendez-vous");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void table() {
        ObservableList<Calendar> calendriers = FXCollections.observableArrayList();
        String sql = "SELECT * FROM calendar";

        if (count == 0) {
            selectUsers();
            count++;
        }

        try {
            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int user_id = rs.getInt("user_id");

                String title = rs.getString("title");
                LocalDate start = rs.getDate("start").toLocalDate();
                LocalDate end = rs.getDate("end").toLocalDate();
                String description = rs.getString("description");
                Boolean all_day = rs.getBoolean("all_day");
                String background_color = rs.getString("background_color");
                String border_color = rs.getString("border_color");
                String text_color = rs.getString("text_color");
                Calendar calendar = new Calendar();
                calendar.setId(id);
                calendar.setTitle(title);
                calendar.setStart(start);
                calendar.setEnd(end);
                calendar.setDescription(description);
                calendar.setAll_day(all_day);
                calendar.setBackgroundColor(background_color);
                calendar.setBorderColor(border_color);
                calendar.setTextColor(text_color);

                User user = new User();
                user.setId(user_id);

                calendar.setUser(user);

                calendriers.add(calendar);
            }

            tableCalendrier.setItems(calendriers);
            tableCalendrier.setRowFactory(tv -> new TableRow<Calendar>() {
                @Override
                protected void updateItem(Calendar calendar, boolean empty) {
                    super.updateItem(calendar, empty);
                    if (calendar == null || empty) {
                        setStyle("");
                    } else {
                        //setText(item);
                        //setTextFill(Color.web(calendar.getTextColor()));
                        setStyle("-fx-background-color: " + calendar.getBackgroundColor() + ";");

                    }
                }
            });

            description.setCellValueFactory(new PropertyValueFactory<>("description"));
            title.setCellValueFactory(new PropertyValueFactory<>("title"));
            start.setCellValueFactory(new PropertyValueFactory<>("start"));
            end.setCellValueFactory(new PropertyValueFactory<>("end"));
            all_day.setCellValueFactory(new PropertyValueFactory<>("all_day"));

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void ajouterEvent() {

        String sql = "SELECT id FROM `user` WHERE lastname = ? LIMIT 1;";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, userChoiceBox.getValue());
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                User u = new User(rs.getInt("id"));
                LocalDate start = startField.getValue();
                LocalDate end = endField.getValue();
                String bg_color, text_color, border_color, type_field;
                type_field = userChoiceBox1.getValue();
                if ("Congé".equals(type_field)) {
                    bg_color = "#48b9e0";
                    text_color = "#FFFFFF";
                    border_color = "#000000";
                } else {
                    bg_color = "#00FF00";
                    text_color = "#000000";
                    border_color = "#FFFFFF";
                }

                if (start.isBefore(LocalDate.now())) {
                    JOptionPane.showMessageDialog(null, "La date de l'evenement doit être postérieure ou égale à la date actuelle.");
                    return;
                }

                if (end != null && start.isAfter(end)) {
                    JOptionPane.showMessageDialog(null, "La date de fin de l'evenement doit être antérieure à la date de début.");
                    return;
                }

                Calendar Ca;
                Ca = new Calendar(titleField.getText(), start, end, descField.getText(), allDayField.selectedProperty().get(), bg_color, border_color, text_color, u);

                ajouter(Ca);
                notifier();
                System.out.println("Create success");

            }
            table();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    private void ajouter(Calendar c) {

        String req = "INSERT INTO calendar(`user_id`, `title`, `start`, `end`, `description`, `all_day`, `background_color`,`border_color`,`text_color`) VALUES (?,?,?,?,?,?,?,?,?)";

        try {

            PreparedStatement pst = conn.prepareStatement(req);
            pst.setObject(1, c.user.getId());
            pst.setString(2, c.title);
            pst.setObject(3, c.getStart());
            pst.setObject(4, c.getEnd());
            pst.setString(5, c.description);
            pst.setBoolean(6, c.all_day);
            pst.setString(7, c.getBackgroundColor());
            pst.setString(8, c.getBorderColor());
            pst.setString(9, c.getTextColor());
            pst.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        // 
    }

    private void selectLine() {
        tableCalendrier.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Calendar C = tableCalendrier.getItems().get(tableCalendrier.getSelectionModel().getSelectedIndex());
            }
        }
        );
    }
    Calendar del = null;

    @FXML
    private void suppEvent(ActionEvent event) {

        try {
            del = tableCalendrier.getSelectionModel().getSelectedItem();
            String deleteQuery = "DELETE FROM Calendar WHERE id = ? ";
            PreparedStatement pst = conn.prepareStatement(deleteQuery);
            pst.setInt(1, del.getId());
            pst.executeUpdate();
            table();
            System.out.println("Successful DELETE");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    Calendar mod = null;

    private void modifier(Calendar c) {
        try {

            mod = tableCalendrier.getSelectionModel().getSelectedItem();
            String UpdateData = "UPDATE Calendar SET title = ?, start = ?, end = ?, description = ?, all_day = ? WHERE id = ?";
            PreparedStatement upd = conn.prepareStatement(UpdateData);
            upd.setString(1, c.getTitle());
            upd.setObject(2, c.getStart());
            upd.setObject(3, c.getEnd());
            upd.setString(4, c.getDescription());
            upd.setBoolean(5, c.all_day);
            upd.setInt(6, mod.getId());
            upd.executeUpdate();
            table();
            System.out.println("Successful update");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void exporterEvent() throws DecoderException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save As");
        fileChooser.setInitialFileName("data.xlsx");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                XSSFWorkbook workbook = new XSSFWorkbook();
                org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Data");

                ObservableList<TableColumn<Calendar, ?>> columns = tableCalendrier.getColumns();
                List<Calendar> items = tableCalendrier.getItems();

                CellStyle styleGreen = workbook.createCellStyle();
                byte[] rgb = Hex.decodeHex("#00ff00".substring(1).toCharArray());
                XSSFColor colorGreen = new XSSFColor(rgb, new DefaultIndexedColorMap());

                styleGreen.setFillForegroundColor(colorGreen);
                styleGreen.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                CellStyle styleBlue = workbook.createCellStyle();
                byte[] rgb1 = Hex.decodeHex("#48b9e0".substring(1).toCharArray());
                XSSFColor colorBlue = new XSSFColor(rgb1, new DefaultIndexedColorMap());

                styleBlue.setFillForegroundColor(colorBlue);
                styleBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < columns.size(); i++) {
                    TableColumn<Calendar, ?> column = columns.get(i);
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(column.getText());

                }

                for (int i = 0; i < items.size(); i++) {
                    Calendar calendar = items.get(i);
                    Row row = sheet.createRow(i + 1);

                    for (int j = 0; j < columns.size(); j++) {
                        TableColumn<Calendar, ?> column = columns.get(j);

                        Cell cell = row.createCell(j);
                        switch (column.getId()) {
                            case "title":
                                if ("#00FF00".equals(calendar.getBackgroundColor())) {
                                    cell.setCellValue(calendar.getTitle());
                                    cell.setCellStyle(styleGreen);
                                } else {
                                    cell.setCellValue(calendar.getTitle());
                                    cell.setCellStyle(styleBlue);
                                }
                                break;

                            case "description":
                                if ("#00FF00".equals(calendar.getBackgroundColor())) {
                                    cell.setCellValue(calendar.getDescription());
                                    cell.setCellStyle(styleGreen);
                                } else {
                                    cell.setCellValue(calendar.getDescription());
                                    cell.setCellStyle(styleBlue);
                                }
                                break;
                            case "start":
                                if ("#00FF00".equals(calendar.getBackgroundColor())) {
                                    cell.setCellValue(calendar.getStart().toString());
                                    cell.setCellStyle(styleGreen);
                                } else {
                                    cell.setCellValue(calendar.getStart().toString());
                                    cell.setCellStyle(styleBlue);
                                }
                                break;
                            case "end":
                                if ("#00FF00".equals(calendar.getBackgroundColor())) {
                                    cell.setCellValue(calendar.getEnd().toString());
                                    cell.setCellStyle(styleGreen);
                                } else {
                                    cell.setCellValue(calendar.getEnd().toString());
                                    cell.setCellStyle(styleBlue);
                                }
                                break;
                            case "all_day":
                                if ("#00FF00".equals(calendar.getBackgroundColor())) {
                                    cell.setCellValue(calendar.getAll_day());
                                    cell.setCellStyle(styleGreen);
                                } else {
                                    cell.setCellValue(calendar.getAll_day());
                                    cell.setCellStyle(styleBlue);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }

                FileOutputStream fileOut = new FileOutputStream(file);
                workbook.write(fileOut);
                fileOut.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    private void rt(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dash.fxml"));
        Parent root = loader.load();
        retour.getScene().setRoot(root);
    }

    @FXML
    private void qrr(ActionEvent event) throws WriterException {
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
            Calendar qr = tableCalendrier.getSelectionModel().getSelectedItem();
            ByteMatrix matrix = null;
            String data = "Title =" + qr.title + "\nStart " + qr.start + "\nEnd " + qr.end;
            matrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height, new Hashtable<EncodeHintType, Object>(hintMap));
            BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
            String fileName = file.getName().concat(".png");
            ImageIO.write(image, "png", new File(file.getParent(), fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void notifier() {
        Notifications notifications = Notifications.create();
        notifications.text("Evenement enregistré");
        notifications.title("Evenement ajouté !");
        notifications.hideAfter(Duration.seconds(3));
        notifications.show();
    }

    @FXML
    void trierEvent(ActionEvent event) {
        ObservableList<Calendar> c = tableCalendrier.getItems();
        Comparator<Calendar> startComparator = Comparator.comparing(c1 -> c1.getStart());
        c.sort(startComparator);
        tableCalendrier.setItems(c);
    }

}
