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

public class LedMatrixSkin extends TileSkin {

    public Circle[][] ledBorder;

    public Circle[][] leds;

    public Circle[][] brightnessOverlay;

    private Text    titleText;
    private Text    text;

    private int amountLeds = 4;
    private int amountRow = 2;

    public LedMatrixSkin(Tile TILE) {
        super (TILE);
    }

    @Override
    public void initGraphics() {
        super.initGraphics();

        ledBorder = new Circle[amountRow][amountLeds];
        leds = new Circle[amountRow][amountLeds];
        brightnessOverlay = new Circle[amountRow][amountLeds];

        titleText = new Text();
        titleText.setFill(tile.getTitleColor());
        Helper.enableNode(titleText, !tile.getTitle().isEmpty());

        text = new Text(tile.getText());
        text.setFill(tile.getUnitColor());
        Helper.enableNode(text, tile.isTextVisible());

        Color fill = Color.BLACK;
        Color border = Color.GREY;

        getPane().getChildren().addAll(titleText,text);

        initRow(fill, border, ledBorder, leds, brightnessOverlay);

    }


    @Override
    public void registerListeners(){
        super.registerListeners();

    }

    @Override
    public void handleEvents(final String EVENT_TYPE) {
        super.handleEvents(EVENT_TYPE);

        if (TileEvt.VISIBILITY.getName().equals(EVENT_TYPE)) {
            Helper.enableNode(titleText, !tile.getTitle().isEmpty());
            Helper.enableNode(text, tile.isTextVisible());
        }
        else if (TileEvt.REDRAW.getName().equals(EVENT_TYPE)) {
            redraw();
        }
    }

    @Override
    public void handleCurrentValue(final double VALUE) {
    }



    @Override
    public void resizeStaticText() {
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
    public void resize(){
        super.resize();
        resizeRow(ledBorder,leds,brightnessOverlay);
    }

    @Override
    public void redraw() {
        super.redraw();

        titleText.setText(tile.getTitle());
        text.setText(tile.getText());
        resizeStaticText();
        titleText.setFill(tile.getTitleColor());
        text.setFill(tile.getTextColor());

    }

    public int getAmountRow() {
        return amountRow;
    }

    public int getAmountLeds() {
        return amountLeds;
    }

    public void setAmountLeds(int amountLeds) {
        this.amountLeds = amountLeds;
    }

    public void setAmountRow(int amountRow) {
        if(amountRow < 1 || amountRow > 4){
            this.amountRow = 4;
        } else {
            this.amountRow = amountRow;
        }
    }

    //initialize Circle[][] ledBorder, Circle[][] leds & Circle[][] brightness and add to the pane
    protected void initRow(Color fill, Color border, Circle[][] ledBorder, Circle[][] leds,
                           Circle[][] brightness) {

        for (int j = 0; j < this.getAmountRow(); j++) {

            for (int i = 0; i < this.getAmountLeds(); i++) {
                ledBorder[j][i] = new Circle();
                leds[j][i] = new Circle();
                brightness[j][i] = new Circle();

                ledBorder[j][i].setFill(border);
                leds[j][i].setFill(fill);
                brightness[j][i].setFill(fill);

                getPane().getChildren().add(ledBorder[j][i]);
                getPane().getChildren().add(leds[j][i]);
                getPane().getChildren().add(brightness[j][i]);
            }
        }
    }

    void resizeRow(Circle[][] ledBorder, Circle[][] leds, Circle[][] brightness) {

        for (int j = 0; j < this.getAmountRow(); j++) {

            for (int i = 0; i < this.getAmountLeds(); i++) {
                double borderRadius = size * 0.07;
                double ledRadius = size * 0.045;
                double centerX = this.width * ((1+i)*0.2);

                double centerY;

                if(this.getAmountRow() == 4){
                    centerY = height * (0.25 + (0.175 * j));
                }
                else if(this.getAmountRow() == 3){
                    centerY = height * (0.3 + (0.2 * j));
                }
                else {
                    centerY = height * (0.4 + (0.2 * j));
                }

                ledBorder[j][i].setRadius(borderRadius);
                ledBorder[j][i].setCenterX(centerX);
                ledBorder[j][i].setCenterY(centerY);

                leds[j][i].setRadius(ledRadius);
                leds[j][i].setCenterX(centerX);
                leds[j][i].setCenterY(centerY);

                brightness[j][i].setRadius(ledRadius);
                brightness[j][i].setCenterX(centerX);
                brightness[j][i].setCenterY(centerY);
            }
        }
    }
}
