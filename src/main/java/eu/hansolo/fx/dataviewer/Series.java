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
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.scene.paint.Color;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class Series {
    public enum Symbol { NONE, CIRCLE, SQUARE, TRIANGLE, CROSS, FILLED_CIRCLE, FILLED_SQUARE, FILLED_TRIANGLE }
    private LinkedHashMap<Double, Double> data;
    private String                        _name;
    private StringProperty                name;
    private Color                         _color;
    private ObjectProperty<Color>         color;
    private Symbol                        _symbol;
    private ObjectProperty<Symbol>        symbol;
    private boolean                       _connected;
    private BooleanProperty               connected;
    private boolean                       _timeBased;
    private BooleanProperty               timeBased;


    // ******************** Constructors **************************************
    public Series() {
        this(new LinkedHashMap<>(), "", Color.RED, Symbol.NONE, false, false);
    }
    public Series(final String NAME) {
        this(new LinkedHashMap<>(), NAME, Color.RED, Symbol.NONE, false, false);
    }
    public Series(final Map<Double, Double> DATA) {
        this(DATA, "", Color.RED, Symbol.NONE, false, false);
    }
    public Series(final Map<Double, Double> DATA, final Color COLOR) {
        this(DATA, "", COLOR, Symbol.NONE, false, false);
    }
    public Series(final Map<Double, Double> DATA, final String NAME, final Color COLOR) {
        this(DATA, NAME, COLOR, Symbol.NONE, false, false);
    }
    public Series(final Map<Double, Double> DATA, final String NAME, final Color COLOR, final Symbol SYMBOL, final boolean CONNECTED) {
        this(DATA, NAME, COLOR, SYMBOL, CONNECTED, false);
    }
    public Series(final Map<Double, Double> DATA, final String NAME, final Color COLOR, final Symbol SYMBOL, final boolean CONNECTED, final boolean TIME_BASED) {
        data       = new LinkedHashMap<>(DATA.size());
        data.putAll(DATA);
        _name      = NAME;
        _color     = COLOR;
        _symbol    = SYMBOL;
        _connected = CONNECTED;
        _timeBased = TIME_BASED;
    }


    // ******************** Methods *******************************************
    public LinkedHashMap<Double, Double> getData() { return data; }
    public void setData(final Map<Double, Double> DATA) {
        data.clear();
        data.putAll(DATA);
    }

    public String getName() { return null == name ? _name : name.get(); }
    public void setName(final String NAME) {
        if (null == name) {
            _name = NAME;
        } else {
            name.set(NAME);
        }
    }
    public StringProperty nameProperty() {
        if (null == name) {
            name = new StringPropertyBase(_name) {
                @Override public Object getBean() { return Series.this; }
                @Override public String getName() { return "name"; }
            };
            _name = null;
        }
        return name;
    }

    public Color getColor() { return null == color ? _color : color.get(); }
    public void setColor(final Color COLOR) {
        if (null == color) {
            _color = COLOR;
        } else {
            color.set(COLOR);
        }
    }
    public ObjectProperty<Color> colorProperty() {
        if (null == color) {
            color = new ObjectPropertyBase<Color>(_color) {
                @Override public Object getBean() { return Series.this; }
                @Override public String getName() { return "color"; }
            };
            _color = null;
        }
        return color;
    }

    public Symbol getSymbol() { return null == symbol ? _symbol : symbol.get(); }
    public void setSymbol(final Symbol SYMBOL) {
        if (null == symbol) {
            _symbol = SYMBOL;
        } else {
            symbol.set(SYMBOL);
        }
    }
    public ObjectProperty<Symbol> symbolProperty() {
        if (null == symbol) {
            symbol = new ObjectPropertyBase<Symbol>(_symbol) {
                @Override public Object getBean() { return Series.this; }
                @Override public String getName() { return "symbol"; }
            };
            _symbol = null;
        }
        return symbol;
    }

    public boolean isConnected() { return null == connected ? _connected : connected.get(); }
    public void setConnected(final boolean CONNECTED) {
        if (null == connected) {
            _connected = CONNECTED;
        } else {
            connected.set(CONNECTED);
        }
    }
    public BooleanProperty connectedProperty() {
        if (null == connected) {
            connected = new BooleanPropertyBase(_connected) {
                @Override public Object getBean() { return Series.this; }
                @Override public String getName() { return "connected"; }
            };
        }
        return connected;
    }

    public boolean isTimeBased() { return null == timeBased ? _timeBased : timeBased.get(); }
    public void setTimeBased(final boolean SET) {
        if (null == timeBased) {
            _timeBased = SET;
        } else {
            timeBased.set(SET);
        }
    }
    public BooleanProperty timeBasedProperty() {
        if (null == timeBased) {
            timeBased = new BooleanPropertyBase(_timeBased) {
                @Override public Object getBean() { return Series.this; }
                @Override public String getName() { return "timeBased"; }
            };
        }
        return timeBased;
    }

    @Override public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n")
               .append("  \"name\"      : \"").append(getName()).append("\",\n")
               .append("  \"color\"     : \"").append(getColor().toString().replace("0x", "#").substring(0, 7)).append("\",\n")
               .append("  \"symbol\"    : \"").append(getSymbol().name()).append("\",\n")
               .append("  \"connected\" : ").append(isConnected()).append(",\n")
               .append("  \"timeBased\" : ").append(isTimeBased()).append(",\n")
               .append("  \"data\"      : \"");
        getData().entrySet().forEach((entry) -> builder.append(entry.getKey()).append(",").append(entry.getValue()).append(","));
        builder.setLength(builder.length() - 1);
        builder.append("\"\n")
               .append("}");
        return builder.toString();
    }
}
