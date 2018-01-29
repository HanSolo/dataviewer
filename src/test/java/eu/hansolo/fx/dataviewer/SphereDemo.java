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

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;


/**
 * User: hansolo
 * Date: 29.01.18
 * Time: 11:12
 */
public class SphereDemo extends Application {
    private Sphere sphere;

    @Override public void init() {
        sphere = new Sphere();
        sphere.setOnSelectionEvent(e -> System.out.println(e.getName() + "\n" + e.getTile()));
    }

    @Override public void start(Stage stage) {
        StackPane pane = new StackPane(sphere);

        Scene scene = new Scene(pane);

        stage.setTitle("Sphere Demo");
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
