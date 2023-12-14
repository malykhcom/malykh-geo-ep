package com.malykh.geo.enterpoint.action;

import com.malykh.common.swing.tango.TangoManager;
import com.malykh.geo.enterpoint.MainPanel;
import com.malykh.geo.enterpoint.PointsTab;
import com.malykh.geo.enterpoint.RB;
import com.malykh.geo.enterpoint.model.PointsModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * @author Anton Malykh
 */
public class DeletePointAction extends AbstractAction
{
    final MainPanel panel;
    public DeletePointAction(MainPanel panel)
    {
        super(RB.get("action.removepoint"));
        putValue(SMALL_ICON, TangoManager.getIcon("emblems/emblem-unreadable.png"));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
            KeyEvent.VK_DELETE, 0));

        this.panel = panel;
    }
    public void actionPerformed(ActionEvent e)
    {
        final PointsTab tab = panel.getPointsTab();
        if (tab != null)
        {
            final PointsModel model = tab.getModel();
            final JTable table = tab.getTable();
            final int[] rows = table.getSelectedRows();
            if (rows.length == 0)
            {
                return;
            }
            if (rows.length == 1)
            {
                String name = model.getData(rows[0]).getName();
                String msg = String.format(RB.get("removepoint.single"), name);
                if (JOptionPane.showConfirmDialog(panel, msg, RB.get("removepoint.title"), JOptionPane.YES_NO_OPTION)
                    != JOptionPane.YES_OPTION)
                    return;
            }
            else if (rows.length > 1)
            {
                String msg = String.format(RB.get("removepoint.multi"), rows.length);
                if (JOptionPane.showConfirmDialog(panel, msg, RB.get("removepoint.title"), JOptionPane.YES_NO_OPTION)
                    != JOptionPane.YES_OPTION)
                    return;
            }
            for (int t = rows.length-1; t >= 0; t--)
            {
                model.removeRow(rows[t]);
            }
            int size = model.getRowCount();
            for (int t = 0; t < size; t++)
            {
                model.setData(t, model.getData(t));
            }
            if (size > 0)
            {
                int srw = Math.min(rows[0], size-1);
                tab.getTable().setRowSelectionInterval(srw, srw);
            }
            panel.dumpPoints();
        }
    }
}