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

import eu.hansolo.fx.dataviewer.font.Fonts;
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
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.math.BigDecimal;
import java.util.Locale;

import static javafx.geometry.Orientation.VERTICAL;


@DefaultProperty("children")
public class Axis extends Region {
    public  static final double                               MIN_MAJOR_LINE_WIDTH  = 1;
    public  static final double                               MIN_MINOR_LINE_WIDTH  = 0.5;
    private static final double                               MINIMUM_WIDTH         = 0;
    private static final double                               MINIMUM_HEIGHT        = 0;
    private static final double                               MAXIMUM_WIDTH         = 4096;
    private static final double                               MAXIMUM_HEIGHT        = 4096;
    private              double                               size;
    private              double                               width;
    private              double                               height;
    private              Canvas                               canvas;
    private              GraphicsContext                      ctx;
    private              Pane                                 pane;
    private              double                               _minValue;
    private              DoubleProperty                       minValue;
    private              double                               _maxValue;
    private              DoubleProperty                       maxValue;
    private              boolean                              _autoScale;
    private              BooleanProperty                      autoScale;
    private              double                               stepSize;
    private              String                               _title;
    private              StringProperty                       title;
    private              String                               _unit;
    private              StringProperty                       unit;
    private              AxisType                             _type;
    private              ObjectProperty<AxisType>             type;
    private              Orientation                          _orientation;
    private              ObjectProperty<Orientation>          orientation;
    private              Position                             _position;
    private              ObjectProperty<Position>             position;
    private              Color                                _axisBackgroundColor;
    private              ObjectProperty<Color>                axisBackgroundColor;
    private              Color                                _axisColor;
    private              ObjectProperty<Color>                axisColor;
    private              Color                                _tickLabelColor;
    private              ObjectProperty<Color>                tickLabelColor;
    private              Color                                _titleColor;
    private              ObjectProperty<Color>                titleColor;
    private              Color                                _minorTickMarkColor;
    private              ObjectProperty<Color>                minorTickMarkColor;
    private              Color                                _majorTickMarkColor;
    private              ObjectProperty<Color>                majorTickMarkColor;
    private              Color                                _zeroColor;
    private              ObjectProperty<Color>                zeroColor;
    private              double                               _zeroPosition;
    private              DoubleProperty                       zeroPosition;
    private              double                               _minorTickSpace;
    private              double                               _majorTickSpace;
    private              boolean                              _majorTickMarksVisible;
    private              BooleanProperty                      majorTickMarksVisible;
    private              boolean                              _minorTickMarksVisible;
    private              BooleanProperty                      minorTickMarksVisible;
    private              boolean                              _tickLabelsVisible;
    private              BooleanProperty                      tickLabelsVisible;
    private              boolean                              _onlyFirstAndLastTickLabelVisible;
    private              BooleanProperty                      onlyFirstAndLastTickLabelVisible;
    private              Locale                               _locale;
    private              ObjectProperty<Locale>               locale;
    private              int                                  _decimals;
    private              IntegerProperty                      decimals;
    private              String                               tickLabelFormatString;
    private              TickLabelOrientation                 _tickLabelOrientation;
    private              ObjectProperty<TickLabelOrientation> tickLabelOrientation;
    private              TickLabelFormat                      _tickLabelFormat;
    private              ObjectProperty<TickLabelFormat>      tickLabelFormat;
    private              Font                                 tickLabelFont;
    private              Font                                 titleFont;
    private              boolean                              _autoFontSize;
    private              BooleanProperty                      autoFontSize;
    private              double                               _tickLabelFontSize;
    private              DoubleProperty                       tickLabelFontSize;
    private              double                               _titleFontSize;
    private              DoubleProperty                       titleFontSize;


    // ******************** Constructors **************************************
    public Axis() {
        this(0, 100, VERTICAL, AxisType.LINEAR, Position.LEFT, "");
    }
    public Axis(final Orientation ORIENTATION, final Position POSITION) {
        this(0, 100, ORIENTATION, AxisType.LINEAR, POSITION, "");
    }
    public Axis(final Orientation ORIENTATION, final AxisType TYPE, final Position POSITION) {
        this(0, 100, ORIENTATION, TYPE, POSITION, "");
    }
    public Axis(final double MIN_VALUE, final double MAX_VALUE, final Orientation ORIENTATION, final Position POSITION) {
        this(MIN_VALUE, MAX_VALUE, ORIENTATION, AxisType.LINEAR, POSITION, "");
    }
    public Axis(final double MIN_VALUE, final double MAX_VALUE, final Orientation ORIENTATION, final AxisType TYPE, final Position POSITION) {
        this(MIN_VALUE, MAX_VALUE, ORIENTATION, TYPE, POSITION, "");
    }
    public Axis(final double MIN_VALUE, final double MAX_VALUE, final Orientation ORIENTATION, final AxisType TYPE, final Position POSITION, final String TITLE) {
        if (VERTICAL == ORIENTATION) {
            if (Position.LEFT != POSITION && Position.RIGHT != POSITION && Position.CENTER != POSITION) {
                throw new IllegalArgumentException("Wrong combination of orientation and position!");
            }
        } else {
            if (Position.TOP != POSITION && Position.BOTTOM != POSITION && Position.CENTER != POSITION) {
                throw new IllegalArgumentException("Wrong combination of orientation and position!");
            }
        }

        _minValue                         = MIN_VALUE;
        _maxValue                         = MAX_VALUE;

        _type                             = TYPE;
        _autoScale                        = false;
        _title                            = TITLE;
        _unit                             = "";
        _orientation                      = ORIENTATION;
        _position                         = POSITION;
        _axisBackgroundColor              = Color.TRANSPARENT;
        _axisColor                        = Color.BLACK;
        _tickLabelColor                   = Color.BLACK;
        _titleColor                       = Color.BLACK;
        _minorTickMarkColor               = Color.BLACK;
        _majorTickMarkColor               = Color.BLACK;
        _zeroColor                        = Color.BLACK;
        _zeroPosition                     = 0;
        _minorTickSpace                   = 1;
        _majorTickSpace                   = 10;
        _majorTickMarksVisible            = true;
        _minorTickMarksVisible            = true;
        _tickLabelsVisible                = true;
        _onlyFirstAndLastTickLabelVisible = false;
        _locale                           = Locale.US;
        _decimals                         = 0;
        _tickLabelOrientation             = TickLabelOrientation.HORIZONTAL;
        _tickLabelFormat                  = TickLabelFormat.NUMBER;
        _autoFontSize                     = true;
        _tickLabelFontSize                = 10;
        _titleFontSize                    = 10;
        tickLabelFormatString             = new StringBuilder("%.").append(Integer.toString(_decimals)).append("f").toString();

        initGraphics();
        registerListeners();
    }


