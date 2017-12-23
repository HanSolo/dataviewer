/*
 * Copyright (c) 2017 by Gerrit Grunwald
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

import eu.hansolo.fx.dataviewer.Series.Symbol;
import eu.hansolo.fx.dataviewer.ToolButton.Tool;
import eu.hansolo.fx.dataviewer.event.DataViewerEvent;
import eu.hansolo.fx.dataviewer.event.DataViewerEvent.Type;
import eu.hansolo.fx.dataviewer.event.DataViewerEventListener;
import eu.hansolo.fx.dataviewer.font.Fonts;
import eu.hansolo.fx.dataviewer.tools.CtxBounds;
import eu.hansolo.fx.dataviewer.tools.CtxDimension;
import eu.hansolo.fx.dataviewer.tools.Helper;
import javafx.beans.DefaultProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.IntegerPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;


@DefaultProperty("children")
public class DataViewer extends Region {
    private static final double                                         PREFERRED_WIDTH  = 1024;
    private static final double                                         PREFERRED_HEIGHT = 600;
    private static final double                                         MINIMUM_WIDTH    = 50;
    private static final double                                         MINIMUM_HEIGHT   = 50;
    private static final double                                         MAXIMUM_WIDTH    = 4096;
    private static final double                                         MAXIMUM_HEIGHT   = 4096;
    private static final int                                            MAX_DECIMALS     = 10;
    private static final double                                         TOP              = 10;
    private static final double                                         RIGHT            = 10;
    private static final double                                         BOTTOM           = 10;
    private static final double                                         LEFT             = 10;
    private              double                                         size;
    private              double                                         width;
    private              double                                         height;
    private              CtxBounds                                      xAxisArea;
    private              CtxBounds                                      yAxisArea;
    private              CtxBounds                                      chartArea;
    private              CtxDimension                                   gridViewPort;
    private              CtxBounds                                      imageViewPort;
    private              CtxDimension                                   selectedArea;
    private              Rectangle                                      chartBackgroundRect;
    private              ImageView                                      imageView;
    private              Canvas                                         canvasDataLayer;
    private              GraphicsContext                                ctxDataLayer;
    private              Canvas                                         canvasGrid;
    private              GraphicsContext                                ctxGrid;
    private              ToggleGroup                                    toggleGroup;
    private              ToolButton                                     selectTool;
    private              ToolButton                                     panTool;
    private              ToolButton                                     zoomTool;
    private              HBox                                           toolBox;
    private              Rectangle                                      selectionRect;
    private              Rectangle                                      overviewRect;
    private              Rectangle                                      viewportRect;
    private              Text                                           infoText;
    private              Axis                                           xAxis;
    private              Axis                                           yAxis;
    private              Pane                                           pane;
    private              Image                                          _image;
    private              ObjectProperty<Image>                          image;
    private              boolean                                        _toolboxVisible;
    private              BooleanProperty                                toolboxVisible;
    private              Pos                                            _toolboxPosition;
    private              ObjectProperty<Pos>                            toolboxPosition;
    private              boolean                                        _selectToolVisible;
    private              BooleanProperty                                selectToolVisible;
    private              boolean                                        _panToolVisible;
    private              BooleanProperty                                panToolVisible;
    private              boolean                                        _zoomToolVisible;
    private              BooleanProperty                                zoomToolVisible;
    private              double                                         _xAxisMin;
    private              DoubleProperty                                 xAxisMin;
    private              double                                         _xAxisMax;
    private              DoubleProperty                                 xAxisMax;
    private              double                                         _yAxisMin;
    private              DoubleProperty                                 yAxisMin;
    private              double                                         _yAxisMax;
    private              DoubleProperty                                 yAxisMax;
    private              String                                         _xAxisLabel;
    private              StringProperty                                 xAxisLabel;
    private              String                                         _yAxisLabel;
    private              StringProperty                                 yAxisLabel;
    private              double                                         scaleX;
    private              double                                         scaleY;
    private              Color                                          _backgroundColor;
    private              ObjectProperty<Color>                          backgroundColor;
    private              Color                                          _infoTextColor;
    private              ObjectProperty<Color>                          infoTextColor;
    private              Color                                          _axisTextColor;
    private              ObjectProperty<Color>                          axisTextColor;
    private              Color                                          _axisColor;
    private              ObjectProperty<Color>                          axisColor;
    private              Color                                          _axisBackgroundColor;
    private              ObjectProperty<Color>                          axisBackgroundColor;
    private              Color                                          _zoomColor;
    private              ObjectProperty<Color>                          zoomColor;
    private              Color                                          _selectionColor;
    private              ObjectProperty<Color>                          selectionColor;
    private              Color                                          _overviewRectColor;
    private              ObjectProperty<Color>                          overviewRectColor;
    private              Color                                          _chartBackgroundColor;
    private              ObjectProperty<Color>                          chartBackgroundColor;
    private              boolean                                        _overviewVisible;
    private              BooleanProperty                                overviewVisible;
    private              Pos                                            _overviewPosition;
    private              ObjectProperty<Pos>                            overviewPosition;
    private              int                                            _decimals;
    private              IntegerProperty                                decimals;
    private              Locale                                         _locale;
    private              ObjectProperty<Locale>                         locale;
    private              int                                            _xAxisDecimals;
    private              IntegerProperty                                xAxisDecimals;
    private              int                                            _yAxisDecimals;
    private              IntegerProperty                                yAxisDecimals;
    private              String                                         formatString;
    private              EventHandler<MouseEvent>                       mouseHandler;
    private              boolean                                        panSelection;
    private              double                                         selectionStartX;
    private              double                                         selectionStartY;
    private              double                                         lastX;
    private              double                                         lastY;
    private              double                                         panOffsetX;
    private              double                                         panOffsetY;
    private              double                                         zoomStartX;
    private              double                                         zoomStartY;
    private              double                                         zoomFactorX;
    private              double                                         zoomFactorY;
    private              double                                         initialMinX;
    private              double                                         initialMaxX;
    private              double                                         initialMinY;
    private              double                                         initialMaxY;
    private              double                                         initialRangeX;
    private              double                                         initialRangeY;
    private              double                                         initialImageWidth;
    private              double                                         initialImageHeight;
    private              List<Series>                                   series;
    private              boolean                                        _dataLayerVisible;
    private              BooleanProperty                                dataLayerVisible;
    private              boolean                                        _adjustGridToData;
    private              BooleanProperty                                adjustGridToData;
    private              boolean                                         _crossHairVisible;
    private              BooleanProperty                                crossHairVisible;
    private              Color                                          _crossHairColor;
    private              ObjectProperty<Color>                          crossHairColor;
    private              Line                                           crossHairHorizontal;
    private              Line                                           crossHairVertical;
    private              CopyOnWriteArrayList<DataViewerEventListener>  listeners;
    private              double                                         gridToImageScaleX;
    private              double                                         gridToImageScaleY;
    private              boolean                                        _gridVisible;
    private              BooleanProperty                                gridVisible;
    private              Color                                          _gridColor;
    private              ObjectProperty<Color>                          gridColor;
    private              Position                                       _xAxisPosition;
    private              ObjectProperty<Position>                       xAxisPosition;
    private              Position                                       _yAxisPosition;
    private              ObjectProperty<Position>                       yAxisPosition;


    // ******************** Constructors **************************************
    public DataViewer() {
        getStylesheets().add(DataViewer.class.getResource("data-viewer.css").toExternalForm());
        xAxisArea             = new CtxBounds(LEFT, TOP, 70, 510);
        yAxisArea             = new CtxBounds(LEFT + 70, height - TOP - 70, 934, 70);
        chartArea             = new CtxBounds(LEFT, TOP, 934, 510);
        gridViewPort          = new CtxDimension();
        imageViewPort         = new CtxBounds();
        selectedArea          = new CtxDimension();
        initialMinX           = Double.MAX_VALUE;
        initialMaxX           = -Double.MAX_VALUE;
        initialMinY           = Double.MAX_VALUE;
        initialMaxY           = -Double.MAX_VALUE;
        _xAxisMin             = 0;
        _xAxisMax             = 100;
        _yAxisMin             = 0;
        _yAxisMax             = 100;
        _backgroundColor      = Color.TRANSPARENT;
        _infoTextColor        = Color.LIGHTGRAY;
        _axisTextColor        = Color.BLACK;
        _axisColor            = Color.BLACK;
        _axisBackgroundColor  = Color.TRANSPARENT;
        _zoomColor            = Color.rgb(255, 0, 255);
        _selectionColor       = Color.rgb(0, 100, 200);
        _overviewRectColor    = Color.rgb(200, 200, 200, 0.5);
        _chartBackgroundColor = Color.TRANSPARENT;
        _overviewVisible      = true;
        _overviewPosition     = Pos.BOTTOM_RIGHT;
        _decimals             = 0;
        _locale               = Locale.US;
        _xAxisDecimals        = 0;
        _yAxisDecimals        = 0;
        _xAxisLabel           = "";
        _yAxisLabel           = "";
        formatString          = "%.0f";
        zoomFactorX           = 1.0;
        zoomFactorY           = 1.0;
        initialImageWidth     = -1;
        initialImageHeight    = -1;
        series                = new LinkedList<>();
        _dataLayerVisible     = true;
        _toolboxVisible       = true;
        _toolboxPosition      = Pos.TOP_RIGHT;
        _selectToolVisible    = true;
        _panToolVisible       = true;
        _zoomToolVisible      = true;
        _adjustGridToData     = false;
        _crossHairVisible     = false;
        _crossHairColor       = Color.rgb(128, 128, 128, 0.5);
        _gridVisible          = false;
        _gridColor            = Color.rgb(128, 128, 128, 0.2);
        _xAxisPosition        = Position.BOTTOM;
        _yAxisPosition        = Position.LEFT;
        listeners             = new CopyOnWriteArrayList<>();

        mouseHandler = e -> {
            double    x         = e.getX();
            double    y         = e.getY();
            double    valueX    = (x * scaleX) + xAxis.getMinValue();
            double    valueY    = ((chartArea.getHeight() - y) * scaleY) + yAxis.getMinValue();
            String    text      = String.format(getLocale(), formatString, valueX) + ", " + String.format(getLocale(), formatString, valueY);
            infoText.setText(text);
            double    textWidth = infoText.getLayoutBounds().getWidth();
            double    textX;
            double    textY;
            double    xInWindow;
            double    yInWindow;
            if (Position.LEFT == _yAxisPosition) {
                textX     = x < textWidth * 0.5 ? chartArea.getMinX() + 5 : x > chartArea.getWidth() - textWidth * 0.5 ? chartArea.getMaxX() - textWidth - 5 : chartArea.getMinX() + x - textWidth * 0.5;
                xInWindow = LEFT + yAxisArea.getWidth() + x;
            } else {
                textX     = x < textWidth * 0.5 ? chartArea.getMinX() + 5 : x > chartArea.getWidth() - textWidth * 0.5 ? chartArea.getMaxX() - textWidth - 5 : chartArea.getMinX() + x - textWidth * 0.5;
                xInWindow = LEFT + x;
            }
            if (Position.BOTTOM == _xAxisPosition) {
                textY     = y < 25 ? y + 20 : y - 13;
                yInWindow = TOP + y;
            } else {
                textY     = y < 25 + xAxisArea.getHeight() ? y + 20 + xAxisArea.getHeight() : y - 13 + xAxisArea.getHeight();
                yInWindow = TOP + xAxisArea.getHeight() + y;
            }
            EventType type      = e.getEventType();
            if (MouseEvent.MOUSE_ENTERED.equals(type)) {
                mouseEntered(x, y);
            } else if (MouseEvent.MOUSE_EXITED.equals(type)) {
                mouseExited(x, y);
            } else if (MouseEvent.MOUSE_MOVED.equals(type)) {
                mouseMoved(x, y, textX, textY);
            } else if (MouseEvent.MOUSE_PRESSED.equals(type)) {
                mousePressed(x, y, xInWindow, yInWindow);
            } else if (MouseEvent.MOUSE_DRAGGED.equals(type)) {
                mouseDragged(x, y, textX, textY);
            } else if (MouseEvent.MOUSE_RELEASED.equals(type)) {
                Toggle selectedButton = toggleGroup.getSelectedToggle();
                if (selectTool.equals(selectedButton)) {
                    endSelection(x, y);
                } else if (panTool.equals(selectedButton)) {
                    endPan(x, y);
                } else if (zoomTool.equals(selectedButton)) {
                    endZoom(x, y);
                }
            }
        };

        initGraphics();
        registerListeners();
        recalc();
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

        getStyleClass().add("data-viewer");

        xAxis = new Axis(getXAxisMin(), getXAxisMax(), Orientation.HORIZONTAL, _xAxisPosition);
        xAxis.setDecimals(getXAxisDecimals());
        yAxis = new Axis(getYAxisMin(), getYAxisMax(), Orientation.VERTICAL, _yAxisPosition);
        yAxis.setDecimals(getYAxisDecimals());

        chartBackgroundRect = new Rectangle();
        chartBackgroundRect.setFill(getChartBackgroundColor());

        imageView = new ImageView();
        imageView.setSmooth(true);

        overviewRect = new Rectangle();
        overviewRect.setFill(Color.TRANSPARENT);
        overviewRect.setStroke(getOverviewRectColor());
        overviewRect.setMouseTransparent(true);
        overviewRect.setVisible(false);

        viewportRect = new Rectangle();
        viewportRect.setStroke(Helper.getColorWithOpacity(getZoomColor(), 0.5));
        viewportRect.setFill(Helper.getColorWithOpacity(getZoomColor(), 0.3));
        viewportRect.setMouseTransparent(true);
        viewportRect.setVisible(false);

        canvasGrid = new Canvas(chartArea.getWidth(), chartArea.getHeight());
        canvasGrid.setMouseTransparent(true);
        ctxGrid = canvasGrid.getGraphicsContext2D();

        canvasDataLayer = new Canvas(chartArea.getWidth(), chartArea.getHeight());
        ctxDataLayer    = canvasDataLayer.getGraphicsContext2D();

        crossHairHorizontal = new Line();
        crossHairHorizontal.setStroke(getCrossHairColor());

        crossHairVertical = new Line();
        crossHairVertical.setStroke(getCrossHairColor());

        toggleGroup = new ToggleGroup();
        selectTool  = new ToolButton(Tool.SELECT, toggleGroup, "Select area");
        panTool     = new ToolButton(Tool.PAN, toggleGroup, "Pan zoomed area");
        zoomTool    = new ToolButton(Tool.ZOOM, toggleGroup, "Zoom to area");

        toolBox = new HBox(5, selectTool, panTool, zoomTool);
        toolBox.relocate(PREFERRED_WIDTH - RIGHT - 10 - 94, 10 + TOP);

        infoText  = new Text("");
        infoText.setFont(Fonts.latoLight(12));
        infoText.setFill(getInfoTextColor());
        infoText.setTextAlignment(TextAlignment.CENTER);
        infoText.setTextOrigin(VPos.CENTER);

        selectionRect = new Rectangle(0, 0, 0, 0);
        selectionRect.setMouseTransparent(true);

        pane = new Pane(xAxis, yAxis, chartBackgroundRect, imageView, canvasGrid, crossHairHorizontal, crossHairVertical, overviewRect, viewportRect, canvasDataLayer, selectionRect, toolBox, infoText);

        getChildren().setAll(pane);
    }

    private void registerListeners() {
        widthProperty().addListener(o -> resize());
        heightProperty().addListener(o -> resize());
        canvasDataLayer.addEventHandler(MouseEvent.MOUSE_MOVED, mouseHandler);
        canvasDataLayer.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseHandler);
        canvasDataLayer.addEventHandler(MouseEvent.MOUSE_EXITED, mouseHandler);
        canvasDataLayer.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseHandler);
        canvasDataLayer.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseHandler);
        canvasDataLayer.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseHandler);

        // Resize once the control is shown on the screen to apply
        // settings that have been modified before the control was visible
        sceneProperty().addListener(o1 -> {
            if (null == getScene()) return;
            getScene().windowProperty().addListener(o2 -> {
                if (null == getScene().getWindow()) return;
                getScene().getWindow().setOnShown(e -> resize());
            });
        });
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

    @Override public ObservableList<Node> getChildren() { return super.getChildren(); }

    public Image getImage() { return null == image ? _image : image.get(); }
    public void setImage(final Image IMAGE) {
        if (null == image) {
            if (initialImageWidth < 0)  { initialImageWidth = IMAGE.getWidth(); }
            if (initialImageHeight < 0) { initialImageHeight = IMAGE.getHeight(); }
            _image = IMAGE;
            imageView.setImage(IMAGE);
            recalc();
        } else {
            image.set(IMAGE);
        }
    }
    public ObjectProperty<Image> imageProperty() {
        if (null == image) {
            image = new ObjectPropertyBase<Image>(_image) {
                @Override protected void invalidated() {
                    if (initialImageWidth < 0)  { initialImageWidth = get().getWidth(); }
                    if (initialImageHeight < 0) { initialImageHeight = get().getHeight(); }
                    imageView.setImage(get());
                    recalc();
                }
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "image"; }
            };
            _image = null;
        }
        return image;
    }

    public boolean isToolboxVisible() { return null == toolboxVisible ? _toolboxVisible : toolboxVisible.get(); }
    public void setToolboxVisible(final boolean VISIBLE) {
        if (null == toolboxVisible) {
            _toolboxVisible = VISIBLE;
            toolBox.setVisible(VISIBLE);
        } else {
            toolboxVisible.set(VISIBLE);
        }
    }
    public BooleanProperty toolboxVisibleProperty() {
        if (null == toolboxVisible) {
            toolboxVisible = new BooleanPropertyBase() {
                @Override protected void invalidated() { toolBox.setVisible(get()); }
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "toolboxVisible"; }
            };
        }
        return toolboxVisible;
    }

    public Pos getToolboxPosition() { return null == toolboxPosition ? _toolboxPosition : toolboxPosition.get(); }
    public void setToolboxPosition(final Pos POSITION) {
        if (null == toolboxPosition) {
            _toolboxPosition = POSITION;
            redraw();
        } else {
            toolboxPosition.set(POSITION);
        }
    }
    public ObjectProperty<Pos> toolboxPositionProperty() {
        if (null == toolboxPosition) {
            toolboxPosition = new ObjectPropertyBase<Pos>(_toolboxPosition) {
                @Override protected void invalidated() { redraw(); }
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "toolboxPosition"; }
            };
            _toolboxPosition = null;
        }
        return toolboxPosition;
    }
    public boolean isSelectToolVisible() { return null == selectToolVisible ? _selectToolVisible : selectToolVisible.get(); }
    public void setSelectToolVisible(final boolean VISIBLE) {
        if (null == selectToolVisible) {
            _selectToolVisible = VISIBLE;
            Helper.enableNode(selectTool, VISIBLE);
            resize();
        } else {
            selectToolVisible.set(VISIBLE);
        }
    }
    public BooleanProperty selectToolVisibleProperty() {
        if (null == selectToolVisible) {
            selectToolVisible = new BooleanPropertyBase(_selectToolVisible) {
                @Override protected void invalidated() {
                    Helper.enableNode(selectTool, get());
                    resize();
                }
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "selectToolVisible"; }
            };
        }
        return selectToolVisible;
    }

    public boolean isPanToolVisible() { return null == panToolVisible ? _panToolVisible : panToolVisible.get(); }
    public void setPanToolVisible(final boolean VISIBLE) {
        if (null == panToolVisible) {
            _panToolVisible = VISIBLE;
            Helper.enableNode(panTool, VISIBLE);
            resize();
        } else {
            panToolVisible.set(VISIBLE);
        }
    }
    public BooleanProperty panToolVisibleProperty() {
        if (null == panToolVisible) {
            panToolVisible = new BooleanPropertyBase(_panToolVisible) {
                @Override protected void invalidated() {
                    Helper.enableNode(panTool, get());
                    resize();
                }
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "panToolVisible"; }
            };
        }
        return panToolVisible;
    }

    public boolean isZoomToolVisible() { return null == zoomToolVisible ? _zoomToolVisible : zoomToolVisible.get(); }
    public void setZoomToolVisible(final boolean VISIBLE) {
        if (null == zoomToolVisible) {
            _zoomToolVisible = VISIBLE;
            Helper.enableNode(zoomTool, VISIBLE);
            resize();
        } else {
            zoomToolVisible.set(VISIBLE);
        }
    }
    public BooleanProperty zoomToolVisibleProperty() {
        if (null == zoomToolVisible) {
            zoomToolVisible = new BooleanPropertyBase(_zoomToolVisible) {
                @Override protected void invalidated() {
                    Helper.enableNode(zoomTool, get());
                    resize();
                }
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "zoomToolVisible"; }
            };
        }
        return zoomToolVisible;
    }

    public double getXAxisMin() { return null == xAxisMin ? _xAxisMin : xAxisMin.get(); }
    public void setXAxisMin(final double MIN) {
        if (null == xAxisMin) {
            if (Double.compare(initialMinX, Double.MAX_VALUE) == 0) { initialMinX = MIN; }
            _xAxisMin = MIN;
            xAxis.setMinValue(MIN);
            recalc();
        } else {
            xAxisMin.set(MIN);
        }
    }
    public DoubleProperty xAxisMinProperty() {
        if (null == xAxisMin) {
            xAxisMin = new DoublePropertyBase(_xAxisMin) {
                @Override protected void invalidated() {
                    if (Double.compare(initialMinX, Double.MAX_VALUE) == 0) { initialMinX = get(); }
                    xAxis.setMinValue(get());
                    recalc();
                }
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "xAxisMin"; }
            };
        }
        return xAxisMin;
    }

    public double getXAxisMax() { return null == xAxisMax ? _xAxisMax : xAxisMax.get(); }
    public void setXAxisMax(final double MAX) {
        if (null == xAxisMax) {
            if (Double.compare(initialMaxX, -Double.MAX_VALUE) == 0) { initialMaxX = MAX; }
            _xAxisMax = MAX;
            xAxis.setMaxValue(MAX);
            recalc();
        } else {
            xAxisMax.set(MAX);
        }
    }
    public DoubleProperty xAxisMaxProperty() {
        if (null == xAxisMax) {
            xAxisMax = new DoublePropertyBase(_xAxisMax) {
                @Override protected void invalidated() {
                    if (Double.compare(initialMaxX, -Double.MAX_VALUE) == 0) { initialMaxX = get(); }
                    xAxis.setMaxValue(get());
                    recalc();
                }
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "xAxisMax"; }
            };
        }
        return xAxisMax;
    }

    public double getRangeX() { return getXAxisMax() - getXAxisMin(); }

    public double getYAxisMin() { return null == yAxisMin ? _yAxisMin : yAxisMin.get(); }
    public void setYAxisMin(final double MIN) {
        if (null == yAxisMin) {
            if (Double.compare(initialMinY, Double.MAX_VALUE) == 0) { initialMinY = MIN; }
            _yAxisMin = MIN;
            yAxis.setMinValue(MIN);
            recalc();
        } else {
            yAxisMin.set(MIN);
        }
    }
    public DoubleProperty yAxisMinProperty() {
        if (null == yAxisMin) {
            yAxisMin = new DoublePropertyBase(_yAxisMin) {
                @Override protected void invalidated() {
                    if (Double.compare(initialMinY, Double.MAX_VALUE) == 0) { initialMinY = get(); }
                    yAxis.setMinValue(get());
                    recalc();
                }
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "yAxisMin"; }
            };
        }
        return yAxisMin;
    }

    public double getYAxisMax() { return null == yAxisMax ? _yAxisMax : yAxisMax.get(); }
    public void setYAxisMax(final double MAX) {
        if (null == yAxisMax) {
            if (Double.compare(initialMaxY, -Double.MAX_VALUE) == 0) { initialMaxY = MAX; }
            _yAxisMax = MAX;
            yAxis.setMaxValue(MAX);
            recalc();
        } else {
            yAxisMax.set(MAX);
        }
    }
    public DoubleProperty yAxisMaxProperty() {
        if (null == yAxisMax) {
            yAxisMax = new DoublePropertyBase(_yAxisMax) {
                @Override protected void invalidated() {
                    if (Double.compare(initialMaxY, -Double.MAX_VALUE) == 0) { initialMaxY = get(); }
                    yAxis.setMaxValue(get());
                    recalc();
                }
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "yAxisMax"; }
            };
        }
        return yAxisMax;
    }

    public double getRangeY() { return getYAxisMax() - getYAxisMin(); }

    public Color getBackgroundColor() { return null == backgroundColor ? _backgroundColor : backgroundColor.get(); }
    public void setBackgroundColor(final Color COLOR) {
        if (null == backgroundColor) {
            _backgroundColor = COLOR;
            drawBackground();
        } else {
            backgroundColor.set(COLOR);
        }
    }
    public ObjectProperty<Color> backgroundColorProperty() {
        if (null == backgroundColor) {
            backgroundColor = new ObjectPropertyBase<Color>(_backgroundColor) {
                @Override protected void invalidated() { drawBackground(); }
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "backgroundColor"; }
            };
            _backgroundColor = null;
        }
        return backgroundColor;
    }

    public Color getInfoTextColor() { return null == infoTextColor ? _infoTextColor : infoTextColor.get(); }
    public void setInfoTextColor(final Color COLOR) {
        if (null == infoTextColor) {
            _infoTextColor = COLOR;
            infoText.setFill(COLOR);
        } else {
            infoTextColor.set(COLOR);
        }
    }
    public ObjectProperty<Color> infoTextColorProperty() {
        if (null == infoTextColor) {
            infoTextColor = new ObjectPropertyBase<Color>(_infoTextColor) {
                @Override protected void invalidated() { infoText.setFill(get()); }
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "infoTextColor"; }
            };
            _infoTextColor = null;
        }
        return infoTextColor;
    }

    public Color getAxisTextColor() { return null == axisTextColor ? _axisTextColor : axisTextColor.get(); }
    public void setAxisTextColor(final Color COLOR) {
        if (null == axisTextColor) {
            _axisTextColor = COLOR;
            drawAxis();
        } else {
            axisTextColor.set(COLOR);
        }
    }
    public ObjectProperty<Color> axisTextColorProperty() {
        if (null == axisTextColor) {
            axisTextColor = new ObjectPropertyBase<Color>(_axisTextColor) {
                @Override protected void invalidated() { drawAxis(); }
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "axisTextColor"; }
            };
            _axisTextColor = null;
        }
        return axisTextColor;
    }

    public Color getAxisColor() { return null == axisColor ? _axisColor : axisColor.get(); }
    public void setAxisColor(final Color COLOR) {
        if (null == axisColor) {
            _axisColor = COLOR;
            drawAxis();
        } else {
            axisColor.set(COLOR);
        }
    }
    public ObjectProperty<Color> axisColorProperty() {
        if (null == axisColor) {
            axisColor = new ObjectPropertyBase<Color>(_axisColor) {
                @Override protected void invalidated() { drawAxis(); }
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "axisColor"; }
            };
            _axisColor = null;
        }
        return axisColor;
    }

    public Color getAxisBackgroundColor() { return null == axisBackgroundColor ? _axisBackgroundColor : axisBackgroundColor.get(); }
    public void setAxisBackgroundColor(final Color COLOR) {
        if (null == axisBackgroundColor) {
            _axisBackgroundColor = COLOR;
            drawAxis();
        } else {
            axisBackgroundColor.set(COLOR);
        }
    }
    public ObjectProperty<Color> axisBackgroundColorProperty() {
        if (null == axisBackgroundColor) {
            axisColor = new ObjectPropertyBase<Color>(_axisBackgroundColor) {
                @Override protected void invalidated() { drawAxis(); }
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "axisBackgroundColor"; }
            };
            _axisBackgroundColor = null;
        }
        return axisBackgroundColor;
    }

    public Color getZoomColor() { return null == zoomColor ? _zoomColor : zoomColor.get(); }
    public void setZoomColor(final Color COLOR) {
        if (null == zoomColor) {
            _zoomColor = COLOR;
            viewportRect.setStroke(Helper.getColorWithOpacity(COLOR, 0.5));
            viewportRect.setFill(Helper.getColorWithOpacity(COLOR, 0.3));
        } else {
            zoomColor.set(COLOR);
        }
    }
    public ObjectProperty<Color> zoomColorProperty() {
        if (null == zoomColor) {
            zoomColor = new ObjectPropertyBase<Color>(_zoomColor) {
                @Override protected void invalidated() {
                    viewportRect.setStroke(Helper.getColorWithOpacity(get(), 0.5));
                    viewportRect.setFill(Helper.getColorWithOpacity(get(), 0.3));
                }
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "zoomColor"; }
            };
            _zoomColor = null;
        }
        return zoomColor;
    }

    public Color getSelectionColor() { return null == selectionColor ? _selectionColor : selectionColor.get(); }
    public void setSelectionColor(final Color COLOR) {
        if (null == selectionColor) {
            _selectionColor = COLOR;
        } else {
            selectionColor.set(COLOR);
        }
    }
    public ObjectProperty<Color> selectionColorProperty() {
        if (null == selectionColor) {
            selectionColor = new ObjectPropertyBase<Color>(_selectionColor) {
                @Override protected void invalidated() {};
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "selectionColor"; }
            };
            _selectionColor = null;
        }
        return selectionColor;
    }

    public Color getOverviewRectColor() { return null == overviewRectColor ? _overviewRectColor : overviewRectColor.get(); }
    public void setOverviewRectColor(final Color COLOR) {
        if (null == overviewRectColor) {
            _overviewRectColor = COLOR;
            overviewRect.setStroke(Helper.getColorWithOpacity(COLOR, 0.5));
        } else {
            overviewRectColor.set(COLOR);
        }
    }
    public ObjectProperty<Color> overviewRectColorProperty() {
        if (null == overviewRectColor) {
            overviewRectColor = new ObjectPropertyBase<Color>(_overviewRectColor) {
                @Override protected void invalidated() { overviewRect.setStroke(Helper.getColorWithOpacity(get(), 0.5)); }
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "overviewRectColor"; }
            };
            _overviewRectColor = null;
        }
        return overviewRectColor;
    }

    public Color getChartBackgroundColor() { return null == chartBackgroundColor ? _chartBackgroundColor : chartBackgroundColor.get(); }
    public void setChartBackgroundColor(final Color COLOR) {
        if (null == chartBackgroundColor) {
            _chartBackgroundColor = COLOR;
            chartBackgroundRect.setFill(COLOR);
        } else {
            chartBackgroundColor.set(COLOR);
        }
    }
    public ObjectProperty<Color> chartBackgroundColorProperty() {
        if (null == chartBackgroundColor) {
            chartBackgroundColor = new ObjectPropertyBase<Color>(_chartBackgroundColor) {
                @Override protected void invalidated() { chartBackgroundRect.setFill(get()); }
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "chartBackgroundColor"; }
            };
            _chartBackgroundColor = null;
        }
        return chartBackgroundColor;
    }

    public boolean isOverviewVisible() { return null == overviewVisible ? _overviewVisible : overviewVisible.get(); }
    public void setOverViewVisible(final boolean VISIBLE) {
        if (null == overviewVisible) {
            _overviewVisible = VISIBLE;
        } else {
            overviewVisible.set(VISIBLE);
        }
    }
    public BooleanProperty overviewVisibleProperty() {
        if (null == overviewVisible) {
            overviewVisible = new BooleanPropertyBase(_overviewVisible) {
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "overviewVisible"; }
            };
        }
        return overviewVisible;
    }

    public Pos getOverviewPosition() { return null == overviewPosition ? _overviewPosition : overviewPosition.get(); }
    public void setOverviewPosition(final Pos POSITION) {
        if (null == overviewPosition) {
            _overviewPosition = POSITION;
            redraw();
        } else {
            overviewPosition.set(POSITION);
        }
    }
    public ObjectProperty<Pos> overviewPositionProperty() {
        if (null == overviewPosition) {
            overviewPosition = new ObjectPropertyBase<Pos>(_overviewPosition) {
                @Override protected void invalidated() { redraw(); }
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "overviewPosition"; }
            };
            _overviewPosition= null;
        }
        return overviewPosition;
    }

    public int getDecimals() { return null == decimals ? _decimals : decimals.get(); }
    public void setDecimals(final int DECIMALS) {
        if (null == decimals) {
            _decimals = Helper.clamp(0, MAX_DECIMALS, DECIMALS);
            formatString = new StringBuilder("%.").append(_decimals).append("f").toString();
        } else {
            decimals.set(DECIMALS);
        }
    }
    public IntegerProperty decimalsProperty() {
        if (null == decimals) {
            decimals = new IntegerPropertyBase(_decimals) {
                @Override protected void invalidated() {
                    set(Helper.clamp(0, MAX_DECIMALS, get()));
                    formatString = new StringBuilder("%.").append(_decimals).append("f").toString();
                }
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "decimals"; }
            };
        }
        return decimals;
    }

    public Locale getLocale() { return null == locale ? _locale : locale.get(); }
    public void setLocale(final Locale LOCALE) {
        if (null == locale) {
            _locale = LOCALE;
        } else {
            locale.set(LOCALE);
        }
    }
    public ObjectProperty<Locale> localeProperty() {
        if (null == locale) {
            locale = new ObjectPropertyBase<Locale>(_locale) {
                @Override protected void invalidated() { }
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "locale"; }
            };
            _locale = null;
        }
        return locale;
    }

    public int getXAxisDecimals() { return null == xAxisDecimals ? _xAxisDecimals : xAxisDecimals.get(); }
    public void setXAxisDecimals(final int DECIMALS) {
        if (null == xAxisDecimals) {
            _xAxisDecimals = Helper.clamp(0, MAX_DECIMALS, DECIMALS);
            xAxis.setDecimals(_xAxisDecimals);
        } else {
            xAxisDecimals.set(DECIMALS);
        }
    }
    public IntegerProperty xAxisDecimalsProperty() {
        if (null == xAxisDecimals) {
            xAxisDecimals = new IntegerPropertyBase(_xAxisDecimals) {
                @Override protected void invalidated() {
                    set(Helper.clamp(0, MAX_DECIMALS, get()));
                    xAxis.setDecimals(get());
                }
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "xAxisDecimals"; }
            };
        }
        return xAxisDecimals;
    }

    public int getYAxisDecimals() { return null == yAxisDecimals ? _yAxisDecimals : yAxisDecimals.get(); }
    public void setYAxisDecimals(final int DECIMALS) {
        if (null == yAxisDecimals) {
            _yAxisDecimals = Helper.clamp(0, MAX_DECIMALS, DECIMALS);
            yAxis.setDecimals(_yAxisDecimals);
        } else {
            yAxisDecimals.set(DECIMALS);
        }
    }
    public IntegerProperty yAxisDecimalsProperty() {
        if (null == yAxisDecimals) {
            yAxisDecimals = new IntegerPropertyBase(_yAxisDecimals) {
                @Override protected void invalidated() {
                    set(Helper.clamp(0, MAX_DECIMALS, get()));
                    yAxis.setDecimals(get());
                }
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "yAxisDecimals"; }
            };
        }
        return yAxisDecimals;
    }

    public String getXAxisLabel() { return null == xAxisLabel ? _xAxisLabel : xAxisLabel.get(); }
    public void setXAxisLabel(final String LABEL) {
        if (null == xAxisLabel) {
            _xAxisLabel = LABEL;
            xAxis.setTitle(LABEL);
        } else {
            xAxisLabel.set(LABEL);
        }
    }
    public StringProperty xAxisLabelProperty() {
        if (null == xAxisLabel) {
            xAxisLabel = new StringPropertyBase(_xAxisLabel) {
                @Override protected void invalidated() { xAxis.setTitle(get());}
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "xAxisLabel"; }
            };
            _xAxisLabel = null;
        }
        return xAxisLabel;
    }

    public String getYAxisLabel() { return null == yAxisLabel ? _yAxisLabel : yAxisLabel.get(); }
    public void setYAxisLabel(final String LABEL) {
        if (null == yAxisLabel) {
            _yAxisLabel = LABEL;
            yAxis.setTitle(LABEL);
        } else {
            yAxisLabel.set(LABEL);
        }
    }
    public StringProperty yAxisLabelProperty() {
        if (null == yAxisLabel) {
            yAxisLabel = new StringPropertyBase(_yAxisLabel) {
                @Override protected void invalidated() { yAxis.setTitle(get());}
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "yAxisLabel"; }
            };
            _yAxisLabel = null;
        }
        return yAxisLabel;
    }

    public List<Series> getSeries() { return series; }
    public void setSeries(final Series... SERIES) { setSeries(Arrays.asList(SERIES)); }
    public void setSeries(final List<Series> SERIES) {
        series = SERIES;
        if (seriesAreTimeBased()) {
            xAxis.setTickLabelFormat(TickLabelFormat.TIME);
            xAxis.resize();
        }
        redraw();
    }
    public void addSeries(final Series SERIES) {
        if (series.contains(SERIES)) return;
        series.add(SERIES);
        if (seriesAreTimeBased()) {
            xAxis.setTickLabelFormat(TickLabelFormat.TIME);
            xAxis.resize();
        };
        redraw();
    }
    public void removeSeries(final Series SERIES) {
        if (series.contains(SERIES)) {
            series.remove(SERIES);
            redraw();
        }
    }

    public boolean isDataLayerVisible() { return null == dataLayerVisible ? _dataLayerVisible : dataLayerVisible.get(); }
    public void setDataLayerVisible(final boolean VISIBLE) {
        if (null == dataLayerVisible) {
            _dataLayerVisible = VISIBLE;
            canvasDataLayer.setVisible(VISIBLE);
        } else {
            dataLayerVisible.set(VISIBLE);
        }
    }
    public BooleanProperty dataLayerVisibleProperty() {
        if (null == dataLayerVisible) {
            dataLayerVisible = new BooleanPropertyBase(_dataLayerVisible) {
                @Override protected void invalidated() { canvasDataLayer.setVisible(get()); }
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "dataLayerVisible"; }
            };
        }
        return dataLayerVisible;
    }

    public boolean getAdjustGridToData() { return null == adjustGridToData ? _adjustGridToData : adjustGridToData.get(); }
    public void setAdjustGridToData(final boolean ADJUST) {
        if (null == adjustGridToData) {
            _adjustGridToData = ADJUST;
            adjustToData();
        } else {
            adjustGridToData.set(ADJUST);
        }
    }
    public BooleanProperty adjustGridToDataProperty() {
        if (null == adjustGridToData) {
            adjustGridToData = new BooleanPropertyBase(_adjustGridToData) {
                @Override protected void invalidated() { adjustToData(); }
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "adjustGridToData"; }
            };
        }
        return adjustGridToData;
    }

    public boolean isCrossHairVisible() { return null == crossHairVisible ? _crossHairVisible : crossHairVisible.get(); }
    public void setCrossHairVisible(final boolean VISIBLE) {
        if (null == crossHairVisible) {
            _crossHairVisible = VISIBLE;
        } else {
            crossHairVisible.set(VISIBLE);
        }
    }
    public BooleanProperty crossHairVisibleProperty() {
        if (null == crossHairVisible) {
            crossHairVisible = new BooleanPropertyBase(_crossHairVisible) {
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "crossHairVisible"; }
            };
        }
        return crossHairVisible;
    }

    public Color getCrossHairColor() { return null == crossHairColor ? _crossHairColor : crossHairColor.get(); }
    public void setCrossHairColor(final Color COLOR) {
        if (null == crossHairColor) {
            _crossHairColor = COLOR;
            crossHairHorizontal.setStroke(COLOR);
            crossHairVertical.setStroke(COLOR);
        } else {
            crossHairColor.set(COLOR);
        }
    }
    public ObjectProperty<Color> crossHairColorProperty() {
        if (null == crossHairColor) {
            crossHairColor = new ObjectPropertyBase<Color>(_crossHairColor) {
                @Override protected void invalidated() {
                    crossHairHorizontal.setStroke(get());
                    crossHairVertical.setStroke(get());
                }
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "crossHairColor"; }
            };
            _crossHairColor = null;
        }
        return crossHairColor;
    }

    public boolean isGridVisible() { return null == gridVisible ? _gridVisible : gridVisible.get(); }
    public void setGridVisible(final boolean VISIBLE) {
        if (null == gridVisible) {
            _gridVisible = VISIBLE;
            canvasGrid.setVisible(VISIBLE);
        } else {
            gridVisible.set(VISIBLE);
        }
    }
    public BooleanProperty gridVisibleProperty() {
        if (null == gridVisible) {
            gridVisible = new BooleanPropertyBase(_gridVisible) {
                @Override protected void invalidated() { canvasGrid.setVisible(get()); }
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "gridVisible"; }
            };
        }
        return gridVisible;
    }

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
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "gridColor"; }
            };
            _gridColor = null;
        }
        return gridColor;
    }

    public boolean isXAxisAutoFontSize() { return xAxis.isAutoFontSize(); }
    public void setXAxisAutoFontSize(final boolean AUTO) { xAxis.setAutoFontSize(AUTO); }
    public BooleanProperty xAxisAutoFontSizeProperty() { return xAxis.autoFontSizeProperty(); }

    public boolean isYAxisAutoFontSize() { return yAxis.isAutoFontSize(); }
    public void setYAxisAutoFontSize(final boolean AUTO) { yAxis.setAutoFontSize(AUTO); }
    public BooleanProperty yAxisAutoFontSizeProperty() { return yAxis.autoFontSizeProperty(); }

    public double getXAxisTickLabelFontSize() { return xAxis.getTickLabelFontSize(); }
    public void setXAxisTickLabelFontSize(final double SIZE) { xAxis.setTickLabelFontSize(SIZE); }
    public DoubleProperty xAxisTickLabelFontSizeProperty() { return xAxis.tickLabelFontSizeProperty(); }

    public double getYAxisTickLabelFontSize() { return yAxis.getTickLabelFontSize(); }
    public void setYAxisTickLabelFontSize(final double SIZE) { yAxis.setTickLabelFontSize(SIZE); }
    public DoubleProperty yAxisTickLabelFontSizeProperty() { return yAxis.tickLabelFontSizeProperty(); }

    public double getXAxisTitleFontSize() { return xAxis.getTitleFontSize(); }
    public void setXAxisTitleFontSize(final double SIZE) { xAxis.setTitleFontSize(SIZE); }
    public DoubleProperty xAxisTitleFontSizeProperty() { return xAxis.titleFontSizeProperty(); }

    public double getYAxisTitleFontSize() { return yAxis.getTitleFontSize(); }
    public void setYAxisTitleFontSize(final double SIZE) { yAxis.setTitleFontSize(SIZE); }
    public DoubleProperty yAxisTitleFontSizeProperty() { return yAxis.titleFontSizeProperty(); }

    public void setAxisAutoFontSize(final boolean AUTO) {
        xAxis.setAutoFontSize(AUTO);
        yAxis.setAutoFontSize(AUTO);
    }
    public void setAxisTickLabelFontSize(final double SIZE) {
        xAxis.setTickLabelFontSize(SIZE);
        yAxis.setTickLabelFontSize(SIZE);
    }
    public void setAxisTitleFontSize(final double SIZE) {
        xAxis.setTitleFontSize(SIZE);
        yAxis.setTitleFontSize(SIZE);
    }

    public Position getXAxisPosition() { return null == xAxisPosition ? _xAxisPosition : xAxisPosition.get(); }
    public void setXAxisPosition(final Position POSITION) {
        if (null == xAxisPosition) {
            _xAxisPosition = POSITION;
            xAxis.setPosition(POSITION);
            resize();
        } else {
            xAxisPosition.set(POSITION);
        }
    }
    public ObjectProperty<Position> xAxisPositionProperty() {
        if (null == xAxisPosition) {
            xAxisPosition = new ObjectPropertyBase<Position>(_xAxisPosition) {
                @Override protected void invalidated() {
                    xAxis.setPosition(get());
                    resize();
                }
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "xAxisPosition"; }
            };
            _xAxisPosition = null;
        }
        return xAxisPosition;
    }

    public Position getYAxisPosition() { return null == yAxisPosition ? _yAxisPosition : yAxisPosition.get(); }
    public void setYAxisPosition(final Position POSITION) {
        if (null == yAxisPosition) {
            _yAxisPosition = POSITION;
            yAxis.setPosition(POSITION);
            resize();
        } else {
            yAxisPosition.set(POSITION);
        }
    }
    public ObjectProperty<Position> yAxisPositionProperty() {
        if (null == yAxisPosition) {
            yAxisPosition = new ObjectPropertyBase<Position>(_yAxisPosition) {
                @Override public Object getBean() { return DataViewer.this; }
                @Override public String getName() { return "yAxisPosition"; }
            };
            _yAxisPosition = null;
        }
        return yAxisPosition;
    }

    public void setZoom(final DataViewerEvent EVENT) {
        setZoom(EVENT.getDimension());
    }
    public void setZoom(final CtxDimension DIM) {
        double oldZoomFactorX = zoomFactorX;
        double oldZoomFactorY = zoomFactorY;
        zoomFactorX = initialRangeX / DIM.getWidth();
        zoomFactorY = initialRangeY / DIM.getHeight();
        if (zoomFactorX < 0 || zoomFactorY < 0) {
            zoomFactorX = oldZoomFactorX;
            zoomFactorY = oldZoomFactorY;
        };
        assureCorrectZoomFactors();
        drawOverview();

        // Calculate the current view port
        gridViewPort.setMinX(DIM.getMinX());
        gridViewPort.setMaxX(DIM.getMaxX());
        gridViewPort.setMinY(DIM.getMinY());
        gridViewPort.setMaxY(DIM.getMaxY());
        assureCorrectGridViewPort();

        // Set image viewport according to selection rectangle
        double imageViewPortX      = (gridToImageScaleX * gridViewPort.getMinX() - gridToImageScaleX * initialMinX);
        double imageViewPortY      = (initialImageHeight - (gridToImageScaleY * gridViewPort.getMaxY() - gridToImageScaleY * initialMinY));
        double imageViewPortWidth  = Helper.clampMin(0, (gridToImageScaleX * gridViewPort.getMaxX() - gridToImageScaleX * initialMinX) - imageViewPortX);
        double imageViewPortHeight = Helper.clampMin(0, (initialImageHeight - (gridToImageScaleY * gridViewPort.getMinY() - gridToImageScaleY * initialMinY)) - imageViewPortY);
        imageViewPort.set(imageViewPortX, imageViewPortY, imageViewPortWidth, imageViewPortHeight);
        imageView.setViewport(new Rectangle2D(imageViewPortX, imageViewPortY, imageViewPortWidth, imageViewPortHeight));

        // Adjust axis related to zoom
        xAxis.setMinMax(gridViewPort.getMinX(), gridViewPort.getMaxX());
        yAxis.setMinMax(gridViewPort.getMinY(), gridViewPort.getMaxY());

        recalc();

        drawOverview();

        drawDataLayer();

        // Reset rectangle
        selectionRect.setStroke(Color.TRANSPARENT);
        selectionRect.setFill(Color.TRANSPARENT);
        selectionRect.setX(0);
        selectionRect.setY(0);
        selectionRect.setWidth(0);
        selectionRect.setHeight(0);
    }

    public void reset() {
        initialImageWidth  = -1;
        initialImageHeight = -1;
        initialMinX        = Double.MAX_VALUE;
        initialMaxX        = -Double.MAX_VALUE;
        initialMinY        = Double.MAX_VALUE;
        initialMaxY        = -Double.MAX_VALUE;
        gridViewPort.setMinX(initialMinX);
        gridViewPort.setMaxX(initialMaxX);
        gridViewPort.setMinY(initialMinY);
        gridViewPort.setMaxY(initialMaxY);
        if (null == xAxisMin) { _xAxisMin = 0; } else { xAxisMin.set(0); }
        if (null == xAxisMax) { _xAxisMax = 100; } else { xAxisMax.set(100); }
        if (null == yAxisMin) { _yAxisMin = 0; } else { yAxisMin.set(0); }
        if (null == yAxisMax) { _yAxisMax = 100; } else { yAxisMax.set(100); }
    }


    // ******************** Handle mouse events *******************************
    private void mouseEntered(final double X, final double Y) {
        Scene scene = getScene();
        if (null == scene) return;
        scene.setCursor(Cursor.CROSSHAIR);
        infoText.setVisible(true);
    }
    private void mousePressed(final double X, final double Y, final double X_IN_WINDOW, final double Y_IN_WINDOW) {
        Toggle selectedButton = toggleGroup.getSelectedToggle();
        if (selectTool.equals(selectedButton)) {
            startSelection(X, Y, X_IN_WINDOW, Y_IN_WINDOW);
        } else if (panTool.equals(selectedButton)) {
            startPan(X, Y);
        } else if (zoomTool.equals(selectedButton)) {
            startZoom(X, Y, X_IN_WINDOW, Y_IN_WINDOW);
        }
    }
    private void mouseDragged(final double X, final double Y, final double TEXT_X, final double TEXT_Y) {
        Toggle selectedButton = toggleGroup.getSelectedToggle();
        if (selectTool.equals(selectedButton)) {
            dragSelection(X, Y);
        } else if (panTool.equals(selectedButton)) {
            dragPan(X, Y);
        } else if (zoomTool.equals(selectedButton)) {
            dragZoom(X, Y);
        }
        infoText.relocate(TEXT_X, TEXT_Y);
        if (isCrossHairVisible()) { drawCrossHair(X, Y); }
    }
    private void mouseMoved(final double X, final double Y, final double TEXT_X, final double TEXT_Y) {
        infoText.relocate(TEXT_X, TEXT_Y);
        if (isCrossHairVisible()) { drawCrossHair(X, Y); }
    }
    private void mouseExited(final double X, final double Y) {
        Scene scene = getScene();
        if (null == scene) return;
        scene.setCursor(Cursor.DEFAULT);
        infoText.setVisible(false);
    }

    private void startSelection(final double X, final double Y, final double X_IN_WINDOW, final double Y_IN_WINDOW) {
        selectionRect.getStrokeDashArray().setAll(2d, 4d);
        if (selectionRect.contains(X_IN_WINDOW, Y_IN_WINDOW)) {
            panSelection = true;
            panOffsetX   = (X_IN_WINDOW) - selectionRect.getX();
            panOffsetY   = (Y_IN_WINDOW) - selectionRect.getY();
        } else {
            panSelection = false;
            selectionRect.setStroke(Helper.getColorWithOpacity(getSelectionColor(), 0.6));
            selectionRect.setFill(Helper.getColorWithOpacity(getSelectionColor(), 0.2));
            selectionRect.setX(X_IN_WINDOW);
            selectionRect.setY(Y_IN_WINDOW);
            selectionRect.setWidth(0);
            selectionRect.setHeight(0);
            selectionStartX = X;
            selectionStartY = Y;
        }
    }
    private void dragSelection(final double X, final double Y) {
        if (panSelection) {
            selectionRect.setX(Helper.clamp(0, chartArea.getWidth() - selectionRect.getWidth(), X - panOffsetX) + chartArea.getMinX());
            selectionRect.setY(Helper.clamp(0, chartArea.getHeight() - selectionRect.getHeight(), Y - panOffsetY) + chartArea.getMinY());
        } else {
            selectionRect.setWidth(Helper.clamp(0, chartArea.getWidth(), X) - selectionStartX);
            selectionRect.setHeight(Helper.clamp(0, chartArea.getHeight(), Y) - selectionStartY);
        }
    }
    private void endSelection(final double X, final double Y) {
        selectedArea.setMinX((((selectionRect.getX() - LEFT - yAxisArea.getWidth()) * scaleX) + xAxis.getMinValue()));
        selectedArea.setMaxX((((selectionRect.getX() + selectionRect.getWidth() - LEFT - yAxisArea.getWidth()) * scaleX) + xAxis.getMinValue()));
        selectedArea.setMinY((((chartArea.getHeight() - selectionRect.getY() - selectionRect.getHeight() + TOP) * scaleY) + yAxis.getMinValue()));
        selectedArea.setMaxY((((chartArea.getHeight() - selectionRect.getY() + TOP) * scaleY) + yAxis.getMinValue()));

        if (Double.compare(selectedArea.getWidth(), 0) == 0 || Double.compare(selectedArea.getHeight(), 0) == 0) {
            selectedArea.setMinX(getXAxisMin());
            selectedArea.setMaxX(getXAxisMax());
            selectedArea.setMinY(getYAxisMin());
            selectedArea.setMaxY(getYAxisMax());
        }
        // Fire event that contains the selected area
        fireDataEvent(new DataViewerEvent(DataViewer.this, Type.SELECT, selectedArea));
    }

    private void startPan(final double X, final double Y) {
        //panStartX = X;
        //panStartY = Y;
        lastX     = X;
        lastY     = Y;
    }
    private void dragPan(final double X, final double Y) {
        if (zoomFactorX != 1 && zoomFactorY != 1) {
            shiftViewPort((X - lastX), (Y - lastY));
            lastX = X;
            lastY = Y;
        }
    }
    private void endPan(final double X, final double Y) {
        // Fire event that contains the current grid viewport
        fireDataEvent(new DataViewerEvent(DataViewer.this, Type.PAN, gridViewPort));
    }

    private void startZoom(final double X, final double Y, final double X_IN_WINDOW, final double Y_IN_WINDOW) {
        selectionRect.getStrokeDashArray().clear();
        selectionRect.setStroke(getZoomColor());
        selectionRect.setFill(Color.TRANSPARENT);
        selectionRect.setX(X_IN_WINDOW);
        selectionRect.setY(Y_IN_WINDOW);
        selectionRect.setWidth(0);
        selectionRect.setHeight(0);
        zoomStartX = X;
        zoomStartY = Y;
    }
    private void dragZoom(final double X, final double Y) {
        selectionRect.setWidth(Helper.clamp(0, chartArea.getWidth(), X) - zoomStartX);
        selectionRect.setHeight(Helper.clamp(0, chartArea.getHeight(), Y) - zoomStartY);
    }
    private void endZoom(final double X, final double Y) {
        // Calculate zoom factor according to selection rectangle width/height related to chartArea width/height
        if (zoomFactorX != 1 && zoomFactorY != 1) {
            zoomFactorX = selectionRect.getWidth() / initialImageWidth;
            zoomFactorY = selectionRect.getHeight() / initialImageHeight;
        } else {
            double oldZoomFactorX = zoomFactorX;
            double oldZoomFactorY = zoomFactorY;
            zoomFactorX = selectionRect.getWidth() / chartArea.getWidth();
            zoomFactorY = selectionRect.getHeight() / chartArea.getHeight();
            if ((zoomFactorX < 0 || zoomFactorY < 0)) {
                zoomFactorX = oldZoomFactorX;
                zoomFactorY = oldZoomFactorY;
            }
        }
        assureCorrectZoomFactors();
        drawOverview();

        // Calculate the current view port
        gridViewPort.setMinX((zoomStartX * scaleX) + xAxis.getMinValue());
        gridViewPort.setMaxX((X * scaleX) + xAxis.getMinValue());
        gridViewPort.setMinY(((chartArea.getHeight() - Y) * scaleY) + yAxis.getMinValue());
        gridViewPort.setMaxY(((chartArea.getHeight() - zoomStartY) * scaleY) + yAxis.getMinValue());
        assureCorrectGridViewPort();

        // Set image viewport according to selection rectangle
        double imageViewPortX      = (gridToImageScaleX * gridViewPort.getMinX() - gridToImageScaleX * initialMinX);
        double imageViewPortY      = (initialImageHeight - (gridToImageScaleY * gridViewPort.getMaxY() - gridToImageScaleY * initialMinY));
        double imageViewPortWidth  = Helper.clampMin(0, (gridToImageScaleX * gridViewPort.getMaxX() - gridToImageScaleX * initialMinX) - imageViewPortX);
        double imageViewPortHeight = Helper.clampMin(0, (initialImageHeight - (gridToImageScaleY * gridViewPort.getMinY() - gridToImageScaleY * initialMinY)) - imageViewPortY);
        imageViewPort.set(imageViewPortX, imageViewPortY, imageViewPortWidth, imageViewPortHeight);
        imageView.setViewport(new Rectangle2D(imageViewPortX, imageViewPortY, imageViewPortWidth, imageViewPortHeight));

        // Adjust axis related to zoom
        xAxis.setMinMax(gridViewPort.getMinX(), gridViewPort.getMaxX());
        yAxis.setMinMax(gridViewPort.getMinY(), gridViewPort.getMaxY());

        recalc();

        drawOverview();

        redraw();

        // Reset rectangle
        selectionRect.setStroke(Color.TRANSPARENT);
        selectionRect.setFill(Color.TRANSPARENT);
        selectionRect.setX(0);
        selectionRect.setY(0);
        selectionRect.setWidth(0);
        selectionRect.setHeight(0);

        // Fire event that contains the current grid viewport
        fireDataEvent(new DataViewerEvent(DataViewer.this, Type.ZOOM, gridViewPort));
    }


    // ******************** Event handling ************************************
    public void setOnDataEvent(final DataViewerEventListener LISTENER) { addDataEventListener(LISTENER); }
    public void addDataEventListener(final DataViewerEventListener LISTENER) { if (!listeners.contains(LISTENER)) listeners.add(LISTENER); }
    public void removeDataEventListener(final DataViewerEventListener LISTENER) { if (listeners.contains(LISTENER)) listeners.remove(LISTENER); }

    public void fireDataEvent(final DataViewerEvent EVENT) {
        for (DataViewerEventListener listener : listeners) { listener.onDataViewerEvent(EVENT); }
    }


    // ******************** Misc **********************************************
    private void shiftViewPort(final double DELTA_X, final double DELTA_Y) {
        double gridShiftX = -DELTA_X * scaleX;
        double gridShiftY = DELTA_Y * scaleY;

        // Don't pan outside initial axis limits
        if (xAxis.getMinValue() + gridShiftX < initialMinX || xAxis.getMaxValue() + gridShiftX > initialMaxX ||
            yAxis.getMinValue() + gridShiftY < initialMinY || yAxis.getMaxValue() + gridShiftY > initialMaxY) {
            return;
        }

        // Calculate the current grid viewport
        gridViewPort.shiftX(null == imageView.getImage() ? gridShiftX : -gridShiftX);
        gridViewPort.shiftY(null == imageView.getImage() ? gridShiftY : -gridShiftY);

        // Shift axis values
        xAxis.shift(gridShiftX);
        yAxis.shift(gridShiftY);

        // Adjust the current image viewport
        double imageViewPortMinX = imageView.getViewport().getMinX() + (gridShiftX * gridToImageScaleX);
        double imageViewPortMinY = imageView.getViewport().getMinY() - (gridShiftY * gridToImageScaleY);
        imageViewPort.setX(imageViewPortMinX);
        imageViewPort.setY(imageViewPortMinY);
        imageView.setViewport(new Rectangle2D(imageViewPortMinX, imageViewPortMinY, imageView.getViewport().getWidth(), imageView.getViewport().getHeight()));

        drawOverview();

        redraw();
    }

    private void adjustToData() {
        if (getAdjustGridToData()) {
            double minX = Double.MAX_VALUE;
            double maxX = -Double.MAX_VALUE;
            double minY = Double.MAX_VALUE;
            double maxY = -Double.MAX_VALUE;
            int length = series.size();
            for (int i = 0 ; i < length ; i++) {
                Series s = series.get(i);
                minX = Math.min(minX, s.getData().entrySet().stream().mapToDouble(Entry::getKey).min().getAsDouble());
                maxX = Math.max(maxX, s.getData().entrySet().stream().mapToDouble(Entry::getKey).max().getAsDouble());
                minY = Math.min(minY, s.getData().entrySet().stream().mapToDouble(Entry::getValue).min().getAsDouble());
                maxY = Math.max(maxY, s.getData().entrySet().stream().mapToDouble(Entry::getValue).max().getAsDouble());
            }
            setXAxisMin(minX);
            setXAxisMax(maxX);
            setYAxisMin(minY);
            setYAxisMax(maxY);

            recalc();

            drawOverview();

            redraw();
        }
    }

    private boolean seriesAreTimeBased() {
        return (series.stream().filter(s -> s.isTimeBased()).count()) == series.size();
    }

    private void assureCorrectGridViewPort() {
        if (Double.compare(gridViewPort.getWidth(), 0) <= 0 || Double.compare(gridViewPort.getHeight(), 0) <= 0) {
            gridViewPort.setMinX(initialMinX);
            gridViewPort.setMaxX(initialMaxX);
            gridViewPort.setMinY(initialMinY);
            gridViewPort.setMaxY(initialMaxY);
        }
    }

    private void assureCorrectZoomFactors() {
        if (Double.isInfinite(zoomFactorX) || Double.compare(Math.abs(zoomFactorX), 0) == 0) { zoomFactorX = 1; }
        if (Double.isInfinite(zoomFactorY) || Double.compare(Math.abs(zoomFactorY), 0) == 0) { zoomFactorY = 1; }
    }

    private void recalc() {
        scaleX            = xAxis.getRange() / chartArea.getWidth();
        scaleY            = yAxis.getRange() / chartArea.getHeight();
        initialRangeX     = initialMaxX - initialMinX;
        initialRangeY     = initialMaxY - initialMinY;
        gridToImageScaleX = initialImageWidth / initialRangeX;
        gridToImageScaleY = initialImageHeight / initialRangeY;
    }


    // ******************** Drawing *******************************************
    private void redraw() {
        if (isGridVisible()) { drawGrid(); }
        if (isDataLayerVisible()) { drawDataLayer(); }
    }

    private void drawBackground() {
        setBackground(new Background(new BackgroundFill(getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    private void drawAxis() {
        xAxis.setAxisBackgroundColor(getAxisBackgroundColor());
        xAxis.setTickLabelColor(getAxisTextColor());
        xAxis.setTitleColor(getAxisTextColor());
        xAxis.setAxisColor(getAxisColor());
        xAxis.setMajorTickMarkColor(getAxisColor());
        xAxis.setMinorTickMarkColor(getAxisColor());
        yAxis.setAxisBackgroundColor(getAxisBackgroundColor());
        yAxis.setTickLabelColor(getAxisTextColor());
        yAxis.setTitleColor(getAxisTextColor());
        yAxis.setAxisColor(getAxisColor());
        yAxis.setMajorTickMarkColor(getAxisColor());
        yAxis.setMinorTickMarkColor(getAxisColor());
    }

    private void drawGrid() {
        ctxGrid.clearRect(0, 0, chartArea.getWidth(), chartArea.getHeight());
        ctxGrid.setStroke(getGridColor());
        ctxGrid.setLineDashes(4, 4);
        ctxGrid.setLineWidth(1);

        double rangeX     = xAxis.getRange();
        double rangeY     = yAxis.getRange();
        double tickSpaceX = xAxis.getMinorTickSpace();
        double tickSpaceY = yAxis.getMinorTickSpace();
        double stepX      = chartArea.getWidth() / rangeX;
        double stepY      = chartArea.getHeight() / rangeY;
        double minX       = Position.LEFT == _yAxisPosition ? chartArea.getMinX() - LEFT - yAxisArea.getWidth() : chartArea.getMinX() - yAxisArea.getWidth();
        double maxX       = chartArea.getMaxX();
        double minY       = Position.BOTTOM == _xAxisPosition ? chartArea.getMinY() - TOP : chartArea.getMinY() - xAxisArea.getHeight() - TOP;
        double maxY       = chartArea.getMaxY();

        for (double x = 0 ; x < rangeX ; x += tickSpaceX) {
            ctxGrid.strokeLine(x * stepX, minY, x * stepX, maxY);
        }
        for (double y = 0 ; y < rangeY ; y += tickSpaceY) {
            ctxGrid.strokeLine(minX, minY + y * stepY, maxX, minY + y * stepY);
        }

    }

    private void drawDataLayer() {
        double chartWidth  = chartArea.getWidth();
        double chartHeight = chartArea.getHeight();
        double symbolSize  = chartHeight * 0.019;
        double minX        = xAxis.getMinValue();
        double maxX        = xAxis.getMaxValue();
        double minY        = yAxis.getMinValue();
        double maxY        = yAxis.getMaxValue();
        double stepX       = chartWidth / xAxis.getRange();
        double stepY       = chartHeight / yAxis.getRange();

        ctxDataLayer.clearRect(0, 0, chartWidth, chartHeight);
        ctxDataLayer.save();
        series.forEach(series -> {
            // Filter visible values and draw them
            LinkedHashMap<Double, Double> visibleData = new LinkedHashMap<>();
            series.getData()
                  .entrySet()
                  .stream()
                  //.filter(entry -> Double.compare(entry.getKey(), minX) >= 0 && Double.compare(entry.getKey(), maxX) <= 0)
                  //.filter(entry -> Double.compare(entry.getValue(), minY) >= 0 && Double.compare(entry.getValue(), maxY) <= 0)
                  .sorted(Map.Entry.comparingByKey())
                  .forEachOrdered(c -> visibleData.put(c.getKey(), c.getValue()));

            Symbol  symbol      = series.getSymbol();
            boolean connected   = series.isConnected();
            double  lastX       = -Double.MAX_VALUE;
            double  lastY       = -Double.MAX_VALUE;
            ctxDataLayer.setStroke(series.getColor());
            ctxDataLayer.setFill(series.getColor());
            for (Entry<Double, Double> entry : visibleData.entrySet()) {
                double x = (entry.getKey() - minX) * stepX;
                double y = chartHeight - (entry.getValue() - minY) * stepY;
                drawSymbol(x, y, symbol, symbolSize);
                if (connected && lastX > -Double.MAX_VALUE && lastY > -Double.MAX_VALUE) {
                    ctxDataLayer.strokeLine(lastX, lastY, x, y);
                }
                lastX = x;
                lastY = y;
            }
        });
        ctxDataLayer.restore();
    }

    private void drawSymbol(final double X, final double Y, final Symbol SYMBOL, final double SYMBOL_SIZE) {
        double halfSymbolSize = SYMBOL_SIZE * 0.5;
        switch(SYMBOL) {
            case CIRCLE:
                ctxDataLayer.strokeOval(X - halfSymbolSize, Y - halfSymbolSize, SYMBOL_SIZE, SYMBOL_SIZE);
                break;
            case SQUARE:
                ctxDataLayer.strokeRect(X - halfSymbolSize, Y - halfSymbolSize, SYMBOL_SIZE, SYMBOL_SIZE);
                break;
            case TRIANGLE:
                ctxDataLayer.strokeLine(X, Y - halfSymbolSize, X + halfSymbolSize, Y + halfSymbolSize);
                ctxDataLayer.strokeLine(X + halfSymbolSize, Y + halfSymbolSize, X - halfSymbolSize, Y + halfSymbolSize);
                ctxDataLayer.strokeLine(X - halfSymbolSize, Y + halfSymbolSize, X, Y - halfSymbolSize);
                break;
            case CROSS:
                ctxDataLayer.strokeLine(X - halfSymbolSize, Y - halfSymbolSize, X + halfSymbolSize, Y + halfSymbolSize);
                ctxDataLayer.strokeLine(X - halfSymbolSize, Y + halfSymbolSize, X + halfSymbolSize, Y - halfSymbolSize);
                break;
            case FILLED_CIRCLE:
                ctxDataLayer.fillOval(X - halfSymbolSize, Y - halfSymbolSize, SYMBOL_SIZE, SYMBOL_SIZE);
                break;
            case FILLED_SQUARE:
                ctxDataLayer.fillRect(X - halfSymbolSize, Y - halfSymbolSize, SYMBOL_SIZE, SYMBOL_SIZE);
                break;
            case FILLED_TRIANGLE: // Slower than the others!!!
                ctxDataLayer.beginPath();
                ctxDataLayer.moveTo(X, Y - halfSymbolSize);
                ctxDataLayer.lineTo(X + halfSymbolSize, Y + halfSymbolSize);
                ctxDataLayer.lineTo(X + halfSymbolSize, Y + halfSymbolSize);
                ctxDataLayer.lineTo(X - halfSymbolSize, Y + halfSymbolSize);
                ctxDataLayer.lineTo(X - halfSymbolSize, Y + halfSymbolSize);
                ctxDataLayer.closePath();
                ctxDataLayer.fill();
                break;
            case NONE:
            default  :
                break;
        }
    }

    private void drawCrossHair(final double X, final double Y) {
        double x;
        double y;

        if (Position.LEFT == _yAxisPosition) {
            x = Helper.clamp(chartArea.getMinX() - LEFT - yAxisArea.getWidth(), chartArea.getMaxX() - LEFT, X);
            crossHairVertical.setStartX(x + LEFT + yAxisArea.getWidth()); crossHairVertical.setStartY(chartArea.getMinY());
            crossHairVertical.setEndX(x + LEFT + yAxisArea.getWidth()); crossHairVertical.setEndY(chartArea.getMaxY());
        } else {
            x = Helper.clamp(chartArea.getMinX() - LEFT, chartArea.getMaxX() - LEFT, X);
            crossHairVertical.setStartX(x + LEFT); crossHairVertical.setStartY(chartArea.getMinY());
            crossHairVertical.setEndX(x + LEFT); crossHairVertical.setEndY(chartArea.getMaxY());
        }
        if (Position.BOTTOM == _xAxisPosition) {
            y = Helper.clamp(chartArea.getMinY() - TOP, chartArea.getMaxY() - TOP, Y);
            crossHairHorizontal.setStartX(chartArea.getMinX()); crossHairHorizontal.setStartY(y + TOP);
            crossHairHorizontal.setEndX(chartArea.getMaxX()); crossHairHorizontal.setEndY(y + TOP);
        } else {
            y = Helper.clamp(chartArea.getMinY() - TOP - xAxisArea.getHeight(), chartArea.getMaxY() - TOP - xAxisArea.getHeight(), Y);
            crossHairHorizontal.setStartX(chartArea.getMinX()); crossHairHorizontal.setStartY(y + TOP + xAxisArea.getHeight());
            crossHairHorizontal.setEndX(chartArea.getMaxX()); crossHairHorizontal.setEndY(y + TOP + xAxisArea.getHeight());
        }
    }

    private void drawOverview() {
        if (zoomFactorX != 1 && zoomFactorY != 1) {
            if (isOverviewVisible()) {
                // Draw the overlay window
                double inset           = 0.00833333 * size;
                double overviewWidth   = chartArea.getWidth() * 0.1;
                double overviewHeight  = chartArea.getHeight() * 0.1;
                double overviewOriginX;
                double overviewOriginY;
                switch(getOverviewPosition()) {
                    case TOP_LEFT:
                        overviewOriginX = chartArea.getMinX() + inset;
                        overviewOriginY = chartArea.getMinY() + inset;
                        break;
                    case TOP_RIGHT:
                        overviewOriginX = chartArea.getMaxX() - overviewWidth - inset;
                        overviewOriginY = chartArea.getMinY() + inset;
                        break;
                    case BOTTOM_LEFT:
                        overviewOriginX = chartArea.getMinX() + inset;
                        overviewOriginY = chartArea.getMaxY() - overviewHeight - inset;
                        break;
                    case BOTTOM_RIGHT:
                    default:
                        overviewOriginX = chartArea.getMaxX() - overviewWidth - inset;
                        overviewOriginY = chartArea.getMaxY() - overviewHeight - inset;
                        break;
                }

                overviewRect.setWidth(chartArea.getWidth() * 0.1);
                overviewRect.setHeight(chartArea.getHeight() * 0.1);
                overviewRect.relocate(overviewOriginX, overviewOriginY);

                if (null == imageView.getImage()) {
                    double w     = (gridViewPort.getWidth() / initialRangeX) * chartArea.getWidth();
                    double h     = (gridViewPort.getHeight() / initialRangeY) * chartArea.getHeight();
                    viewportRect.setWidth(w * 0.1);
                    viewportRect.setHeight(h * 0.1);
                    double x = (gridViewPort.getMinX() - initialMinX) * (chartArea.getWidth() / initialRangeX);
                    double y = chartArea.getHeight() - ((gridViewPort.getMaxY() - initialMinY) * (chartArea.getHeight() / initialRangeY));
                    viewportRect.relocate(overviewOriginX + x * 0.1, overviewOriginY + y * 0.1);
                } else {
                    double scaleFactorX = 0.1 * (imageView.getFitWidth() / imageView.getImage().getWidth());
                    double scaleFactorY = 0.1 * (imageView.getFitHeight() / imageView.getImage().getHeight());
                    viewportRect.setWidth(imageViewPort.getWidth() * scaleFactorX);
                    viewportRect.setHeight(imageViewPort.getHeight() * scaleFactorY);
                    viewportRect.relocate(overviewOriginX + imageViewPort.getMinX() * scaleFactorX, overviewOriginY + imageViewPort.getMinY() * scaleFactorY);
                }

                if (!overviewRect.isVisible()) {
                    overviewRect.setVisible(true);
                    viewportRect.setVisible(true);
                }
            }
        } else {
            if (isOverviewVisible()) {
                overviewRect.setVisible(false);
                viewportRect.setVisible(false);
            }
        }
    }


    // ******************** Resizing ******************************************
    private void resize() {
        width  = getWidth() - getInsets().getLeft() - getInsets().getRight();
        height = getHeight() - getInsets().getTop() - getInsets().getBottom();
        size   = width < height ? width : height;

        if (width > 0 && height > 0) {
            pane.setMaxSize(width, height);
            pane.setPrefSize(width, height);
            pane.relocate((getWidth() - width) * 0.5, (getHeight() - height) * 0.5);

            double horizontalInsets = LEFT + RIGHT;
            double verticalInsets   = TOP + BOTTOM;
            double axisShorterSide  = 0.1372549 * size;

            switch(_yAxisPosition) {
                case RIGHT:
                    yAxisArea.setX(width - axisShorterSide - LEFT);
                    yAxisArea.setWidth(axisShorterSide);
                    yAxisArea.setHeight(height - verticalInsets - axisShorterSide);

                    xAxisArea.setX(LEFT);
                    chartArea.setX(LEFT);
                    break;
                case LEFT:
                default  :
                    yAxisArea.setX(LEFT);
                    yAxisArea.setWidth(axisShorterSide);
                    yAxisArea.setHeight(height - verticalInsets - axisShorterSide);

                    xAxisArea.setX(LEFT + axisShorterSide);
                    chartArea.setX(LEFT + axisShorterSide);
                    break;
            }
            switch(_xAxisPosition) {
                case TOP:
                    xAxisArea.setY(TOP);
                    xAxisArea.setWidth(width - horizontalInsets - axisShorterSide);
                    xAxisArea.setHeight(axisShorterSide);

                    yAxisArea.setY(TOP + axisShorterSide);
                    chartArea.setY(TOP + axisShorterSide);
                    break;
                case BOTTOM:
                default:
                    xAxisArea.setY(height - BOTTOM - axisShorterSide);
                    xAxisArea.setWidth(width - horizontalInsets - axisShorterSide);
                    xAxisArea.setHeight(axisShorterSide);

                    yAxisArea.setY(TOP);
                    chartArea.setY(TOP);
                    break;
            }

            chartArea.setWidth(xAxisArea.getWidth());
            chartArea.setHeight(yAxisArea.getHeight());

            chartBackgroundRect.setWidth(chartArea.getWidth());
            chartBackgroundRect.setHeight(chartArea.getHeight());
            chartBackgroundRect.relocate(chartArea.getX(), chartArea.getY());

            xAxis.setLayoutX(xAxisArea.getX());
            xAxis.setLayoutY(xAxisArea.getY());
            xAxis.setMinSize(xAxisArea.getWidth(), xAxisArea.getHeight());
            xAxis.setMaxSize(xAxisArea.getWidth(), xAxisArea.getHeight());
            xAxis.setPrefSize(xAxisArea.getWidth(), xAxisArea.getHeight());

            yAxis.setLayoutX(yAxisArea.getX());
            yAxis.setLayoutY(yAxisArea.getY());
            yAxis.setMinSize(yAxisArea.getWidth(), yAxisArea.getHeight());
            yAxis.setMaxSize(yAxisArea.getWidth(), yAxisArea.getHeight());
            yAxis.setPrefSize(yAxisArea.getWidth(), yAxisArea.getHeight());

            imageView.setLayoutX(chartArea.getX());
            imageView.setLayoutY(chartArea.getY());
            imageView.setFitWidth(chartArea.getWidth());
            imageView.setFitHeight(chartArea.getHeight());

            drawOverview();

            canvasGrid.setWidth(chartArea.getWidth());
            canvasGrid.setHeight(chartArea.getHeight());
            canvasGrid.relocate(chartArea.getX(), chartArea.getY());

            canvasDataLayer.setWidth(chartArea.getWidth());
            canvasDataLayer.setHeight(chartArea.getHeight());
            canvasDataLayer.relocate(chartArea.getX(), chartArea.getY());

            if (toolBox.getLayoutBounds().getWidth() != 0) {
                double inset = 10;
                switch(getToolboxPosition()) {
                    case BOTTOM_LEFT:
                        toolBox.relocate(Position.LEFT == _yAxisPosition ? LEFT + yAxisArea.getWidth() + inset : LEFT + inset, Position.BOTTOM == _xAxisPosition ? height - BOTTOM - xAxis.getHeight() - toolBox.getLayoutBounds().getHeight() - inset : height - BOTTOM - toolBox.getLayoutBounds().getHeight() - inset);
                        break;
                    case BOTTOM_RIGHT:
                        toolBox.relocate(Position.LEFT == _yAxisPosition ? width - toolBox.getLayoutBounds().getWidth() - RIGHT - inset : width - toolBox.getLayoutBounds().getWidth() - RIGHT - yAxisArea.getWidth() - inset, Position.BOTTOM == _xAxisPosition ? height - BOTTOM - xAxis.getHeight() - toolBox.getLayoutBounds().getHeight() - inset : height - BOTTOM - toolBox.getLayoutBounds().getHeight() - inset);
                        break;
                    case TOP_LEFT:
                        toolBox.relocate(Position.LEFT == _yAxisPosition ? LEFT + yAxisArea.getWidth() + inset : LEFT + inset, Position.BOTTOM == _xAxisPosition ? TOP + inset : TOP + xAxisArea.getHeight() + inset);
                        break;
                    case TOP_RIGHT:
                    default:
                        toolBox.relocate(Position.LEFT == _yAxisPosition ? width - toolBox.getLayoutBounds().getWidth() - RIGHT - inset : width - toolBox.getLayoutBounds().getWidth() - RIGHT - yAxisArea.getWidth() - inset, Position.BOTTOM == _xAxisPosition ? TOP + inset : TOP + xAxisArea.getHeight() + inset);
                        break;
                }
                // Hide if wider than window
                toolBox.setVisible(chartArea.getWidth() > toolBox.getLayoutBounds().getWidth() + RIGHT + LEFT);
            }

            if (selectionRect.getWidth() > 0 && selectionRect.getHeight() > 0) {
                double selectionRectWidthScale  = selectionRect.getWidth() / chartArea.getOldWidth();
                double selectionRectHeightScale = selectionRect.getHeight() / chartArea.getOldHeight();

                double factorX = chartArea.getWidth() / chartArea.getOldWidth();
                double factorY = chartArea.getHeight() / chartArea.getOldHeight();
                double deltaX  = (selectionRect.getX() - chartArea.getOldMinX());
                double deltaY  = (selectionRect.getY() - chartArea.getOldMinY());

                selectionRect.setX(chartArea.getMinX() + (deltaX * factorX));
                selectionRect.setY(chartArea.getMinY() + (deltaY * factorY));
                selectionRect.setWidth(chartArea.getWidth() * selectionRectWidthScale);
                selectionRect.setHeight(chartArea.getHeight() * selectionRectHeightScale);
            }

            drawCrossHair(lastX, lastY);

            recalc();

            redraw();
        }
    }
}
