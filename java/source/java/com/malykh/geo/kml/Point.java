package com.malykh.geo.kml;

import org.jdom.Element;

/**
 * @author Anton Malykh
 */
public class Point
{
    protected final double latitude;
    protected final double longitude;
    private static final char COMMA = ',';

    public Point(double latitude, double longitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public Point getRound()
    {
        return new Point(Math.floor(latitude),
                         Math.floor(longitude));
    }
    public Element toXML()
    {
        Element pointEl = new Element("Point", KML.NAMESPACE);
        Element coord = new Element("coordinates", KML.NAMESPACE);
        coord.addContent(longitude+","+latitude);
        pointEl.addContent(coord);
        return pointEl;
    }

    public static Point parse(Element pointEl)
    {
        String coords = pointEl.getChildTextNormalize("coordinates", KML.NAMESPACE);
        int index = coords.indexOf(COMMA);
        String lon = coords.substring(0, index).trim();
        coords = coords.substring(index+1).trim();
        index = coords.indexOf(COMMA);
        if (index != -1) // skip altitude
            coords = coords.substring(0, index).trim();
        String lat = coords;
        return new Point(Double.parseDouble(lat), Double.parseDouble(lon));
    }
    public static int[] getDMS(double degrees)
    {
        int[] ret = new int[3];
        double seconds = Math.round(degrees*3600d);
        ret[0] = (int) (seconds/3600d);
        seconds -= ret[0]*3600d;
        ret[1] = (int) (seconds/60d);
        seconds -= ret[1]*60d;
        ret[2] = (int) seconds;
        return ret;
    }
    private static String intString(int v)
    {
        String ret = String.valueOf(v);
        final int len = ret.length();
        if (len == 1)
        {
            return '0' + ret;
        }
        return ret;
    }
    public static String getStringDMS(double degrees)
    {
        int[] dms = getDMS(degrees);
        return intString(dms[0])+"° "+intString(dms[1])+"\' "+intString(dms[2])+"\"";
    }
    public static double getD(int d, int m, int s)
    {
        double ret = d;
        ret += m/60d;
        ret += s/3600d;
        return ret;
    }
    public static String getStringD(double degrees)
    {
        return String.valueOf(degrees)+'°';
    }
}
