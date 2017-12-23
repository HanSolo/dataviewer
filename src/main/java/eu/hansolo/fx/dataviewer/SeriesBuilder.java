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
import eu.hansolo.fx.dataviewer.tools.Helper;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;


public class SeriesBuilder<B extends SeriesBuilder<B>> {
    private HashMap<String, Property> properties = new HashMap<>();


    // ******************** Constructors **************************************
    protected SeriesBuilder() {}


    // ******************** Methods *******************************************
    public static final SeriesBuilder create() {
        return new SeriesBuilder();
    }

    public final B dataPairs(final String DATA_PAIRS) {
        properties.put("dataPairs", new SimpleStringProperty(DATA_PAIRS));
        return (B)this;
    }

    public final B data(final Map<Double, Double> DATA) {
        properties.put("data", new SimpleObjectProperty<>(DATA));
        return (B)this;
    }

    public final B name(final String NAME) {
        properties.put("name", new SimpleStringProperty(NAME));
        return (B)this;
    }

    public final B color(final Color COLOR) {
        properties.put("color", new SimpleObjectProperty<>(COLOR));
        return (B)this;
    }

    public final B symbol(final Symbol SYMBOL) {
        properties.put("symbol", new SimpleObjectProperty<>(SYMBOL));
        return (B)this;
    }

    public final B connected(final boolean CONNECTED) {
        properties.put("connected", new SimpleBooleanProperty(CONNECTED));
        return (B)this;
    }

    public final B zoomColor(final Color COLOR) {
        properties.put("zoomColor", new SimpleObjectProperty(COLOR));
        return (B)this;
    }

    public final B timeBased(final boolean TIME_BASED) {
        properties.put("timeBased", new SimpleBooleanProperty(TIME_BASED));
        return (B)this;
    }

    public final Series build() {
        final Series SERIES = new Series();
        for (String key : properties.keySet()) {
            if ("dataPairs".equals(key)) {
                Map<Double, Double> data = Helper.convertDataPairsToMap(((StringProperty) properties.get(key)).get());
                SERIES.setData(data);
            } else if ("data".equals(key)) {
                SERIES.setData(((ObjectProperty<Map<Double, Double>>) properties.get("data")).get());
            } else if ("name".equals(key)) {
                SERIES.setName(((StringProperty) properties.get(key)).get());
            } else if ("color".equals(key)) {
                SERIES.setColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if("symbol".equals(key)) {
                SERIES.setSymbol(((ObjectProperty<Symbol>) properties.get(key)).get());
            } else if("connected".equals(key)) {
                SERIES.setConnected(((BooleanProperty) properties.get(key)).get());
            } else if ("timeBased".equals(key)) {
                SERIES.setTimeBased(((BooleanProperty) properties.get(key)).get());
            }
        }
        return SERIES;
    }
}
