package com.pi4j.components.tiles.Skins;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.events.TileEvt;
import eu.hansolo.tilesfx.fonts.Fonts;
import eu.hansolo.tilesfx.skins.TileSkin;
import eu.hansolo.tilesfx.tools.Helper;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class LedButtonSkin extends TileSkin {

    private Circle      ledBorder;
    private Circle      led;

    private Paint       borderFill;
    private Paint       onFill;
    private Paint       offFill;


    private Text titleText;
    private Text text;

    private EventHandler<MouseEvent> mouseHandler;


    public LedButtonSkin(Tile TILE) {
        super(TILE);
    }


    @Override
    protected void initGraphics (){
        super.initGraphics();

        mouseHandler = event -> {
            final javafx.event.EventType<? extends MouseEvent> TYPE = event.getEventType();
            if (MouseEvent.MOUSE_CLICKED.equals(TYPE)) {
                tile.setActive(!tile.isActive());
            }
        };

        titleText = new Text();
        titleText.setFill(tile.getTitleColor());
        Helper.enableNode(titleText, !tile.getTitle().isEmpty());

        text = new Text(tile.getText());
        text.setFill(tile.getUnitColor());
        Helper.enableNode(text, tile.isTextVisible());


        ledBorder  = new Circle();
        led        = new Circle();

        tile.setActiveColor(Color.rgb(0,255,0));


        getPane().getChildren().addAll(titleText, text, ledBorder, led);

    }

    @Override
    protected void registerListeners(){
        super.registerListeners();
        led.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseHandler);
    }


    @Override
    protected void handleEvents(final String EVENT_TYPE) {
        super.handleEvents(EVENT_TYPE);

        if (TileEvt.VISIBILITY.getName().equals(EVENT_TYPE)) {
            Helper.enableNode(titleText, !tile.getTitle().isEmpty());
            Helper.enableNode(text, tile.isTextVisible());
        }
        else if (TileEvt.REDRAW.getName().equals(EVENT_TYPE)) {
            updateFills();
            redraw();
        }
    }


    @Override protected void handleCurrentValue(final double VALUE) {
        led.setFill(tile.isActive() ? onFill : offFill);
    }

    private void updateFills() {
        if (tile.isFlatUI()) {
            borderFill    = Color.rgb(200, 200, 200, 0.5);
            onFill = tile.getActiveColor();
            offFill = tile.getActiveColor().darker().darker().darker().darker();
            led.setEffect(null);
        }
        else {

            borderFill = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, new Stop(0.0, Color.rgb(20, 20, 20, 0.65)), new Stop(0.15, Color.rgb(20, 20, 20, 0.65)),
                new Stop(0.26, Color.rgb(41, 41, 41, 0.65)), new Stop(0.26, Color.rgb(41, 41, 41, 0.64)), new Stop(0.85, Color.rgb(200, 200, 200, 0.41)),
                new Stop(1.0, Color.rgb(200, 200, 200, 0.35)));

            final Color ledColor = tile.getActiveColor();

            onFill = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, new Stop(0.0, ledColor.deriveColor(0d, 1d, 0.77, 1d)),
                new Stop(0.49, ledColor.deriveColor(0d, 1d, 0.5, 1d)), new Stop(1.0, ledColor));

            offFill = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, new Stop(0.0, ledColor.deriveColor(0d, 1d, 0.20, 1d)),
                new Stop(0.49, ledColor.deriveColor(0d, 1d, 0.13, 1d)), new Stop(1.0, ledColor.deriveColor(0d, 1d, 0.2, 1d)));

        }
    }

    @Override protected void resizeStaticText() {
        double maxWidth = width - size * 0.1;
        double fontSize = size * textSize.factor;

        boolean customFontEnabled = tile.isCustomFontEnabled();
        Font customFont        = tile.getCustomFont();
        Font font              = (customFontEnabled && customFont != null) ? Font.font(customFont.getFamily(), fontSize) : Fonts.latoRegular(fontSize);

        titleText.setFont(font);
        if (titleText.getLayoutBounds().getWidth() > maxWidth) { Helper.adjustTextSize(titleText, maxWidth, fontSize); }
        switch(tile.getTitleAlignment()) {
        default    :
        case LEFT  : titleText.relocate(size * 0.05, size * 0.05); break;
        case CENTER: titleText.relocate((width - titleText.getLayoutBounds().getWidth()) * 0.5, size * 0.05); break;
        case RIGHT : titleText.relocate(width - (size * 0.05) - titleText.getLayoutBounds().getWidth(), size * 0.05); break;
        }

        text.setText(tile.getText());
        text.setFont(font);
        if (text.getLayoutBounds().getWidth() > maxWidth) { Helper.adjustTextSize(text, maxWidth, fontSize); }
        switch(tile.getTextAlignment()) {
        default    :
        case LEFT  : text.setX(size * 0.05); break;
        case CENTER: text.setX((width - text.getLayoutBounds().getWidth()) * 0.5); break;
        case RIGHT : text.setX(width - (size * 0.05) - text.getLayoutBounds().getWidth()); break;
        }
        text.setY(height - size * 0.05);
    }

    @Override protected void resize() {
        super.resize();

        updateFills();

        ledBorder.setRadius(size * 0.19);
        ledBorder.setCenterX(width * 0.5);
        ledBorder.setCenterY(height * 0.5);

        led.setRadius(size * 0.15);
        led.setCenterX(width * 0.5);
        led.setCenterY(height * 0.5);

    }

    @Override
    protected void redraw(){
        super.redraw();

        titleText.setText(tile.getTitle());
        text.setText(tile.getText());

        resizeStaticText();

        titleText.setFill(tile.getTitleColor());
        text.setFill(tile.getTextColor());

        ledBorder.setFill(borderFill);
        led.setFill(tile.isActive() ? onFill : offFill);
    }



}