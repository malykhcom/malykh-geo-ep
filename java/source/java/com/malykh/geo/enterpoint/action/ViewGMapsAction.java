package com.malykh.geo.enterpoint.action;

import com.malykh.common.code.java.JavaString;
import com.malykh.common.swing.tango.TangoManager;
import com.malykh.common.web.HTMLHelper;
import com.malykh.geo.enterpoint.MainPanel;
import com.malykh.geo.enterpoint.PointsTab;
import com.malykh.geo.enterpoint.RB;
import com.malykh.geo.kml.*;
import com.malykh.geo.kml.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;

/**
 * @author Anton Malykh
 */
public class ViewGMapsAction extends AbstractAction
{
    private final MainPanel panel;
    public ViewGMapsAction(MainPanel panel)
    {
        super(RB.get("action.showgmap"));
        putValue(SMALL_ICON, TangoManager.getIcon("actions/system-search.png"));
        this.panel = panel;
    }

    public void actionPerformed(ActionEvent e)
    {
        final int index = panel.getTabbedPane().getSelectedIndex();
        if (index != -1)
        {
            try
            {
                PointsTab tab = panel.getPointsTab();
                panel.dumpPoints();
                final java.util.List<Placemark> data = tab.getModel().getAllData();
                if (data.isEmpty())
                {
                    JOptionPane.showMessageDialog(panel.getFrame(), RB.get("view.nopoints"));
                    return;
                }
                final String name = tab.getTabName();

                final File file = File.createTempFile("google-maps-", ".html");
                Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
                try
                {
                    writer.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n" +
                                 "    \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
                                 "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" style=\"height: 100%; margin: 0; padding: 0\">\n" +
                                 "  <head>\n" +
                                 "    <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/>\n" +
                                 "    <title>"+ HTMLHelper.escape(name)+"</title>\n" +
                                 "\t<script src=\"http://maps.google.com/maps?file=api&amp;v=2&amp;sensor=true&amp;key=ABQIAAAAAgwrLPf86zcCkDzqo1k8NhR7TjSi4nCfF5iE2ynVZnkXCWgg8BSCNoHvK_SCNWDQH4dGzLFWJBVQ_Q\" type=\"text/javascript\"></script>\n" +
                                 "\n" +
                                 "    <script type=\"text/javascript\">\n" +
                                 "    \n" +
                                 "    function initialize() {\n" +
                                 "      if (GBrowserIsCompatible()) {\n" +
                                 "        var map = new GMap2(document.getElementById(\"map_canvas\"));\n" +
                                 "\t\tmap.setMapType(G_HYBRID_MAP);\n" +
                                 "\t\tmap.setUIToDefault();\n" +
                                 " \n" +
                                 "\t\tvar b = new GLatLngBounds();\n" +
                                 "\t\tvar p;\n");
                    for (Placemark placemark : data)
                    {
                        final Point p = placemark.getPoint();
                        writer.write("\t\tp = new GLatLng("+p.getLatitude()+", "+p.getLongitude()+");\n");
                        writer.write("\t\tmap.addOverlay(new GMarker(p, { title: \""+ JavaString.escape(placemark.getName())+"\" }));\n");
                        writer.write("\t\tb.extend(p);\n");
                    }
                    writer.write("\t\tmap.setCenter(b.getCenter(), map.getBoundsZoomLevel(b));\n"+
                                 "      }\n" +
                                 "    }\n" +
                                 "\n" +
                                 "    </script>\n" +
                                 "  </head>\n" +
                                 "\n" +
                                 "  <body onload=\"initialize()\" onunload=\"GUnload()\" style=\"height: 100%; margin: 0; padding: 0\">\n" +
                                 "    <div id=\"map_canvas\"  style=\"width: 100%; height: 100%;\"></div>\n" +
                                 "  </body>\n" +
                                 "</html>");
                }
                finally
                {
                    writer.close();
                }
                file.deleteOnExit();
                final int ret =
                    JOptionPane.showConfirmDialog(panel.getFrame(), "Показать точки группы \""+ name +"\" (" + file +
                        ")\nв Google Maps?\n" +
                        "Google Maps будет открыт в браузере, требуется наличие интернет-соединения.", name, JOptionPane.YES_NO_OPTION);
                if (ret == JOptionPane.YES_OPTION)
                    Desktop.getDesktop().open(file);
            }
            catch (IOException ex)
            {
                throw new RuntimeException(ex);
            }
        }
    }
}