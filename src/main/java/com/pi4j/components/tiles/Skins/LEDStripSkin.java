package com.pi4j.components.tiles.Skins;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.events.TileEvt;
import eu.hansolo.tilesfx.fonts.Fonts;
import eu.hansolo.tilesfx.skins.TileSkin;
import eu.hansolo.tilesfx.tools.Helper;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class LEDStripSkin extends TileSkin {

    private Circle  ledBorder1;
    private Circle  ledBorder2;
    private Circle  ledBorder3;
    private Circle  ledBorder4;

    public Circle[] leds;

    private Text    titleText;
    private Text    text;

    public LEDStripSkin(Tile TILE) {
        super (TILE);
    }

    @Override
    protected void initGraphics() {
        super.initGraphics();

        leds = new Circle[4];
        titleText = new Text();
        titleText.setFill(tile.getTitleColor());
        Helper.enableNode(titleText, !tile.getTitle().isEmpty());

        text = new Text(tile.getText());
        text.setFill(tile.getUnitColor());
        Helper.enableNode(text, tile.isTextVisible());

        Color fill = Color.BLACK;
        Color border = Color.GREY;

        ledBorder1 = new Circle();
        ledBorder1.setFill(border);
        leds[0] = new Circle();
        leds[0].setFill(fill);

        ledBorder2 = new Circle();
        ledBorder2.setFill(border);
        leds[1] = new Circle();
        leds[1].setFill(fill);

        ledBorder3 = new Circle();
        ledBorder3.setFill(border);
        leds[2] = new Circle();
        leds[2].setFill(fill);

        ledBorder4 = new Circle();
        ledBorder4.setFill(border);
        leds[3] = new Circle();
        leds[3].setFill(fill);

        getPane().getChildren().addAll(titleText,text, ledBorder1, ledBorder2, ledBorder3, ledBorder4);

        for (Circle led : leds) {
            getPane().getChildren().add(led);
        }
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
//        led.setFill(tile.isActive() ? onFill : offFill);
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
        ledBorder1.setRadius(size * 0.07);
        ledBorder1.setCenterX(width * 0.2);
        ledBorder1.setCenterY(height * 0.5);
        leds[0].setRadius(size * 0.045);
        leds[0].setCenterX(width * 0.2);
        leds[0].setCenterY(height * 0.5);

        ledBorder2.setRadius(size * 0.07);
        ledBorder2.setCenterX(width * 0.4);
        ledBorder2.setCenterY(height * 0.5);
        leds[1].setRadius(size * 0.045);
        leds[1].setCenterX(width * 0.4);
        leds[1].setCenterY(height * 0.5);

        ledBorder3.setRadius(size * 0.07);
        ledBorder3.setCenterX(width * 0.6);
        ledBorder3.setCenterY(height * 0.5);
        leds[2].setRadius(size * 0.045);
        leds[2].setCenterX(width * 0.6);
        leds[2].setCenterY(height * 0.5);

        ledBorder4.setRadius(size * 0.07);
        ledBorder4.setCenterX(width * 0.8);
        ledBorder4.setCenterY(height * 0.5);
        leds[3].setRadius(size * 0.045);
        leds[3].setCenterX(width * 0.8);
        leds[3].setCenterY(height * 0.5);

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
}
