/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.legoff_venet.projet;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.legoff_venet.projet.Vues.MyHorizontalLayout;
import fr.insa.legoff_venet.projet.Vues.MyVerticalLayout;
import fr.insa.legoff_venet.projet.Vues.PageAccueil;


/**
 *
 * @author laura
 */
  @Route(value = "")
@PageTitle("Projet Encheres")
public class VuePrincipale extends VerticalLayout {
    

    public MyHorizontalLayout entete;
    public MyVerticalLayout mainContent;

    public void setEntete(Component c) {
        this.entete.removeAll();
        this.entete.add(c);
    }

public void setMainContent(Component c) {
        this.mainContent.removeAll();
        this.mainContent.add(c);
    }

    public VuePrincipale() {
        this.entete = new MyHorizontalLayout();
        this.entete.setWidthFull();
        this.add(this.entete);

        this.mainContent = new MyVerticalLayout();
        this.mainContent.setWidthFull();
        this.mainContent.setHeightFull();
        this.mainContent.setJustifyContentMode(JustifyContentMode.CENTER);
        this.mainContent.setAlignItems(FlexComponent.Alignment.STRETCH);
        this.add(this.mainContent);
        this.setMainContent(new PageAccueil(this));
    }  
}
