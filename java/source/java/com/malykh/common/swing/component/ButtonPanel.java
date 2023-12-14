package com.malykh.common.swing.component;

import javax.swing.*;

/**
 * @author Anton Malykh
 */
public class ButtonPanel extends JPanel
{
    public ButtonPanel(JComponent... comps)
    {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(Box.createHorizontalGlue());
        boolean first = true;
        for (JComponent comp : comps)
        {
            if (first)
                first = false;
            else
                add(Box.createHorizontalStrut(10));
            add(comp);
        }
        add(Box.createHorizontalGlue());
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    }
}
