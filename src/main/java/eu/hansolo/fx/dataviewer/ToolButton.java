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

import eu.hansolo.fx.dataviewer.tools.Helper;
import javafx.beans.DefaultProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;


@DefaultProperty("children")
public class ToolButton extends Region implements Toggle {
    public enum Tool {
        NONE("none"), SELECT("select"), PAN("pan"), ZOOM("zoom");

        public String id;

        Tool(final String ID) {
            id = ID;
        }
    }
    private static final Color                       DEFAULT_BACKGROUND_COLOR   = Color.rgb(128, 128, 128, 0.5);
    private static final Color                       DEFAULT_SELECTED_BKG_COLOR = Color.rgb(128, 128, 128, 0.75);
    private static final Color                       DEFAULT_FILL_COLOR         = Color.rgb(0, 0, 0, 0.25);
    private static final Color                       DEFAULT_SELECTED_COLOR     = Color.WHITE;
    private static final double                      PREFERRED_WIDTH            = 24;
    private static final double                      PREFERRED_HEIGHT   = 24;
    private static final double                      MINIMUM_WIDTH      = 10;
    private static final double                      MINIMUM_HEIGHT     = 10;
    private static final double                      MAXIMUM_WIDTH      = 1024;
    private static final double                      MAXIMUM_HEIGHT     = 1024;
    private              double                      size;
    private              double                      width;
    private              double                      height;
    private              Pane                        pane;
    private              Tooltip                     tooltip;
    private              String                      _tooltipText;
    private              StringProperty              tooltipText;
    private              ToggleGroup                 _toggleGroup;
    private              ObjectProperty<ToggleGroup> toggleGroup;
    private              boolean                     _selected;
    private              BooleanProperty             selected;
    private              Tool                        _tool;
    private              ObjectProperty<Tool>        tool;
    private              Color                       _fillColor;
    private              ObjectProperty<Color>       fillColor;
    private              Color                       _selectedColor;
    private              ObjectProperty<Color>       selectedColor;
    private              Color                       _backgroundColor;
    private              ObjectProperty<Color>       backgroundColor;
    private              Color                       _selectedBackgroundColor;
    private              ObjectProperty<Color>       selectedBackgroundColor;


    // ******************** Constructors **************************************
    public ToolButton() {
        this(Tool.NONE, null, "");
    }
    public ToolButton(final Tool TOOL) {
        this(TOOL, null, "");
    }
    public ToolButton(final Tool TOOL, final ToggleGroup TOGGLE_GROUP) {
        this(TOOL, TOGGLE_GROUP, "");
    }
    public ToolButton(final Tool TOOL, final ToggleGroup TOGGLE_GROUP, final String TOOL_TIP_TEXT) {
        getStylesheets().add(ToolButton.class.getResource("tool-button.css").toExternalForm());
        size                     = PREFERRED_WIDTH;
        width                    = PREFERRED_WIDTH;
        height                   = PREFERRED_HEIGHT;
        tooltip                  = new Tooltip(TOOL_TIP_TEXT);
        _tooltipText             = TOOL_TIP_TEXT;
        _toggleGroup             = TOGGLE_GROUP;
        _selected                = false;
        _tool                    = TOOL;
        _fillColor               = DEFAULT_FILL_COLOR;
        _selectedColor           = DEFAULT_SELECTED_COLOR;
        _backgroundColor         = DEFAULT_BACKGROUND_COLOR;
        _selectedBackgroundColor = DEFAULT_SELECTED_BKG_COLOR;
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

        getStyleClass().setAll("tool-button");

        pane = new Pane();
        pane.setId(getTool().id);

        getChildren().setAll(pane);

        Tooltip.install(this, tooltip);
    }

