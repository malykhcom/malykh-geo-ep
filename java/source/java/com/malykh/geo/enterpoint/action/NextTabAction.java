package com.malykh.geo.enterpoint.action;

import com.malykh.geo.enterpoint.MainPanel;
import com.malykh.geo.enterpoint.RB;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.*;

/**
 * @author Anton Malykh
 */
public class NextTabAction extends AbstractAction
{
    private final MainPanel panel;
    public NextTabAction(MainPanel panel)
    {
        super(RB.get("action.nexttab"));
        this.panel = panel;
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
                KeyEvent.VK_PAGE_DOWN, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    }

    public void actionPerformed(ActionEvent e)
    {
        final JTabbedPane tabbedPane = panel.getTabbedPane();
        final int count = tabbedPane.getTabCount();
        int index = tabbedPane.getSelectedIndex();
        if (index != -1)
        {
            index++;
            if (index == count)
                index = 0;
        }
        else
        {
            index = 0;
        }
        if (count > 0)
            tabbedPane.setSelectedIndex(index);
    }
}