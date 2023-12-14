package com.malykh.geo.enterpoint.action;

import com.malykh.common.swing.tango.TangoManager;
import com.malykh.geo.enterpoint.Main;
import com.malykh.geo.enterpoint.MainPanel;
import com.malykh.geo.enterpoint.RB;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Anton Malykh
 */
public class InfoAction extends AbstractAction
{
    private final MainPanel panel;
    public InfoAction(MainPanel panel)
    {
        super(RB.get("action.info"));
        putValue(SMALL_ICON, TangoManager.getIcon("apps/help-browser.png"));
        this.panel = panel;
    }

    public void actionPerformed(ActionEvent e)
    {
        showInfo();
    }
    public void showInfo()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(Main.TITLE).append('\n');
        sb.append("(c) Anton Malykh, 2009-2010\n");
        sb.append("anton@malykh.com\n\n");
        sb.append("KML directory: ").append(MainPanel.getDir()).append("\n\n");
        sb.append("PATH: ").append(Main.PATH).append("\n");
        sb.append("OS: ").append(System.getProperty("os.name")).append(' ').append(System.getProperty("os.version")).append(" (").append(System.getProperty("os.arch")).append(", ").append(System.getProperty("sun.arch.data.model")).append(")\n");
        sb.append("Java: ").append(System.getProperty("java.runtime.name")).append(' ').append(System.getProperty("java.runtime.version")).append('\n');
        sb.append("Java home: ").append(System.getProperty("java.home"));
        JOptionPane.showMessageDialog(panel.getFrame(), sb.toString(), RB.get("action.info"), JOptionPane.INFORMATION_MESSAGE);
    }
}
