/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Clinimate.gui;

import Clinimate.entities.User;
import Clinimate.service.Users;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
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
import javafx.scene.control.Button;
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
public class InscriptionPController implements Initializable {

    @FXML
    private TextField emailP;
    @FXML
    private TextField PasswordP;
    @FXML
    private TextField firstnameP;
    @FXML
    private TextField LastnameP;
    @FXML
    private TextField AddressP;
    @FXML
    private TextField TelephoneP;
    @FXML
    private Button validerP;
    @FXML
    private ComboBox<String> roleP;
    @FXML
    private Button Loginn;
    @FXML
    private TextField animalP;
    private ComboBox<String> QuestionP;
    @FXML
    private ComboBox<String> questionP;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ObservableList<String> listt = FXCollections.observableArrayList("[\"ROLE_PATIENT\"]");
        roleP.setItems(listt);
        roleP.setConverter(new StringConverter<String>() {
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
                } else if (value.equals("Receptionniste")) {
                    return "[\"ROLE_RECEPT\"]";
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
        ObservableList<String> listtQ = FXCollections.observableArrayList("Quelle est la marque de voiture préférée", " votre nom préféré ", "votre club préféré");
        questionP.setItems(listtQ);
    }

    private void MailB() throws Exception {
        try {
            String to = "mohamed.jmai@esprit.tn";
            String from = "mohamedjmai811s@gmail.com";
            String subject = "Registration Account CLINIMATE";
            String body = "BONJOUR MR/MME "
                    + firstnameP.getText()
                    + "this your mail for connecter to our application CLINIAMTE\n email :"
                    + emailP.getText()
                    + "\n password :"
                    + PasswordP.getText();
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
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailP.getText()));
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
    private void InscriptionButton(ActionEvent event) throws IOException, NoSuchAlgorithmException, Exception {
        String Email = emailP.getText();
        String Password = PasswordP.getText();
        String Role = roleP.getSelectionModel().getSelectedItem().toString();
        String firstname = firstnameP.getText();
        String lastname = LastnameP.getText();
        String address = AddressP.getText();
        String animal = animalP.getText();
        String question = questionP.getSelectionModel().getSelectedItem().toString();

        int telephone = Integer.parseInt(TelephoneP.getText());
        String telSTR = Integer.toString(telephone);
        if (telSTR.isEmpty() || Email.isEmpty() || Password.isEmpty() || Role.isEmpty() || firstname.isEmpty() || lastname.isEmpty() || address.isEmpty() || animal.isEmpty()) {
            JOptionPane.showMessageDialog(null, "entrer svp votre champs ");
        } else {
            if (telephone < 99999999 || telephone > 0) {
                try {
                    System.out.println(question);
                    Users U = new Users();
                    System.out.println(question);
                    User Us = new User(Email, Password, Role, firstname, lastname, address, telephone, animal, question);
                    U.SignUp(Us);

                    MailB();

                    System.out.println("email secccceuuufftly");
                    System.out.println(question);
                    getData.Email = Email;
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
                    Parent root = loader.load();
                    emailP.getScene().setRoot(root);
                } catch (SQLException ex) {
                    System.out.println("ex");
                }
            } else {
                JOptionPane.showMessageDialog(null, "champs telephone il faut etre entre 8 et 6 chiffre ");
            }
        }
    }

    @FXML
    private void BackLogin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();
        emailP.getScene().setRoot(root);
    }

}
