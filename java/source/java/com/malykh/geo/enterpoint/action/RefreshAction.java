package com.malykh.geo.enterpoint.action;

import com.malykh.common.swing.tango.TangoManager;
import com.malykh.geo.enterpoint.MainPanel;
import com.malykh.geo.enterpoint.RB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * @author Anton Malykh
 */
public class RefreshAction extends AbstractAction
{
    private final MainPanel panel;
    public RefreshAction(MainPanel panel)
    {
        super(RB.get("action.saveall"));
        putValue(SMALL_ICON, TangoManager.getIcon("actions/view-refresh.png"));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
            KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        this.panel = panel;
    }

    public void actionPerformed(ActionEvent e)
    {
        panel.dumpPoints();
    }
}