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
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


/**
 * User: hansolo
 * Date: 07.12.17
 * Time: 16:56
 */
public class Demo extends Application {
    private DataViewer selectionDataViewerL;
    private DataViewer selectionDataViewerR;
    private DataViewer mainDataViewer;

    @Override public void init() {
        String dataPairs1 = "1.0,1.0, 1.1,1.07, 1.2,1.17, 1.3,1.22, 1.4,1.29, 1.5,1.29, 1.6,1.31, 1.7,1.42, 1.8,1.56, 1.9,1.69, 2.0,1.85";
        Series series1    = SeriesBuilder.create()
                                         .name("CoastLine")
                                         .dataPairs(dataPairs1)
                                         .color(Color.LIME)
                                         .symbol(Symbol.FILLED_CIRCLE)
                                         .connected(true)
                                         //.timeBased(true)
                                         .build();

        String dataPairs2 = "1.066,1.115, 1.1,1.2, 1.2,1.1, 1.3,1.15, 1.4,1.20, 1.5,1.23, 1.6,1.4, 1.7,1.34, 1.8,1.45, 1.9,1.52, 2.0,1.7";
        Series series2    = SeriesBuilder.create()
                                         .name("CoastLine")
                                         .dataPairs(dataPairs2)
                                         .color(Color.rgb(0, 150, 200))
                                         .symbol(Symbol.FILLED_SQUARE)
                                         .connected(true)
                                         //.timeBased(true)
                                         .build();

        selectionDataViewerL = DataViewerBuilder.create()
                                                .prefSize(512, 300)
                                                .decimals(3)
                                                .xAxisMin(1)
                                                .xAxisMax(2)
                                                .yAxisMin(1)
                                                .yAxisMax(2)
                                                .xAxisDecimals(3)
                                                .yAxisDecimals(3)
                                                //.xAxisLabel("Whatever X")
                                                //.yAxisLabel("Whatever Y")
                                                .infoTextColor(Color.WHITE)
                                                .zoomColor(Color.rgb(26, 159, 249))
                                                .selectionColor(Color.WHITE)
                                                .series(series1, series2)
                                                .dataLayerVisible(true)
                                                .crossHairVisible(true)
                                                .backgroundColor(Color.rgb(90, 90, 90))
                                                //.chartBackgroundColor(Color.rgb(90, 90, 90))
                                                //.axisBackgroundColor(Color.rgb(90, 90, 90))
                                                .axisColor(Color.WHITE)
                                                .axisTextColor(Color.WHITE)
                                                .gridVisible(true)
                                                .gridColor(Color.rgb(255, 255, 255, 0.1))
                                                .axisAutoFontSize(false)
                                                .axisTickLabelFontSize(13)
                                                .axisTitleFontSize(13)
                                                .yAxisPosition(Position.RIGHT)
                                                .build();

        selectionDataViewerR = DataViewerBuilder.create()
                                                .prefSize(512, 300)
                                                .decimals(3)
                                                .xAxisMin(1)
                                                .xAxisMax(2)
                                                .yAxisMin(1)
                                                .yAxisMax(2)
                                                .xAxisDecimals(3)
                                                .yAxisDecimals(3)
                                                .xAxisLabel("Whatever X")
                                                .yAxisLabel("Whatever Y")
                                                //.infoTextColor(Color.WHITE)
                                                //.zoomColor(Color.MAGENTA)
                                                //.selectionColor(Color.RED)
                                                .overviewRectColor(Color.rgb(255, 255, 255, 0.5))
                                                .series(series1, series2)
                                                .dataLayerVisible(true)
                                                .crossHairVisible(true)
                                                .chartBackgroundColor(Color.DARKGRAY)
                                                .build();

        mainDataViewer = DataViewerBuilder.create()
                                          .image(new Image(Demo.class.getResourceAsStream("background.png")))
                                          .decimals(3)
                                          .xAxisMin(1)
                                          .xAxisMax(2)
                                          .yAxisMin(1)
                                          .yAxisMax(2)
                                          .xAxisDecimals(3)
                                          .yAxisDecimals(3)
                                          .xAxisLabel("Whatever X")
                                          .yAxisLabel("Whatever Y")
                                          .infoTextColor(Color.WHITE)
                                          .zoomColor(Color.MAGENTA)
                                          .selectionColor(Color.RED)
                                          .series(series1, series2)
                                          .dataLayerVisible(true)
                                          //.toolboxVisible(false)
                                          //.toolboxPosition(Pos.BOTTOM_RIGHT)
                                          //.selectToolVisible(false)
                                          //.panToolVisible(false)
                                          //.zoomToolVisible(false)
                                          .crossHairVisible(true)
                                          .overviewVisible(true)
                                          //.overviewPosition(Pos.TOP_LEFT)
                                          .gridVisible(true)
                                          //.gridColor(Color.RED)
                                          .chartBackgroundColor(Color.rgb(90, 90, 90))
                                          .build();

        selectionDataViewerR.setOnDataEvent(e -> {
            switch(e.getType()) {
                case SELECT:
                    mainDataViewer.setZoom(e);
                    break;
                case PAN:

                    break;
                case ZOOM:

                    break;
            }
        });
    }

    @Override public void start(Stage stage) {
        VBox pane = new VBox(10, mainDataViewer, new HBox(10, selectionDataViewerL, selectionDataViewerR));
        //StackPane pane = new StackPane(mainDataViewer);
        pane.setPadding(new Insets(10));

        Scene scene = new Scene(pane);

        stage.setTitle("DataViewer");
        stage.setScene(scene);
        stage.show();
    }

    @Override public void stop() {
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
