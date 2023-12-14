package com.malykh.geo.enterpoint;

import com.malykh.common.swing.exception.ErrorHandler;
import com.malykh.common.swing.laf.LAFChanger;
import com.malykh.common.util.TimeZoneCorrector;

import java.io.File;
import java.util.logging.Logger;

/**
 * @author Anton Malykh
 */
public class Main
{
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    public static final String TITLE = RB.get("main.title");
    public static File PATH = null;
    public static void main(String[] args)
    {
        if (args.length > 0)
        {
            File f = new File(args[0]);
            if (f.exists())
            {
                if (f.isFile())
                {
                    PATH = f.getParentFile();
                }
                else if (f.isDirectory())
                {
                    PATH = f;
                }
            }
        }
        TimeZoneCorrector.correct(); // workaround for old JRE
        final LAFChanger laf = new LAFChanger(Main.class);
        ErrorHandler.installYourself();
        new MainFrame(laf).setVisible(true);
    }
}
