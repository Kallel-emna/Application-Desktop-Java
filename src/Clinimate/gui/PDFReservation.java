/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinimate.gui;

import Clinimate.utils.DataSource;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import Clinimate.entities.ReservationChambre;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javafx.scene.control.Alert;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author LENOVO
 */
public class PDFReservation {

    PreparedStatement pst;

    Connection conn = DataSource.getInstance().getCnx();

    public void createPDF(ReservationChambre rese) throws SQLException {
        // Get the chambre ID and prix from the selected reservation
        String patient = rese.getPatient().getLastname();
        String prenompatient = "";
        String labelle = rese.getChambre().getLibelle_chambre();
        double prix = 0;
        double prix_total = 0;
        String email = "";
        String adresse = "";
        int telephone = 0;
        int id = rese.getId();
        Date admission = rese.getDate_admission();
        Date sortie = rese.getDate_sortie();
        String nom_service = "";
        long diffInMilliseconds = sortie.getTime() - admission.getTime();
        long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMilliseconds);
        // Set the file name and path for the PDF
        String fileName = "reservation-chambre-" + rese.getId() + ".pdf";
        String filePath = "C:\\xampp\\htdocs\\integration\\fff\\integ_final\\Clinimate_Desktop\\" + fileName; // change the path as needed
        String query = "SELECT c.prix, s.nom_service, u.firstname, u.telephone, u.address, u.email\n"
                + "FROM reservation_chambre rc\n"
                + "JOIN chambre c ON c.id = rc.chambre_id\n"
                + "JOIN service s ON s.id = c.service_id\n"
                + "JOIN user u ON u.id = rc.patient_id\n"
                + "WHERE rc.id = ?";
        try {

            pst = conn.prepareStatement(query);
            pst.setInt(1, id);

            // execute query and retrieve result set
            ResultSet rs = pst.executeQuery();

            // retrieve data from result set
            if (rs.next()) {
                prix = rs.getDouble("prix");
                nom_service = rs.getString("nom_service");
                prenompatient = rs.getString("firstname");
                telephone = rs.getInt("telephone");
                adresse = rs.getString("address");
                email = rs.getString("email");

            } else {
                System.out.println("Aucune chambre correspondante trouvée.");
            }
            prix_total = prix * diffInDays;
            // Create a new PDF document
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

   // Add the reservation details to the PDF
            Paragraph title = new Paragraph("FACTURE", FontFactory.getFont(FontFactory.TIMES_BOLD, 18));
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph date = new Paragraph("Date: " + new Date().toString(), FontFactory.getFont(FontFactory.TIMES, 12));
            date.setAlignment(Element.ALIGN_RIGHT);
            document.add(date);

            document.add(new Paragraph("------------------------------------------------------------------------------------------------------------------------"));

            Paragraph reservationDetails = new Paragraph();
            Chunk reseLabel = new Chunk("Numero de reservation: ", FontFactory.getFont(FontFactory.TIMES_BOLD, 12));
            Chunk reseNum = new Chunk(String.valueOf(id), FontFactory.getFont(FontFactory.TIMES, 12));
                        Chunk serviceLabel = new Chunk("nom de service: ", FontFactory.getFont(FontFactory.TIMES_BOLD, 12));
            Chunk serviceName = new Chunk(nom_service, FontFactory.getFont(FontFactory.TIMES, 12));
            Chunk chambreLabel = new Chunk("Chambre réservée: ", FontFactory.getFont(FontFactory.TIMES_BOLD, 12));
            Chunk chambreName = new Chunk(labelle, FontFactory.getFont(FontFactory.TIMES, 12));
            reservationDetails.add(reseLabel);
            reservationDetails.add(reseNum);
            reservationDetails.add(Chunk.NEWLINE);
            reservationDetails.add(serviceLabel);
            reservationDetails.add(serviceName);
            reservationDetails.add(Chunk.NEWLINE);
            reservationDetails.add(chambreLabel);
            reservationDetails.add(chambreName);
            reservationDetails.setAlignment(Element.ALIGN_LEFT);
            document.add(reservationDetails);

            document.add(new Paragraph("------------------------------------------------------------------------------------------------------------------------"));

            Paragraph patientDetails = new Paragraph();
            Chunk patientLabel = new Chunk("Nom du patient: ", FontFactory.getFont(FontFactory.TIMES_BOLD, 12));
            Chunk patientName = new Chunk(patient, FontFactory.getFont(FontFactory.TIMES, 12));
            Chunk prenomLabel = new Chunk("Prénom du patient: ", FontFactory.getFont(FontFactory.TIMES_BOLD, 12));
            Chunk prenomValue = new Chunk(prenompatient, FontFactory.getFont(FontFactory.TIMES, 12));
            Chunk adresseLabel = new Chunk("Adresse du patient: ", FontFactory.getFont(FontFactory.TIMES_BOLD, 12));
            Chunk adresseValue = new Chunk(adresse, FontFactory.getFont(FontFactory.TIMES, 12));
            Chunk telephoneLabel = new Chunk("Téléphone du patient: ", FontFactory.getFont(FontFactory.TIMES_BOLD, 12));
            Chunk telephoneValue = new Chunk(String.valueOf(telephone), FontFactory.getFont(FontFactory.TIMES, 12));
            Chunk emailLabel = new Chunk("E-mail du patient: ", FontFactory.getFont(FontFactory.TIMES_BOLD, 12));
            Chunk emailValue = new Chunk(email, FontFactory.getFont(FontFactory.TIMES, 12));
            patientDetails.add(patientLabel);

            patientDetails.add(patientName);

            patientDetails.add(Chunk.NEWLINE);
            patientDetails.add(prenomLabel);
            patientDetails.add(prenomValue);

            patientDetails.add(Chunk.NEWLINE);
            patientDetails.add(adresseLabel);
            patientDetails.add(adresseValue);
            patientDetails.add(Chunk.NEWLINE);

            patientDetails.add(telephoneLabel);
            patientDetails.add(telephoneValue);
            patientDetails.add(Chunk.NEWLINE);
            patientDetails.add(emailLabel);
            patientDetails.add(emailValue);
            patientDetails.add(Chunk.NEWLINE);

            document.add(patientDetails);
            document.add(new Paragraph("------------------------------------------------------------------------------------------------------------------------"));

            Paragraph billingDetails = new Paragraph();
            Chunk priceLabel = new Chunk("Prix journalier de la chambre: ", FontFactory.getFont(FontFactory.TIMES_BOLD, 12));
            Chunk priceValue = new Chunk(String.valueOf(prix) + " TND", FontFactory.getFont(FontFactory.TIMES, 12));
            Chunk daysLabel = new Chunk("Durée de séjour: ", FontFactory.getFont(FontFactory.TIMES_BOLD, 12));
            Chunk daysValue = new Chunk(String.valueOf(diffInDays) + " jour(s)", FontFactory.getFont(FontFactory.TIMES, 12));
            Chunk totalLabel = new Chunk("Prix total: ", FontFactory.getFont(FontFactory.TIMES_BOLD, 12));
            Chunk totalValue = new Chunk(String.valueOf(prix_total) + " TND", FontFactory.getFont(FontFactory.TIMES, 12));
            billingDetails.add(priceLabel);
            billingDetails.add(priceValue);
            billingDetails.add(Chunk.NEWLINE);
            billingDetails.add(daysLabel);
            billingDetails.add(daysValue);
            billingDetails.add(Chunk.NEWLINE);
            billingDetails.add(totalLabel);
            billingDetails.add(totalValue);
            billingDetails.setAlignment(Element.ALIGN_LEFT);
            document.add(billingDetails);
            document.add(new Paragraph("------------------------------------------------------------------------------------------------------------------------"));
// Add a signature section to the PDF
            Paragraph signature = new Paragraph();
            signature.add(Chunk.NEWLINE);
            signature.add(Chunk.NEWLINE);
            signature.add(Chunk.NEWLINE);
            signature.add(Chunk.NEWLINE);

            Chunk signatureLabel = new Chunk("Signature du patient: ", FontFactory.getFont(FontFactory.TIMES_BOLD, 12));
            signature.add(signatureLabel);
            signature.setAlignment(Element.ALIGN_RIGHT);
            document.add(signature);
            document.add(new Paragraph("------------------------------------------------------------------------------------------------------------------------"));
// create the footer table
            PdfPTable footerTable = new PdfPTable(3);
            footerTable.setWidthPercentage(100);

// add the left cell
            PdfPCell leftCell = new PdfPCell(new Paragraph("Entreprise:\n"
                    + "Adresse: Boujaafar Tunis \n"
                    + "TVA: 4322121214", FontFactory.getFont(FontFactory.TIMES, 12)));
            leftCell.setBorder(Rectangle.NO_BORDER);
            leftCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            footerTable.addCell(leftCell);

// add the center cell
            PdfPCell centerCell = new PdfPCell(new Paragraph("Coordonne:\n"
                    + "Adresse: Boujaafar Tunis \n"
                    + "Téléphone: 73246550\n"
                    + "Email:Cinimate@clinimate.com ", FontFactory.getFont(FontFactory.TIMES, 12)));
            centerCell.setBorder(Rectangle.NO_BORDER);
            centerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            footerTable.addCell(centerCell);

// add the right cell
            PdfPCell rightCell = new PdfPCell(new Paragraph("Coordonne Banquaire:\n"
                    + "Banque: BIAT\n"
                    + "Code banque: 451525625655\n"
                    + "IBAN:CHGG56656556G555 ", FontFactory.getFont(FontFactory.TIMES, 12)));
            rightCell.setBorder(Rectangle.NO_BORDER);
            rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            footerTable.addCell(rightCell);

// create a cell to wrap the footer table
            PdfPCell footerCell = new PdfPCell(footerTable);
            footerCell.setBorder(Rectangle.NO_BORDER);
            footerCell.setPadding(10);
            footerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);

// create a table to hold the footer cell
            PdfPTable footerTableWrapper = new PdfPTable(1);
            footerTableWrapper.addCell(footerCell);

// add a new line to the document
            document.add(Chunk.NEWLINE);

// add the footer table wrapper to the document
            document.add(footerTableWrapper);


            document.close();

            // Display a success message to the user
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText("PDF créé avec succès");
            alert.setContentText("Le PDF de la réservation a été créé avec succès à l'emplacement suivant : " + filePath);
            alert.showAndWait();
            System.out.println(filePath);
        } catch (DocumentException | FileNotFoundException e) {
            // Display an error message to the user
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible de créer le PDF");
            alert.setContentText("Une erreur s'est produite lors de la création du PDF de la réservation : " + e.getMessage());
            alert.showAndWait();
        }
    }

}