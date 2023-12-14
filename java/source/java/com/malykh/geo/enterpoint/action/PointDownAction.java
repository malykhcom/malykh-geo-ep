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
public class PointDownAction extends AbstractAction
{
    final MainPanel panel;
    public PointDownAction(MainPanel panel)
    {
        super(RB.get("action.movedown"));
        putValue(SMALL_ICON, TangoManager.getIcon("actions/go-down.png"));
        putValue(ACCELERATOR_KEY,
                KeyStroke.getKeyStroke(
                        KeyEvent.VK_DOWN,
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
            final PointsModel model = tab.getModel();
            if (row >= 0 && row < (model.getRowCount()-1))
            {
                final Placemark mark1 = model.getData(row);
                final int nextRow = row + 1;
                model.setData(row, model.getData(nextRow));
                model.setData(nextRow, mark1);
                table.setRowSelectionInterval(nextRow, nextRow);
                panel.dumpPoints();
            }
        }
    }
}