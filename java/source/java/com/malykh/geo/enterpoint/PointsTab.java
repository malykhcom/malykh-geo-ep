package com.malykh.geo.enterpoint;

import com.malykh.common.swing.table.ROTable;
import com.malykh.geo.kml.Placemark;
import com.malykh.geo.enterpoint.model.PointsModel;

import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.*;

/**
 * @author Anton Malykh
 */
public class PointsTab extends JPanel
{
    protected String name;
    protected PointsModel model;
    protected final JTable table;
    protected final MainPanel panel;
    public PointsTab(String name, MainPanel panel)
    {
        super(new BorderLayout());
        this.name = name;
        this.panel = panel;
        model = new PointsModel(panel);
        table = new ROTable(model);
        table.setColumnSelectionAllowed(false);
//        table.setDragEnabled(true);
//        table.setDropMode(DropMode.INSERT_ROWS);
//        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public JTable getTable()
    {
        return table;
    }

    public PointsModel getModel()
    {
        return model;
    }

    public String getTabName()
    {
        return name;
    }

    public void setTabName(String name)
    {
        this.name = name;
    }

    public void rebuildTable()
    {
        final List<Placemark> points = new ArrayList<Placemark>();
        for (int t = 0; t < model.getRowCount(); t++)
            points.add(model.getData(t));
        model = new PointsModel(panel);
        for (Placemark point : points)
        {
            model.addData(point);
        }
        table.setModel(model);
    }
}
