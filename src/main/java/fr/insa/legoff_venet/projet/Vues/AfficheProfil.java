/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.legoff_venet.projet.Vues;

import static com.fasterxml.jackson.databind.util.ClassUtil.name;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import fr.insa.legoff_venet.projet.VuePrincipale;
import static org.apache.tomcat.jni.User.username;

/**
 *
 * @author laura
 */
public class AfficheProfil extends MyVerticalLayout {
    
    private VuePrincipale main;
        
    public AfficheProfil (VuePrincipale main){
        this.main = main;
        
        //Ajout du profil
        Avatar avatar = new Avatar ();
        avatar.setHeight("150px");
        avatar.setWidth("150px");
        
        //ptete rajouter un get qqchose pour choper l'email 
        Label nom = new Label("Nom");
        nom.getStyle().set("font-size", "20px");
        nom.getStyle().set("font-weight", "bold");
        
        Label prenom = new Label("Prénom");
        prenom.getStyle().set("font-size", "20px");
        prenom.getStyle().set("font-weight", "bold");
        
        Label email = new Label("Email");
        email.getStyle().set("font-size", "20px");
        email.getStyle().set("font-weight", "bold");
        
        Label postcode = new Label("Code Postal");
        postcode.getStyle().set("font-size", "20px");
        postcode.getStyle().set("font-weight", "bold");
        
        add(avatar, nom, prenom, email, postcode);
    
       //Bouton d'affichage des Ventes
       Button Mesventes = new Button ("Mes Ventes",new Icon(VaadinIcon.WALLET));
       Mesventes.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);
       Mesventes.addClickListener((event)-> {
        this.main.setMainContent(new MesVentes(this.main));   
       });
       
       //Bouton d'affichage des Enchères
       Button MesEncheres = new Button ("Mes Enchères",new Icon(VaadinIcon.CART));
       MesEncheres.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);
       MesEncheres.addClickListener((event)-> {
        this.main.setMainContent(new MesVentes(this.main));   
       });
        
        //Button de retour à la page d'accueil site
        Button home = new Button(new Icon(VaadinIcon.HOME));
        home.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        home.addClickListener((event)-> {
            this.main.setMainContent(new PageAccueilSite(this.main));
        });
        
        add (Mesventes, MesEncheres, home);
    }
}
