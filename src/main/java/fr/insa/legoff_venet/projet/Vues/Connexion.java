/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.legoff_venet.projet.Vues;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import fr.insa.legoff_venet.projet.VuePrincipale;

/**
 *
 * @author laura
 */
public class Connexion extends MyVerticalLayout{    
    
    private VuePrincipale main;
              
    public Connexion(VuePrincipale main) {
        this.main = main;
        TextField lastName = new TextField("Email");
        PasswordField password = new PasswordField("Password"); 
        Button login = new Button ("Connexion");
        Button Retour = new Button ("Retour",new Icon(VaadinIcon.ARROW_LEFT));
        login.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
        this.add(lastName, password, login, Retour);
        login.addClickListener((event) -> {
            this.main.setMainContent(new PageAccueilSite(this.main));   
         });
        Retour.addThemeVariants(ButtonVariant.LUMO_ERROR);
        Retour.addClickListener((event) -> {
            this.main.setMainContent(new PageAccueil(this.main));   
         });
    }
}
