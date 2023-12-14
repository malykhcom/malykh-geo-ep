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
public class AddTabAction extends AbstractAction
{
    final MainPanel panel;
    public AddTabAction(MainPanel panel)
    {
        super(RB.get("action.addtab"));
        putValue(SMALL_ICON, TangoManager.getIcon("actions/folder-new.png"));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
            KeyEvent.VK_T, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        this.panel = panel;
    }

    public void actionPerformed(ActionEvent e)
    {
        final String s = JOptionPane.showInputDialog(panel, RB.get("addtab.name"), RB.get("addtab.name.default"));
        if (s != null)
        {
            PointsTab tab = new PointsTab(s, panel);
            panel.addTab(tab);
            panel.selectTab(tab);
            panel.dumpPoints();
        }
    }
}
