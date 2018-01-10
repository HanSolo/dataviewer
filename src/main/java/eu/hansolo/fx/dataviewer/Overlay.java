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

import eu.hansolo.fx.dataviewer.event.OverlayEvent;
import eu.hansolo.fx.dataviewer.event.OverlayEvent.Type;
import eu.hansolo.fx.dataviewer.event.OverlayEventListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class Overlay {
    public enum Symbol { NONE, CIRCLE, SQUARE, TRIANGLE, CROSS, FILLED_CIRCLE, FILLED_SQUARE, FILLED_TRIANGLE }
    public  static final Paint        DEFAULT_FILL         = Color.rgb(128, 128,128, 0.5);
    public  static final Color        DEFAULT_STROKE       = Color.rgb(128, 128, 128);
    public  static final Color        DEFAULT_SYMBOL_COLOR = Color.rgb(128, 128, 128);
    public  static final Symbol       DEFAULT_SYMBOL       = Symbol.CIRCLE;
    private        final OverlayEvent UPDATE_EVENT         = new OverlayEvent(this, Type.UPDATE);

    private String                                     _name;
    private StringProperty                             name;
    private boolean                                    _doFill;
    private BooleanProperty                            doFill;
    private boolean                                    _doStroke;
    private BooleanProperty                            doStroke;
    private boolean                                    _symbolsVisible;
    private BooleanProperty                            symbolsVisible;
    private Paint                                      _fill;
    private ObjectProperty<Paint>                      fill;
    private Color                                      _stroke;
    private ObjectProperty<Color>                      stroke;
    private Color                                      _symbolColor;
    private ObjectProperty<Color>                      symbolColor;
    private Symbol                                     _symbol;
    private ObjectProperty<Symbol>                     symbol;
    private boolean                                    _timeBased;
    private BooleanProperty                            timeBased;
    private ObservableList<Pair<Double, Double>>       points;
    private CopyOnWriteArrayList<OverlayEventListener> listeners;


    // ******************** Constructors **************************************
    public Overlay() {
        this("", false, true, true,
             DEFAULT_FILL, DEFAULT_STROKE, DEFAULT_SYMBOL_COLOR, DEFAULT_SYMBOL,false,
             new ArrayList<>());
    }
    public Overlay(final String NAME, final boolean DO_FILL, final boolean DO_STROKE, final boolean SYMBOLS_VISIBLE,
                   final Paint FILL, final Color STROKE, final Color SYMBOL_COLOR, final Symbol SYMBOL, final boolean TIME_BASED,
                   final Pair<Double, Double>... POINTS) {
        this(NAME, DO_FILL, DO_STROKE, SYMBOLS_VISIBLE, FILL, STROKE, SYMBOL_COLOR, SYMBOL, TIME_BASED, Arrays.asList(POINTS));
    }
    public Overlay(final String NAME, final boolean DO_FILL, final boolean DO_STROKE, final boolean SYMBOLS_VISIBLE,
                   final Paint FILL, final Color STROKE, final Color SYMBOL_COLOR, final Symbol SYMBOL, final boolean TIME_BASED,
                   final List<Pair<Double, Double>> POINTS) {
        _name           = NAME;
        _doFill         = DO_FILL;
        _doStroke       = DO_STROKE;
        _symbolsVisible = SYMBOLS_VISIBLE;
        _fill           = FILL;
        _stroke         = STROKE;
        _symbolColor    = SYMBOL_COLOR;
        _symbol         = SYMBOL;
        _timeBased      = TIME_BASED;
        points          = FXCollections.observableArrayList();
        listeners       = new CopyOnWriteArrayList<>();

        points.setAll(POINTS);
    }


    // ******************** Methods *******************************************
    public String getName() { return null == name ? _name : name.get(); }
    public void setName(final String NAME) {
        if (null == name) {
            _name = NAME;
            fireOverlayEvent(UPDATE_EVENT);
        } else {
            name.set(NAME);
        }
    }
    public StringProperty nameProperty() {
        if (null == name) {
            name = new StringPropertyBase(_name) {
                @Override protected void invalidated() { fireOverlayEvent(UPDATE_EVENT); }
                @Override public Object getBean() { return Overlay.this; }
                @Override public String getName() {
                    return "name";
                }
            };
            _name = null;
        }
        return name;
    }

    public boolean isDoFill() { return null == doFill ? _doFill : doFill.get(); }
    public void setDoFill(final boolean DO_FILL) {
        if (null == doFill) {
            _doFill = DO_FILL;
            fireOverlayEvent(UPDATE_EVENT);
        } else {
            doFill.set(DO_FILL);
        }
    }
    public BooleanProperty doFillProperty() {
        if (null == doFill) {
            doFill = new BooleanPropertyBase(_doFill) {
                @Override protected void invalidated() { fireOverlayEvent(UPDATE_EVENT); }
                @Override public Object getBean() { return Overlay.this; }
                @Override public String getName() { return "doFill"; }
            };
        }
        return doFill;
    }

    public boolean isDoStroke() { return null == doStroke ? _doStroke : doStroke.get(); }
    public void setDoStroke(final boolean DO_STROKE) {
        if (null == doStroke) {
            _doStroke = DO_STROKE;
            fireOverlayEvent(UPDATE_EVENT);
        } else {
            doStroke.set(DO_STROKE);
        }
    }
    public BooleanProperty doStrokeProperty() {
        if (null == doStroke) {
            doStroke = new BooleanPropertyBase(_doStroke) {
                @Override protected void invalidated() { fireOverlayEvent(UPDATE_EVENT); }
                @Override public Object getBean() { return Overlay.this; }
                @Override public String getName() { return "doStroke"; }
            };
        }
        return doStroke;
    }

    public boolean isSymbolsVisible() { return null == symbolsVisible ? _symbolsVisible : symbolsVisible.get(); }
    public void setSymbolsVisible(final boolean VISIBLE) {
        if (null == symbolsVisible) {
            _symbolsVisible = VISIBLE;
            fireOverlayEvent(UPDATE_EVENT);
        } else {
            symbolsVisible.set(VISIBLE);
        }
    }
    public BooleanProperty symbolsVisibleProperty() {
        if (null == symbolsVisible) {
            symbolsVisible = new BooleanPropertyBase(_symbolsVisible) {
                @Override protected void invalidated() { fireOverlayEvent(UPDATE_EVENT); }
                @Override public Object getBean() { return Overlay.this; }
                @Override public String getName() { return "symbolsVisible"; }
            };
        }
        return symbolsVisible;
    }

    public Paint getFill() { return null == fill ? _fill : fill.get(); }
    public void setFill(final Paint FILL) {
        if (null == fill) {
            _fill = FILL;
            fireOverlayEvent(UPDATE_EVENT);
        } else {
            fill.set(FILL);
        }
    }
    public ObjectProperty<Paint> fillProperty() {
        if (null == fill) {
            fill = new ObjectPropertyBase<Paint>(_fill) {
                @Override protected void invalidated() { fireOverlayEvent(UPDATE_EVENT); }
                @Override public Object getBean() { return Overlay.this; }
                @Override public String getName() { return "fill"; }
            };
            _fill = null;
        }
        return fill;
    }

    public Color getStroke() { return null == stroke ? _stroke : stroke.get(); }
    public void setStroke(final Color STROKE) {
        if (null == stroke) {
            _stroke = STROKE;
            fireOverlayEvent(UPDATE_EVENT);
        } else {
            stroke.set(STROKE);
        }
    }
    public ObjectProperty<Color> strokeProperty() {
        if (null == stroke) {
            stroke = new ObjectPropertyBase<Color>(_stroke) {
                @Override protected void invalidated() { fireOverlayEvent(UPDATE_EVENT); }
                @Override public Object getBean() { return Overlay.this; }
                @Override public String getName() { return "stroke"; }
            };
            _stroke = null;
        }
        return stroke;
    }

    public Color getSymbolColor() { return null == symbolColor ? _symbolColor : symbolColor.get(); }
    public void setSymbolColor(final Color COLOR) {
        if (null == symbolColor) {
            _symbolColor = COLOR;
            fireOverlayEvent(UPDATE_EVENT);
        } else {
            symbolColor.set(COLOR);
        }
    }
    public ObjectProperty<Color> symbolColorProperty() {
        if (null == symbolColor) {
            symbolColor = new ObjectPropertyBase<Color>(_symbolColor) {
                @Override public Object getBean() { return Overlay.this; }
                @Override public String getName() { return "symbolColor"; }
            };
            _symbolColor = null;
        }
        return symbolColor;
    }

    public Symbol getSymbol() { return null == symbol ? _symbol : symbol.get(); }
    public void setSymbol(final Symbol SYMBOL) {
        if (null == symbol) {
            _symbol = SYMBOL;
            fireOverlayEvent(UPDATE_EVENT);
        } else {
            symbol.set(SYMBOL);
        }
    }
    public ObjectProperty<Symbol> symbolProperty() {
        if (null == symbol) {
            symbol = new ObjectPropertyBase<Symbol>(_symbol) {
                @Override protected void invalidated() { fireOverlayEvent(UPDATE_EVENT); }
                @Override public Object getBean() { return Overlay.this; }
                @Override public String getName() { return "symbol"; }
            };
            _symbol = null;
        }
        return symbol;
    }

    public boolean isTimeBased() { return null == timeBased ? _timeBased : timeBased.get(); }
    public void setTimeBased(final boolean TIME_BASED) {
        if (null == timeBased) {
            _timeBased = TIME_BASED;
            fireOverlayEvent(UPDATE_EVENT);
        } else {
            timeBased.set(TIME_BASED);
        }
    }
    public BooleanProperty timeBasedProperty() {
        if (null == timeBased) {
            timeBased = new BooleanPropertyBase(_timeBased) {
                @Override protected void invalidated() { fireOverlayEvent(UPDATE_EVENT); }
                @Override public Object getBean() { return Overlay.this; }
                @Override public String getName() { return "timeBased"; }
            };
        }
        return timeBased;
    }

    public ObservableList<Pair<Double,Double>> getPoints() { return points; }
    public void setPoints(final Pair<Double,Double>... POINTS) { setPoints(Arrays.asList(POINTS)); }
    public void setPoints(final List<Pair<Double,Double>> POINTS) {
        points.setAll(POINTS);
        fireOverlayEvent(UPDATE_EVENT);
    }
    public void addPoints(final Pair<Double,Double>... POINTS) { addPoints(Arrays.asList(POINTS)); }
    public void addPoints(final List<Pair<Double,Double>> POINTS) {
        points.addAll(POINTS);
        fireOverlayEvent(UPDATE_EVENT);
    }
    public void removePoints(final Pair<Double,Double>... POINTS) { removePoints(Arrays.asList(POINTS)); }
    public void removePoints(final List<Pair<Double,Double>> POINTS) { POINTS.forEach(point -> removePoint(point)); }
    public void addPoint(final double X, final double Y) { addPoint(new Pair<>(X, Y)); }
    public void addPoint(final Pair<Double,Double> POINT) { points.add(POINT); }
    public void removePoint(final Pair<Double,Double> POINT) { if (points.contains(POINT)) { points.remove(POINT); }}


    // ******************** EventHandling *************************************
    public void setOnOverlayEvent(final OverlayEventListener LISTENER) { addOverlayEventListener(LISTENER); }
    public void addOverlayEventListener(final OverlayEventListener LISTENER) { if (!listeners.contains(LISTENER)) { listeners.add(LISTENER); }}
    public void removeOverlayEventListener(final OverlayEventListener LISTENER) { if (listeners.contains(LISTENER)) { listeners.remove(LISTENER); }}

    public void fireOverlayEvent(final OverlayEvent EVENT) { for(OverlayEventListener listener : listeners) { listener.onOverviewEvent(EVENT); }}
}
