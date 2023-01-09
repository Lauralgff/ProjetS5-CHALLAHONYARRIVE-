/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.legoff_venet.projet.Vues;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import fr.insa.legoff_venet.projet.VuePrincipale;

/**
 *
 * @author laura
 */
public class AfficheProfil extends MyVerticalLayout {
    
    private VuePrincipale main;
    
    public AfficheProfil (VuePrincipale main){
        this.main = main;
        
        Button home = new Button(new Icon(VaadinIcon.HOME));
        home.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        home.addClickListener((event)-> {
        this.main.setMainContent(new PageAccueilSite(this.main));
        });
        
        add (home);
    }
}
