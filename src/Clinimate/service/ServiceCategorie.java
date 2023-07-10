/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinimate.service;

import Clinimate.entities.Categorie;
import Clinimate.utils.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author kalle
 */
public class ServiceCategorie implements IServiceCategorie<Categorie> {

    public ServiceCategorie() {
        conn = DataSource.getInstance().getCnx();
    }

    Connection cnx = DataSource.getInstance().getCnx();
    private Connection conn;
    PreparedStatement stmt;
    PreparedStatement ste;

    @Override
    public List<Categorie> afficherCategorie() {
        // ObservableList<Categorie> liste = FXCollections.observableArrayList();
        List<Categorie> liste = new ArrayList();
        String selectData = "SELECT * FROM categorie";
        try {
            PreparedStatement pst = cnx.prepareStatement(selectData);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Categorie sData = new Categorie(rs.getInt("id"), rs.getString("nom_cat"));
                liste.add(sData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return liste;
    }

    @Override
    public void ajouterCategorie(Categorie c) {
          String req = "INSERT INTO categorie( `nom_cat`) VALUES ('"+c.getNom_cat()+"')";
             try {
             //insert
            Statement st = cnx.createStatement();
            st.executeUpdate(req,0);
            System.out.println("Catégorie ajoutée avec succés");
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void modifierCategorie(int id, String nc) {
       try {      
       String req=" UPDATE categorie SET  nom_cat`='"+nc+"' WHERE `id`='"+id+"' " ;
       Statement st=cnx.createStatement();
       st.executeUpdate(req);
       System.out.println("Catégorie modifiée avec succés");
         }
     catch(SQLException ex){
         ex.printStackTrace();
     }
    }

    @Override
    public void supprimerCategorie(Categorie c) {
          String req="DELETE FROM categorie WHERE id="+c.getId();
        try{
              Statement st=cnx.createStatement();
              st.executeUpdate(req);
              System.out.println("Catégorie supprimée avec succés");
           
        }
        catch(SQLException ex){  
         ex.printStackTrace();
        }
    }
    
    public Categorie rechercherCategorie(int id) {
    String sql = "SELECT * FROM categorie WHERE id = ?";
    try {
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Categorie categorie = new Categorie();
            categorie.setId(rs.getInt("id"));
            categorie.setNom_cat(rs.getString("nom_cat"));

            return categorie;
        } else {
            return null;
        }
    } catch (SQLException ex) {
        Logger.getLogger(ServiceCategorie.class.getName()).log(Level.SEVERE, null, ex);
        return null;
    }
}

}
