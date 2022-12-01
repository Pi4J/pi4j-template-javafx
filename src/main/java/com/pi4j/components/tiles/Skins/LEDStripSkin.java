package com.pi4j.components.tiles.Skins;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.events.TileEvt;
import eu.hansolo.tilesfx.fonts.Fonts;
import eu.hansolo.tilesfx.skins.TileSkin;
import eu.hansolo.tilesfx.tools.Helper;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class LEDStripSkin extends TileSkin {

    public Circle[] ledBorder;

    public Circle[] leds;

    public Circle[] brightness;

    private Text    titleText;
    private Text    text;

    private static int amountLight;

    public LEDStripSkin(Tile TILE) {
        super (TILE);
    }

    @Override
    protected void initGraphics() {
        super.initGraphics();
        amountLight = 4;
        leds = new Circle[amountLight];
        brightness = new Circle[amountLight];
        ledBorder = new Circle[amountLight];
        titleText = new Text();
        titleText.setFill(tile.getTitleColor());
        Helper.enableNode(titleText, !tile.getTitle().isEmpty());

        text = new Text(tile.getText());
        text.setFill(tile.getUnitColor());
        Helper.enableNode(text, tile.isTextVisible());

        Color fill = Color.BLACK;
        Color border = Color.GREY;

        getPane().getChildren().addAll(titleText,text);

        initRow(fill, border, ledBorder, leds, brightness);
    }

    @Override
    protected void registerListeners(){
        super.registerListeners();
    }

    @Override
    protected void handleEvents(final String EVENT_TYPE) {
        super.handleEvents(EVENT_TYPE);

        if (TileEvt.VISIBILITY.getName().equals(EVENT_TYPE)) {
            Helper.enableNode(titleText, !tile.getTitle().isEmpty());
            Helper.enableNode(text, tile.isTextVisible());
        }
        else if (TileEvt.REDRAW.getName().equals(EVENT_TYPE)) {
            redraw();
        }
    }

    @Override protected void handleCurrentValue(final double VALUE) {
    }



    @Override protected void resizeStaticText() {
        double maxWidth = width - size * 0.1;
        double fontSize = size * textSize.factor;

        boolean customFontEnabled = tile.isCustomFontEnabled();
        Font customFont        = tile.getCustomFont();
        Font font              = (customFontEnabled && customFont != null) ? Font.font(customFont.getFamily(), fontSize) : Fonts.latoRegular(fontSize);

        titleText.setFont(font);
        if (titleText.getLayoutBounds().getWidth() > maxWidth) { Helper.adjustTextSize(titleText, maxWidth, fontSize); }
        switch (tile.getTitleAlignment()) {
            case LEFT -> titleText.relocate(size * 0.05, size * 0.05);
            case CENTER -> titleText.relocate((width - titleText.getLayoutBounds().getWidth()) * 0.5, size * 0.05);
            case RIGHT -> titleText.relocate(width - (size * 0.05) - titleText.getLayoutBounds().getWidth(), size * 0.05);
        }

        text.setText(tile.getText());
        text.setFont(font);
        if (text.getLayoutBounds().getWidth() > maxWidth) { Helper.adjustTextSize(text, maxWidth, fontSize); }
        switch (tile.getTextAlignment()) {
            case LEFT -> text.setX(size * 0.05);
            case CENTER -> text.setX((width - text.getLayoutBounds().getWidth()) * 0.5);
            case RIGHT -> text.setX(width - (size * 0.05) - text.getLayoutBounds().getWidth());
        }
        text.setY(height - size * 0.05);
    }

    @Override
    protected void resize(){
        super.resize();

        for( int i = 0; i< amountLight;i++){
            double borderRadius = size * 0.07;
            double ledRadius = size * 0.045;
            double centerX = this.width * ((1+i)*0.2);
            double centerY = height * 0.5;

            resizeRow(i, borderRadius, ledRadius, centerX, centerY, ledBorder, leds, brightness);
        }

    }

    @Override
    protected void redraw() {
        super.redraw();

        titleText.setText(tile.getTitle());
        text.setText(tile.getText());
        resizeStaticText();
        titleText.setFill(tile.getTitleColor());
        text.setFill(tile.getTextColor());

    }

    protected void initRow(Color fill, Color border, Circle[] ledBorder, Circle[] leds,
                        Circle[] brightness) {
        for (int i = 0; i < amountLight; i++){
            ledBorder[i] = new Circle();
            leds[i] = new Circle();
            brightness[i] = new Circle();

            ledBorder[i].setFill(border);
            leds[i].setFill(fill);
            brightness[i].setFill(fill);
            brightness[i].setOpacity(0.0);

            getPane().getChildren().add(ledBorder[i]);
            getPane().getChildren().add(leds[i]);
            getPane().getChildren().add(brightness[i]);
        }
    }

     static void resizeRow(int i, double borderRadius, double ledRadius, double centerX, double centerY,
                           Circle[] ledBorder, Circle[] leds, Circle[] brightness) {
        ledBorder[i].setRadius(borderRadius);
        ledBorder[i].setCenterX(centerX);
        ledBorder[i].setCenterY(centerY);

        leds[i].setRadius(ledRadius);
        leds[i].setCenterX(centerX);
        leds[i].setCenterY(centerY);

        brightness[i].setRadius(ledRadius);
        brightness[i].setCenterX(centerX);
        brightness[i].setCenterY(centerY);
    }
}
