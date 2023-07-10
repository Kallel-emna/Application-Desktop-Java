/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Clinimate.service;

import Clinimate.entities.DossierMedical;

import javafx.collections.ObservableList;

/**
 *
 * @author rania
 * @param <d>

 */
public interface IServiceDossier<d> {
    
      public ObservableList<DossierMedical> AfficherDossier() ; 
       public void SupprimerDossier(DossierMedical u) ; 
       public void ModifierDossier(String c, String gr,int id,int d ) ;   
       public void ajouterDossier(DossierMedical u);
           // public void ajouterLivraison(BilanMedical l);     
}
