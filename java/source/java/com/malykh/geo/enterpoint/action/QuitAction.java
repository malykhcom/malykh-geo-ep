package com.malykh.geo.enterpoint.action;


import com.malykh.common.swing.tango.TangoManager;
import com.malykh.geo.enterpoint.RB;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.*;

/**
 * @author Anton Malykh
 */
public class QuitAction extends AbstractAction
{
    public QuitAction()
    {
        super(RB.get("action.quit"));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
                KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        putValue(SMALL_ICON, TangoManager.getIcon("actions/system-log-out.png"));
    }

    public void actionPerformed(ActionEvent e)
    {
        System.exit(0);
    }
}
