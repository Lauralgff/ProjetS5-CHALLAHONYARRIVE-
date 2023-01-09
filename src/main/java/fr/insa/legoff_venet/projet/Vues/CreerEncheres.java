/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.legoff_venet.projet.Vues;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import fr.insa.legoff_venet.projet.VuePrincipale;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 *
 * @author laura
 */
public class CreerEncheres extends MyVerticalLayout {
    
    private VuePrincipale main;
    private List<String> items = new ArrayList<>(
            Arrays.asList("Meubles", "Habits", "Sport"));
    
    public CreerEncheres(VuePrincipale main) {

        this.main = main;
        
        this.add(new H1("Créez votre enchère !"));
        TextField title = new TextField("Titre");
        TextField prix = new TextField();
        prix.setSuffixComponent(new Span("EUR"));
        TextArea description = new TextArea();
        description.setWidthFull();
        description.setLabel("Description");   
        DatePicker date = new DatePicker("Date de fin");
        TimePicker to = new TimePicker("Heure de fin");
        ComboBox<String> RechercheCat = new ComboBox<>("Catégories");
        RechercheCat.setAllowCustomValue(true);
        RechercheCat.addCustomValueSetListener(e -> {
            String customValue = e.getDetail();
            items.add(customValue);
            RechercheCat.setItems(items);
            RechercheCat.setValue(customValue);
        });

        FormLayout formLayout = new FormLayout();
        formLayout.add(title, prix, RechercheCat, description, date,  to);
        formLayout.setColspan(description, 3);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2));
        add(formLayout);
        
        Button home = new Button(new Icon(VaadinIcon.HOME));
        home.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        home.addClickListener((event)-> {
        this.main.setMainContent(new PageAccueilSite(this.main));
        });
        
        Button valider = new Button ("Valider");
        valider.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
         valider.addClickListener(clickEvent -> {
            valider.setEnabled(false);

            Notification notification = Notification
                    .show("Enchère ajoutée :)");
            notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
            notification.setPosition(Notification.Position.MIDDLE);

            notification
                    .addDetachListener(detachEvent -> valider.setEnabled(true));
            });
            
        add (valider, home);
    }
}