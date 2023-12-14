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
public class OkCancelDialog extends JDialog
{
    public static final String OK_ACTION_COMMAND = "OK";
    protected final Frame frame;
    protected final JButton ok;
    protected final JButton cancel;
    protected boolean okPressed;

    public OkCancelDialog(Frame frame, String title, String okTitle)
    {
        super(frame, title, true);
        this.frame = frame;
        ok = new JButton(okTitle);
        ok.setActionCommand(OK_ACTION_COMMAND);
        cancel = new JButton(CommonRB.get("dialog.cancel"));
        ActionListener listener = new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                okPressed = (OK_ACTION_COMMAND.equals(e.getActionCommand()));
                dispose();
            }
        };
        ok.addActionListener(listener);
        getRootPane().setDefaultButton(ok);
        cancel.addActionListener(listener);
        getContentPane().add(new ButtonPanel(ok, cancel),
                             BorderLayout.SOUTH);
        EscapeCanceler.install(this);
    }

    public void setMainPanel(JComponent comp)
    {
        getContentPane().add(comp, BorderLayout.CENTER);
    }

    public void packAndCenter()
    {
        pack();
        setLocationRelativeTo(frame);
    }

    public void pressOK()
    {
        okPressed = true;
        dispose();
    }
    
    public boolean isOkPressed()
    {
        return okPressed;
    }

    public JButton getOkButton()
    {
        return ok;
    }
    
    public static void main(String[] args){
        OkCancelDialog dialog = new OkCancelDialog(null, "test title", "OK");
        JPanel panel= new JPanel();
        JTextField textField = new JTextField(20);
        panel.add(textField);
        dialog.setMainPanel(panel);
        dialog.packAndCenter();
        dialog.setVisible(true);
    }
}
