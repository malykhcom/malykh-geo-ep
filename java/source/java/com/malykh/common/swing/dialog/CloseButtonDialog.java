package com.malykh.common.swing.dialog;

import com.malykh.common.swing.CommonRB;
import com.malykh.common.swing.component.ButtonPanel;
import com.malykh.common.swing.tool.EscapeCanceler;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;

/**
 * @author Anton Malykh
 */
public class CloseButtonDialog extends JDialog implements ActionListener
{
    public CloseButtonDialog(Frame owner, String title, JComponent comp)
    {
        this(owner, title, CommonRB.get("dialog.close"), comp);
    }

    public CloseButtonDialog(Frame owner, String title, String close, JComponent comp)
    {
        super(owner, title, true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        JButton closeButton = new JButton(close);
        closeButton.setActionCommand("close");
        closeButton.addActionListener(this);
        getContentPane().add(comp, BorderLayout.CENTER);
        getContentPane().add(new ButtonPanel(closeButton),
                             BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(owner);
        EscapeCanceler.install(this);
    }

    public void actionPerformed(ActionEvent e)
    {
        if ("close".equals(e.getActionCommand()))
            dispose();
    }
}
