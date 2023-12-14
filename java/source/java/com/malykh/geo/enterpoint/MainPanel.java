package com.malykh.geo.enterpoint;

import com.malykh.common.swing.component.CompactToolBar;
import com.malykh.common.swing.component.DynamicMenu;
import com.malykh.common.swing.component.IconNFButton;
import com.malykh.common.swing.exception.ErrorMessageDialog;
import com.malykh.common.swing.laf.LAFChanger;
import com.malykh.common.swing.tango.TangoManager;
import com.malykh.geo.kml.Placemarks;
import com.malykh.geo.kml.WPT;
import com.malykh.geo.enterpoint.action.*;
import com.malykh.geo.enterpoint.model.PointsModel;
import com.malykh.geo.kml.Placemark;
import com.malykh.geo.kml.KML;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.util.prefs.Preferences;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

/**
 * @author Anton Malykh
 */
public class MainPanel extends JPanel
{
    private final MainFrame frame;

    public final Config config = new Config();

    private final DeleteTabAction deleteTabAction;
    private final EditPointAction editPointAction;
    private final DeletePointAction deletePointAction;
    private final EditTabAction editTabAction;

    private final List<PointsTab> tabs = new ArrayList<PointsTab>();
    private final JTabbedPane tabbedPane = new JTabbedPane();
    private final PointUpAction pointUpAction;
    private final PointDownAction pointDownAction;
    private final ViewExternalAction viewExternalAction;
    private final ViewGMapsAction viewGMapsAction;

