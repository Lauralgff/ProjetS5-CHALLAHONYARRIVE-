/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.legoff_venet.projet.Vues;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
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

    
    public PageAccueilSite(VuePrincipale main) {
        
        this.main = main;
        //Barre de recherche par catégories
        ComboBox<String> RechercheCat = new ComboBox<>("Catégories");
        RechercheCat.setAllowCustomValue(true);
        RechercheCat.addCustomValueSetListener(e -> {
            String customValue = e.getDetail();
            items.add(customValue);
            RechercheCat.setItems(items);
            RechercheCat.setValue(customValue);
        });
        //Barre de recherche textuelle
        TextField textField = new TextField();
        textField.setPlaceholder("Search");
        textField.setPrefixComponent(VaadinIcon.SEARCH.create());
        RechercheCat.setItems(items);
        RechercheCat.setHelperText("Sélectionnez une catégorie");
        
        add (textField);
        add (RechercheCat);
       
        Button Deconnextion = new Button ("Déconnextion",new Icon(VaadinIcon.ARROW_LEFT));
        Deconnextion.addThemeVariants(ButtonVariant.LUMO_ERROR);
        Deconnextion.addClickListener((event) -> {
            this.main.setMainContent(new PageAccueil(this.main));   
         });
        add (Deconnextion);
        
    Button Profil = new Button("Mon profil",
                new Icon(VaadinIcon.USER));
    Profil.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);
    Profil.addClickListener((event)-> {
        this.main.setMainContent(new AfficheProfil(this.main));   
     });
    
     Button AVendre = new Button("Vendre",
                new Icon(VaadinIcon.WALLET));
     AVendre.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);
     AVendre.addClickListener((event)-> {
        this.main.setMainContent(new CreerEncheres(this.main));   
     });
     
     
        
    
     this.main.entete.add(Profil,AVendre); 
     
    
}
}  

    