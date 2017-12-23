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

package eu.hansolo.fx.dataviewer.tools;


public class CtxBounds {
    private double x;
    private double y;
    private double width;
    private double height;
    private double oldX;
    private double oldY;
    private double oldWidth;
    private double oldHeight;


    // ******************** Constructors **************************************
    public CtxBounds() {
        this(0, 0, 0, 0);
    }
    public CtxBounds(final double WIDTH, final double HEIGHT) {
        this(0, 0, WIDTH, HEIGHT);
    }
    public CtxBounds(final double X, final double Y, final double WIDTH, final double HEIGHT) {
        x      = X;
        y      = Y;
        width  = WIDTH;
        height = HEIGHT;
    }


    // ******************** Methods *******************************************
    public double getOldX() { return oldX; }
    public double getX() { return x; }
    public void setX(final double X) {
        oldX = x;
        x    = X;
    }

    public double getOldY() { return oldY; }
    public double getY() { return y; }
    public void setY(final double Y) {
        oldY = y;
        y    = Y;
    }

    public double getOldMinX() { return oldX; }
    public double getMinX() { return x; }

    public double getOldMaxX() { return oldX + oldWidth; }
    public double getMaxX() { return x + width; }

    public double getOldMinY() { return oldY; }
    public double getMinY() { return y; }

    public double getOldMaxY() { return oldY + oldHeight; }
    public double getMaxY() { return y + height; }

    public double getOldWidth() { return oldWidth; }
    public double getWidth() { return width; }
    public void setWidth(final double WIDTH) {
        oldWidth = width;
        width    = Helper.clamp(0, Double.MAX_VALUE, WIDTH);
    }

    public double getOldHeight() { return oldHeight; }
    public double getHeight() { return height; }
    public void setHeight(final double HEIGHT) {
        oldHeight = height;
        height    = Helper.clamp(0, Double.MAX_VALUE, HEIGHT);
    }

    public void shiftX(final double SHIFT_X) {
        setX(x + SHIFT_X);
    }
    public void shiftY(final double SHIFT_Y) {
        setY(x + SHIFT_Y);
    }

    public void set(final CtxBounds BOUNDS) {
        setX(BOUNDS.getX());
        setY(BOUNDS.getY());
        setWidth(BOUNDS.getWidth());
        setHeight(BOUNDS.getHeight());
    }
    public void set(final double X, final double Y, final double WIDTH, final double HEIGHT) {
        setX(X);
        setY(Y);
        setWidth(WIDTH);
        setHeight(HEIGHT);
    }

    @Override public String toString() {
        return new StringBuilder().append("{\n")
                                  .append("  \"minX\"  : ").append(getMinX()).append(",\n")
                                  .append("  \"minY\"  : ").append(getMinY()).append(",\n")
                                  .append("  \"maxX\"  : ").append(getMaxX()).append(",\n")
                                  .append("  \"maxY\"  : ").append(getMaxY()).append(",\n")
                                  .append("  \"width\" : ").append(getWidth()).append(",\n")
                                  .append("  \"height\": ").append(getHeight()).append("\n")
                                  .append("}").toString();
    }
}