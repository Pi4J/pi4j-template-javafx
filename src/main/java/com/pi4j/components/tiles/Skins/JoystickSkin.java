package com.pi4j.components.tiles.Skins;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.events.TileEvt;
import eu.hansolo.tilesfx.fonts.Fonts;
import eu.hansolo.tilesfx.skins.TileSkin;
import eu.hansolo.tilesfx.tools.Helper;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class JoystickSkin extends TileSkin {

    private Circle      button;
    private Polygon     up;
    private Polygon     down;
    private Polygon     left;

    private Polygon     right;

    private Text        titleText;
    private Text        text;

    public JoystickSkin(Tile TILE) {
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

        Color fill = Color.RED;
        Color border = Color.WHITE;

        button = new Circle();
        button.setFill(fill);
        button.setStroke(border);

        up = new Polygon();
        up.setFill(fill);
        up.setStroke(border);

        down = new Polygon();
        down.setFill(fill);
        down.setStroke(border);

        left = new Polygon();
        left.setFill(fill);
        left.setStroke(border);

        right = new Polygon();
        right.setFill(fill);
        right.setStroke(border);


        getPane().getChildren().addAll(titleText, text, button, up, down, left, right);
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

    @Override protected void resize() {
        super.resize();

        button.setRadius(size * 0.12);
        button.setCenterX(width * 0.5);
        button.setCenterY(height * 0.5);

        //size and position of arrows
        up.getPoints().addAll(
                button.getCenterX(), 35.0,
                        148.0, 75.0,
                        102.0, 75.0);

        down.getPoints().addAll(
            button.getCenterX(), 215.0,
            148.0, 175.0,
            102.0, 175.0);

        left.getPoints().addAll(
             35.0, 125.0,
            75.0, 102.0,
            75.0, 148.0);

        right.getPoints().addAll(
            215.0, 125.0,
            175.0, 148.0,
            175.0, 102.0);

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


    public Circle getButton() {
        return button;
    }

    public void setButton(Circle button) {
        this.button = button;
    }

    public Polygon getUp() {
        return up;
    }

    public void setUp(Polygon up) {
        this.up = up;
    }

    public Polygon getDown() {
        return down;
    }

    public void setDown(Polygon down) {
        this.down = down;
    }

    public Polygon getLeft() {
        return left;
    }

    public void setLeft(Polygon left) {
        this.left = left;
    }

    public Polygon getRight() {
        return right;
    }

    public void setRight(Polygon right) {
        this.right = right;
    }
}
