package com.malykh.geo.enterpoint.action;

import com.malykh.common.swing.tango.TangoManager;
import com.malykh.geo.enterpoint.*;
import com.malykh.geo.enterpoint.dialog.EditPointDialog;
import com.malykh.geo.enterpoint.model.DefaultPlacemark;
import com.malykh.geo.enterpoint.model.PointsModel;
import com.malykh.geo.kml.*;
import com.malykh.geo.kml.Point;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @author Anton Malykh
 */
public class AddPointAction extends AbstractAction
{
    private final MainPanel panel;
    private final Map<String, Point> lastPoints = new HashMap<String, Point>();
    public AddPointAction(MainPanel panel)
    {
        super(RB.get("action.addpoint"));
        putValue(SMALL_ICON, TangoManager.getIcon("actions/list-add.png"));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
            KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        this.panel = panel;
    }

    public void actionPerformed(ActionEvent e)
    {
        final PointsTab tab = panel.getPointsTab();
        if (tab != null)
        {
            final PointsModel model = tab.getModel();
            final String modelId = model.getId();
            for (;;)
            {
                Placemark def;
                final Point lastPoint = lastPoints.get(modelId);
                if (lastPoint == null)
                {
                    final List<Placemark> data = model.getAllData();
                    if (data.isEmpty())
                        def = new DefaultPlacemark();
                    else
                        def = new DefaultPlacemark(data.get(data.size()-1).getPoint().getRound());
                }
                else
                {
                    def = new DefaultPlacemark(lastPoint);
                }
                final Placemark cp = EditPointDialog.editPoint(panel, def);
                if (cp == null)
                    break;
                lastPoints.put(modelId, cp.getPoint().getRound());
                model.addData(cp);
                int size = tab.getModel().getRowCount();
                final JTable table = tab.getTable();
                table.setRowSelectionInterval(size-1, size-1);
                table.scrollRectToVisible(table.getCellRect(size-1, 0, true));
                table.requestFocusInWindow();
                panel.dumpPoints();
                if (!panel.config.next)
                    break;
            }
        }
    }
}
