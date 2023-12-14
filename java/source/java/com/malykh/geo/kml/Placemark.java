package com.malykh.geo.kml;

import org.jdom.Element;

/**
 * @author Anton Malykh
 */
public class Placemark
{
    protected final String name;
    protected final Point point;
    public Placemark(String name, Point point)
    {
        this.name = name;
        this.point = point;
    }

    public String getName()
    {
        return name;
    }

    public Point getPoint()
    {
        return point;
    }

    public Element toKML()
    {
        Element placemark = new Element("Placemark", KML.NAMESPACE);
        Element n = new Element("name", KML.NAMESPACE);
        n.addContent(name);
        placemark.addContent(n);
        placemark.addContent(point.toXML());
        return placemark;
    }

    public static Placemark parse(Element el)
    {
        String name = el.getChildTextNormalize("name", KML.NAMESPACE);
        if (name == null)
            name = "Точка";
        return new Placemark(name, Point.parse(el.getChild("Point", KML.NAMESPACE)));
    }
}
