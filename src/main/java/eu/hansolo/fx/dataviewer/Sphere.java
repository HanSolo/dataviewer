/*
 * Copyright (c) 2018 by Gerrit Grunwald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.hansolo.fx.dataviewer;

import eu.hansolo.fx.dataviewer.tools.CtxDimension;
import javafx.beans.DefaultProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;


/**
 * User: hansolo
 * Date: 29.01.18
 * Time: 10:46
 */
@DefaultProperty("children")
public class Sphere extends Region {
    private static final double                   PREFERRED_WIDTH  = 260;
    private static final double                   PREFERRED_HEIGHT = 240;
    private static final double                   MINIMUM_WIDTH    = 50;
    private static final double                   MINIMUM_HEIGHT   = 50;
    private static final double                   MAXIMUM_WIDTH    = 1024;
    private static final double                   MAXIMUM_HEIGHT   = 1024;
    private static       double                   aspectRatio;
    private              boolean                  keepAspect;
    private              double                   size;
    private              double                   width;
    private              double                   height;
    private              Canvas                   canvasOval;
    private              GraphicsContext          ctxOval;
    private              Canvas                   canvasGrid;
    private              GraphicsContext          ctxGrid;
    private              Pane                     pane;
    private              Color                    _gridColor;
    private              ObjectProperty<Color>    gridColor;
    private              EventHandler<MouseEvent> mouseHandler;


    // ******************** Constructors **************************************
    public Sphere() {
        getStylesheets().add(Sphere.class.getResource("sphere.css").toExternalForm());
        aspectRatio  = PREFERRED_HEIGHT / PREFERRED_WIDTH;
        keepAspect   = true;
        _gridColor   = Color.rgb(128, 128, 128, 1.0);//0.2);
        mouseHandler = e -> handleMouseEvent(e);
        initGraphics();
        registerListeners();
    }


    // ******************** Initialization ************************************
    private void initGraphics() {
        if (Double.compare(getPrefWidth(), 0.0) <= 0 || Double.compare(getPrefHeight(), 0.0) <= 0 || Double.compare(getWidth(), 0.0) <= 0 ||
            Double.compare(getHeight(), 0.0) <= 0) {
            if (getPrefWidth() > 0 && getPrefHeight() > 0) {
                setPrefSize(getPrefWidth(), getPrefHeight());
            } else {
                setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
            }
        }

        getStyleClass().add("sphere");

        canvasOval = new Canvas(PREFERRED_WIDTH, PREFERRED_HEIGHT);
        ctxOval    = canvasOval.getGraphicsContext2D();

        canvasGrid = new Canvas(PREFERRED_WIDTH, PREFERRED_HEIGHT);
        ctxGrid    = canvasGrid.getGraphicsContext2D();

        pane = new Pane(canvasOval, canvasGrid);

        getChildren().setAll(pane);
    }