    // ******************** Initialization ************************************
    private void initGraphics() {
        if (Double.compare(getPrefWidth(), 0.0) <= 0 || Double.compare(getPrefHeight(), 0.0) <= 0 || Double.compare(getWidth(), 0.0) <= 0 ||
            Double.compare(getHeight(), 0.0) <= 0) {
            if (getPrefWidth() != 0 && getPrefHeight() != 0) {
                if (VERTICAL == getOrientation()) {
                    setPrefSize(20, 250);
                } else {
                    setPrefSize(250, 20);
                }
            }
        }

        getStyleClass().add("axis");

        canvas = new Canvas(width, height);
        ctx = canvas.getGraphicsContext2D();

        pane = new Pane(canvas);

        getChildren().setAll(pane);
    }
    private void registerListeners() {
        widthProperty().addListener(o -> resize());
        heightProperty().addListener(o -> resize());
    }


    // ******************** Methods *******************************************
    @Override protected double computeMinWidth(final double HEIGHT) { return MINIMUM_WIDTH; }
    @Override protected double computeMinHeight(final double WIDTH) { return MINIMUM_HEIGHT; }
    @Override protected double computePrefWidth(final double HEIGHT) { return super.computePrefWidth(HEIGHT); }
    @Override protected double computePrefHeight(final double WIDTH) { return super.computePrefHeight(WIDTH); }
    @Override protected double computeMaxWidth(final double HEIGHT) { return MAXIMUM_WIDTH; }
    @Override protected double computeMaxHeight(final double WIDTH) { return MAXIMUM_HEIGHT; }

    @Override public ObservableList<Node> getChildren() { return super.getChildren(); }

    public double getMinValue() {  return null == minValue ? _minValue : minValue.get();  }
    public void setMinValue(final double VALUE) {
        if (null == minValue) {
            if (VALUE > getMaxValue()) { setMaxValue(VALUE); }
            _minValue = Helper.clamp(-Double.MAX_VALUE, getMaxValue(), VALUE);
        } else {
            minValue.set(VALUE);
        }
    }
    public DoubleProperty minValueProperty() {
        if (null == minValue) {
            minValue = new DoublePropertyBase(_minValue) {
                @Override protected void invalidated() { if (getValue() > getMaxValue()) setMaxValue(getValue()); }
                @Override public Object getBean() {  return Axis.this;  }
                @Override public String getName() {  return "minValue"; }
            };
        }
        return minValue;
    }

    public double getMaxValue() { return null == maxValue ? _maxValue : maxValue.get(); }
    public void setMaxValue(final double VALUE) {
        if (null == maxValue) {
            if (VALUE < getMinValue()) { setMinValue(VALUE); }
            _maxValue = Helper.clamp(getMinValue(), Double.MAX_VALUE, VALUE);
        } else {
            maxValue.set(VALUE);
        }
    }
    public DoubleProperty maxValueProperty() {
        if (null == maxValue) {
            maxValue = new DoublePropertyBase(_maxValue) {
                @Override protected void invalidated() { if (get() < getMinValue()) setMinValue(get()); }
                @Override public Object getBean() { return Axis.this; }
                @Override public String getName() { return "maxValue"; }
            };
        }
        return maxValue;
    }

    public void setMinMax(final double MIN_VALUE, final double MAX_VALUE) {
        setMinValue(MIN_VALUE);
        setMaxValue(MAX_VALUE);
        resize();
    }
    public double getRange() { return getMaxValue() - getMinValue(); }

    public boolean isAutoScale() { return null == autoScale ? _autoScale : autoScale.get(); }
    public void setAutoScale(final boolean AUTO_SCALE) {
        if (null == autoScale) {
            _autoScale = AUTO_SCALE;
            redraw();
        } else {
            autoScale.set(AUTO_SCALE);
        }
    }
    public BooleanProperty autoScaleProperty() {
        if (null == autoScale) {
            autoScale = new BooleanPropertyBase(_autoScale) {
                @Override protected void invalidated() { redraw(); }
                @Override public Object getBean() { return Axis.this; }
                @Override public String getName() { return "autoScale"; }
            };
        }
        return autoScale;
    }

    public void shift(final double VALUE) {
        setMinMax(getMinValue() + VALUE, getMaxValue() + VALUE);
    }

    public String getTitle() {  return null == title ? _title : title.get(); }
    public void setTitle(final String TITLE) {
        if (null == title) {
            _title = TITLE;
            redraw();
        } else {
            title.set(TITLE);
        }
    }
    public StringProperty titleProperty() {
        if (null == title) {
            title = new StringPropertyBase(_title) {
                @Override protected void invalidated() { redraw(); }
                @Override public Object getBean() {  return Axis.this;  }
                @Override public String getName() {  return "title";  }
            };
            _title = null;
        }
        return title;
    }

    public String getUnit() { return null == unit ? _unit : unit.get(); }
    public void setUnit(final String UNIT) {
        if (null == unit) {
            _unit = UNIT;
            redraw();
        } else {
            unit.set(UNIT);
        }
    }
    public StringProperty unitProperty() {
        if (null == unit) {
            unit = new StringPropertyBase(_unit) {
                @Override protected void invalidated() { redraw(); }
                @Override public Object getBean() {  return Axis.this;  }
                @Override public String getName() {  return "unit";  }
            };
            _unit = null;
        }
        return unit;
    }

