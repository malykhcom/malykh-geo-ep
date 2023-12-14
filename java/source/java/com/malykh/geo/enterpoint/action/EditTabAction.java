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
public class EditTabAction extends AbstractAction
{
    final MainPanel panel;
    public EditTabAction(MainPanel panel)
    {
        super(RB.get("action.edittab"));
        putValue(SMALL_ICON, TangoManager.getIcon("actions/document-properties.png"));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
            KeyEvent.VK_R, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        this.panel = panel;
    }
    public void actionPerformed(ActionEvent e)
    {
        final PointsTab tab = panel.getPointsTab();
        if (tab != null)
        {
            String name = JOptionPane.showInputDialog(panel, RB.get("edittab.name"), tab.getTabName());
            if (name != null)
            {
                panel.setTabName(tab, name);
                panel.dumpPoints();
            }
        }
    }
}