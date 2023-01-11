package com.pi4j.components.tiles.Skins;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.events.TileEvt;
import eu.hansolo.tilesfx.fonts.Fonts;
import eu.hansolo.tilesfx.skins.TileSkin;
import eu.hansolo.tilesfx.tools.Helper;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class PotentiometerTileSkin extends TileSkin {

    private Circle  button;
//    private Line line;

    private Rectangle bar;

    private Text    titleText;
    private Text    text;

    private Text   description;

    public PotentiometerTileSkin(Tile TILE) {
        super(TILE);
    }

    @Override
    protected void initGraphics (){
        super.initGraphics();

        titleText = new Text();
        titleText.setFill(tile.getTitleColor());
        Helper.enableNode(titleText, !tile.getTitle().isEmpty());

        text = new Text(tile.getText());
        text.setFill(tile.getUnitColor());
        Helper.enableNode(text, tile.isTextVisible());

        description = new Text(tile.getDescription());
        description.setFill(tile.getUnitColor());
        Helper.enableNode(description, !tile.getDescription().isEmpty());

        Color buttonFill = Color.WHITE;
        Color buttonBorder = Color.GRAY;

//        line = new Line();
//        line.setStroke(Color.GRAY);

        bar = new Rectangle();
        bar.setFill(Color.GRAY);

        button = new Circle();
        button.setFill(buttonFill);
        button.setStroke(buttonBorder);


        getPane().getChildren().addAll(titleText, text, description,bar, button);
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
            Helper.enableNode(description, !tile.getDescription().isEmpty());

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
        titlePosition(titleText, tile.getTitleAlignment(),maxWidth, fontSize);

        text.setText(tile.getText());
        text.setFont(font);
        textPosition(text, tile.getTextAlignment(), maxWidth, fontSize);

        description.setText(tile.getDescription());
        description.setFont(font);
        titlePosition(description, TextAlignment.RIGHT,maxWidth,fontSize);
    }

    @Override
    protected void resize() {
        super.resize();

        double marginWidth = width*0.1;
        double centerY = height* 0.5;


        button.setRadius(size * 0.05);
        button.setCenterX(marginWidth);
        button.setCenterY(centerY);

//        line.setStrokeWidth(3);
//        line.setStartX(marginWidth);
//        line.setEndX(width-marginWidth);
//        line.setStartY(centerY);
//        line.setEndY(centerY);

        bar.setHeight(3);
        bar.setWidth(width*0.8);
        bar.setX(marginWidth);
        bar.setY(centerY);

    }

    @Override
    protected void redraw() {
        super.redraw();

        titleText.setText(tile.getTitle());
        text.setText(tile.getText());
        description.setText(tile.getDescription());

        resizeStaticText();

        titleText.setFill(tile.getTitleColor());
        text.setFill(tile.getTextColor());
        description.setFill(tile.getDescriptionColor());
    }

    public Circle getButton() {
        return button;
    }

//    public Line getLine() {
//        return line;
//    }


    public Rectangle getBar() {
        return bar;
    }

    public double getBarWidth(){
        return width*0.8;
    }

    public void textPosition(Text text, TextAlignment alignment, double maxWidth, double fontSize){
        if (text.getLayoutBounds().getWidth() > maxWidth) { Helper.adjustTextSize(text, maxWidth, fontSize); }
        switch (alignment) {
        case LEFT -> text.setX(size * 0.05);
        case CENTER -> text.setX((width - text.getLayoutBounds().getWidth()) * 0.5);
        case RIGHT -> text.setX(width - (size * 0.05) - text.getLayoutBounds().getWidth());
        }
        text.setY(height - size * 0.05);
    }

    public void titlePosition(Text titleText, TextAlignment alignment, double maxWidth, double fontSize){
        if (titleText.getLayoutBounds().getWidth() > maxWidth) { Helper.adjustTextSize(titleText, maxWidth, fontSize); }
        switch (alignment) {
        case LEFT -> titleText.relocate(size * 0.05, size * 0.05);
        case CENTER -> titleText.relocate((width - titleText.getLayoutBounds().getWidth()) * 0.5, size * 0.05);
        case RIGHT -> titleText.relocate(width - (size * 0.05) - titleText.getLayoutBounds().getWidth(), size * 0.05);
        }
    }
}
