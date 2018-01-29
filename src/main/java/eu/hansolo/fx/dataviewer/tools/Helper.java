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


import eu.hansolo.fx.dataviewer.TickLabelOrientation;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.LinkedList;


public class Helper {
    public static final int clamp(final int MIN, final int MAX, final int VALUE) {
        if (VALUE < MIN) return MIN;
        if (VALUE > MAX) return MAX;
        return VALUE;
    }
    public static final long clamp(final long MIN, final long MAX, final long VALUE) {
        if (VALUE < MIN) return MIN;
        if (VALUE > MAX) return MAX;
        return VALUE;
    }
    public static final double clamp(final double MIN, final double MAX, final double VALUE) {
        if (Double.compare(VALUE, MIN) < 0) return MIN;
        if (Double.compare(VALUE, MAX) > 0) return MAX;
        return VALUE;
    }

    public static final double clampMin(final double MIN, final double VALUE) {
        if (VALUE < MIN) return MIN;
        return VALUE;
    }
    public static final double clampMax(final double MAX, final double VALUE) {
        if (VALUE > MAX) return MAX;
        return VALUE;
    }

    public static final double nearest(final double LESS, final double VALUE, final double MORE) {
        double lessDiff = VALUE - LESS;
        double moreDiff = MORE - VALUE;
        return lessDiff < moreDiff ? LESS : MORE;
    }

    public static final double[] calcAutoScale(final double MIN_VALUE, final double MAX_VALUE) {
        double maxNoOfMajorTicks = 10;
        double maxNoOfMinorTicks = 10;
        double minorTickSpace    = 1;
        double majorTickSpace    = 10;
        double niceRange         = (Helper.calcNiceNumber((MAX_VALUE - MIN_VALUE), false));
        majorTickSpace           = Helper.calcNiceNumber(niceRange / (maxNoOfMajorTicks - 1), true);
        minorTickSpace           = Helper.calcNiceNumber(majorTickSpace / (maxNoOfMinorTicks - 1), true);
        double niceMinValue      = (Math.floor(MIN_VALUE / majorTickSpace) * majorTickSpace);
        double niceMaxValue      = (Math.ceil(MAX_VALUE / majorTickSpace) * majorTickSpace);

        return new double[] { minorTickSpace, majorTickSpace, niceMinValue, niceMaxValue };
    }

    public static final double calcNiceNumber(final double RANGE, final boolean ROUND) {
        double niceFraction;
        double exponent = Math.floor(Math.log10(RANGE));   // exponent of range
        double fraction = RANGE / Math.pow(10, exponent);  // fractional part of range

        if (ROUND) {
            if (Double.compare(fraction, 1.5) < 0) {
                niceFraction = 1;
            } else if (Double.compare(fraction, 3)  < 0) {
                niceFraction = 2;
            } else if (Double.compare(fraction, 7) < 0) {
                niceFraction = 5;
            } else {
                niceFraction = 10;
            }
        } else {
            if (Double.compare(fraction, 1) <= 0) {
                niceFraction = 1;
            } else if (Double.compare(fraction, 2) <= 0) {
                niceFraction = 2;
            } else if (Double.compare(fraction, 5) <= 0) {
                niceFraction = 5;
            } else {
                niceFraction = 10;
            }
        }
        return niceFraction * Math.pow(10, exponent);
    }

    public static final double calcStepSize(final double RANGE, final double NO_OF_TICKS) {
        double tempStep = RANGE / NO_OF_TICKS;
        double mag      = Math.floor(Math.log10(tempStep));
        double magPow   = Math.pow(10, mag);
        double magMsd   = (int) (tempStep / magPow + 0.5);

        if (magMsd > 7.5) {
            magMsd = 10.0;
        } else if (magMsd > 5.0) {
            magMsd = 7.5;
        } else if (magMsd > 2.5) {
            magMsd = 5.0;
        } else if (magMsd > 2.0) {
            magMsd = 2.5;
        } else if (magMsd > 1.0) {
            magMsd = 2.0;
        }

        return magMsd * magPow;
    }

