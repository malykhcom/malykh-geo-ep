package com.malykh.geo.enterpoint.action;

import com.malykh.common.swing.tango.TangoManager;
import com.malykh.geo.enterpoint.MainPanel;
import com.malykh.geo.enterpoint.PointsTab;
import com.malykh.geo.enterpoint.RB;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.*;

/**
 * @author Anton Malykh
 */
public class DeleteTabAction extends AbstractAction
{
    final MainPanel panel;
    public DeleteTabAction(MainPanel panel)
    {
        super(RB.get("action.removetab"));
        putValue(SMALL_ICON, TangoManager.getIcon("places/user-trash.png"));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
            KeyEvent.VK_D, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        this.panel = panel;
    }
    public void actionPerformed(ActionEvent e)
    {
        deleteTab();
    }

    public void deleteTab()
    {
        final PointsTab tab = panel.getPointsTab();
        if (tab != null)
        {
            String msg =
                    String.format(RB.get("removetab.msg"), tab.getTabName(), tab.getModel().getRowCount());
            final int ret = JOptionPane.showConfirmDialog(panel, msg, RB.get("removetab.title"), JOptionPane.YES_NO_OPTION);
            if (ret == JOptionPane.YES_OPTION)
            {
                panel.deleteTab(tab);
                panel.dumpPoints();
            }
        }
    }
}
