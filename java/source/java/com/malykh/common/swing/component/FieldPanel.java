package com.malykh.common.swing.component;


import com.malykh.common.swing.tool.FieldPanelLayout;

import javax.swing.*;
import java.awt.*;

/**
 * @author Anton Malykh
 */
public class FieldPanel extends JPanel
{
    protected final FieldPanelLayout layout;

    public FieldPanel()
    {
        layout = FieldPanelLayout.install(this);
    }

    /**
     * Needed if Panel used inside JScrollPane
     *
     * @see JScrollPane
     */
    public void addGlue()
    {
        layout.addGlue();
    }

    /**
     * Adds label and control to the panel. You can use tool tip for control
     *
     * @param label     Label for control comp
     * @param help      Tool tip
     * @param comp      Component
     * @param fullWidth option, if true, Control is on the new line. Else it is on the label line.
     */
    public void addField(String label, String help,
                         Component comp,
                         boolean fullWidth)
    {
        layout.addField(label, help, comp, fullWidth);
    }

}
