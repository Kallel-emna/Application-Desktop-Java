/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinimate.service;

import Clinimate.entities.Categorie;
import Clinimate.entities.Produit;
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
public class ServiceProduit implements IServiceProduit<Produit> {

    public ServiceProduit() {
        conn = DataSource.getInstance().getCnx();
    }

    Connection cnx = DataSource.getInstance().getCnx();
    private Connection conn;
    PreparedStatement stmt;
    PreparedStatement ste;

    
    
    
    @Override
    public List<Produit> afficherProduit() {
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

        } catch (Exception e) {
            e.printStackTrace();
        }
        return liste;
    }

    @Override
    public void ajouterProduit(Produit p) {
        String req = "INSERT INTO produit( `nom_produit`,`quantite`,`prix`,`categorie_id` ) VALUES ('" + p.getNom_produit() + "','" + p.getQuantite() + "','" + p.getPrix() + "','" + p.getCategorie_id() + "')";
        try {
            //insert
            Statement st = cnx.createStatement();
            st.executeUpdate(req, 0);
            System.out.println("Produit ajouté avec succés");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void modifierProduit(int id, String np, int q, int p, int idcat) {
        try {
            String req = " UPDATE produit SET  (nom_produit`='" + np + "',quantite`='" + q + "',prix`='" + p + "',categorie_id`='" + idcat + "') WHERE `id`='" + id + "' ";
            Statement st = cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("Produit modifié avec succés");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void supprimerProduit(Produit p) {
        String req = "DELETE FROM produit WHERE id=" + p.getId();
        try {
            Statement st = cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("Produit supprimé avec succés");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
     public ObservableList<Produit> search2(String searchTerm) {
        {
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

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return liste;
        }
    }

   
}