    private void registerListeners() {
        widthProperty().addListener(o -> resize());
        heightProperty().addListener(o -> resize());
        pane.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> setSelected(!isSelected()));
        pane.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> hover(e));
        pane.addEventHandler(MouseEvent.MOUSE_EXITED, e -> hover(e));
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

    public String getTooltipText() { return null == tooltipText ? _tooltipText : tooltipText.get(); }
    public void setTooltipText(final String TEXT) {
        if (null == tooltipText) {
            _tooltipText = TEXT;
            tooltip.setText(TEXT);
        } else {
            tooltipText.set(TEXT);
        }
    }
    public StringProperty tooltipTextProperty() {
        if (null == tooltipText) {
            tooltipText = new StringPropertyBase(_tooltipText) {
                @Override protected void invalidated() { tooltip.setText(get()); }
                @Override public Object getBean() { return ToolButton.this; }
                @Override public String getName() { return "tooltipText"; }
            };
            _tooltipText = null;
        }
        return tooltipText;
    }

    @Override public ToggleGroup getToggleGroup() { return null == toggleGroup ? _toggleGroup : toggleGroup.get(); }
    @Override public void setToggleGroup(final ToggleGroup TOGGLE_GROUP) {
        if (null == toggleGroup) {
            _toggleGroup = TOGGLE_GROUP;
            _toggleGroup.getToggles().add(ToolButton.this);
        } else {
            toggleGroup.set(TOGGLE_GROUP);
        }
    }
    @Override public ObjectProperty<ToggleGroup> toggleGroupProperty() {
        if (null == toggleGroup) {
            toggleGroup = new ObjectPropertyBase<ToggleGroup>(_toggleGroup) {
                @Override protected void invalidated() { get().getToggles().add(ToolButton.this); }
                @Override public Object getBean() { return ToolButton.this; }
                @Override public String getName() { return "toggleGroup"; }
            };
            _toggleGroup = null;
        }
        return toggleGroup;
    }

    @Override public boolean isSelected() { return null == selected ? _selected : selected.get(); }
    @Override public void setSelected(final boolean SELECTED) {
        if (null == selected) {
            _selected = SELECTED;
            if (null != getToggleGroup()) { getToggleGroup().selectToggle(this); }
            redraw();
        } else {
            selected.set(SELECTED);
        }
    }
    @Override public BooleanProperty selectedProperty() {
        if (null == selected) {
            selected = new BooleanPropertyBase(_selected) {
                @Override protected void invalidated() {
                    if (null != getToggleGroup()) { getToggleGroup().selectToggle(ToolButton.this); }
                    redraw();
                }
                @Override public Object getBean() { return ToolButton.this; }
                @Override public String getName() { return "selected"; }
            };
        }
        return selected;
    }

    public Tool getTool() { return null == tool ? _tool : tool.get(); }
    public void setTool(final Tool TOOL) {
        if (null == tool) {
            _tool = TOOL;
            pane.setId(_tool.id);
            redraw();
        } else {
            tool.set(TOOL);
        }
    }
    public ObjectProperty<Tool> toolProperty() {
        if (null == tool) {
            tool = new ObjectPropertyBase<Tool>(_tool) {
                @Override protected void invalidated() { pane.setId(get().id); redraw(); }
                @Override public Object getBean() { return ToolButton.this; }
                @Override public String getName() { return "tool"; }
            };
            _tool = null;
        }
        return tool;
    }

    public Color getFillColor() { return null == fillColor ? _fillColor : fillColor.getValue(); }
    public void setFillColor(final Color COLOR) {
        if (null == fillColor) {
            _fillColor = COLOR;
            redraw();
        } else {
            fillColor.setValue(COLOR);
        }
    }
    public ObjectProperty<Color> fillColorProperty() {
        if (null == fillColor) {
            fillColor = new ObjectPropertyBase<Color>(_fillColor) {
                @Override protected void invalidated() { redraw(); }
                @Override public Object getBean() { return ToolButton.this; }
                @Override public String getName() { return "fillColor"; }
            };
            _fillColor = null;
        }
        return fillColor;
    }

    public Color getSelectedColor() { return null == selectedColor ? _selectedColor : selectedColor.getValue(); }
    public void setSelectedColor(final Color COLOR) {
        if (null == selectedColor) {
            _selectedColor = COLOR;
            redraw();
        } else {
            selectedColor.setValue(COLOR);
        }
    }
    public ObjectProperty<Color> selectedColorProperty() {
        if (null == selectedColor) {
            selectedColor = new ObjectPropertyBase<Color>(_selectedColor) {
                @Override protected void invalidated() { redraw(); }
                @Override public Object getBean() { return ToolButton.this; }
                @Override public String getName() { return "selectedColor"; }
            };
            _selectedColor = null;
        }
        return selectedColor;
    }

    public Color getBackgroundColor() { return null == backgroundColor ? _backgroundColor : backgroundColor.getValue(); }
    public void setBackgroundColor(final Color COLOR) {
        if (null == backgroundColor) {
            _backgroundColor = COLOR;
            redraw();
        } else {
            backgroundColor.setValue(COLOR);
        }
    }
    public ObjectProperty<Color> backgroundColorProperty() {
        if (null == backgroundColor) {
            backgroundColor = new ObjectPropertyBase<Color>(_backgroundColor) {
                @Override protected void invalidated() { redraw(); }
                @Override public Object getBean() { return ToolButton.this; }
                @Override public String getName() { return "backgroundColor"; }
            };
            _backgroundColor = null;
        }
        return backgroundColor;
    }

    public Color getSelectedBackgroundColor() { return null == selectedBackgroundColor ? _selectedBackgroundColor : selectedBackgroundColor.getValue(); }
    public void setSelectedBackgroundColor(final Color COLOR) {
        if (null == selectedBackgroundColor) {
            _selectedBackgroundColor = COLOR;
            redraw();
        } else {
            selectedBackgroundColor.setValue(COLOR);
        }
    }
    public ObjectProperty<Color> selectedBackgroundColorProperty() {
        if (null == selectedBackgroundColor) {
            selectedBackgroundColor = new ObjectPropertyBase<Color>(_selectedBackgroundColor) {
                @Override protected void invalidated() { redraw(); }
                @Override public Object getBean() { return ToolButton.this; }
                @Override public String getName() { return "selectedBackgroundColor"; }
            };
            _selectedBackgroundColor = null;
        }
        return selectedBackgroundColor;
    }


    // ******************** Resizing ******************************************
    private void resize() {
        width  = getWidth() - getInsets().getLeft() - getInsets().getRight();
        height = getHeight() - getInsets().getTop() - getInsets().getBottom();
        size   = width < height ? width : height;

        if (width > 0 && height > 0) {
            double symbolSize = size - size * 0.2;
            pane.setMaxSize(symbolSize, symbolSize);
            pane.setPrefSize(symbolSize, symbolSize);
            pane.relocate((getWidth() - symbolSize) * 0.5, (getHeight() - symbolSize) * 0.5);
            setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(size * 0.2), Insets.EMPTY)));

            redraw();
        }
    }

    private void hover(final MouseEvent EVT) {
        EventType<? extends MouseEvent> type = EVT.getEventType();
        if (isSelected()) {
            pane.setBackground(new Background(new BackgroundFill(getSelectedColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            if (MouseEvent.MOUSE_ENTERED.equals(type)) {
                pane.setBackground(new Background(new BackgroundFill(Helper.getColorWithOpacity(DEFAULT_SELECTED_COLOR, 0.5), CornerRadii.EMPTY, Insets.EMPTY)));
            } else {
                pane.setBackground(new Background(new BackgroundFill(getFillColor(), CornerRadii.EMPTY, Insets.EMPTY)));
            }
        }
    }

    private void redraw() {
        if (isSelected()) {
            setBackground(new Background(new BackgroundFill(getSelectedBackgroundColor(), new CornerRadii(size * 0.05), Insets.EMPTY)));
            pane.setBackground(new Background(new BackgroundFill(getSelectedColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            setBackground(new Background(new BackgroundFill(getBackgroundColor(), new CornerRadii(size * 0.05), Insets.EMPTY)));
            pane.setBackground(new Background(new BackgroundFill(getFillColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }
}
