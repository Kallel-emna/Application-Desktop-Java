/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Clinimate.gui;

import Clinimate.entities.User;
import Clinimate.service.Users;
import Clinimate.utils.DataSource;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author moham
 */
public class SignUpController implements Initializable {

    Connection cnx = DataSource.getInstance().getCnx();

    @FXML
    private TextField emailR;
    @FXML
    private ComboBox<String> roleR;
    @FXML
    private TextField PasswordR;
    @FXML
    private TextField firstnameR;
    @FXML
    private TextField LastnameR;
    @FXML
    private TextField AddressR;
    @FXML
    private TextField TelephoneR;
    @FXML
    private Button valider;
    @FXML
    private Button Dashboard;
    @FXML
    private TextField animalR;
    private ComboBox<String> QuestionR;
    @FXML
    private ComboBox<String> questionR;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> list = FXCollections.observableArrayList("[\"ROLE_ADMIN\"]", "[\"ROLE_PATIENT\"]", "[\"ROLE_Doctor\"]", "[\"ROLE_RECEPT\"]", "[\"ROLE_PHARMACIEN\"]");
        roleR.setItems(list);

        roleR.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String value) {
                if (value == null) {
                    return null;
                } else if (value.equals("[\"ROLE_ADMIN\"]")) {
                    return "Admin";
                } else if (value.equals("[\"ROLE_PATIENT\"]")) {
                    return "Patient";
                } else if (value.equals("[\"ROLE_PHARMACIEN\"]")) {
                    return "Pharmacien";
                } else if (value.equals("[\"ROLE_Doctor\"]")) {
                    return "Doctor";
                } else if (value.equals("[\"ROLE_RECEPT\"]")) {
                    return "Receptionniste" ;
                } else {
                    return value;
                }
            }

            @Override
            public String fromString(String value) {
                if (value == null) {
                    return null;
                } else if (value.equals("Admin")) {
                    return "[\"ROLE_ADMIN\"]";
                } else if (value.equals("Patient")) {
                    return "[\"ROLE_PATIENT\"]";
                } else if (value.equals("Pharmacien")) {
                    return "[\"ROLE_PHARMACIEN\"]";
                } else if (value.equals("Doctor")) {
                    return "[\"ROLE_Doctor\"]";
                } else if (value.equals("Receptionniste")) {
                    return "[\"ROLE_RECEPT\"]";
                } else {
                    return value;
                }
            }
        });

        ObservableList<String> listtQse = FXCollections.observableArrayList("Quelle est la marque de voiture préférée", " votre nom préféré ", "votre club préféré");
        questionR.setItems(listtQse);
    }

    private void MailB() throws Exception {
        try {
            String to = "mohamed.jmai@esprit.tn";
            String from = "mohamedjmai811s@gmail.com";
            String subject = "Registration Account CLINIMATE";
            String body = "BONJOUR MR/MME "
                    + firstnameR.getText()
                    + "this your mail for connecter to our application CLINIAMTE\n email :"
                    + emailR.getText()
                    + "\n password :"
                    + PasswordR.getText();
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("mohamed.jmai@esprit.tn", "wrshgovdrqjkqejb");
                }
            });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(to));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailR.getText()));
                message.setSubject(subject);
                message.setText(body);
                System.out.println(body);
                Transport.send(message);

                System.out.println("registration sucedffly done ");
            } catch (MessagingException e) {
                System.err.println("Failed to send email: " + e.getMessage());
            }
        } catch (RuntimeException e) {
            if (e.getCause() instanceof InvocationTargetException) {
                Throwable targetException = e.getCause().getCause();
                targetException.printStackTrace();
            } else {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void SignUpButton(ActionEvent event) throws IOException, NoSuchAlgorithmException {
        String Email = emailR.getText();
        String Password = PasswordR.getText();
        String Role = roleR.getSelectionModel().getSelectedItem().toString();
        String firstname = firstnameR.getText();
        String lastname = LastnameR.getText();
        String address = AddressR.getText();
        String animal = animalR.getText();
        String question = questionR.getSelectionModel().getSelectedItem().toString();
        int telephone = Integer.parseInt(TelephoneR.getText());
        if (Email.isEmpty() || Password.isEmpty() || Role.isEmpty() || firstname.isEmpty() || lastname.isEmpty() || address.isEmpty() || animal.isEmpty()) {
            JOptionPane.showMessageDialog(null, "entrer svp votre champs ");
        } else {
            if (telephone < 99999999 || telephone > 0) {
                try {
                    Users U = new Users();
                    User Us = new User(Email, Password, Role, firstname, lastname, address, telephone, animal, question);

                    U.SignUp(Us);
                    try {
                        MailB();
                    } catch (Exception ex) {
                        Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println("email secccceuuufftly");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Dash.fxml"));
                    Parent root = loader.load();
                    emailR.getScene().setRoot(root);
                } catch (SQLException ex) {
                    System.out.println("ex");
                }
            } else {
                JOptionPane.showMessageDialog(null, "champs telephone il faut etre entre 8 et 6 chiffre ");
            }
        }

    }

    @FXML
    private void BackDashboard(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dash.fxml"));
        Parent root = loader.load();
        emailR.getScene().setRoot(root);
    }

}
