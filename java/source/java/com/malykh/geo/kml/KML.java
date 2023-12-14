package com.malykh.geo.kml;

import org.jdom.Document;
import org.jdom.Namespace;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;

/**
 * @author Anton Malykh
 */
public class KML
{
    protected static final Namespace NAMESPACE = Namespace.getNamespace("http://earth.google.com/kml/2.2");
    public static Element toXML(Placemarks placemarks)
    {
        Element root = new Element("kml", NAMESPACE);
        Element doc = new Element("Document", NAMESPACE);
        root.addContent(doc);
        Element n = new Element("name", NAMESPACE);
        n.addContent(placemarks.name);
        doc.addContent(n);
        for (Placemark placemark : placemarks.getPlacemarks())
        {
            doc.addContent(placemark.toKML());
        }
        return root;
    }
    public static void saveToFile(File file, Placemarks placemarks) throws IOException
    {
        FileOutputStream out = new FileOutputStream(file);
        try
        {
            new XMLOutputter(Format.getPrettyFormat()).output(new Document(toXML(placemarks)), out);
        }
        finally
        {
            out.close();
        }
    }
    public static Placemarks loadFromFile(File file) throws JDOMException, IOException, ParseException
    {
        Document doc = new SAXBuilder().build(file);
        Element root = doc.getRootElement();
        if (!root.getName().equals("kml"))
            throw new RuntimeException("Root must be \"kml\"");
        Element documentEl = root.getChild("Document", NAMESPACE);
        Element name = documentEl.getChild("name", NAMESPACE);
        String n = name.getTextNormalize();
        List<Placemark> placemarks = new ArrayList<Placemark>();
        for (Object o : documentEl.getChildren("Placemark", NAMESPACE))
        {
            placemarks.add(Placemark.parse((Element) o));
        }
        return new Placemarks(n, placemarks);
    }
}
