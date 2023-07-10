/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinimate.service;

import Clinimate.utils.DataSource;
import Clinimate.entities.BilanMedical;
import Clinimate.entities.DossierMedical;
import Clinimate.entities.User;
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
public class ServiceBilan implements IServiceBilan<BilanMedical> {

    Connection cnx = DataSource.getInstance().getCnx();
    private Connection conn;
    PreparedStatement stmt;

    @Override
    public ObservableList<BilanMedical> AfficherBilan() {
        ObservableList<BilanMedical> liste = FXCollections.observableArrayList();
        String selectData = "SELECT b.id, b.antecedents, b.taille,  b.poids  ,b.examens_biologiques,b.imagerie_medicale, d.certificat, b.user_id, u.lastname "
                + "FROM bilan_medical b "
                + "JOIN dossier_medical d ON b.dossier_medical_id = d.id "
                + "JOIN user u ON b.user_id = u.id";
        try {
            PreparedStatement pst = cnx.prepareStatement(selectData);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {

                int id = rs.getInt("id");

                String an = rs.getString("antecedents");
                String taille = rs.getString("taille");
                String poids = rs.getString("poids");
                String ex = rs.getString("examens_biologiques");
                String image = rs.getString("imagerie_medicale");
                String certificat = rs.getString("certificat");
                int patient_id = rs.getInt("user_id");
                String lastname = rs.getString("lastname");

                BilanMedical bilan = new BilanMedical();
                bilan.setId(id);
                bilan.setAntecedents(an);
                bilan.setTaille(taille);
                bilan.setPoids(poids);
                bilan.setExamens_biologiques(ex);
                bilan.setImagerie_medicale(image);

                DossierMedical dossier = new DossierMedical();
                dossier.setCertificat(certificat);

                User patient = new User();
                patient.setLastname(lastname);

                bilan.setDossier(dossier);
                bilan.setUser(patient);

                liste.add(bilan);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return liste;
    }

    @Override
    public void SupprimerBilan(BilanMedical u) {
        String req = "DELETE FROM bilan_medical WHERE id=" + u.getId();
        try {
            Statement st = cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("Bilan supprimee avec succes");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void ModifierBilan(String an, String t, String p, String e, String i, int d, int dossier, int id) {
        try {
            String req = " UPDATE `bilan_medical` SET  `antecedents`='" + an + "',`taille`='" + t + "',`poids`='" + p + "', `examens_biologiques`='" + e + "', `imagerie_medicale` ='" + i + "',`user_id` ='" + d + "',`dossier_medical_id` ='" + dossier + "' WHERE `id`='" + id + "' ";
            Statement st = cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("Bilan modifie avec succes");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void ajouterBilan(BilanMedical u) {

        String req = "INSERT INTO `bilan_medical`( `antecedents`,`taille`,`poids`, `examens_biologiques`, `imagerie_medicale`,user_id,dossier_medical_id) VALUES ('" + u.getAntecedents() + "','" + u.getTaille() + "','" + u.getPoids() + "','" + u.getExamens_biologiques() + "','" + u.getImagerie_medicale() + "','" + u.getUser_id() + "','" + u.getDossier_id() + "')";
        try {
            //insert
            Statement st = cnx.createStatement();
            st.executeUpdate(req, 0);
            System.out.println("Bilan ajoute avec succes");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public ObservableList<BilanMedical>search2(String searchTerm) {
{   
    
     ObservableList<BilanMedical> liste = FXCollections.observableArrayList();
       String selectData = "SELECT b.id, b.antecedents, b.taille,  b.poids  ,b.examens_biologiques,b.imagerie_medicale, d.certificat, b.user_id, u.lastname "
                + "FROM bilan_medical b "
                + "JOIN dossier_medical d ON b.dossier_medical_id = d.id "
                + "JOIN user u ON b.user_id = u.id";
        try {
            PreparedStatement pst = cnx.prepareStatement(selectData);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                
             int id = rs.getInt("id");
           
              String an  = rs.getString("antecedents") ; 
              String taille = rs.getString("taille");
              String poids = rs.getString("poids") ; 
              String ex = rs.getString("examens_biologiques"); 
              String image = rs.getString("imagerie_medicale"); 
              String certificat = rs.getString("certificat");
              int patient_id = rs.getInt("user_id");
              String lastname = rs.getString("lastname");
                
                BilanMedical bilan = new BilanMedical();
                bilan.setId(id);
                bilan.setAntecedents(an);
                bilan.setTaille(taille);
                bilan.setPoids(poids);
                bilan.setExamens_biologiques(ex);
                bilan.setImagerie_medicale(image);
                
                
                DossierMedical dossier = new DossierMedical();
                dossier.setCertificat(certificat);
                
                User patient = new User();
                patient.setLastname(lastname);
                
             
                bilan.setDossier(dossier);
                bilan.setUser(patient);
                
                liste.add(bilan);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return liste;
}
           }

}
