package com.malykh.geo.enterpoint.dialog;

import com.malykh.common.swing.component.FieldPanel;
import com.malykh.common.swing.dialog.OkCancelDialog;
import com.malykh.geo.enterpoint.Help;
import com.malykh.geo.enterpoint.component.CoordChangeListener;
import com.malykh.geo.enterpoint.component.NativeCoordField;
import com.malykh.geo.kml.Placemark;
import com.malykh.geo.kml.Point;
import com.malykh.geo.enterpoint.MainPanel;

import javax.swing.*;
import java.awt.*;

/**
 * @author Anton Malykh
 */
public class EditPointDialog
{
    public static Placemark editPoint(final MainPanel panel, Placemark mark)
    {
        final TextField name = new TextField(30);
        name.setText(mark.getName());
        final Point point = mark.getPoint();
        final NativeCoordField lon = new NativeCoordField(panel.config.dmsmode);
        final NativeCoordField lat = new NativeCoordField(panel.config.dmsmode);
        final JLabel dInfo = new JLabel();
        final JLabel dmsInfo = new JLabel();
        FieldPanel p = new FieldPanel();
        p.addField(Help.NAME, null, name, false);
        if (panel.config.latlongmode)
        {
            p.addField(Help.LAT +" "+ lat.getHint(), null, lat, false);
            p.addField(Help.LON +" "+ lon.getHint(), null, lon, false);
        }
        else
        {
            p.addField(Help.LON +" "+ lon.getHint(), null, lon, false);
            p.addField(Help.LAT +" "+ lat.getHint(), null, lat, false);
        }
        p.addField(Help.CONTROL +" "+"(°)", null, dInfo, false);
        p.addField(Help.CONTROL +" "+"(°\'\")", null, dmsInfo, false);
        final OkCancelDialog dialog = new OkCancelDialog(panel.getFrame(), Help.POINT, Help.SAVE);
        dialog.setMainPanel(p);
        dialog.packAndCenter();
        name.selectAll();

        final CoordChangeListener listener = new CoordChangeListener()
        {
            private String getD(Double d)
            {
                if (d == null)
                    return Help.ERROR_POINTER;
                return Point.getStringD(d);
            }
            private String getDMS(Double d)
            {
                if (d == null)
                    return Help.ERROR_POINTER;
                return Point.getStringDMS(d);
            }
            public void changed()
            {
                final Double latD = lat.getDouble();
                final Double lonD = lon.getDouble();
                final StringBuilder sbD = new StringBuilder();
                final StringBuilder sbDMS = new StringBuilder();
                if (panel.config.latlongmode)
                {
                    sbD.append(getD(latD)).append(", ").append(getD(lonD));
                    sbDMS.append(getDMS(latD)).append(", ").append(getDMS(lonD));
                }
                else
                {
                    sbD.append(getD(lonD)).append(", ").append(getD(latD));
                    sbDMS.append(getDMS(lonD)).append(", ").append(getDMS(latD));
                }
                dInfo.setText(sbD.toString());
                dmsInfo.setText(sbDMS.toString());
                dialog.getOkButton().setEnabled(latD != null && lonD != null);
            }
        };
        lat.addChangeListener(listener);
        lon.addChangeListener(listener);
        //
        lon.setDouble(point.getLongitude());
        lat.setDouble(point.getLatitude());
        //

        dialog.setVisible(true);
        if (dialog.isOkPressed())
        {
            final Double latv = lat.getDouble();
            final Double lonv = lon.getDouble();
            if (latv != null && lonv != null)
                return new Placemark(name.getText(), new Point(latv, lonv));
        }
        return null;
    }
}
