package com.malykh.common.swing.tool;

import javax.swing.*;
import java.awt.*;

/**
 * @author Anton Malykh
 */
public class FieldPanelLayout extends GridBagLayout
{
    protected final JComponent panel;

    protected FieldPanelLayout(JComponent panel)
    {
        this.panel = panel;
    }

    public void addGlue()
    {
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 1.0;
        Component comp = Box.createVerticalGlue();
        setConstraints(comp, c);
        panel.add(comp);
    }

    protected JComponent createLabelForComponent(String label, String help, Component comp, boolean fullWidth)
    {
        JLabel jl = new JLabel(label);
        if (help == null)
            help = label;
        jl.setToolTipText(help);
        if (comp instanceof JComponent)
        {
            JComponent jcomp = (JComponent) comp;
            jcomp.setToolTipText(help);
        }
        jl.setLabelFor(comp);
        return jl;
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
        JComponent jl = createLabelForComponent(label, help, comp, fullWidth);
        if (!fullWidth)
        {
            GridBagConstraints c = new GridBagConstraints();
            c.gridwidth = GridBagConstraints.RELATIVE;
            c.fill = GridBagConstraints.NONE;
            c.anchor = GridBagConstraints.WEST;
            c.insets = new Insets(4, 8, 0, 8);
            c.weightx = 0.0;
            setConstraints(jl, c);
            panel.add(jl);
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(4, 8, 0, 8);
            c.weightx = 1.0;
            setConstraints(comp, c);
            panel.add(comp);
        }
        else
        {
            GridBagConstraints c = new GridBagConstraints();
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.fill = GridBagConstraints.HORIZONTAL;
            //c.anchor = GridBagConstraints.WEST;
            c.insets = new Insets(4, 8, 0, 8);
            c.weightx = 0.0;
            setConstraints(jl, c);
            panel.add(jl);
            c.insets = new Insets(4, 8, 0, 8);
            c.weightx = 1.0;
            setConstraints(comp, c);
            panel.add(comp);
        }
    }

    public static FieldPanelLayout install(JComponent panel)
    {
        FieldPanelLayout l = new FieldPanelLayout(panel);
        panel.setLayout(l);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        return l;
    }
}
