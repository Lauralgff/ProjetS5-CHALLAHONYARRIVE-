/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.legoff_venet.projet.Vues;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 *
 * @author laura
 */
public class MyVerticalLayout extends VerticalLayout {

    public static String CSS_COLOR = "red";

    public MyVerticalLayout(Component... children) {
        super(children);
        if (ConfigGenerale.ENCADRE_LAYOUT) {
            Paragraph nom = new Paragraph(this.getClass().getSimpleName());
            nom.getStyle().set("font-family", "Monospace")
                    .set("font-size", "0.8em").set("color", CSS_COLOR)
                    .set("font-style", "italic");
            this.addComponentAsFirst(nom);
            this.getStyle().set("margin-top", "32px"+CSS_COLOR);

        }
    }

}