    public AxisType getType() { return null == type ? _type : type.get(); }
    public void setType(final AxisType TYPE) {
        if (null == type) {
            _type = TYPE;
            redraw();
        } else {
            type.set(TYPE);
        }
    }
    public ObjectProperty<AxisType> typeProperty() {
        if (null == type) {
            type = new ObjectPropertyBase<AxisType>(_type) {
                @Override protected void invalidated() { redraw(); }
                @Override public Object getBean() {  return Axis.this;  }
                @Override public String getName() {  return "axisType";  }
            };
            _type = null;
        }
        return type;
    }

    public Orientation getOrientation() { return null == orientation ? _orientation : orientation.get(); }
    public void setOrientation(final Orientation ORIENTATION) {
        if (null == orientation) {
            _orientation = ORIENTATION;
            redraw();
        } else {
            orientation.set(ORIENTATION);
        }
    }
    public ObjectProperty<Orientation> orientationProperty() {
        if (null == orientation) {
            orientation = new ObjectPropertyBase<Orientation>(_orientation) {
                @Override protected void invalidated() { redraw(); }
                @Override public Object getBean() { return Axis.this; }
                @Override public String getName() {  return "orientation";  }
            };
            _orientation = null;
        }
        return orientation;
    }

    public Position getPosition() { return null == position ? _position : position.get(); }
    public void setPosition(final Position POSITION) {
        if (null == position) {
            _position = POSITION;
            redraw();
        } else {
            position.set(POSITION);
        }
    }
    public ObjectProperty<Position> positionProperty() {
        if (null == position) {
            position = new ObjectPropertyBase<Position>(_position) {
                @Override protected void invalidated() { redraw(); }
                @Override public Object getBean() { return Axis.this; }
                @Override public String getName() { return "position"; }
            };
            _position = null;
        }
        return position;
    }

    public Color getAxisBackgroundColor() { return null == axisBackgroundColor ? _axisBackgroundColor : axisBackgroundColor.get(); }
    public void setAxisBackgroundColor(final Color COLOR) {
        if (null == axisBackgroundColor) {
            _axisBackgroundColor = COLOR;
            redraw();
        } else {
            axisBackgroundColor.set(COLOR);
        }
    }
    public ObjectProperty<Color> axisBackgroundColorProperty() {
        if (null == axisBackgroundColor) {
            axisBackgroundColor = new ObjectPropertyBase<Color>(_axisBackgroundColor) {
                @Override protected void invalidated() { redraw(); }
                @Override public Object getBean() { return Axis.this; }
                @Override public String getName() { return "axisBackgroundColor"; }
            };
            _axisBackgroundColor = null;
        }
        return axisBackgroundColor;
    }

    public Color getAxisColor() { return null == axisColor ? _axisColor : axisColor.get(); }
    public void setAxisColor(final Color COLOR) {
        if (null == axisColor) {
            _axisColor = COLOR;
            redraw();
        } else {
            axisColor.set(COLOR);
        }
    }
    public ObjectProperty<Color> axisColorProperty() {
        if (null == axisColor) {
            axisColor = new ObjectPropertyBase<Color>(_axisColor) {
                @Override protected void invalidated() { redraw(); }
                @Override public Object getBean() { return Axis.this; }
                @Override public String getName() { return "axisColor"; }
            };
            _axisColor = null;
        }
        return axisColor;
    }

    public Color getTickLabelColor() { return null == tickLabelColor ? _tickLabelColor : tickLabelColor.get(); }
    public void setTickLabelColor(final Color COLOR) {
        if (null == tickLabelColor) {
            _tickLabelColor = COLOR;
            redraw();
        } else {
            tickLabelColor.set(COLOR);
        }
    }
    public ObjectProperty<Color> tickLabelColorProperty() {
        if (null == tickLabelColor) {
            tickLabelColor = new ObjectPropertyBase<Color>(_tickLabelColor) {
                @Override protected void invalidated() { redraw(); }
                @Override public Object getBean() { return Axis.this; }
                @Override public String getName() { return "tickLabelColor"; }
            };
            _tickLabelColor = null;
        }
        return tickLabelColor;
    }

    public Color getTitleColor() { return null == titleColor ? _titleColor : titleColor.get(); }
    public void setTitleColor(final Color COLOR) {
        if (null == titleColor) {
            _titleColor = COLOR;
            redraw();
        } else {
            titleColor.set(COLOR);
        }
    }
    public ObjectProperty<Color> titleColorProperty() {
        if (null == titleColor) {
            titleColor = new ObjectPropertyBase<Color>(_titleColor) {
                @Override protected void invalidated() { redraw(); }
                @Override public Object getBean() { return Axis.this; }
                @Override public String getName() { return "titleColor"; }
            };
            _titleColor = null;
        }
        return titleColor;
    }

    public Color getMinorTickMarkColor() { return null == minorTickMarkColor ? _minorTickMarkColor : minorTickMarkColor.get(); }
    public void setMinorTickMarkColor(final Color COLOR) {
        if (null == minorTickMarkColor) {
            _minorTickMarkColor = COLOR;
            redraw();
        } else {
            minorTickMarkColor.set(COLOR);
        }
    }
    public ObjectProperty<Color> minorTickMarkColorProperty() {
        if (null == minorTickMarkColor) {
            minorTickMarkColor = new ObjectPropertyBase<Color>(_minorTickMarkColor) {
                @Override protected void invalidated() { redraw(); }
                @Override public Object getBean() { return Axis.this; }
                @Override public String getName() { return "minorTickMarkColor"; }
            };
            _minorTickMarkColor = null;
        }
        return minorTickMarkColor;
    }

    public Color getMajorTickMarkColor() { return null == majorTickMarkColor ? _majorTickMarkColor : majorTickMarkColor.get(); }
    public void setMajorTickMarkColor(final Color COLOR) {
        if (null == majorTickMarkColor) {
            _majorTickMarkColor = COLOR;
            redraw();
        } else {
            majorTickMarkColor.set(COLOR);
        }
    }
    public ObjectProperty<Color> majorTickMarkColorProperty() {
        if (null == majorTickMarkColor) {
            majorTickMarkColor = new ObjectPropertyBase<Color>(_majorTickMarkColor) {
                @Override protected void invalidated() { redraw(); }
                @Override public Object getBean() { return Axis.this; }
                @Override public String getName() { return "majorTickMarkColor"; }
            };
            _majorTickMarkColor = null;
        }
        return majorTickMarkColor;
    }

