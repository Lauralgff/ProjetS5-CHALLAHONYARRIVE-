/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.legoff_venet.projet.Vues;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import fr.insa.legoff_venet.projet.VuePrincipale;
/**
 *
 * @author laura
 */

public class PageAccueil extends MyVerticalLayout{
    
    private VuePrincipale main;
   
    public PageAccueil(VuePrincipale main) {
        this.main = main;
        
        this.add(new H1("Bienvenue"));
        Button Sinscrire = new Button("S'inscrire");
        Sinscrire.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
        Sinscrire.addClickListener((event) -> {
         this.main.setMainContent(new Inscription(this.main));   
         });
        
        Button Seconnecter = new Button("Se connecter");
        Seconnecter.addThemeVariants(ButtonVariant.LUMO_ERROR);
        Seconnecter.addClickListener((event) -> {
         this.main.setMainContent(new Connexion(this.main));   
         });
        
        VerticalLayout Boutton = new VerticalLayout (Sinscrire, Seconnecter);
        add (Boutton);
    }
     
}