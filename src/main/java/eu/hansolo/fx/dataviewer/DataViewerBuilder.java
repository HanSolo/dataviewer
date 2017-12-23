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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class DataViewerBuilder<B extends DataViewerBuilder<B>> {
    private HashMap<String, Property> properties = new HashMap<>();


    // ******************** Constructors **************************************
    protected DataViewerBuilder() {}


    // ******************** Methods *******************************************
    public static final DataViewerBuilder create() {
        return new DataViewerBuilder();
    }

    public final B xAxisMin(final double MIN) {
        properties.put("xAxisMin", new SimpleDoubleProperty(MIN));
        return (B)this;
    }

    public final B xAxisMax(final double MAX) {
        properties.put("xAxisMax", new SimpleDoubleProperty(MAX));
        return (B)this;
    }

    public final B yAxisMin(final double MIN) {
        properties.put("yAxisMin", new SimpleDoubleProperty(MIN));
        return (B)this;
    }

    public final B yAxisMax(final double MAX) {
        properties.put("yAxisMax", new SimpleDoubleProperty(MAX));
        return (B)this;
    }

    public final B zoomColor(final Color COLOR) {
        properties.put("zoomColor", new SimpleObjectProperty(COLOR));
        return (B)this;
    }

    public final B selectionColor(final Color COLOR) {
        properties.put("selectionColor", new SimpleObjectProperty(COLOR));
        return (B)this;
    }

    public final B backgroundColor(final Color COLOR) {
        properties.put("backgroundColor", new SimpleObjectProperty<>(COLOR));
        return (B)this;
    }

    public final B infoTextColor(final Color COLOR) {
        properties.put("infoTextColor", new SimpleObjectProperty(COLOR));
        return (B)this;
    }

    public final B axisTextColor(final Color COLOR) {
        properties.put("axisTextColor", new SimpleObjectProperty<>(COLOR));
        return (B)this;
    }

    public final B axisColor(final Color COLOR) {
        properties.put("axisColor", new SimpleObjectProperty<>(COLOR));
        return (B)this;
    }

    public final B axisBackgroundColor(final Color COLOR) {
        properties.put("axisBackgroundColor", new SimpleObjectProperty<>(COLOR));
        return (B)this;
    }

    public final B overviewRectColor(final Color COLOR) {
        properties.put("overviewRectColor", new SimpleObjectProperty(COLOR));
        return (B)this;
    }

    public final B decimals(final int DECIMALS) {
        properties.put("decimals", new SimpleIntegerProperty(DECIMALS));
        return (B)this;
    }

    public final B locale(final Locale LOCALE) {
        properties.put("locale", new SimpleObjectProperty<>(LOCALE));
        return (B)this;
    }

    public final B image(final Image IMAGE) {
        properties.put("image", new SimpleObjectProperty(IMAGE));
        return (B)this;
    }

    public final B xAxisDecimals(final int DECIMALS) {
        properties.put("xAxisDecimals", new SimpleIntegerProperty(DECIMALS));
        return (B)this;
    }

    public final B yAxisDecimals(final int DECIMALS) {
        properties.put("yAxisDecimals", new SimpleIntegerProperty(DECIMALS));
        return (B)this;
    }

    public final B overviewVisible(final boolean VISIBLE) {
        properties.put("overviewVisible", new SimpleBooleanProperty(VISIBLE));
        return (B)this;
    }

    public final B overviewPosition(final Pos POSITION) {
        properties.put("overviewPosition", new SimpleObjectProperty(POSITION));
        return (B)this;
    }

    public final B toolboxVisible(final boolean VISIBLE) {
        properties.put("toolboxVisible", new SimpleBooleanProperty(VISIBLE));
        return (B)this;
    }

    public final B toolboxPosition(final Pos POSITION) {
        properties.put("toolboxPosition", new SimpleObjectProperty<>(POSITION));
        return (B)this;
    }

    public final B selectToolVisible(final boolean VISIBLE) {
        properties.put("selectToolVisible", new SimpleBooleanProperty(VISIBLE));
        return (B)this;
    }

    public final B panToolVisible(final boolean VISIBLE) {
        properties.put("panToolVisible", new SimpleBooleanProperty(VISIBLE));
        return (B)this;
    }

    public final B zoomToolVisible(final boolean VISIBLE) {
        properties.put("zoomToolVisible", new SimpleBooleanProperty(VISIBLE));
        return (B)this;
    }

    public final B adjustGridToData(final boolean ADJUST) {
        properties.put("adjustGridToData", new SimpleBooleanProperty(ADJUST));
        return (B)this;
    }

    public final B series(final Series... SERIES) {
        properties.put("seriesArray", new SimpleObjectProperty<>(SERIES));
        return (B)this;
    }

    public final B series(final List<Series> SERIES) {
        properties.put("seriesList", new SimpleObjectProperty<>(SERIES));
        return (B)this;
    }

    public final B dataLayerVisible(final boolean VISIBLE) {
        properties.put("dataLayerVisible", new SimpleBooleanProperty(VISIBLE));
        return (B)this;
    }

    public final B xAxisLabel(final String LABEL) {
        properties.put("xAxisLabel", new SimpleStringProperty(LABEL));
        return (B)this;
    }

    public final B yAxisLabel(final String LABEL) {
        properties.put("yAxisLabel", new SimpleStringProperty(LABEL));
        return (B)this;
    }

    public final B crossHairVisible(final boolean VISIBLE) {
        properties.put("crossHairVisible", new SimpleBooleanProperty(VISIBLE));
        return (B)this;
    }

    public final B crossHairColor(final Color COLOR) {
        properties.put("crossHairColor", new SimpleObjectProperty<>(COLOR));
        return (B)this;
    }

    public final B chartBackgroundColor(final Color COLOR) {
        properties.put("chartBackgroundColor", new SimpleObjectProperty<>(COLOR));
        return (B)this;
    }

    public final B gridVisible(final boolean VISIBLE) {
        properties.put("gridVisible", new SimpleBooleanProperty(VISIBLE));
        return (B)this;
    }

    public final B gridColor(final Color COLOR) {
        properties.put("gridColor", new SimpleObjectProperty<>(COLOR));
        return (B)this;
    }

    public final B axisAutoFontSize(final boolean AUTO) {
        properties.put("axisAutoFontSize", new SimpleBooleanProperty(AUTO));
        return (B)this;
    }

    public final B axisTickLabelFontSize(final double SIZE) {
        properties.put("axisTickLabelFontSize", new SimpleDoubleProperty(SIZE));
        return (B)this;
    }

    public final B axisTitleFontSize(final double SIZE) {
        properties.put("axisTitleFontSize", new SimpleDoubleProperty(SIZE));
        return (B)this;
    }

    public final B xAxisAutoFontSize(final boolean AUTO) {
        properties.put("xAxisAutoFontSize", new SimpleBooleanProperty(AUTO));
        return (B)this;
    }

    public final B xAxisTickLabelFontSize(final double SIZE) {
        properties.put("xAxisTickLabelFontSize", new SimpleDoubleProperty(SIZE));
        return (B)this;
    }

    public final B xAxisTitleFontSize(final double SIZE) {
        properties.put("xAxisTitleFontSize", new SimpleDoubleProperty(SIZE));
        return (B)this;
    }

    public final B yAxisAutoFontSize(final boolean AUTO) {
        properties.put("yAxisAutoFontSize", new SimpleBooleanProperty(AUTO));
        return (B)this;
    }

    public final B yAxisTickLabelFontSize(final double SIZE) {
        properties.put("yAxisTickLabelFontSize", new SimpleDoubleProperty(SIZE));
        return (B)this;
    }

    public final B yAxisTitleFontSize(final double SIZE) {
        properties.put("yAxisTitleFontSize", new SimpleDoubleProperty(SIZE));
        return (B)this;
    }

    public final B xAxisPosition(final Position POSITION) {
        properties.put("xAxisPosition", new SimpleObjectProperty<>(POSITION));
        return (B)this;
    }

    public final B yAxisPosition(final Position POSITION) {
        properties.put("yAxisPosition", new SimpleObjectProperty<>(POSITION));
        return (B)this;
    }

    public final B prefSize(final double WIDTH, final double HEIGHT) {
        properties.put("prefSize", new SimpleObjectProperty<>(new Dimension2D(WIDTH, HEIGHT)));
        return (B)this;
    }
    public final B minSize(final double WIDTH, final double HEIGHT) {
        properties.put("minSize", new SimpleObjectProperty<>(new Dimension2D(WIDTH, HEIGHT)));
        return (B)this;
    }
    public final B maxSize(final double WIDTH, final double HEIGHT) {
        properties.put("maxSize", new SimpleObjectProperty<>(new Dimension2D(WIDTH, HEIGHT)));
        return (B)this;
    }

    public final B prefWidth(final double PREF_WIDTH) {
        properties.put("prefWidth", new SimpleDoubleProperty(PREF_WIDTH));
        return (B)this;
    }
    public final B prefHeight(final double PREF_HEIGHT) {
        properties.put("prefHeight", new SimpleDoubleProperty(PREF_HEIGHT));
        return (B)this;
    }

    public final B minWidth(final double MIN_WIDTH) {
        properties.put("minWidth", new SimpleDoubleProperty(MIN_WIDTH));
        return (B)this;
    }
    public final B minHeight(final double MIN_HEIGHT) {
        properties.put("minHeight", new SimpleDoubleProperty(MIN_HEIGHT));
        return (B)this;
    }

    public final B maxWidth(final double MAX_WIDTH) {
        properties.put("maxWidth", new SimpleDoubleProperty(MAX_WIDTH));
        return (B)this;
    }
    public final B maxHeight(final double MAX_HEIGHT) {
        properties.put("maxHeight", new SimpleDoubleProperty(MAX_HEIGHT));
        return (B)this;
    }

    public final B scaleX(final double SCALE_X) {
        properties.put("scaleX", new SimpleDoubleProperty(SCALE_X));
        return (B)this;
    }
    public final B scaleY(final double SCALE_Y) {
        properties.put("scaleY", new SimpleDoubleProperty(SCALE_Y));
        return (B)this;
    }

    public final B layoutX(final double LAYOUT_X) {
        properties.put("layoutX", new SimpleDoubleProperty(LAYOUT_X));
        return (B)this;
    }
    public final B layoutY(final double LAYOUT_Y) {
        properties.put("layoutY", new SimpleDoubleProperty(LAYOUT_Y));
        return (B)this;
    }

    public final B translateX(final double TRANSLATE_X) {
        properties.put("translateX", new SimpleDoubleProperty(TRANSLATE_X));
        return (B)this;
    }
    public final B translateY(final double TRANSLATE_Y) {
        properties.put("translateY", new SimpleDoubleProperty(TRANSLATE_Y));
        return (B)this;
    }

    public final B padding(final Insets INSETS) {
        properties.put("padding", new SimpleObjectProperty<>(INSETS));
        return (B)this;
    }

    public final DataViewer build() {
        final DataViewer CONTROL = new DataViewer();

        if (properties.keySet().contains("seriesArray")) {
            CONTROL.setSeries(((ObjectProperty<Series[]>) properties.get("seriesArray")).get());
        }
        if(properties.keySet().contains("seriesList")) {
            CONTROL.setSeries(((ObjectProperty<List<Series>>) properties.get("seriesList")).get());
        }

        for (String key : properties.keySet()) {
            if ("prefSize".equals(key)) {
                Dimension2D dim = ((ObjectProperty<Dimension2D>) properties.get(key)).get();
                CONTROL.setPrefSize(dim.getWidth(), dim.getHeight());
            } else if("minSize".equals(key)) {
                Dimension2D dim = ((ObjectProperty<Dimension2D>) properties.get(key)).get();
                CONTROL.setMinSize(dim.getWidth(), dim.getHeight());
            } else if("maxSize".equals(key)) {
                Dimension2D dim = ((ObjectProperty<Dimension2D>) properties.get(key)).get();
                CONTROL.setMaxSize(dim.getWidth(), dim.getHeight());
            } else if("prefWidth".equals(key)) {
                CONTROL.setPrefWidth(((DoubleProperty) properties.get(key)).get());
            } else if("prefHeight".equals(key)) {
                CONTROL.setPrefHeight(((DoubleProperty) properties.get(key)).get());
            } else if("minWidth".equals(key)) {
                CONTROL.setMinWidth(((DoubleProperty) properties.get(key)).get());
            } else if("minHeight".equals(key)) {
                CONTROL.setMinHeight(((DoubleProperty) properties.get(key)).get());
            } else if("maxWidth".equals(key)) {
                CONTROL.setMaxWidth(((DoubleProperty) properties.get(key)).get());
            } else if("maxHeight".equals(key)) {
                CONTROL.setMaxHeight(((DoubleProperty) properties.get(key)).get());
            } else if("scaleX".equals(key)) {
                CONTROL.setScaleX(((DoubleProperty) properties.get(key)).get());
            } else if("scaleY".equals(key)) {
                CONTROL.setScaleY(((DoubleProperty) properties.get(key)).get());
            } else if ("layoutX".equals(key)) {
                CONTROL.setLayoutX(((DoubleProperty) properties.get(key)).get());
            } else if ("layoutY".equals(key)) {
                CONTROL.setLayoutY(((DoubleProperty) properties.get(key)).get());
            } else if ("translateX".equals(key)) {
                CONTROL.setTranslateX(((DoubleProperty) properties.get(key)).get());
            } else if ("translateY".equals(key)) {
                CONTROL.setTranslateY(((DoubleProperty) properties.get(key)).get());
            } else if ("padding".equals(key)) {
                CONTROL.setPadding(((ObjectProperty<Insets>) properties.get(key)).get());
            } else if ("zoomColor".equals(key)) {
                CONTROL.setZoomColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("selectionColor".equals(key)) {
                CONTROL.setSelectionColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("backgroundColor".equals(key)) {
                CONTROL.setBackgroundColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("infoTextColor".equals(key)) {
                CONTROL.setInfoTextColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("axisTextColor".equals(key)) {
                CONTROL.setAxisTextColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("axisColor".equals(key)) {
                CONTROL.setAxisColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("axisBackgroundColor".equals(key)) {
                CONTROL.setAxisBackgroundColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("decimals".equals(key)) {
                CONTROL.setDecimals(((IntegerProperty) properties.get(key)).get());
            } else if ("locale".equals(key)) {
                CONTROL.setLocale(((ObjectProperty<Locale>) properties.get(key)).get());
            } else if ("xAxisMin".equals(key)) {
                CONTROL.setXAxisMin(((DoubleProperty) properties.get(key)).get());
            } else if ("xAxisMax".equals(key)) {
                CONTROL.setXAxisMax(((DoubleProperty) properties.get(key)).get());
            } else if ("yAxisMin".equals(key)) {
                CONTROL.setYAxisMin(((DoubleProperty) properties.get(key)).get());
            } else if ("yAxisMax".equals(key)) {
                CONTROL.setYAxisMax(((DoubleProperty) properties.get(key)).get());
            } else if ("image".equals(key)) {
                CONTROL.setImage(((ObjectProperty<Image>) properties.get(key)).get());
            } else if ("xAxisDecimals".equals(key)) {
                CONTROL.setXAxisDecimals(((IntegerProperty) properties.get(key)).get());
            } else if ("yAxisDecimals".equals(key)) {
                CONTROL.setYAxisDecimals(((IntegerProperty) properties.get(key)).get());
            } else if ("overviewRectColor".equals(key)) {
                CONTROL.setOverviewRectColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("overviewVisible".equals(key)) {
                CONTROL.setOverViewVisible(((BooleanProperty) properties.get(key)).get());
            } else if("overviewPosition".equals(key)) {
                CONTROL.setOverviewPosition(((ObjectProperty<Pos>) properties.get(key)).get());
            } else if ("dataLayerVisible".equals(key)) {
                CONTROL.setDataLayerVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("xAxisLabel".equals(key)) {
                CONTROL.setXAxisLabel(((StringProperty) properties.get(key)).get());
            } else if ("yAxisLabel".equals(key)) {
                CONTROL.setYAxisLabel(((StringProperty) properties.get(key)).get());
            } else if ("toolboxVisible".equals(key)) {
                CONTROL.setToolboxVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("toolboxPosition".equals(key)) {
                CONTROL.setToolboxPosition(((ObjectProperty<Pos>) properties.get(key)).get());
            } else if ("selectToolVisible".equals(key)) {
                CONTROL.setSelectToolVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("panToolVisible".equals(key)) {
                CONTROL.setPanToolVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("zoomToolVisible".equals(key)) {
                CONTROL.setZoomToolVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("adjustGridToData".equals(key)) {
                CONTROL.setAdjustGridToData(((BooleanProperty) properties.get(key)).get());
            } else if ("crossHairVisible".equals(key)) {
                CONTROL.setCrossHairVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("crossHairColor".equals(key)) {
                CONTROL.setCrossHairColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("chartBackgroundColor".equals(key)) {
                CONTROL.setChartBackgroundColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("gridVisible".equals(key)) {
                CONTROL.setGridVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("gridColor".equals(key)) {
                CONTROL.setGridColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("axisAutoFontSize".equals(key)) {
                CONTROL.setAxisAutoFontSize(((BooleanProperty) properties.get(key)).get());
            } else if ("axisTickLabelFontSize".equals(key)) {
                CONTROL.setAxisTickLabelFontSize(((DoubleProperty) properties.get(key)).get());
            } else if ("axisTitleFontSize".equals(key)) {
                CONTROL.setAxisTitleFontSize(((DoubleProperty) properties.get(key)).get());
            } else if ("xAxisAutoFontSize".equals(key)) {
                CONTROL.setXAxisAutoFontSize(((BooleanProperty) properties.get(key)).get());
            } else if ("xAxisTickLabelFontSize".equals(key)) {
                CONTROL.setXAxisTickLabelFontSize(((DoubleProperty) properties.get(key)).get());
            } else if ("xAxisTitleFontSize".equals(key)) {
                CONTROL.setXAxisTitleFontSize(((DoubleProperty) properties.get(key)).get());
            } else if ("yAxisAutoFontSize".equals(key)) {
                CONTROL.setYAxisAutoFontSize(((BooleanProperty) properties.get(key)).get());
            } else if ("yAxisTickLabelFontSize".equals(key)) {
                CONTROL.setYAxisTickLabelFontSize(((DoubleProperty) properties.get(key)).get());
            } else if ("yAxisTitleFontSize".equals(key)) {
                CONTROL.setYAxisTitleFontSize(((DoubleProperty) properties.get(key)).get());
            } else if ("xAxisPosition".equals(key)) {
                CONTROL.setXAxisPosition(((ObjectProperty<Position>) properties.get(key)).get());
            } else if ("yAxisPosition".equals(key)) {
                CONTROL.setYAxisPosition(((ObjectProperty<Position>) properties.get(key)).get());
            }
        }
        return CONTROL;
    }
}
