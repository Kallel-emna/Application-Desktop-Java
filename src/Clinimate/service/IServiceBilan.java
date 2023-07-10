/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Clinimate.service;

import Clinimate.entities.BilanMedical;
import java.util.List;
import javafx.collections.ObservableList;

/**
 *
 * @author rania
 * @param <b>

 */
public interface IServiceBilan<b> {
    
      public ObservableList<BilanMedical> AfficherBilan() ; 
       public void SupprimerBilan(BilanMedical u) ; 
         public void ModifierBilan(String an, String t,String p,String e,String i,int d ,int dossier,int id) ;   
         public void ajouterBilan(BilanMedical u);
           // public void ajouterLivraison(BilanMedical l);    
     //      public List<BilanMedical> displayByID(int id_b) ; 
        //     public ObservableList<BilanMedical> chercherUserid(String mot) ; 
}
