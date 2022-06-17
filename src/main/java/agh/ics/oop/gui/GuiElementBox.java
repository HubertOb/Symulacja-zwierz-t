package agh.ics.oop.gui;


import agh.ics.oop.Animal;
import agh.ics.oop.Grass;
import agh.ics.oop.IMapElement;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static java.lang.String.valueOf;


public class GuiElementBox {
    private final VBox vbox=new VBox();
    //private Image image;
    public GuiElementBox(IMapElement o) throws FileNotFoundException {
        if (o instanceof Animal) {
            Label label = new Label(valueOf(((Animal) o).getEnergy()));
            vbox.getChildren().addAll(label);
            vbox.setAlignment(Pos.CENTER);
        }
        else if(o instanceof Grass){
            Label label=new Label("*");
            vbox.getChildren().addAll(label);
            vbox.setAlignment(Pos.CENTER);
        }
    }


    public VBox getVbox() {
        return vbox;
    }

}
