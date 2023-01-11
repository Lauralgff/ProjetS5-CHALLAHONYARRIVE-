/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.legoff_venet.projet.Vues;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import fr.insa.legoff_venet.projet.VuePrincipale;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author laura
 */
public class PageAccueilSite extends MyVerticalLayout {
    
    public VuePrincipale main;
    private List<String> items = new ArrayList<>(
            Arrays.asList("Meubles", "Habits", "Sport"));
    public TextField textfield = new TextField ();
    public ComboBox<String> RechercheCat = new ComboBox<>();
    public Button Deconnextion = new Button ("Déconnexion",new Icon(VaadinIcon.ARROW_LEFT));
    public Button Profil = new Button( "Mon profil",new Icon(VaadinIcon.USER));
    public Button AVendre = new Button ("Vendre",new Icon(VaadinIcon.WALLET));
    
    public PageAccueilSite(VuePrincipale main) {
        
        this.main = main;
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
        
        
        //Bouton de connexion, retour à la page d'accueil       
        
        this.Deconnextion.addThemeVariants(ButtonVariant.LUMO_ERROR);
        this.Deconnextion.addClickListener((event) -> {
            this.main.setMainContent(new PageAccueil(this.main)); 
            this.main.entete.removeAll();
         });
        add (Deconnextion);
    
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
    
     this.main.entete.add(Profil,AVendre, textfield, RechercheCat); 
     
    
}
}  

    