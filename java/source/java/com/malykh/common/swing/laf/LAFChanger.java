package com.malykh.common.swing.laf;


import com.malykh.common.swing.CommonRB;
import com.malykh.common.swing.dialog.DialogSize;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.OceanTheme;
import javax.swing.plaf.metal.MetalTheme;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.prefs.Preferences;

/**
 * @author Anton Malykh
 */
public class LAFChanger
{
    private static final Logger logger = Logger.getLogger(LAFChanger.class.getName());
    protected static final String KEY = "laf";
    protected Class prefClass;
    protected final String[] defaultsMAC = { "SYSTEM", "JGPLASTIC", "NIMBUS", "METAL_DEF" };
    //protected String[] defaults = { "JGPLASTIC", "NIMBUS", "METAL_DEF" };
    protected final String[] defaults = defaultsMAC;
    protected String currentLAF = null;
    protected final List<LAFInfo> lafInfos =
            new ArrayList<LAFInfo>();
    protected JFrame frame;

    private boolean addLAF(String name, String title, String laf, String metalTheme)
    {
        try
        {
            Class.forName(laf);
            lafInfos.add(new LAFInfo(name, title,
                                     laf, metalTheme));
            logger.info(name+" ("+title+"): "+laf+" added");
            return true;
        }
        catch (ClassNotFoundException e)
        {
            logger.warning(name+" ("+title+"): "+laf+" not found");
            return false;
        }
    }
    /**
     * @param cl класс для preferences, если null, то стандартный
     */
    public LAFChanger(Class cl)
    {
        prefClass = cl;
        if (prefClass == null)
            prefClass = getClass();
        addLAF("SYSTEM", CommonRB.get("laf.system"),
               UIManager.getSystemLookAndFeelClassName(), null);
        addLAF("NIMBUS", CommonRB.get("laf.nimbus"),
               "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel", null);
        addLAF("METAL_DEF", CommonRB.get("laf.metal"),
               MetalLookAndFeel.class.getName(), DefaultMetalTheme.class.getName());
        addLAF("METAL_OCEAN", CommonRB.get("laf.metal.ocean"),
               MetalLookAndFeel.class.getName(), OceanTheme.class.getName());
        addLAF("METAL_GREEN", CommonRB.get("laf.metal.green"),
               MetalLookAndFeel.class.getName(), GreenTheme.class.getName());
        addLAF("METAL_CONTRAST", CommonRB.get("laf.metal.contrast"),
               MetalLookAndFeel.class.getName(), ContrastTheme.class.getName());
        addLAF("MOTIF", CommonRB.get("laf.motif"),
               "com.sun.java.swing.plaf.motif.MotifLookAndFeel", null);
        addLAF("JGWIN", "Windows (JG)",
               "com.jgoodies.looks.windows.WindowsLookAndFeel", null);
        addLAF("JGPLASTIC", CommonRB.get("laf.plastic")+" (JG)",
               "com.jgoodies.looks.plastic.PlasticLookAndFeel", null);
        addLAF("JG3D", CommonRB.get("laf.plastic")+"-3D (JG)",
               "com.jgoodies.looks.plastic.Plastic3DLookAndFeel", null);
        addLAF("JGWIN", CommonRB.get("laf.plastic")+"-XP (JG)",
               "com.jgoodies.looks.plastic.PlasticXPLookAndFeel", null);
        addLAF("LIQUID", CommonRB.get("laf.liquid"),
               "com.birosoft.liquid.LiquidLookAndFeel", null);
        String name = getPreferences().get(KEY, null);
        if (name == null || findLafInfo(name) == null)
        {
            String osName = System.getProperty("os.name");
            String[] defs;
            if (osName.startsWith("Mac OS"))
            {
                logger.info("MacOS found");
                defs = defaultsMAC;
            }
            else
            {
                defs = defaults;
            }
            name = getDefault(defs);
            if (name != null)
                logger.info("Default LAF: "+name);
        }
        setLAF(null, name, true);
    }

    protected Preferences getPreferences()
    {
        return Preferences.userNodeForPackage(prefClass);
    }

    private String getDefault(String[] defs)
    {
        for (String def : defs)
        {
            if (findLafInfo(def) != null)
                return def;
        }
        return null;
    }

    protected LAFInfo findLafInfo(String name)
    {
        for (LAFInfo lafInfo : lafInfos)
        {
            if (lafInfo.getName().equals(name))
                return lafInfo;
        }
        logger.warning("LAF for "+name+" is not found");
        return null;
    }

    public String getLAF()
    {
        return currentLAF;
    }
    protected boolean setLAF(JFrame frame, String name, boolean asDefault)
    {
        if (name == null)
            return false;
        LAFInfo info = findLafInfo(name);
        if (info == null)
            return false;
        try
        {
            if (info.getMetalTheme() != null)
                MetalLookAndFeel.setCurrentTheme((MetalTheme) Class.forName(info.getMetalTheme()).newInstance());
            UIManager.setLookAndFeel(info.getLaf());
            if (frame != null)
                SwingUtilities.updateComponentTreeUI(frame);
            if (!asDefault)
            {
                getPreferences().put(KEY, info.getName());
            }
            currentLAF = info.getName();
            return true;
        }
        catch (Exception e)
        {
            logger.log(Level.SEVERE, name, e);
            return false;
        }
    }

    //
    public JMenu generateMenu(JFrame frame)
    {
        JMenu laf = new JMenu(CommonRB.get("laf.menu"));
        ButtonGroup group = new ButtonGroup();
        for (LAFInfo info : lafInfos)
        {
            JRadioButtonMenuItem i = new JRadioButtonMenuItem(info.getTitle());
            i.addActionListener(new LAFActionListener(frame, info.getName(), this));
            laf.add(i);
            group.add(i);
            if (info.getName().equals(currentLAF))
                i.setSelected(true);
        }
        return laf;
    }

    protected static class LAFActionListener implements ActionListener
    {
        protected final JFrame frame;
        protected final String lafName;
        protected final LAFChanger changer;

        public LAFActionListener(JFrame frame, String lafName, LAFChanger changer)
        {
            this.frame = frame;
            this.lafName = lafName;
            this.changer = changer;
        }

        public void actionPerformed(ActionEvent e)
        {
            if (changer.setLAF(frame, lafName, false))
            {
                JOptionPane.showMessageDialog(frame,
                                              CommonRB.get("laf.ok"),
                                              CommonRB.get("laf.title"), JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(frame,
                                              CommonRB.get("laf.error"),
                                              CommonRB.get("laf.title"), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    protected static class LAFInfo
    {
        protected final String name;
        protected final String title;
        protected final String laf;
        protected final String metalTheme;

        public LAFInfo(String name, String title, String laf, String metalTheme)
        {
            this.name = name;
            this.title = title;
            this.laf = laf;
            this.metalTheme = metalTheme;
        }

        public String getName()
        {
            return name;
        }

        public String getTitle()
        {
            return title;
        }

        public String getLaf()
        {
            return laf;
        }

        public String getMetalTheme()
        {
            return metalTheme;
        }
    }

    public static void main(String[] args)
    {
        new LAFChanger(null);
        JFrame frame = new JFrame("test");
        JMenuBar bar = new JMenuBar();
        JMenuItem item = new JMenuItem("Test");
        JMenu menu = new JMenu("Menu");
        menu.add(item);
        bar.add(menu);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(bar);
        frame.setSize(DialogSize.calcViewSize());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