    public Color getZeroColor() { return null == zeroColor ? _zeroColor : zeroColor.get(); }
    public void setZeroColor(final Color COLOR) {
        if (null == zeroColor) {
            _zeroColor = COLOR;
            redraw();
        } else {
            zeroColor.set(COLOR);
        }
    }
    public ObjectProperty<Color> zeroColorProperty() {
        if (null == zeroColor) {
            zeroColor = new ObjectPropertyBase<Color>(_zeroColor) {
                @Override protected void invalidated() { redraw(); }
                @Override public Object getBean() { return Axis.this; }
                @Override public String getName() { return "zeroColor"; }
            };
            _zeroColor = null;
        }
        return zeroColor;
    }

    public double getZeroPosition() { return null == zeroPosition ? _zeroPosition : zeroPosition.get(); }
    private void setZeroPosition(final double POSITION) {
        if (null == zeroPosition) {
            _zeroPosition = POSITION;
        } else {
            zeroPosition.set(POSITION);
        }
    }
    public ReadOnlyDoubleProperty zeroPositionProperty() {
        if (null == zeroPosition) {
            zeroPosition = new DoublePropertyBase(_zeroPosition) {
                @Override public Object getBean() { return Axis.this; }
                @Override public String getName() { return "zeroPosition"; }
            };
        }
        return zeroPosition;
    }

    protected double getMajorTickSpace() { return _majorTickSpace; }
    protected void setMajorTickSpace(final double SPACE) { _majorTickSpace = SPACE; }

    protected double getMinorTickSpace() { return _minorTickSpace; }
    protected void setMinorTickSpace(final double SPACE) { _minorTickSpace = SPACE; }

    public boolean getMajorTickMarksVisible() { return null == majorTickMarksVisible ? _majorTickMarksVisible : majorTickMarksVisible.get(); }
    public void setMajorTickMarksVisible(final boolean VISIBLE) {
        if (null == majorTickMarksVisible) {
            _majorTickMarksVisible = VISIBLE;
            redraw();
        } else {
            majorTickMarksVisible.set(VISIBLE);
        }
    }
    public BooleanProperty majorTickMarksVisibleProperty() {
        if (null == majorTickMarksVisible) {
            majorTickMarksVisible = new BooleanPropertyBase(_majorTickMarksVisible) {
                @Override protected void invalidated() { redraw(); }
                @Override public Object getBean() { return Axis.this; }
                @Override public String getName() { return "majorTickMarksVisible"; }
            };
        }
        return majorTickMarksVisible;
    }

    public boolean getMinorTickMarksVisible() { return null == minorTickMarksVisible ? _minorTickMarksVisible : minorTickMarksVisible.get(); }
    public void setMinorTickMarksVisible(final boolean VISIBLE) {
        if (null == minorTickMarksVisible) {
            _minorTickMarksVisible = VISIBLE;
            redraw();
        } else {
            minorTickMarksVisible.set(VISIBLE);
        }
    }
    public BooleanProperty minorTickMarksVisibleProperty() {
        if (null == minorTickMarksVisible) {
            minorTickMarksVisible = new BooleanPropertyBase(_minorTickMarksVisible) {
                @Override protected void invalidated() { redraw(); }
                @Override public Object getBean() { return Axis.this; }
                @Override public String getName() { return "minorTickMarksVisible"; }
            };
        }
        return minorTickMarksVisible;
    }

    public boolean getTickLabelsVisible() { return null == tickLabelsVisible ? _tickLabelsVisible : tickLabelsVisible.get(); }
    public void setTickLabelsVisible(final boolean VISIBLE) {
        if (null == tickLabelsVisible) {
            _tickLabelsVisible = VISIBLE;
            redraw();
        } else {
            tickLabelsVisible.set(VISIBLE);
        }
    }
    public BooleanProperty tickLabelsVisibleProperty() {
        if (null == tickLabelsVisible) {
            tickLabelsVisible = new BooleanPropertyBase(_tickLabelsVisible) {
                @Override protected void invalidated() { redraw(); }
                @Override public Object getBean() { return Axis.this; }
                @Override public String getName() { return "tickLabelsVisible"; }
            };
        }
        return tickLabelsVisible;
    }

    public boolean isOnlyFirstAndLastTickLabelVisible() { return null == onlyFirstAndLastTickLabelVisible ? _onlyFirstAndLastTickLabelVisible : onlyFirstAndLastTickLabelVisible.get(); }
    public void setOnlyFirstAndLastTickLabelVisible(final boolean VISIBLE) {
        if (null == onlyFirstAndLastTickLabelVisible) {
            _onlyFirstAndLastTickLabelVisible = VISIBLE;
            redraw();
        } else {
            onlyFirstAndLastTickLabelVisible.set(VISIBLE);
        }
    }
    public BooleanProperty onlyFirstAndLastTickLabelVisibleProperty() {
        if (null == onlyFirstAndLastTickLabelVisible) {
            onlyFirstAndLastTickLabelVisible = new BooleanPropertyBase(_onlyFirstAndLastTickLabelVisible) {
                @Override protected void invalidated() { redraw(); }
                @Override public Object getBean() { return Axis.this; }
                @Override public String getName() { return "onlyFirstAndLastTickLabelVisible"; }
            };
        }
        return onlyFirstAndLastTickLabelVisible;
    }

