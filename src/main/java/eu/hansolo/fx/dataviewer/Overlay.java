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
import eu.hansolo.fx.dataviewer.tools.Helper;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Pair;

import java.rmi.UnmarshalException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class Overlay {
    public enum Symbol { NONE, PLUS, CROSS, STAR, STAR_FILLED, BOX, BOX_FILLED, CIRCLE, CIRCLE_FILLED, ELLIPSE, ELLIPSE_FILLED, TRIANGLE_UP, TRIANGLE_UP_FILLED, TRIANGLE_DOWN, TRIANGLE_DOWN_FILLED }
    public enum LineStyle {
        EMPTY(0),
        SOLID(1),
        DASHED(2),
        DOTTED(3),
        DASH_DOTTED(4);

        private final int id;

        // ******************** Constructors **********************************
        LineStyle(final int ID) {
            id = ID;
        }


        // ******************** Methods ***************************************
        public int getId() { return id; }
    }
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
    private double                                     _lineWidth;
    private DoubleProperty                             lineWidth;
    private LineStyle                                  _lineStyle;
    private ObjectProperty<LineStyle>                  lineStyle;
    private Image                                      image;
    private Point2D                                    _imagePos;
    private ObjectProperty<Point2D>                    imagePos;
    private Dimension2D                                _imageSize;
    private ObjectProperty<Dimension2D>                imageSize;
    private Pos                                        _imageAnchor;
    private ObjectProperty<Pos>                        imageAnchor;
    private boolean                                    _visible;
    private BooleanProperty                            visible;
    private ObservableList<Pair<Double, Double>>       points;
    private CopyOnWriteArrayList<OverlayEventListener> listeners;


    // ******************** Constructors **************************************
    public Overlay() {
        this(null, new Point2D(0, 0), "", false, true, true,
             DEFAULT_FILL, DEFAULT_STROKE, DEFAULT_SYMBOL_COLOR, DEFAULT_SYMBOL,false,
             1.0, new ArrayList<>());
    }
    public Overlay(final Image IMAGE, final Point2D IMAGE_POS) {
        this(IMAGE, IMAGE_POS, "", false, true, true,
             DEFAULT_FILL, DEFAULT_STROKE, DEFAULT_SYMBOL_COLOR, DEFAULT_SYMBOL,false,
             1.0, new ArrayList<>());
    }
    public Overlay(final String NAME, final boolean DO_FILL, final boolean DO_STROKE, final boolean SYMBOLS_VISIBLE,
                   final Paint FILL, final Color STROKE, final Color SYMBOL_COLOR, final Symbol SYMBOL, final boolean TIME_BASED,
                   final double LINE_WIDTH, final Pair<Double, Double>... POINTS) {
        this(null, new Point2D(0, 0), NAME, DO_FILL, DO_STROKE, SYMBOLS_VISIBLE, FILL, STROKE, SYMBOL_COLOR, SYMBOL, TIME_BASED, LINE_WIDTH, Arrays.asList(POINTS));
    }
    public Overlay(final String NAME, final boolean DO_FILL, final boolean DO_STROKE, final boolean SYMBOLS_VISIBLE,
                   final Paint FILL, final Color STROKE, final Color SYMBOL_COLOR, final Symbol SYMBOL, final boolean TIME_BASED,
                   final double LINE_WIDTH, final List<Pair<Double, Double>> POINTS) {
        this(null, new Point2D(0, 0), NAME, DO_FILL, DO_STROKE, SYMBOLS_VISIBLE, FILL, STROKE, SYMBOL_COLOR, SYMBOL, TIME_BASED, LINE_WIDTH, POINTS);
    }
    public Overlay(final Image IMAGE, final Point2D IMAGE_POS, final String NAME, final boolean DO_FILL, final boolean DO_STROKE, final boolean SYMBOLS_VISIBLE,
                   final Paint FILL, final Color STROKE, final Color SYMBOL_COLOR, final Symbol SYMBOL, final boolean TIME_BASED,
                   final double LINE_WIDTH, final List<Pair<Double, Double>> POINTS) {
        this(IMAGE, IMAGE_POS, NAME, DO_FILL, DO_STROKE, SYMBOLS_VISIBLE, FILL, STROKE, SYMBOL_COLOR, SYMBOL, TIME_BASED, LINE_WIDTH, LineStyle.SOLID, POINTS);
    }
    public Overlay(final Image IMAGE, final Point2D IMAGE_POS, final String NAME, final boolean DO_FILL, final boolean DO_STROKE, final boolean SYMBOLS_VISIBLE,
                   final Paint FILL, final Color STROKE, final Color SYMBOL_COLOR, final Symbol SYMBOL, final boolean TIME_BASED,
                   final double LINE_WIDTH, final LineStyle LINE_STYLE, final List<Pair<Double, Double>> POINTS) {
        _name           = NAME;
        _doFill         = DO_FILL;
        _doStroke       = DO_STROKE;
        _symbolsVisible = SYMBOLS_VISIBLE;
        _fill           = FILL;
        _stroke         = STROKE;
        _symbolColor    = SYMBOL_COLOR;
        _symbol         = SYMBOL;
        _timeBased      = TIME_BASED;
        _lineWidth      = Helper.clamp(0.1, 10, LINE_WIDTH);
        _lineStyle      = LINE_STYLE;
        image           = IMAGE;
        _imagePos       = null == IMAGE_POS ? _imagePos = new Point2D(0, 0) : IMAGE_POS;
        _imageSize      = null == IMAGE ? new Dimension2D(0, 0) : new Dimension2D(IMAGE.getWidth(), IMAGE.getHeight());
        _imageAnchor    = Pos.CENTER;
        _visible        = true;
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

    public double getLineWidth() { return null == lineWidth ? _lineWidth : lineWidth.get(); }
    public void setLineWidth(final double WIDTH) {
        if (null == lineWidth) {
            _lineWidth = Helper.clamp(0.1, 10, WIDTH);
            fireOverlayEvent(UPDATE_EVENT);
        } else {
            lineWidth.set(WIDTH);
        }
    }
    public DoubleProperty lineWidthProperty() {
        if (null == lineWidth) {
            lineWidth = new DoublePropertyBase(_lineWidth) {
                @Override protected void invalidated() {
                    set(Helper.clamp(0.1, 10, get()));
                    fireOverlayEvent(UPDATE_EVENT);
                }
                @Override public Object getBean() { return Overlay.this; }
                @Override public String getName() { return "lineWidth"; }
            };
        }
        return lineWidth;
    }

    public LineStyle getLineStyle() { return null == lineStyle ? _lineStyle : lineStyle.get(); }
    public void setLineStyle(final LineStyle LINE_STYLE) {
        if (null == lineStyle) {
            _lineStyle = LINE_STYLE;
            fireOverlayEvent(UPDATE_EVENT);
        } else {
            lineStyle.set(LINE_STYLE);
        }
    }
    public ObjectProperty<LineStyle> lineStyleProperty() {
        if (null == lineStyle) {
            lineStyle = new ObjectPropertyBase<LineStyle>(_lineStyle) {
                @Override protected void invalidated() { fireOverlayEvent(UPDATE_EVENT); }
                @Override public Object getBean() { return Overlay.this; }
                @Override public String getName() { return "lineStyle"; }
            };
            _lineStyle = null;
        }
        return lineStyle;
    }

    public Image getImage() { return image; }
    public void setImage(final Image IMAGE) {
        image = IMAGE;
        if (null != IMAGE && (Double.compare(getImageSize().getWidth(), 0) == 0 ||
            Double.compare(getImageSize().getHeight(), 0) == 0)) {
            setImageSize(new Dimension2D(image.getWidth(), image.getHeight()));
        }
        fireOverlayEvent(UPDATE_EVENT);
    }

    public Point2D getImagePos() { return null == imagePos ? _imagePos : imagePos.get(); }
    public void setImagePos(final Point2D POS) {
        if (null == imagePos) {
            _imagePos = null == POS ? new Point2D(0, 0) : POS;
            fireOverlayEvent(UPDATE_EVENT);
        } else {
            imagePos.set(POS);
        }
    }
    public ObjectProperty<Point2D> imagePosProperty() {
        if (null == imagePos) {
            imagePos = new ObjectPropertyBase<Point2D>(_imagePos) {
                @Override protected void invalidated() {
                    if (null == get()) { set(new Point2D(0, 0)); }
                    fireOverlayEvent(UPDATE_EVENT);
                }
                @Override public void set(final Point2D POS) { super.set(null == POS ? new Point2D(0, 0) : POS); }
                @Override public Object getBean() { return Overlay.this; }
                @Override public String getName() { return "imagePos"; }
            };
            _imagePos = null;
        }
        return imagePos;
    }

    public Pos getImageAnchor() { return null == imageAnchor ? _imageAnchor : imageAnchor.get(); }
    public void setImageAnchor(final Pos ANCHOR) {
        if (null == ANCHOR) {
            _imageAnchor = ANCHOR;
            fireOverlayEvent(UPDATE_EVENT);
        } else {
            imageAnchor.set(ANCHOR);
        }
    }
    public ObjectProperty<Pos> imageAnchorProperty() {
        if (null == imageAnchor) {
            imageAnchor = new ObjectPropertyBase<Pos>(_imageAnchor) {
                @Override protected void invalidated() { fireOverlayEvent(UPDATE_EVENT); }
                @Override public Object getBean() { return Overlay.this; }
                @Override public String getName() { return "imageAnchor"; }
            };
            _imageAnchor = null;
        }
        return imageAnchor;
    }

    public boolean isVisible() { return null == visible ? _visible : visible.get(); }
    public void setVisible(final boolean VISIBLE) {
        if (null == visible) {
            _visible = VISIBLE;
            fireOverlayEvent(UPDATE_EVENT);
        } else {
            visible.set(VISIBLE);
        }
    }
    public BooleanProperty visibleProperty() {
        if (null == visible) {
            visible = new BooleanPropertyBase(_visible) {
                @Override protected void invalidated() { fireOverlayEvent(UPDATE_EVENT); }
                @Override public Object getBean() { return Overlay.this; }
                @Override public String getName() { return "visible"; }
            };
        }
        return visible;
    }

    public Dimension2D getImageSize() { return null == imageSize ? _imageSize : imageSize.get(); }
    public void setImageSize(final Dimension2D SIZE) {
        if (null == imageSize) {
            _imageSize = null == SIZE ? new Dimension2D(0, 0) : SIZE;
            fireOverlayEvent(UPDATE_EVENT);
        } else {
            imageSize.set(SIZE);
        }
    }
    public ObjectProperty<Dimension2D> imageSizeProperty() {
        if (null == imageSize) {
            imageSize = new ObjectPropertyBase<Dimension2D>(_imageSize) {
                @Override protected void invalidated() {
                    if (null == get()) { set(new Dimension2D(0, 0)); }
                    fireOverlayEvent(UPDATE_EVENT);
                }
                @Override public void set(final Dimension2D SIZE) {
                    super.set(null == SIZE ? new Dimension2D(0, 0) : SIZE);
                }
                @Override public Object getBean() { return Overlay.this; }
                @Override public String getName() { return "imageSize"; }
            };
            _imageSize = null;
        }

        return imageSize;
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
