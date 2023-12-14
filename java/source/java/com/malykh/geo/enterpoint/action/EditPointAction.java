package com.malykh.geo.enterpoint.action;

import com.malykh.common.swing.tango.TangoManager;
import com.malykh.geo.enterpoint.*;
import com.malykh.geo.enterpoint.dialog.EditPointDialog;
import com.malykh.geo.kml.Placemark;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.*;

/**
 * @author Anton Malykh
 */
public class EditPointAction extends AbstractAction
{
    final MainPanel panel;
    public EditPointAction(MainPanel panel)
    {
        super(RB.get("action.editpoint"));
        putValue(SMALL_ICON, TangoManager.getIcon("apps/accessories-text-editor.png"));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
            KeyEvent.VK_E, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
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
            final int row = tab.getTable().getSelectedRow();
            if (row != -1)
            {
                Placemark cp = EditPointDialog.editPoint(panel, tab.getModel().getData(row));
                if (cp != null)
                {
                    tab.getModel().setData(row, cp);
                    panel.dumpPoints();
                }
            }
        }
    }
}