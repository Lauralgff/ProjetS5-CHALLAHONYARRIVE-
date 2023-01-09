/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.legoff_venet.projet.Vues;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import fr.insa.legoff_venet.projet.VuePrincipale;


/**
 *
 * @author laura
 */
public class Inscription extends MyVerticalLayout {
    
    private VuePrincipale main;
    
    public Inscription (VuePrincipale main){
  
    this.main = main;
    
    
        TextField firstName = new TextField("Prénom");
        TextField lastName = new TextField("Nom");
        TextField email = new TextField("Email");
        TextField postCode = new TextField("Code Postal");
        
        PasswordField password = new PasswordField("Password");
        PasswordField confirmPassword = new PasswordField("Confirm password");
        
        Button valider = new Button("Valider");
        valider.setEnabled(false);
        //activer le bouton valider
        confirmPassword.addInputListener(event ->  {
            valider.setEnabled(true);
        });
        Button Retour = new Button ("Retour",new Icon(VaadinIcon.ARROW_LEFT));
        
        valider.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
        this.add(firstName,lastName, email, postCode, password,confirmPassword,valider, Retour);
        valider.addClickListener((event) -> {
            this.main.setMainContent(new PageAccueilSite(this.main));   
         });
        Retour.addThemeVariants(ButtonVariant.LUMO_ERROR);
        Retour.addClickListener((event) -> {
            this.main.setMainContent(new PageAccueil(this.main));   
         });
    }
}