    public Locale getLocale() { return null == locale ? _locale : locale.get(); }
    public void setLocale(final Locale LOCALE) {
        if (null == locale) {
            _locale = LOCALE;
            tickLabelFormatString = new StringBuilder("%.").append(Integer.toString(getDecimals())).append("f").toString();
            redraw();
        } else {
            locale.set(LOCALE);
        }
    }
    public ObjectProperty<Locale> localeProperty() {
        if (null == locale) {
            locale = new ObjectPropertyBase<Locale>(_locale) {
                @Override protected void invalidated() {
                    tickLabelFormatString = new StringBuilder("%.").append(Integer.toString(getDecimals())).append("f").toString();
                    redraw();
                }
                @Override public Object getBean() { return Axis.this; }
                @Override public String getName() { return "locale"; }
            };
            _locale = null;
        }
        return locale;
    }

    public int getDecimals() { return null == decimals ? _decimals : decimals.get(); }
    public void setDecimals(final int DECIMALS) {
        if (null == decimals) {
            _decimals = Helper.clamp(0, 12, DECIMALS);
            tickLabelFormatString = new StringBuilder("%.").append(Integer.toString(_decimals)).append("f").toString();
            redraw();
        } else {
            decimals.set(DECIMALS);
        }
    }
    public IntegerProperty decimals() {
        if (null == decimals) {
            decimals = new IntegerPropertyBase(_decimals) {
                @Override protected void invalidated() {
                    set(Helper.clamp(0, 12, get()));
                    tickLabelFormatString = new StringBuilder("%.").append(Integer.toString(get())).append("f").toString();
                    redraw();
                }
                @Override public Object getBean() { return Axis.this; }
                @Override public String getName() { return "decimals"; }
            };
        }
        return decimals;
    }

    public TickLabelOrientation getTickLabelOrientation() { return null == tickLabelOrientation ? _tickLabelOrientation : tickLabelOrientation.get(); }
    public void setTickLabelOrientation(final TickLabelOrientation ORIENTATION) {
        if (null == tickLabelOrientation) {
            _tickLabelOrientation = ORIENTATION;
            redraw();
        } else {
            tickLabelOrientation.set(ORIENTATION);
        }
    }
    public ObjectProperty<TickLabelOrientation> tickLabelOrientationProperty() {
        if (null == tickLabelOrientation) {
            tickLabelOrientation = new ObjectPropertyBase<TickLabelOrientation>(_tickLabelOrientation) {
                @Override protected void invalidated() { redraw(); }
                @Override public Object getBean() {  return Axis.this;  }
                @Override public String getName() {  return "tickLabelOrientation";  }
            };
            _tickLabelOrientation = null;
        }
        return tickLabelOrientation;
    }

    public TickLabelFormat getTickLabelFormat() { return null == tickLabelFormat ? _tickLabelFormat : tickLabelFormat.get(); }
    public void setTickLabelFormat(final TickLabelFormat FORMAT) {
        if (null == tickLabelFormat) {
            _tickLabelFormat = FORMAT;
            redraw();
        } else {
            tickLabelFormat.set(FORMAT);
        }
    }
    public ObjectProperty<TickLabelFormat> tickLabelFormatProperty() {
        if (null == tickLabelFormat) {
            tickLabelFormat = new ObjectPropertyBase<TickLabelFormat>(_tickLabelFormat) {
                @Override protected void invalidated() { redraw(); }
                @Override public Object getBean() { return Axis.this; }
                @Override public String getName() { return "tickLabelFormat"; }
            };
            _tickLabelFormat = null;
        }
        return tickLabelFormat;
    }

    public boolean isAutoFontSize() { return null == autoFontSize ? _autoFontSize : autoFontSize.get(); }
    public void setAutoFontSize(final boolean AUTO) {
        if (null == autoFontSize) {
            _autoFontSize = AUTO;
            redraw();
        } else {
            autoFontSize.set(AUTO);
        }
    }
    public BooleanProperty autoFontSizeProperty() {
        if (null == autoFontSize) {
            autoFontSize = new BooleanPropertyBase(_autoFontSize) {
                @Override protected void invalidated() { redraw(); }
                @Override public Object getBean() { return Axis.this; }
                @Override public String getName() { return "autoFontSize"; }
            };
        }
        return autoFontSize;
    }

    public double getTickLabelFontSize() { return null == tickLabelFontSize ? _tickLabelFontSize : tickLabelFontSize.get(); }
    public void setTickLabelFontSize(final double SIZE) {
        if (null == tickLabelFontSize) {
            _tickLabelFontSize = SIZE;
            tickLabelFont      = Fonts.latoLight(SIZE);
            drawAxis();
        } else {
            tickLabelFontSize.set(SIZE);
        }
    }
    public DoubleProperty tickLabelFontSizeProperty() {
        if (null == tickLabelFontSize) {
            tickLabelFontSize = new DoublePropertyBase(_tickLabelFontSize) {
                @Override protected void invalidated() {
                    tickLabelFont = Fonts.latoLight(get());
                    drawAxis();
                }
                @Override public Object getBean() { return Axis.this; }
                @Override public String getName() { return "tickLabelFontSize"; }
            };
        }
        return tickLabelFontSize;
    }

    public double getTitleFontSize() { return null == titleFontSize ? _titleFontSize : titleFontSize.get(); }
    public void setTitleFontSize(final double SIZE) {
        if (null == titleFontSize) {
            _titleFontSize = SIZE;
            titleFont      = Fonts.latoRegular(getTitleFontSize());
            drawAxis();
        } else {
            titleFontSize.set(SIZE);
        }
    }
    public DoubleProperty titleFontSizeProperty() {
        if (null == titleFontSize) {
            titleFontSize = new DoublePropertyBase(_titleFontSize) {
                @Override protected void invalidated() {
                    titleFont = Fonts.latoRegular(get());
                    drawAxis();
                }
                @Override public Object getBean() { return Axis.this; }
                @Override public String getName() { return "titleFontSize"; }
            };
        }
        return titleFontSize;
    }

