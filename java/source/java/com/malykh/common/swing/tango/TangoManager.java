package com.malykh.common.swing.tango;

import javax.swing.*;
import java.net.URL;
import java.util.logging.Logger;

/**
 * @author Anton Malykh
 */
public class TangoManager
{
    private static final Logger logger = Logger.getLogger(TangoManager.class.getName());
    public static ImageIcon getIcon(String path)
    {
        final String Path = "16x16/" + path;
        final URL imgURL = TangoManager.class.getResource(Path);
        if (imgURL != null)
        {
            return new ImageIcon(imgURL);
        }
        else
        {
            logger.warning("Couldn\'t find resource: " + path);
            return null;
        }
    }
}
