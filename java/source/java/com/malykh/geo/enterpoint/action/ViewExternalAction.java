package com.malykh.geo.enterpoint.action;

import com.malykh.common.swing.tango.TangoManager;
import com.malykh.geo.enterpoint.PointsTab;
import com.malykh.geo.enterpoint.MainPanel;
import com.malykh.geo.enterpoint.RB;
import com.malykh.geo.kml.Placemark;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author Anton Malykh
 */
public class ViewExternalAction extends AbstractAction
{
    private final MainPanel panel;
    public ViewExternalAction(MainPanel panel)
    {
        super(RB.get("action.showexternal"));
        putValue(SMALL_ICON, TangoManager.getIcon("actions/system-search.png"));
        this.panel = panel;
    }

    public void actionPerformed(ActionEvent e)
    {
        final int index = panel.getTabbedPane().getSelectedIndex();
        if (index != -1)
        {
            try
            {
                PointsTab tab = panel.getPointsTab();
                panel.dumpPoints();
                final java.util.List<Placemark> data = tab.getModel().getAllData();
                if (data.isEmpty())
                {
                    JOptionPane.showMessageDialog(panel.getFrame(), RB.get("view.nopoints"));
                    return;
                }
                final File file = MainPanel.fileKML(index);
                final String name = tab.getTabName();
                final int ret =
                    JOptionPane.showConfirmDialog(panel.getFrame(), "Показать точки группы \""+ name +"\" (" + file +
                        ")\nво внешней программе, которая назначена в системе для показа KML-файлов?\n" +
                        "Если такой программы нет, то будет выведено сообщение об ошибке.", name, JOptionPane.YES_NO_OPTION);
                if (ret == JOptionPane.YES_OPTION)
                    Desktop.getDesktop().open(file);
            }
            catch (IOException ex)
            {
                throw new RuntimeException(ex);
            }
        }
    }
}
