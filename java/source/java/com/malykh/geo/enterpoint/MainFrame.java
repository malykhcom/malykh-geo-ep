package com.malykh.geo.enterpoint;

import com.malykh.common.swing.laf.LAFChanger;
import com.malykh.common.swing.tool.PositionSizeSaver;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.util.Date;

/**
 * @author Anton Malykh
 */
public class MainFrame extends JFrame
{
    public MainFrame(LAFChanger laf)
        throws HeadlessException
    {
        super(Main.TITLE);
        try
        {
            setIconImage(ImageIO.read(getClass().getResource("ep.png")));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        MainPanel panel = new MainPanel(this, laf);
        getContentPane().add(panel, BorderLayout.CENTER);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //pack();
        new PositionSizeSaver(this, MainFrame.class).install();
    }
    public void refreshTitle()
    {
        //DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);
        setTitle(Main.TITLE +". "+RB.get("mainframe.saved")+": "+df.format(new Date()));
    }
}
