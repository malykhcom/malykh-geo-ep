package com.malykh.geo.enterpoint.action;

import com.malykh.common.swing.tango.TangoManager;
import com.malykh.geo.enterpoint.*;
import com.malykh.geo.enterpoint.model.PointsModel;
import com.malykh.geo.kml.Placemark;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.*;

/**
 * @author Anton Malykh
 */
public class PointUpAction extends AbstractAction
{
    final MainPanel panel;
    public PointUpAction(MainPanel panel)
    {
        super(RB.get("action.modeup"));
        putValue(SMALL_ICON, TangoManager.getIcon("actions/go-up.png"));
        putValue(ACCELERATOR_KEY,
                KeyStroke.getKeyStroke(
                        KeyEvent.VK_UP,
                        Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | InputEvent.ALT_DOWN_MASK));
        this.panel = panel;
    }

    public void actionPerformed(ActionEvent e)
    {
        editPoint();
    }

    public void editPoint()
    {
        final PointsTab tab = panel.getPointsTab();
        if (tab != null)
        {
            final JTable table = tab.getTable();
            final int row = table.getSelectedRow();
            if (row > 0)
            {
                final PointsModel model = tab.getModel();
                final Placemark mark1 = model.getData(row);
                final int prevRow = row - 1;
                model.setData(row, model.getData(prevRow));
                model.setData(prevRow, mark1);
                table.setRowSelectionInterval(prevRow, prevRow);
                panel.dumpPoints();
            }
        }
    }
}