    private void calcAutoScale() {
        double maxNoOfMajorTicks = 10;
        double maxNoOfMinorTicks = 10;
        double niceRange         = (Helper.calcNiceNumber((getMaxValue() - getMinValue()), false));
        setMajorTickSpace(Helper.calcNiceNumber(niceRange / (maxNoOfMajorTicks - 1), true));
        setMinorTickSpace(Helper.calcNiceNumber(getMajorTickSpace() / (maxNoOfMinorTicks - 1), true));
        double niceMinValue = (Math.floor(getMinValue() / getMajorTickSpace()) * getMajorTickSpace());
        double niceMaxValue = (Math.ceil(getMaxValue() / getMajorTickSpace()) * getMajorTickSpace());
        setMinValue(niceMinValue);
        setMaxValue(niceMaxValue);
    }

    private void calcScale() {
        double maxNoOfMajorTicks = 10;
        double maxNoOfMinorTicks = 10;

        setMajorTickSpace(Helper.calcNiceNumber(getRange() / (maxNoOfMajorTicks - 1), false));
        setMinorTickSpace(Helper.calcNiceNumber(getMajorTickSpace() / (maxNoOfMinorTicks - 1), false));
    }

    private double calcTextWidth(final Font FONT, final String TEXT) {
        Text text = new Text(TEXT);
        text.setFont(FONT);
        double width = text.getBoundsInParent().getWidth();
        text = null;
        return width;
    }


