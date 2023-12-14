package com.malykh.common.swing.component;

import javax.swing.*;

/**
 * @author Anton Malykh
 */
public class IconNFButton extends NFButton
{
    public IconNFButton(Action action, String tooltip)
    {
        super(action);
        setText("");
        setToolTipText(tooltip);
    }
}
