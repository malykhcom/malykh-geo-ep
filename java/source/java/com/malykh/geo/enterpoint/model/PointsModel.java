package com.malykh.geo.enterpoint.model;

import com.malykh.common.swing.table.BaseTableModel;
import com.malykh.geo.enterpoint.Help;
import com.malykh.geo.kml.Placemark;
import com.malykh.geo.kml.Point;
import com.malykh.geo.enterpoint.MainPanel;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @author Anton Malykh
 */
public class PointsModel extends BaseTableModel<Placemark>
{
    private final MainPanel panel;
    private final String id;
    public PointsModel(MainPanel panel)
    {
        super("N", Help.NAME,
                panel.config.latlongmode ? Help.LAT : Help.LON,
                panel.config.latlongmode ? Help.LON : Help.LAT);
        this.panel = panel;
        id = UUID.randomUUID().toString(); 
    }
    public String getId()
    {
        return id;
    }
    private String coord(double d)
    {
        return panel.config.dmsmode ? Point.getStringDMS(d) : Point.getStringD(d);
    }
    protected List<?> convertToRow(int i, Placemark mark)
    {
        List<Object> ret = new ArrayList<Object>();
        ret.add(i+1);
        ret.add(mark.getName());
        final Point point = mark.getPoint();
        if (panel.config.latlongmode)
        {
            ret.add(coord(point.getLatitude()));
            ret.add(coord(point.getLongitude()));
        }
        else
        {
            ret.add(coord(point.getLongitude()));
            ret.add(coord(point.getLatitude()));
        }
        return ret;
    }
}