    private static Preferences getPreferences()
    {
        return Preferences.userNodeForPackage(Main.class);
    }
    private static final int ADD_W = 16;
    private static final int ADD_H = 10;
    public JButton expand(JButton button)
    {
        Icon i = button.getIcon();
        if (i instanceof ImageIcon)
        {
            ImageIcon icon = (ImageIcon) i;
            BufferedImage image = new BufferedImage(icon.getIconWidth()+ ADD_W, icon.getIconHeight()+ ADD_H, BufferedImage.TYPE_INT_ARGB);
            image.getGraphics().drawImage(icon.getImage(), ADD_W /2, ADD_H /2, null);
            button.setIcon(new ImageIcon(image));
        }
        return button;
    }
    public MainPanel(final MainFrame frame, LAFChanger laf)
    {
        super(new BorderLayout());
        this.frame = frame;

        AddTabAction addTabAction = new AddTabAction(this);
        editTabAction = new EditTabAction(this);
        deleteTabAction = new DeleteTabAction(this);
        final AddPointAction addPointAction = new AddPointAction(this);
        editPointAction = new EditPointAction(this);
        deletePointAction = new DeletePointAction(this);
        pointUpAction = new PointUpAction(this);
        pointDownAction = new PointDownAction(this);
        viewExternalAction = new ViewExternalAction(this);
        viewGMapsAction = new ViewGMapsAction(this);

        JToolBar tool = new CompactToolBar();
        tool.add(expand(new IconNFButton(addPointAction, RB.get("action.addpoint.hint"))));
        tool.add(expand(new IconNFButton(editPointAction, RB.get("action.editpoint.hint"))));
        tool.add(expand(new IconNFButton(deletePointAction, RB.get("action.removepoint.hint"))));
        tool.addSeparator();
        tool.add(expand(new IconNFButton(pointUpAction, RB.get("action.modeup.hint"))));
        tool.add(expand(new IconNFButton(pointDownAction, RB.get("action.modedown.hint"))));
        tool.addSeparator();
        tool.add(expand(new IconNFButton(new RefreshAction(this), RB.get("action.saveall.hint"))));

        editPointAction.setEnabled(false);
        deletePointAction.setEnabled(false);
        deleteTabAction.setEnabled(false);
        pointUpAction.setEnabled(false);
        pointDownAction.setEnabled(false);

        tabbedPane.addChangeListener(new ChangeListener()
        {
            public void stateChanged(ChangeEvent e)
            {
                final boolean enabled = getPointsTab() != null;
                deleteTabAction.setEnabled(enabled);
                editTabAction.setEnabled(enabled);
                addPointAction.setEnabled(enabled);
                verifyPointsActions();
            }
        });

        add(tool, BorderLayout.PAGE_START);
        add(tabbedPane, BorderLayout.CENTER);

        JMenuBar bar = new JMenuBar();
        JMenu pointsMenu = new JMenu(RB.get("mainmenu.groups"));
        bar.add(pointsMenu);
        pointsMenu.add(addTabAction);
        pointsMenu.add(editTabAction);
        pointsMenu.add(deleteTabAction);
        pointsMenu.addSeparator();
        pointsMenu.add(new RefreshAction(this));
        pointsMenu.addSeparator();
        pointsMenu.add(new DynamicMenu(RB.get("mainmenu.groups.switch"))
        {
            public JMenu generateMenu()
            {
                JMenu m = new JMenu();
                PointsTab selected = getPointsTab();
                for (final PointsTab tab : tabs)
                {
                    JRadioButtonMenuItem item = new JRadioButtonMenuItem(tab.getTabName());
                    item.setSelected(tab == selected);
                    item.addActionListener(new ActionListener()
                    {
                        public void actionPerformed(ActionEvent e)
                        {
                            int index = tabs.indexOf(tab);
                            if (index != -1)
                            {
                                tabbedPane.setSelectedIndex(index);
                            }
                        }
                    });
                    m.add(item);
                }
                return m;
            }
        });
        pointsMenu.addSeparator();
        pointsMenu.add(new QuitAction());
        JMenu pointMenu = new JMenu(RB.get("mainmenu.points"));
        pointMenu.add(addPointAction);
        pointMenu.add(editPointAction);
        pointMenu.add(deletePointAction);
        pointMenu.addSeparator();
        pointMenu.add(pointUpAction);
        pointMenu.add(pointDownAction);
        pointMenu.addSeparator();
        pointMenu.add(viewExternalAction);
        pointMenu.add(viewGMapsAction);
        bar.add(pointMenu);

        JMenu setMenu = new JMenu(RB.get("mainmenu.settings"));
        bar.add(setMenu);
        setMenu.add(createSwitchMenu(RB.get("mainmenu.settings.order"),
                                       Help.LAT+", "+Help.LON,
                                       Help.LON+", "+Help.LAT,
                                       new ActionListener()
                                       {
                                           public void actionPerformed(ActionEvent e)
                                           {
                                               config.changeLatLonMode(true);
                                           }
                                       },
                                       new ActionListener()
                                       {
                                           public void actionPerformed(ActionEvent e)
                                           {
                                               config.changeLatLonMode(false);
                                           }
                                       }, config.latlongmode));
        setMenu.add(createSwitchMenu(RB.get("mainmenu.settings.format"),
                                       RB.get("mainmenu.settings.format.full"),
                                       RB.get("mainmenu.settings.format.degree"),
                                       new ActionListener()
                                       {
                                           public void actionPerformed(ActionEvent e)
                                           {
                                               config.changeDMSMode(true);
                                           }
                                       },
                                       new ActionListener()
                                       {
                                           public void actionPerformed(ActionEvent e)
                                           {
                                               config.changeDMSMode(false);
                                           }
                                       }, config.dmsmode));
        setMenu.addSeparator();
        setMenu.add(createSwitchMenu(RB.get("mainmenu.settings.new"),
                                     RB.get("mainmenu.settings.new.savenext"),
                                     RB.get("mainmenu.settings.new.save"),
                                     new ActionListener()
                                     {
                                         public void actionPerformed(ActionEvent e)
                                         {
                                             config.changeNext(true);
                                         }
                                     },
                                     new ActionListener()
                                     {
                                         public void actionPerformed(ActionEvent e)
                                         {
                                             config.changeNext(false);
                                         }
                                     },
                                     config.next));
        final JMenu lafMenu = laf.generateMenu(frame);
        lafMenu.setIcon(TangoManager.getIcon("devices/video-display.png"));
        setMenu.addSeparator();
        setMenu.add(lafMenu);
        setMenu.addSeparator();
        final InfoAction infoAction = new InfoAction(this);
        setMenu.add(infoAction);

        frame.setJMenuBar(bar);

/*        MacOSXSupport.installMenu(new MenuHandler()
        {
            public boolean handleAbout()
            {
                SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        infoAction.showInfo();
                    }
                });
                return true;
            }
            public boolean handlePreferences()
            {
                return false;
            }
            public boolean handleQuit()
            {
                System.exit(0);
                return true;
            }
        });*/

        getActionMap().put("newPoint", new AddPointAction(this));
        getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0), "newPoint");


        loadPoints();
    }

    private static JMenu createSwitchMenu(String title,
                                          String mode1, String mode2,
                                          ActionListener listener1, ActionListener listener2,
                                          boolean switch1)
    {
        JMenu mode = new JMenu(title);
        JRadioButtonMenuItem item1 = new JRadioButtonMenuItem(mode1);
        JRadioButtonMenuItem item2 = new JRadioButtonMenuItem(mode2);
        ButtonGroup group = new ButtonGroup();
        group.add(item1);
        group.add(item2);
        mode.add(item1);
        mode.add(item2);
        item1.setSelected(switch1);
        item2.setSelected(!switch1);
        item1.addActionListener(listener1);
        item2.addActionListener(listener2);
        return mode;
    }

    public JTabbedPane getTabbedPane()
    {
        return tabbedPane;
    }

    private void updateTabs()
    {
        for (PointsTab tab : tabs)
        {
            tab.rebuildTable();
        }
    }

    private void verifyPointsActions()
    {
        boolean selected = false;
        boolean up = false;
        boolean down = false;
        boolean contains = false;
        final PointsTab tab = getPointsTab();
        if (tab != null)
        {
            final JTable table = tab.getTable();
            int row = table.getSelectedRow();
            int rows = tab.getModel().getRowCount();
            selected = table.getSelectedRowCount() > 0;
            up = row > 0;
            down = row >= 0 && row < (rows -1);
            contains = !tab.getModel().getAllData().isEmpty();
        }
        editPointAction.setEnabled(selected);
        deletePointAction.setEnabled(selected);
        viewExternalAction.setEnabled(contains);
        viewGMapsAction.setEnabled(contains);
        pointDownAction.setEnabled(down);
        pointUpAction.setEnabled(up);
    }
    public void addTab(final PointsTab tab)
    {
        tabs.add(tab);
        tabbedPane.addTab(tab.getTabName(), new JScrollPane(tab));
        addListenerForTab(tab);
    }

    private void addListenerForTab(final PointsTab tab)
    {
        final JTable table = tab.getTable();
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent e)
            {
                verifyPointsActions();
            }
        });
        table.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                if (evt.getClickCount() == 2)
                {
                    int row = table.rowAtPoint(evt.getPoint());
                    if (row != -1 && table.getSelectedRowCount() > 0)
                    {
                        editPointAction.editPoint();
                    }
                }
            }
        });
    }
    public void setTabName(PointsTab tab, String name)
    {
        tab.setTabName(name);
        tabbedPane.setTitleAt(tabs.indexOf(tab), name);
    }
    public void deleteTab(PointsTab tab)
    {
        int index = tabs.indexOf(tab);
        if (index != -1)
        {
            tabbedPane.remove(index);
            tabs.remove(index);
        }
    }
    public void selectTab(PointsTab tab)
    {
        int index = tabs.indexOf(tab);
        if (index != -1)
            tabbedPane.setSelectedIndex(index);
    }
    public PointsTab getPointsTab()
    {
        final int index = tabbedPane.getSelectedIndex();
        if (index == -1)
            return null;
        return tabs.get(index);
    }
    public static File fileKML(int f)
    {
        final String name = String.format("points-%02d.kml", f);
        return new File(getDir(), name);
    }
    public static File fileWPT(int f)
    {
        final String name = String.format("points-%02d.wpt", f);
        return new File(getDir(), name);
    }
    public static File getDir()
    {
//        String userPath = System.getProperty("user.home");
//        File ret = new File(userPath, ".malykh-spm");
//        if (!ret.exists())
//            ret.mkdir();
//        return ret;
        if (Main.PATH != null)
            return Main.PATH;
        else
            return new File(System.getProperty("user.dir"));
    }
    public void loadPoints()
    {
        for (int f = 0; f < 100; f++)
        {
            File file = fileKML(f);
            if (file.exists())
            {
                try
                {
                    Placemarks kml = KML.loadFromFile(file);
                    System.out.println("loaded "+file);
                    final PointsTab tab = new PointsTab(kml.getName(), this);
                    final PointsModel model = tab.getModel();
                    for (Placemark placemark : kml.getPlacemarks())
                    {
                        model.addData(placemark);
                    }
                    addTab(tab);
                }
                catch (Exception e)
                {
                    new ErrorMessageDialog(frame, file.toString(), e).setVisible(true);
                }
            }
        }
        if (tabs.isEmpty())
        {
            PointsTab tab = new PointsTab(RB.get("tab.default"), this);
            addTab(tab);
        }
    }
    public void dumpPoints()
    {
        int f = 0;
        for (PointsTab tab : tabs)
        {
            List<Placemark> points = new ArrayList<Placemark>();
            final PointsModel model = tab.getModel();
            int size = model.getRowCount();
            for (int t = 0; t < size; t++)
            {
                points.add(model.getData(t));
            }
            Placemarks marks = new Placemarks(tab.getTabName(), points);
            final File kml = fileKML(f);
            try
            {
                KML.saveToFile(kml, marks);
            }
            catch (IOException e)
            {
                new ErrorMessageDialog(frame, kml.toString(), e).setVisible(true);
            }
            final File wpt = fileWPT(f);
            try
            {
                WPT.saveToFile(wpt, marks);
            }
            catch (IOException e)
            {
                new ErrorMessageDialog(frame, wpt.toString(), e).setVisible(true);
            }
            f++;
        }
        for (; f < 100; f++)
        {
            final File file = fileKML(f);
            if (file.exists())
                file.delete();
        }
        frame.refreshTitle();
    }

    public Frame getFrame()
    {
        return frame;
    }
    public class Config
    {
        private static final String LAT_LON = "LatLon";
        public boolean latlongmode;

        private static final String DMS = "DMS";
        public boolean dmsmode;

        private static final String NEXT = "Next";
        public boolean next;
        public Config()
        {
            final Preferences pref = getPreferences();
            latlongmode = pref.getBoolean(LAT_LON, true);
            dmsmode = pref.getBoolean(DMS, false);
            next = pref.getBoolean(NEXT, false);
        }
        private void save()
        {
            final Preferences pref = getPreferences();
            pref.putBoolean(LAT_LON, latlongmode);
            pref.putBoolean(DMS, dmsmode);
            pref.putBoolean(NEXT, next);
        }
        public void changeLatLonMode(boolean m)
        {
            latlongmode = m;
            updateTabs();
            save();
        }
        public void changeDMSMode(boolean m)
        {
            dmsmode = m;
            updateTabs();
            save();
        }
        public void changeNext(boolean m)
        {
            next = m;
            save();
        }
    }
}
