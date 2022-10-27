package com.pi4j.mvc.tilesapp.view.gui;

import com.pi4j.components.tiles.LedButtonTile;
import com.pi4j.components.tiles.Skins.LedButtonSkin;
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

    private static final String LIGHT_BULB = "\uf0eb";  // the unicode of the lightbulb-icon in fontawesome font

    // declare all the UI elements you need
//    private Button ledButton;
//    private Button increaseButton;
//    private Label  counterLabel;
//    private Label  infoLabel;

    private Tile ledTile;
    private Tile switchTile;

//    private Tile ledButtonTile;

    private LedButtonTile ledButtonTileClass;

    private int tilesize = 400;

    public SomeGUI(SomeController controller) {
        super(3,1);
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
//        ledButton = new Button(LIGHT_BULB);
//        ledButton.getStyleClass().add("icon-button");
//
//        increaseButton = new Button("+");
//
//        counterLabel = new Label();
//        counterLabel.getStyleClass().add("counter-label");
//
//        infoLabel = new Label();
//        infoLabel.getStyleClass().add("info-label");

        ledButtonTileClass = new LedButtonTile();

        ledTile = TileBuilder.create()
            .prefSize(tilesize,tilesize)
            .skinType(Tile.SkinType.LED)
            .title("Simple LED")
            .text("Bottom text")
            .build();

        switchTile = TileBuilder.create()
            .prefSize(tilesize,tilesize)
            .skinType(Tile.SkinType.SWITCH)
            .title("Simple Switch")
            .text("Bottom text")
            .build();

//        ledButtonTile = TileBuilder.create()
//            .prefSize(tilesize,tilesize)
//            .skinType(Tile.SkinType.CUSTOM)
//            .title("LED Button")
//            .text("Bottom text")
//            .build();
//
//        ledButtonTile.setSkin(new LedButtonSkin(ledButtonTile));


    }

    @Override
    public void layoutParts() {
        getChildren().addAll(ledTile, switchTile, ledButtonTileClass);

    }

    @Override
    public void setupUiToActionBindings(SomeController controller) {
        // look at that: all EventHandlers just trigger an action on 'controller'
        // by calling a single method



//        increaseButton.setOnAction  (event -> controller.increaseCounter());
//        ledButton.setOnMousePressed (event -> controller.setIsActive(true));
//        ledButton.setOnMouseReleased(event -> controller.setIsActive(false));
    }





//    @Override
//    public void setupModelToUiBindings(SomeModel model) {
//        onChangeOf(model.systemInfo)                       // the value we need to observe, in this case that's an ObservableValue<String>, no need to convert it
//                .update(infoLabel.textProperty());         // keeps textProperty and systemInfo in sync
//
//        onChangeOf(model.counter)                          // the value we need to observe, in this case that's an ObservableValue<Integer>
//                .convertedBy(String::valueOf)              // we have to convert the Integer to a String
//                .update(counterLabel.textProperty());      // keeps textProperty and counter in sync
//    }
}
