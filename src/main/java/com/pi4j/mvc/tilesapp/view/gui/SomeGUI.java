package com.pi4j.mvc.tilesapp.view.gui;

import com.pi4j.components.tiles.LedButtonTile;
import com.pi4j.components.tiles.SimpleButtonTile;
import com.pi4j.components.tiles.SimpleLEDTile;
import com.pi4j.mvc.tilesapp.controller.SomeController;
import com.pi4j.mvc.tilesapp.model.SomeModel;
import com.pi4j.mvc.util.mvcbase.ViewMixin;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.tools.FlowGridPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class SomeGUI extends FlowGridPane implements ViewMixin<SomeModel, SomeController> { //all GUI-elements have to implement ViewMixin

    // declare all the UI elements you need

    private Tile switchTile;
    private SimpleLEDTile ledTile;
    private SimpleButtonTile buttonTile;

    private LedButtonTile ledButtonTile;

    private int tilesize = 400;

    public SomeGUI(SomeController controller) {
        super(5,1);
        setHgap(5);
        setVgap(5);
        setAlignment(Pos.CENTER);
        setCenterShape(true);
        setPadding(new Insets(5));
//        setPrefSize(3000, 3000);
        setBackground(new Background(new BackgroundFill(Color.web("#101214"), CornerRadii.EMPTY, Insets.EMPTY)));

        init(controller); //don't forget to call 'init'
    }

    @Override
    public void initializeSelf() {
        //load all fonts you need
        loadFonts("/fonts/Lato/Lato-Lig.ttf", "/fonts/fontawesome-webfont.ttf");

        //apply your style
        addStylesheetFiles("/mvc/tilesapp/style.css");

        getStyleClass().add("root-pane");
        
        
    }

    @Override
    public void initializeParts() {

        ledButtonTile = new LedButtonTile();

        ledTile = new SimpleLEDTile();

        buttonTile = new SimpleButtonTile();

        switchTile = TileBuilder.create()
            .prefSize(tilesize,tilesize)
            .skinType(Tile.SkinType.SWITCH)
            .title("Simple Switch")
            .text("Bottom text")
            .build();
    }

    @Override
    public void layoutParts() {
        getChildren().addAll(switchTile, ledTile, buttonTile);
    }

    @Override
    public void setupUiToActionBindings(SomeController controller) {
        // look at that: all EventHandlers just trigger an action on 'controller'
        // by calling a single method

        buttonTile.setOnMousePressed (event -> controller.setButtonPressed(true));
        buttonTile.setOnMouseReleased(event -> controller.setButtonPressed(false));
    }

//    @Override
    public void setupModelToUiBindings(SomeModel model) {

        onChangeOf(model.isLedActive)
            .execute((oldValue, newValue) -> {
                if (newValue) {
                    ledTile.on();
                } else {
                    ledTile.off();
                }
            });

        //press button to turn on/off SimpleLED
        onChangeOf(model.isButtonPressed)
            .execute((oldValue, newValue) -> {
                if (newValue) {
                    ledTile.on();
                } else {
                    ledTile.off();
                }
            });
    }
}
