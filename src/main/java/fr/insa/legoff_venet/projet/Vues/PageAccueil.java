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
        Button button = new Button("S'inscrire");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
        
        Button button1 = new Button("Se connecter");
        button1.addThemeVariants(ButtonVariant.LUMO_ERROR);
        
        button.addClickListener((event) -> {
         this.main.setMainContent(new Inscription(this.main));   
         });
        button1.addClickListener((event) -> {
         this.main.setMainContent(new Connexion(this.main));   
         });
        VerticalLayout Boutton = new VerticalLayout (button, button1);
        add (Boutton);
    }
     
}
