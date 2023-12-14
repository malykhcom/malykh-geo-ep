package com.malykh.common.swing.tool;

import com.malykh.common.swing.dialog.DialogSize;

import java.awt.*;
import java.awt.event.*;
import java.util.prefs.Preferences;

/**
 * @author Anton Malykh
 */
public class PositionSizeSaver
{
    protected static final String X = ".x";
    protected static final String Y = ".y";
    protected static final String WIDTH = ".width";
    protected static final String HEIGHT = ".height";
    protected static final String EXT_STATE = ".extstate";
    protected final Frame frame;
    protected final Preferences pref;
    protected final String prefix;

    /**
     * @param frame     фрейм
     * @param prefClass класс, для загрузки/сохранения Preferences (используется только пакет)
     * @param prefix префикс для ключей
     */
    public PositionSizeSaver(Frame frame, Class prefClass, String prefix)
    {
        this.frame = frame;
        pref = Preferences.userNodeForPackage(prefClass);
        this.prefix = prefix; 
    }
    protected static String getPrefix(Class cl)
    {
        String prefix = cl.getName();
        prefix = prefix.substring(prefix.lastIndexOf('.')+1);
        //System.out.println(prefix);
        return prefix;
    }
    public PositionSizeSaver(Frame frame, Class prefClass)
    {
        this(frame, prefClass, getPrefix(prefClass));
    }
    public PositionSizeSaver(Frame frame)
    {
        this(frame, frame.getClass());
    }
    protected void load()
    {
        Dimension dimension = DialogSize.calcViewSize();
        int defaultH = dimension.height;
        int defaultW = dimension.width;

        int x = pref.getInt(prefix+X, -1);
        int y = pref.getInt(prefix+Y, -1);
        int width = pref.getInt(prefix+WIDTH, defaultW);
        int height = pref.getInt(prefix+HEIGHT, defaultH);
        if (x != -1 &&
            y != -1)
        {
            Rectangle rect = new Rectangle(x, y, width, height);
            Rectangle screen = new Rectangle();
            GraphicsEnvironment ge =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
            for (GraphicsDevice gd : ge.getScreenDevices())
            {
                GraphicsConfiguration gc = gd.getDefaultConfiguration();
                screen = screen.union(gc.getBounds());
            }
            if (screen.contains(rect))
            {
                frame.setSize(width, height);
                frame.setLocation(x, y);
                int state = pref.getInt(prefix+EXT_STATE, 0);
                frame.setExtendedState(state);
                return;
            }
        }
        frame.setSize(defaultW, defaultH);
        frame.setLocationRelativeTo(null);
        int state = pref.getInt(prefix+EXT_STATE, 0);
        frame.setExtendedState(state);
    }

    public void install()
    {
        load();
        //frame.add
        frame.addComponentListener(new ComponentAdapter()
        {
            public void componentMoved(ComponentEvent e)
            {
                Point p = frame.getLocation();
                pref.putInt(prefix+X, p.x);
                pref.putInt(prefix+Y, p.y);
            }
            public void componentResized(ComponentEvent e)
            {
                Dimension d = frame.getSize();
                pref.putInt(prefix+WIDTH, d.width);
                pref.putInt(prefix+HEIGHT, d.height);
            }
        });
        frame.addWindowStateListener(new WindowStateListener()
        {
            public void windowStateChanged(WindowEvent e)
            {
                int status = frame.getExtendedState();
                status &= Frame.MAXIMIZED_BOTH;
                pref.putInt(prefix+EXT_STATE, status);
            }
        });
    }
}
