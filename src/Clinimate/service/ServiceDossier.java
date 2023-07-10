/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinimate.service;

import Clinimate.entities.Ordonnance;
import Clinimate.entities.DossierMedical;
import Clinimate.utils.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author rania
 */
public abstract class ServiceDossier implements IServiceDossier<DossierMedical> {

    Connection cnx = DataSource.getInstance().getCnx();
    private Connection conn;
    PreparedStatement stmt;
    PreparedStatement ste;
    //     ServiceBilan b=new ServiceBilan();
    //   ServiceUser sc = new ServiceUser();

    @Override
    public void SupprimerDossier(DossierMedical u) {
        String req = "DELETE FROM dossier_medical WHERE id=" + u.getId();
        try {
            Statement st = cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("Dossier supprimee avec succes");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void ajouterDossier(DossierMedical u) {

        String req = "INSERT INTO `dossier_medical`( `certificat`,`groupe_sanguin`,`ordonnance_id`) VALUES ('" + u.getCertificat() + "','" + u.getGroupe_sanguin() + "','" + u.getOrdonnance_id() + "')";
        try {
            //insert
            Statement st = cnx.createStatement();
            st.executeUpdate(req, 0);
            System.out.println("Dossier ajoute avec succes");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void ModifierDossier(String c, String gr, int d, int id) {
        try {
            String req = " UPDATE `dossier_medical` SET  `certificat`='" + c + "',`groupe_sanguin`='" + gr + "',`ordonnance_id`='" + d + "' WHERE `id`='" + id + "' ";
            Statement st = cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("Dossier modifie avec succes");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Object stream() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override

    public ObservableList<DossierMedical> AfficherDossier() {
        ObservableList<DossierMedical> liste = FXCollections.observableArrayList();
        String selectData = "SELECT dossier_medical.id, dossier_medical.certificat, dossier_medical.groupe_sanguin, ordonnance.nompatient "
                + "FROM dossier_medical "
                + "INNER JOIN ordonnance ON dossier_medical.ordonnance_id = ordonnance.id";

        try {
            PreparedStatement pst = cnx.prepareStatement(selectData);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");

                String cer = rs.getString("certificat");
                String gr = rs.getString("groupe_sanguin");

                String nom = rs.getString("nompatient");

                DossierMedical dossier = new DossierMedical();
                dossier.setId(id);
                dossier.setCertificat(cer);
                dossier.setGroupe_sanguin(gr);

                Ordonnance or = new Ordonnance();
                or.setNompatient(nom);

                dossier.setOrdonnance(or);

                liste.add(dossier);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return liste;
    }

    public ObservableList<DossierMedical> search(String searchTerm) {

        ObservableList<DossierMedical> liste = FXCollections.observableArrayList();
        String selectData = "SELECT dossier_medical.id, dossier_medical.certificat, dossier_medical.groupe_sanguin, ordonnance.nompatient "
                + "FROM dossier_medical "
                + "INNER JOIN ordonnance ON dossier_medical.ordonnance_id = ordonnance.id";

        try {
            PreparedStatement pst = cnx.prepareStatement(selectData);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");

                String cer = rs.getString("certificat");
                String gr = rs.getString("groupe_sanguin");

                String nom = rs.getString("nompatient");

                DossierMedical dossier = new DossierMedical();
                dossier.setId(id);
                dossier.setCertificat(cer);
                dossier.setGroupe_sanguin(gr);

                Ordonnance or = new Ordonnance();
                or.setNompatient(nom);

                dossier.setOrdonnance(or);

                liste.add(dossier);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return liste;

    }
}
