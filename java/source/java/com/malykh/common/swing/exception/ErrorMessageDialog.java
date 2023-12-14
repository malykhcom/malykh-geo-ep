package com.malykh.common.swing.exception;

import com.malykh.common.swing.CommonRB;
import com.malykh.common.swing.component.ButtonPanel;
import com.malykh.common.swing.tool.EscapeCanceler;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;


/**
 * Диалог сообщения об ошибке (исключении).
 * Показывает две закладки: с сообщением об ошибке и стек-трейсом (с возможность копирования в буфер обмена).
 * После создания нужно сделать setVisible(true).
 *
 * @author Anton Malykh
 */
public class ErrorMessageDialog extends JDialog implements ActionListener
{
    public ErrorMessageDialog(Frame frame, String title, Throwable tr)
            throws HeadlessException
    {
        super(frame, title, true);
        JButton close = new JButton(CommonRB.get("exception.close"));
        close.addActionListener(this);
        add(new ErrorMessagePanel(tr), BorderLayout.CENTER);
        add(new ButtonPanel(close), BorderLayout.SOUTH);
        setSize(640, 480);
        setLocationRelativeTo(frame);
        EscapeCanceler.install(this);
    }

    public void actionPerformed(ActionEvent e)
    {
        dispose();
    }
}
