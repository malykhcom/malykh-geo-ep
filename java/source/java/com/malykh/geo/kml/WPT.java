package com.malykh.geo.kml;

import com.malykh.common.util.RussianTransliteration;

import java.io.*;

/**
 * @author Anton Malykh
 */
public class WPT
{
    public static void saveToFile(File file, Placemarks placemarks) throws IOException
    {
        Writer writer = new OutputStreamWriter(new FileOutputStream(file), "ASCII");
        try
        {
            writer.write("OziExplorer Waypoint File Version 1.1\r\n" +
                         "WGS 84\r\n" +
                         "Reserved 2\r\n" +
                         "garmin\r\n");
            int t = 1;
            for (Placemark placemark : placemarks.getPlacemarks())
            {
                final Point point = placemark.getPoint();
                writer.write(t+","+escapePointName(placemark.getName())+
                             ","+ point.getLatitude()+","+point.getLongitude()+",,,,,,,,,,,,,,,,,,,,\r\n");
                t++;
            }
        }
        finally
        {
            writer.close();
        }
    }
    private static String escapePointName(String n)
    {
        final String ret = RussianTransliteration.transliterate(n);
        StringBuilder sb = new StringBuilder(ret.length());
        for (char ch : ret.toCharArray())
        {
            if (ch == ',')
                sb.append(' ');
            else
                sb.append(ch);
        }
        return sb.toString();
    }
}
