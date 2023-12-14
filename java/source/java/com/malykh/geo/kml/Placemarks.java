package com.malykh.geo.kml;

import java.util.List;

/**
 * @author Anton Malykh
 */
public class Placemarks
{
    protected final String name;
    protected final List<Placemark> placemarks;
    public Placemarks(String name, List<Placemark> placemarks)
    {
        this.name = name;
        this.placemarks = placemarks;
    }
    public String getName()
    {
        return name;
    }
    public List<Placemark> getPlacemarks()
    {
        return placemarks;
    }
}
