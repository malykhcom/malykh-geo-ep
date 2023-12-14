package com.malykh.geo.enterpoint.model;

import com.malykh.geo.kml.Placemark;
import com.malykh.geo.kml.Point;

/**
 * @author Anton Malykh
 */
public class DefaultPlacemark extends Placemark
{
    public DefaultPlacemark()
    {
        super("Point", new Point(52, 104));
    }
    public DefaultPlacemark(Point point)
    {
        super("Point", point);
    }
}