    // ******************** Drawing *******************************************
    private void drawAxis() {
        if (Double.compare(stepSize, 0) <= 0) return;

        ctx.clearRect(0, 0, width, height);
        ctx.setFill(getAxisBackgroundColor());
        ctx.fillRect(0, 0, width, height);
        ctx.setFont(tickLabelFont);
        ctx.setTextBaseline(VPos.CENTER);

        AxisType    axisType                           = getType();
        double      minValue                           = getMinValue();
        double      maxValue                           = getMaxValue();
        boolean     tickLabelsVisible                  = getTickLabelsVisible();
        boolean     isOnlyFirstAndLastTickLabelVisible = isOnlyFirstAndLastTickLabelVisible();
        double      tickLabelFontSize                  = getTickLabelFontSize();
        double      titleFontSize                      = getTitleFontSize();
        Color       tickLabelColor                     = getTickLabelColor();
        Color       zeroColor                          = getZeroColor();
        Color       majorTickMarkColor                 = getMajorTickMarkColor();
        boolean     majorTickMarksVisible              = getMajorTickMarksVisible();
        Color       minorTickMarkColor                 = getMinorTickMarkColor();
        boolean     minorTickMarksVisible              = getMinorTickMarksVisible();
        double      majorLineWidth                     = size * 0.007 < MIN_MAJOR_LINE_WIDTH ? MIN_MAJOR_LINE_WIDTH : size * 0.007;
        double      minorLineWidth                     = size * 0.005 < MIN_MINOR_LINE_WIDTH ? MIN_MINOR_LINE_WIDTH : size * 0.003;
        double      minPosition;
        double      maxPosition;
        if (VERTICAL == getOrientation()) {
            minPosition = 0;
            maxPosition = height;
        } else {
            minPosition = 0;
            maxPosition = width;
        }

        Locale      locale            = getLocale();
        Orientation orientation       = getOrientation();
        Position    position          = getPosition();
        double      anchorX           = Position.LEFT == position ? 0 : getZeroPosition();
        double      anchorXPlusOffset = anchorX + width;
        double      anchorY           = Position.BOTTOM == position ? 0 : getZeroPosition();
        double      anchorYPlusOffset = anchorY + height;
        boolean     isMinValue;
        boolean     isZero;
        boolean     isMaxValue;
        double      innerPointX;
        double      innerPointY;
        double      outerPointX;
        double      outerPointY;
        double      minorPointX;
        double      minorPointY;
        double      textPointX;
        double      textPointY;
        double      maxTextWidth;


        if (AxisType.LINEAR == axisType) {
            // ******************** Linear ************************************
            boolean    fullRange        = (minValue < 0 && maxValue > 0);
            double     minorTickSpace   = getMinorTickSpace();
            double     majorTickSpace   = getMajorTickSpace();
            double     tmpStepSize      = minorTickSpace;
            BigDecimal minorTickSpaceBD = BigDecimal.valueOf(minorTickSpace);
            BigDecimal majorTickSpaceBD = BigDecimal.valueOf(majorTickSpace);
            BigDecimal counterBD        = BigDecimal.valueOf(minValue);
            double     counter          = minValue;
            double     range            = getRange();

            ctx.setLineWidth(majorLineWidth);

            // Draw axis
            if (VERTICAL == orientation) {
                switch(position) {
                    case LEFT : ctx.strokeLine(anchorXPlusOffset, minPosition, anchorXPlusOffset, maxPosition); break;
                    case RIGHT: ctx.strokeLine(anchorX, minPosition, anchorX, maxPosition); break;
                    default   : ctx.strokeLine(anchorX, minPosition, anchorX, maxPosition); break;
                }
            } else {
                switch(position) {
                    case BOTTOM: ctx.strokeLine(minPosition, anchorY, maxPosition, anchorY); break;
                    case TOP   : ctx.strokeLine(minPosition, anchorYPlusOffset, maxPosition, anchorYPlusOffset); break;
                    default    : ctx.strokeLine(minPosition, anchorY, maxPosition, anchorY); break;
                }
            }


            // Main Loop for tick marks and labels
            BigDecimal tmpStepBD = new BigDecimal(tmpStepSize);
            tmpStepBD = tmpStepBD.setScale(12, BigDecimal.ROUND_HALF_UP);
            double tmpStep = tmpStepBD.doubleValue();
            int tickMarkCounter = 0;
            for (double i = 0; Double.compare(-range - tmpStep, i) <= 0; i -= tmpStep) {
                double fixedPosition = (counter - minValue) * stepSize;
                if (VERTICAL == orientation) {
                    if (Position.LEFT == position) {
                        innerPointX  = anchorXPlusOffset - 0.15 * width;
                        innerPointY  = fixedPosition;
                        minorPointX  = anchorXPlusOffset - 0.05 * width;
                        minorPointY  = fixedPosition;
                        outerPointX  = anchorXPlusOffset;
                        outerPointY  = fixedPosition;
                        textPointX   = anchorXPlusOffset - 0.2 * width;
                        textPointY   = fixedPosition;
                        maxTextWidth = 0.6 * width;
                    } else if (Position.RIGHT == position) {
                        innerPointX  = anchorX + 0.15 * width;
                        innerPointY  = fixedPosition;
                        minorPointX  = anchorX + 0.05 * width;
                        minorPointY  = fixedPosition;
                        outerPointX  = anchorX;
                        outerPointY  = fixedPosition;
                        textPointX   = anchorXPlusOffset - 0.15 * width;
                        textPointY   = fixedPosition;
                        maxTextWidth = 0.6 * width;
                    } else {
                        innerPointX  = anchorX - 0.25 * width;
                        innerPointY  = fixedPosition;
                        minorPointX  = anchorX - 0.15 * width;
                        minorPointY  = fixedPosition;
                        outerPointX  = anchorX;
                        outerPointY  = fixedPosition;
                        textPointX   = anchorXPlusOffset;
                        textPointY   = fixedPosition;
                        maxTextWidth = 0.6 * width;
                    }
                } else {
                    if (Position.BOTTOM == position) {
                        innerPointX  = fixedPosition;
                        innerPointY  = anchorY + 0.15 * height;
                        minorPointX  = fixedPosition;
                        minorPointY  = anchorY + 0.05 * height;
                        outerPointX  = fixedPosition;
                        outerPointY  = anchorY;
                        textPointX   = fixedPosition;
                        textPointY   = innerPointY + tickLabelFontSize * 0.6;
                        maxTextWidth = majorTickSpace * stepSize;
                    } else if (Position.TOP == position) {
                        innerPointX  = fixedPosition;
                        innerPointY  = anchorYPlusOffset - 0.15 * height;
                        minorPointX  = fixedPosition;
                        minorPointY  = anchorYPlusOffset - 0.05 * height;
                        outerPointX  = fixedPosition;
                        outerPointY  = anchorYPlusOffset;
                        textPointX   = fixedPosition;
                        textPointY   = innerPointY - tickLabelFontSize * 0.6;
                        maxTextWidth = majorTickSpace * stepSize;
                    } else {
                        innerPointX  = fixedPosition;
                        innerPointY  = anchorY - 0.25 * height;
                        minorPointX  = fixedPosition;
                        minorPointY  = anchorY - 0.15 * height;
                        outerPointX  = fixedPosition;
                        outerPointY  = anchorY;
                        textPointX   = fixedPosition;
                        textPointY   = anchorY + 0.2 * height;
                        maxTextWidth = majorTickSpace * stepSize;
                    }
                }


                if (Double.compare(counterBD.setScale(12, BigDecimal.ROUND_HALF_UP).remainder(majorTickSpaceBD).doubleValue(), 0.0) == 0) {
                    // Draw major tick mark
                    isMinValue = Double.compare(minValue, counter) == 0;
                    isMaxValue = Double.compare(maxValue, counter) == 0;
                    if (VERTICAL == orientation) {
                        isZero = Double.compare(0.0, maxValue - counter + minValue) == 0;
                    } else {
                        isZero = Double.compare(0.0, counter) == 0;
                    }

                    if (isZero) { setZeroPosition(fixedPosition); }

                    if (majorTickMarksVisible) {
                        drawTickMark((fullRange && isZero) ? zeroColor : majorTickMarkColor, majorLineWidth, innerPointX, innerPointY, outerPointX, outerPointY);
                    } else if (minorTickMarksVisible) {
                        drawTickMark((fullRange && isZero) ? zeroColor : minorTickMarkColor, minorLineWidth, minorPointX, minorPointY, outerPointX, outerPointY);
                    }

                    // Draw tick labels
                    if (tickLabelsVisible && tickLabelFont.getSize() > 6) {
                        String tickLabelString;
                        if (TickLabelFormat.NUMBER == getTickLabelFormat()) {
                            tickLabelString = Orientation.HORIZONTAL == orientation ? String.format(locale, tickLabelFormatString, (minValue - i)) : String.format(locale, tickLabelFormatString, maxValue - counter + minValue);
                        } else {
                            tickLabelString = Orientation.HORIZONTAL == orientation ? Helper.secondsToHHMMString(Helper.toSeconds(Helper.toRealValue(minValue - i), Helper.getZoneOffset())) : String.format(locale, tickLabelFormatString, maxValue - counter + minValue);
                        }
                        drawTickLabel(isOnlyFirstAndLastTickLabelVisible, isZero, isMinValue, isMaxValue, fullRange, zeroColor, tickLabelColor, textPointX, textPointY, maxTextWidth, tickLabelString, orientation);
                    }
                } else if (minorTickMarksVisible && Double.compare(counterBD.setScale(12, BigDecimal.ROUND_HALF_UP).remainder(minorTickSpaceBD).doubleValue(), 0.0) == 0) {
                    // Draw minor tick mark
                    drawTickMark(minorTickMarkColor, minorLineWidth, minorPointX, minorPointY, outerPointX, outerPointY);
                } else if (tickMarkCounter % 10 == 0) {
                    // Draw major tick mark based on number of tick marks
                    isMinValue = Double.compare(minValue, counter) == 0;
                    isMaxValue = Double.compare(maxValue, counter) == 0;
                    if (VERTICAL == orientation) {
                        isZero = Double.compare(0.0, maxValue - counter + minValue) == 0;
                    } else {
                        isZero = Double.compare(0.0, counter) == 0;
                    }

                    if (isZero) { setZeroPosition(fixedPosition); }

                    drawTickMark((fullRange && isZero) ? zeroColor : minorTickMarkColor, minorLineWidth, innerPointX, innerPointY, outerPointX, outerPointY);

                    // Draw tick labels
                    if (tickLabelsVisible && tickLabelFontSize > 6) {
                        String tickLabelString;
                        if (TickLabelFormat.NUMBER == getTickLabelFormat()) {
                            tickLabelString = Orientation.HORIZONTAL == orientation ? String.format(locale, tickLabelFormatString, (minValue - i)) : String.format(locale, tickLabelFormatString, maxValue - counter + minValue);
                        } else {
                            tickLabelString = Orientation.HORIZONTAL == orientation ? Helper.secondsToHHMMString(Helper.toSeconds(Helper.toRealValue(minValue - i), Helper.getZoneOffset())) : String.format(locale, tickLabelFormatString, maxValue - counter + minValue);
                        }
                        drawTickLabel(isOnlyFirstAndLastTickLabelVisible, isZero, isMinValue, isMaxValue, fullRange, zeroColor, tickLabelColor, textPointX, textPointY, maxTextWidth, tickLabelString, orientation);
                    }
                } else if (tickMarkCounter % 1 == 0) {
                    drawTickMark(minorTickMarkColor, minorLineWidth, minorPointX, minorPointY, outerPointX, outerPointY);
                }
                tickMarkCounter++;

                counterBD = counterBD.add(minorTickSpaceBD);
                counter = counterBD.doubleValue();
                if (counter > maxValue) break;
            }
        }

        // Draw axis title
        ctx.setFont(titleFont);
        ctx.setFill(getTitleColor());
        ctx.setTextAlign(TextAlignment.CENTER);
        ctx.setTextBaseline(VPos.CENTER);
        double titleWidth = calcTextWidth(titleFont, getTitle());
        if (Orientation.HORIZONTAL == orientation) {
            switch(position) {
                case TOP:
                    ctx.fillText(getTitle(), (width - titleWidth) * 0.5, titleFontSize * 0.5);
                    break;
                case BOTTOM:
                    ctx.fillText(getTitle(), (width - titleWidth) * 0.5, height - titleFontSize * 0.5);
                    break;
            }
        } else {
            switch(position) {
                case LEFT:
                    ctx.save();
                    ctx.translate(titleFontSize * 0.5, (height - titleFontSize) * 0.5);
                    ctx.rotate(270);
                    ctx.fillText(getTitle(), 0, 0);
                    ctx.restore();
                    break;
                case RIGHT:
                    ctx.save();
                    ctx.translate(width - titleFontSize * 0.5, (height - titleFontSize) * 0.5);
                    ctx.rotate(90);
                    ctx.fillText(getTitle(), 0, 0);
                    ctx.restore();
                    break;
            }
        }
    }

