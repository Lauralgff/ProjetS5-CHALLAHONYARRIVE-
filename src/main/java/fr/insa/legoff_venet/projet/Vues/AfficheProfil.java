/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.legoff_venet.projet.Vues;

import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import fr.insa.legoff_venet.projet.VuePrincipale;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author laura
 */
public class AfficheProfil extends MyVerticalLayout {
    
    private VuePrincipale main;
    public TextField textfield = new TextField ();
    public ComboBox<String> RechercheCat = new ComboBox<>();
    Button Deconnexion = new Button(new Icon(VaadinIcon.POWER_OFF));
    public Button Profil = new Button( "Mon profil",new Icon(VaadinIcon.USER));
    public Button AVendre = new Button ("Vendre",new Icon(VaadinIcon.WALLET));
    private List<String> items = new ArrayList<>(
            Arrays.asList("Meubles", "Habits", "Sport"));
    
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
        
        //Barre de recherche par catégories
        this.RechercheCat.setAllowCustomValue(true);
        this.RechercheCat.setPlaceholder("Rechercher par catégorie");
        this.RechercheCat.addCustomValueSetListener(e -> {
            String customValue = e.getDetail();
            items.add(customValue);
            RechercheCat.setItems(items);
            RechercheCat.setValue(customValue);
        });
        RechercheCat.setItems(items);
        
        //Barre de recherche textuelle
        this.textfield.setPlaceholder("Search");
        this.textfield.setPrefixComponent(VaadinIcon.SEARCH.create());
        
        //Bouton profil, entete
        this.Profil.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);
        this.Profil.addClickListener((event)-> {
            this.main.setMainContent(new AfficheProfil(this.main));   
        });
    
        //Bouton Vendre, entete
        this.AVendre.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);
        this.AVendre.addClickListener((event)-> {
            this.main.setMainContent(new CreerEncheres(this.main));   
        });
           
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
 
       //Bouton de deconnexion, retour à la page d'accueil
        this.Deconnexion.addThemeVariants(ButtonVariant.LUMO_ERROR);
        this.Deconnexion.addClickListener((event) -> {
            this.main.setMainContent(new PageAccueil(this.main)); 
            this.main.entete.removeAll();
         });
        //Button de retour à la page d'accueil site
        Button home = new Button(new Icon(VaadinIcon.HOME));
        home.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        home.addClickListener((event)-> {
            this.main.setMainContent(new PageAccueilSite(this.main));
            this.main.entete.removeAll();
            this.main.entete.add(Profil,AVendre, textfield, RechercheCat,Deconnexion);
        });
        
      
        add (Mesventes, MesEncheres, home);
    }
}