    private void registerListeners() {
        widthProperty().addListener(o -> resize());
        heightProperty().addListener(o -> resize());
        canvasGrid.addEventHandler(MouseEvent.MOUSE_MOVED, mouseHandler);
        canvasGrid.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseHandler);
        canvasGrid.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseHandler);
    }


    // ******************** Methods *******************************************
    @Override public void layoutChildren() {
        super.layoutChildren();
    }

    @Override protected double computeMinWidth(final double HEIGHT) { return MINIMUM_WIDTH; }
    @Override protected double computeMinHeight(final double WIDTH) { return MINIMUM_HEIGHT; }
    @Override protected double computePrefWidth(final double HEIGHT) { return super.computePrefWidth(HEIGHT); }
    @Override protected double computePrefHeight(final double WIDTH) { return super.computePrefHeight(WIDTH); }
    @Override protected double computeMaxWidth(final double HEIGHT) { return MAXIMUM_WIDTH; }
    @Override protected double computeMaxHeight(final double WIDTH) { return MAXIMUM_HEIGHT; }

    private void handleControlPropertyChanged(final String PROPERTY) {
        if ("".equals(PROPERTY)) {

        }
    }

    @Override public ObservableList<Node> getChildren() { return super.getChildren(); }

    public Color getGridColor() { return null == gridColor ? _gridColor : gridColor.get(); }
    public void setGridColor(final Color COLOR) {
        if (null == gridColor) {
            _gridColor = COLOR;
            redraw();
        } else {
            gridColor.set(COLOR);
        }
    }
    public ObjectProperty<Color> gridColorProperty() {
        if (null == gridColor) {
            gridColor = new ObjectPropertyBase<Color>(_gridColor) {
                @Override protected void invalidated() { redraw(); }
                @Override public Object getBean() { return Sphere.this; }
                @Override public String getName() { return "gridColor"; }
            };
            _gridColor = null;
        }
        return gridColor;
    }

    private void handleMouseEvent(final MouseEvent EVT) {
        final EventType<? extends MouseEvent> TYPE = EVT.getEventType();
        if (MouseEvent.MOUSE_MOVED.equals(TYPE)) {
            drawTile(EVT, Color.rgb(30, 49, 116, 0.25));
        } else if (MouseEvent.MOUSE_PRESSED.equals(TYPE)) {
            drawTile(EVT, Color.rgb(30, 49, 116, 0.75));
        } else if (MouseEvent.MOUSE_RELEASED.equals(TYPE)) {
            drawTile(EVT, Color.rgb(30, 49, 116, 0.25));
        }
    }


    // ******************** Drawing *******************************************
    private void drawOval() {
        double tileSize = size * 0.08333333;
        double ovalSize = 12 * tileSize;

        ctxOval.clearRect(0, 0, width, height);

        ctxOval.setStroke(getGridColor());
        ctxOval.strokeOval(tileSize, 0, ovalSize, ovalSize);
    }

    private void drawGrid() {
        double tileSize = size * 0.08333333;

        ctxGrid.clearRect(0, 0, width, height);

        ctxGrid.setStroke(getGridColor());
        for (int y = 0 ; y < 12 ; y++) {
            for (int x = 0 ; x < 13 ; x++) {
                if (y == 0 && x < 2 || y == 0 && x > 10 ||
                    y == 1 && x < 1 || y == 1 && x > 11 ||
                    y == 10 && x < 1 || y == 10 && x > 11 ||
                    y == 11 && x < 2 || y == 11 && x > 10) {
                    continue;
                }
                ctxGrid.strokeRect(x * tileSize, y * tileSize, tileSize, tileSize);
            }
        }
    }

    private void drawTile(final MouseEvent EVT, final Color COLOR) {
        double mouseX   = EVT.getX();
        double mouseY   = EVT.getY();
        double tileSize = size * 0.08333333;
        double minX;
        double minY;

        ctxGrid.clearRect(0, 0, width, height);

        ctxGrid.setStroke(getGridColor());
        ctxGrid.setFill(COLOR);
        for (int y = 0 ; y < 12 ; y++) {
            for (int x = 0 ; x < 13 ; x++) {
                if (y == 0 && x < 2 || y == 0 && x > 10 ||
                    y == 1 && x < 1 || y == 1 && x > 11 ||
                    y == 10 && x < 1 || y == 10 && x > 11 ||
                    y == 11 && x < 2 || y == 11 && x > 10) {
                    continue;
                }
                minX = x * tileSize;
                minY = y * tileSize;
                ctxGrid.strokeRect(minX, minY, tileSize, tileSize);

                if (Double.compare(mouseX, minX) >= 0 &&
                     Double.compare(mouseX, minX + tileSize) <= 0 &&
                     Double.compare(mouseY, minY) >= 0 &&
                     Double.compare(mouseY, minY + tileSize) <= 0) {
                    ctxGrid.fillRect(minX, minY, tileSize, tileSize);
                }
            }
        }
    }


    // ******************** Resizing ******************************************
    private void resize() {
        width  = getWidth() - getInsets().getLeft() - getInsets().getRight();
        height = getHeight() - getInsets().getTop() - getInsets().getBottom();
        size   = width < height ? width : height;

        if (keepAspect) {
            if (aspectRatio * width > height) {
                width = 1 / (aspectRatio / height);
            } else if (1 / (aspectRatio / height) > width) {
                height = aspectRatio * width;
            }
        }

        if (width > 0 && height > 0) {
            pane.setMinSize(width, height);
            pane.setMaxSize(width, height);
            pane.setPrefSize(width, height);
            pane.relocate((getWidth() - width) * 0.5, (getHeight() - height) * 0.5);

            canvasOval.setWidth(width);
            canvasOval.setHeight(height);

            canvasGrid.setWidth(width);
            canvasGrid.setHeight(height);

            redraw();
        }
    }

    private void redraw() {
        drawOval();
        drawGrid();
    }
}
