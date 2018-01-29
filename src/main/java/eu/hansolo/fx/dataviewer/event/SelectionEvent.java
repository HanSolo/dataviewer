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

package eu.hansolo.fx.dataviewer.event;

import eu.hansolo.fx.dataviewer.tools.CtxDimension;

import java.util.EventObject;


public class SelectionEvent extends EventObject {
    private String       name;
    private CtxDimension tile;


    // ******************** Constructors **************************************
    public SelectionEvent(final Object SRC, final String NAME, final CtxDimension TILE) {
        super(SRC);
        name = NAME;
        tile = TILE;
    }


    // ******************** Methods *******************************************
    public String getName() { return name; }

    public CtxDimension getTile() { return tile; }
}
