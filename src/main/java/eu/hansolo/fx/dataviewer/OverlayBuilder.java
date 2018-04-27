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

import eu.hansolo.fx.dataviewer.Overlay.Symbol;
import eu.hansolo.fx.dataviewer.event.OverlayEventListener;
import eu.hansolo.fx.dataviewer.tools.Helper;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;


/**
 * User: hansolo
 * Date: 10.01.18
 * Time: 07:29
 */
public class OverlayBuilder<B extends OverlayBuilder<B>> {
    private HashMap<String, Property> properties = new HashMap<>();


    // ******************** Constructors **************************************
    protected OverlayBuilder() {}


    // ******************** Methods *******************************************
    public static final OverlayBuilder create() {
        return new OverlayBuilder();
    }

    public final B xyPairs(final String XY_PAIRS) {
        properties.put("xyPairs", new SimpleStringProperty(XY_PAIRS));
        return (B)this;
    }

    public final B points(final Double[]... POINTS) {
        properties.put("pointsArray", new SimpleObjectProperty<>(POINTS));
        return (B) this;
    }

    public final B points(final List<Double[]> POINTS) {
        properties.put("pointsList", new SimpleObjectProperty<>(POINTS));
        return (B) this;
    }

    public final B name(final String NAME) {
        properties.put("name", new SimpleStringProperty(NAME));
        return (B) this;
    }

    public final B doFill(final boolean DO_FILL) {
        properties.put("doFill", new SimpleBooleanProperty(DO_FILL));
        return (B) this;
    }

    public final B doStroke(final boolean DO_STROKE) {
        properties.put("doStroke", new SimpleBooleanProperty(DO_STROKE));
        return (B) this;
    }

    public final B symbolsVisible(final boolean VISIBLE) {
        properties.put("symbolsVisible", new SimpleBooleanProperty(VISIBLE));
        return (B) this;
    }

    public final B fill(final Paint FILL) {
        properties.put("fill", new SimpleObjectProperty<>(FILL));
        return (B) this;
    }

    public final B stroke(final Color STROKE) {
        properties.put("stroke", new SimpleObjectProperty<>(STROKE));
        return (B) this;
    }

    public final B symbolColor(final Color COLOR) {
        properties.put("symbolColor", new SimpleObjectProperty<>(COLOR));
        return (B) this;
    }

    public final B symbol(final Symbol SYMBOL) {
        properties.put("symbol", new SimpleObjectProperty<>(SYMBOL));
        return (B) this;
    }

    public final B timeBased(final boolean TIME_BASED) {
        properties.put("timeBased", new SimpleBooleanProperty(TIME_BASED));
        return (B) this;
    }

    public final B lineWidth(final double WIDTH) {
        properties.put("lineWidth", new SimpleDoubleProperty(WIDTH));
        return (B)this;
    }

    public final B image(final Image IMAGE) {
        properties.put("image", new SimpleObjectProperty(IMAGE));
        return (B)this;
    }

    public final B imagePos(final Point2D POS) {
        properties.put("imagePos", new SimpleObjectProperty(POS));
        return (B)this;
    }

    public final B imageAnchor(final Pos ANCHOR) {
        properties.put("imageAnchor", new SimpleObjectProperty(ANCHOR));
        return (B)this;
    }

    public final B imageSize(final Dimension2D SIZE) {
        properties.put("imageSize", new SimpleObjectProperty(SIZE));
        return (B)this;
    }

    public final B listeners(final OverlayEventListener... LISTENERS) {
        properties.put("listenersArray", new SimpleObjectProperty(LISTENERS));
        return (B)this;
    }

    public final B listeners(final List<OverlayEventListener> LISTENERS) {
        properties.put("listenersList", new SimpleObjectProperty(LISTENERS));
        return (B)this;
    }

    public final Overlay build() {
        final Overlay CONTROL = new Overlay();

        if (properties.keySet().contains("pointsArray")) {
            CONTROL.setPoints(((ObjectProperty<Pair<Double,Double>[]>) properties.get("pointsArray")).get());
        }
        if (properties.keySet().contains("pointsList")) {
            CONTROL.setPoints(((ObjectProperty<List<Pair<Double,Double>>>) properties.get("pointsList")).get());
        }

        if (properties.keySet().contains("listenersArray")) {
            OverlayEventListener[] listeners = ((ObjectProperty<OverlayEventListener[]>) properties.get("listenersArray")).get();
            for(OverlayEventListener listener : listeners) { CONTROL.addOverlayEventListener(listener); }
        }
        if (properties.keySet().contains("listenersList")) {
            List<OverlayEventListener> listeners = ((ObjectProperty<List<OverlayEventListener>>) properties.get("listenersList")).get();
            for(OverlayEventListener listener : listeners) { CONTROL.addOverlayEventListener(listener); }
        }

        for (String key : properties.keySet()) {
            if ("xyPairs".equals(key)) {
                List<Pair<Double,Double>> points = Helper.convertXYPairsToList(((StringProperty) properties.get(key)).get());
                CONTROL.setPoints(points);
            } else if ("name".equals(key)) {
                CONTROL.setName(((StringProperty) properties.get(key)).get());
            } else if ("doFill".equals(key)) {
                CONTROL.setDoFill(((BooleanProperty) properties.get(key)).get());
            } else if ("doStroke".equals(key)) {
                CONTROL.setDoStroke(((BooleanProperty) properties.get(key)).get());
            } else if ("symbolsVisible".equals(key)) {
                CONTROL.setSymbolsVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("fill".equals(key)) {
                CONTROL.setFill(((ObjectProperty<Paint>) properties.get(key)).get());
            } else if ("stroke".equals(key)) {
                CONTROL.setStroke(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("symbolColor".equals(key)) {
                CONTROL.setSymbolColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("symbol".equals(key)) {
                CONTROL.setSymbol(((ObjectProperty<Symbol>) properties.get(key)).get());
            } else if ("timeBased".equals(key)) {
                CONTROL.setTimeBased(((BooleanProperty) properties.get(key)).get());
            } else if ("lineWidth".equals(key)) {
                CONTROL.setLineWidth(((DoubleProperty) properties.get(key)).get());
            } else if ("image".equals(key)) {
                CONTROL.setImage(((ObjectProperty<Image>) properties.get(key)).get());
            } else if ("imagePos".equals(key)) {
                CONTROL.setImagePos(((ObjectProperty<Point2D>) properties.get(key)).get());
            } else if ("imageAnchor".equals(key)) {
                CONTROL.setImageAnchor(((ObjectProperty<Pos>) properties.get(key)).get());
            } else if ("imageSize".equals(key)) {
                CONTROL.setImageSize(((ObjectProperty<Dimension2D>) properties.get(key)).get());
            }
        }
        return CONTROL;
    }
}