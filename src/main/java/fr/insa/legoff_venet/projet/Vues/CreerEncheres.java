/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.legoff_venet.projet.Vues;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import fr.insa.legoff_venet.projet.VuePrincipale;


/**
 *
 * @author laura
 */
public class CreerEncheres extends MyVerticalLayout {
    
    private VuePrincipale main;
    
    public CreerEncheres(VuePrincipale main) {

        this.main = main;
        
        TextField title = new TextField("Titre");
        TextArea description = new TextArea();
        description.setWidthFull();
        description.setLabel("Description");
        TextField prix = new TextField();
        prix.setSuffixComponent(new Span("EUR"));
        DatePicker date = new DatePicker("Date de fin");
        TimePicker to = new TimePicker("Heure de fin");

        FormLayout formLayout = new FormLayout();
        formLayout.addFormItem(prix, "Prix");
        formLayout.add(title, description, date, prix, to);
        formLayout.setColspan(title, 3);
        formLayout.setResponsiveSteps(new ResponsiveStep("0", 1),
                new ResponsiveStep("500px", 3));
        add(formLayout);
    }
}

