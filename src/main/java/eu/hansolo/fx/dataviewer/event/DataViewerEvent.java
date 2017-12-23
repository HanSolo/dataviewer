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

package eu.hansolo.fx.dataviewer.event;

import eu.hansolo.fx.dataviewer.tools.CtxDimension;

import java.util.EventObject;


public class DataViewerEvent extends EventObject {
    public enum Type { SELECT, ZOOM, PAN }
    private final Type         type;
    private final CtxDimension dimension;


    // ******************** Constructor ***************************************
    public DataViewerEvent(final Object SRC, final Type TYPE, final CtxDimension DIMENSION) {
        super(SRC);
        type      = TYPE;
        dimension = DIMENSION;
    }


    // ******************** Methods *******************************************
    public Type getType() { return type; }

    public CtxDimension getDimension() { return dimension; }
}
