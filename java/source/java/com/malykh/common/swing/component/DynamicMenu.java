package com.malykh.common.swing.component;

import javax.swing.*;
import javax.swing.event.MenuListener;
import javax.swing.event.MenuEvent;
import java.awt.*;

/**
 * @author Anton Malykh
 */
public abstract class DynamicMenu extends JMenu implements MenuListener
{
    public DynamicMenu(String s)
    {
        super(s);
        addMenuListener(this);
    }

    public DynamicMenu(Action a)
    {
        super(a);
        addMenuListener(this);
    }

    public DynamicMenu(String s, boolean b)
    {
        super(s, b);
        addMenuListener(this);
    }

    public DynamicMenu()
    {
        super();
        addMenuListener(this);
    }


    public void menuSelected(MenuEvent e)
    {
        this.removeAll();
        for (Component cmp : generateMenu().getMenuComponents())
        {
            add(cmp);
        }
    }

    public void menuDeselected(MenuEvent e)
    {
    }

    public void menuCanceled(MenuEvent e)
    {
    }
    public abstract JMenu generateMenu();
}