    private void drawTickMark(final Color COLOR, final double LINE_WIDTH, final double START_X, final double START_Y, final double END_X, final double END_Y) {
        ctx.setStroke(COLOR);
        ctx.setLineWidth(LINE_WIDTH);
        ctx.strokeLine(START_X, START_Y, END_X, END_Y);
    }

    private void drawTickLabel(final boolean ONLY_FIRST_AND_LAST_VISIBLE, final boolean IS_ZERO, final boolean IS_MIN, final boolean IS_MAX, final boolean FULL_RANGE,
                               final Color ZERO_COLOR, final Color COLOR, final double TEXT_X, final double TEXT_Y, final double MAX_WIDTH, final String TEXT, final Orientation ORIENTATION) {
        if (!ONLY_FIRST_AND_LAST_VISIBLE) {
            if (IS_ZERO) {
                ctx.setFill(FULL_RANGE ? ZERO_COLOR : COLOR);
            } else {
                ctx.setFill(COLOR);
            }
        } else {
            if (IS_MIN || IS_MAX) {
                if (IS_ZERO) {
                    ctx.setFill(FULL_RANGE ? ZERO_COLOR : COLOR);
                } else {
                    ctx.setFill(COLOR);
                }
            } else {
                ctx.setFill(Color.TRANSPARENT);
            }
        }

        if (VERTICAL == ORIENTATION) {
            ctx.setTextAlign(TextAlignment.RIGHT);
            double fontSize = getTitleFontSize();
            double textY;
            if (TEXT_Y < fontSize) {
                textY = fontSize * 0.5;
            } else if (TEXT_Y > height - fontSize) {
                textY = height - fontSize * 0.5;
            } else {
                textY = TEXT_Y;
            }
            ctx.fillText(TEXT, TEXT_X, textY, MAX_WIDTH);
        } else {
            if (IS_MIN) {
                ctx.setTextAlign(TextAlignment.LEFT);
            } else if (IS_MAX) {
                ctx.setTextAlign(TextAlignment.RIGHT);
            } else {
                ctx.setTextAlign(TextAlignment.CENTER);
            }

            double tickLabelWidth = calcTextWidth(tickLabelFont, TEXT);
            if (ctx.getTextAlign() == TextAlignment.CENTER && TEXT_X + tickLabelWidth * 0.5 > width) {
                ctx.fillText(TEXT, width - tickLabelWidth * 0.5, TEXT_Y, MAX_WIDTH);
            } else {
                ctx.fillText(TEXT, TEXT_X, TEXT_Y, MAX_WIDTH);
            }
        }
    }


    // ******************** Resizing ******************************************
    protected void resize() {
        width  = getWidth() - getInsets().getLeft() - getInsets().getRight();
        height = getHeight() - getInsets().getTop() - getInsets().getBottom();
        size   = width < height ? width : height;

        double aspectRatio = width / height;

        if (width > 0 && height > 0) {
            if (isAutoFontSize()) {
                setTickLabelFontSize(Helper.clamp(8, 24, 0.175 * size));
                setTitleFontSize(Helper.clamp(8, 24, 0.175 * size));
            }

            if (VERTICAL == getOrientation()) {
                width    = height * aspectRatio;
                size     = width < height ? width : height;
                stepSize = Math.abs(height / getRange());
            } else {
                height   = width / aspectRatio;
                size     = width < height ? width : height;
                stepSize = Math.abs(width / getRange());
            }
            pane.setMaxSize(width, height);
            pane.setMinSize(width, height);
            pane.setPrefSize(width, height);
            pane.relocate((getWidth() - width) * 0.5, (getHeight() - height) * 0.5);

            canvas.setWidth(width);
            canvas.setHeight(height);

            redraw();
        }
    }

    protected void redraw() {
        if (isAutoScale()) {
            calcAutoScale();
        } else {
            calcScale();
        }
        drawAxis();
    }
}
