package com.malykh.common.swing.component;

import javax.swing.*;

/**
 * @author Anton Malykh
 */
public class NFButton extends JButton
{
    public NFButton(String text)
    {
        super(text);
        setFocusable(false);
    }

    public NFButton(Action a)
    {
        super(a);
        setFocusable(false);
    }
}