    public static final void rotateContextForText(final GraphicsContext CTX, final double START_ANGLE, final double ANGLE, final TickLabelOrientation ORIENTATION) {
        switch (ORIENTATION) {
            case ORTHOGONAL:
                if ((360 - START_ANGLE - ANGLE) % 360 > 90 && (360 - START_ANGLE - ANGLE) % 360 < 270) {
                    CTX.rotate((180 - START_ANGLE - ANGLE) % 360);
                } else {
                    CTX.rotate((360 - START_ANGLE - ANGLE) % 360);
                }
                break;
            case TANGENT:
                if ((360 - START_ANGLE - ANGLE - 90) % 360 > 90 && (360 - START_ANGLE - ANGLE - 90) % 360 < 270) {
                    CTX.rotate((90 - START_ANGLE - ANGLE) % 360);
                } else {
                    CTX.rotate((270 - START_ANGLE - ANGLE) % 360);
                }
                break;
            case HORIZONTAL:
            default:
                break;
        }
    }

    public static final boolean isPowerOf10(final double VALUE) {
        double value = VALUE;
        while(value > 9 && value % 10 == 0) { value /= 10; }
        return value == 1;
    }

    public static final Color getColorWithOpacity(final Color COLOR, final double OPACITY) {
        double red     = COLOR.getRed();
        double green   = COLOR.getGreen();
        double blue    = COLOR.getBlue();
        double opacity = clamp(0, 1, OPACITY);
        return Color.color(red, green, blue, opacity);
    }

    public static final LinkedList<Pair<Double,Double>> convertXYPairsToList(final String XY_PAIRS) {
        String[] pointsArray = XY_PAIRS.trim().split(",");
        int      length      = pointsArray.length;
        LinkedList<Pair<Double,Double>> points = new LinkedList<>();
        if (length % 2 != 0) { throw new IllegalArgumentException("XYPairs must contain an equal number of x,y coordinates"); }
        for (int i = 1 ; i < length ; i+=2) {
            points.add(new Pair<>(Double.valueOf(pointsArray[i - 1]), Double.valueOf(pointsArray[i])));
        }
        return points;
    }

    public static final void enableNode(final Node NODE, final boolean ENABLE) {
        NODE.setVisible(ENABLE);
        NODE.setManaged(ENABLE);
    }

    public static final ZoneOffset getZoneOffset() { return getZoneOffset(ZoneId.systemDefault()); }
    public static final ZoneOffset getZoneOffset(final ZoneId ZONE_ID) { return ZONE_ID.getRules().getOffset(Instant.now()); }

    public static final long toMillis(final LocalDateTime DATE_TIME, final ZoneOffset ZONE_OFFSET) { return toSeconds(DATE_TIME, ZONE_OFFSET) * 1000; }
    public static final long toSeconds(final LocalDateTime DATE_TIME, final ZoneOffset ZONE_OFFSET) { return DATE_TIME.toEpochSecond(ZONE_OFFSET); }

    public static final double toNumericValue(final LocalDateTime DATE) { return toNumericValue(DATE, ZoneId.systemDefault()); }
    public static final double toNumericValue(final LocalDateTime DATE, final ZoneId ZONE_ID) { return Helper.toSeconds(DATE, Helper.getZoneOffset(ZONE_ID)); }

    public static final LocalDateTime toRealValue(final double VALUE) { return secondsToLocalDateTime((long) VALUE); }
    public static final LocalDateTime toRealValue(final double VALUE, final ZoneId ZONE_ID) { return secondsToLocalDateTime((long) VALUE, ZONE_ID); }

    public static final LocalDateTime secondsToLocalDateTime(final long SECONDS) { return LocalDateTime.ofInstant(Instant.ofEpochSecond(SECONDS), ZoneId.systemDefault()); }
    public static final LocalDateTime secondsToLocalDateTime(final long SECONDS, final ZoneId ZONE_ID) { return LocalDateTime.ofInstant(Instant.ofEpochSecond(SECONDS), ZONE_ID); }

    public static final String secondsToHHMMString(final long SECONDS) {
        long[] hhmmss = secondsToHHMMSS(SECONDS);
        return String.format("%02d:%02d:%02d", hhmmss[0], hhmmss[1], hhmmss[2]);
    }
    public static final long[] secondsToHHMMSS(final long SECONDS) {
        long seconds = SECONDS % 60;
        long minutes = (SECONDS / 60) % 60;
        long hours   = (SECONDS / (60 * 60)) % 24;
        return new long[] { hours, minutes, seconds };
    }
}
