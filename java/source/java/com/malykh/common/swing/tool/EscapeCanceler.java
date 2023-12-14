package com.malykh.common.swing.tool;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

/**
 * @author Anton Malykh
 */
public class EscapeCanceler
{
    /**
     * Добавляет обработку кнопку Escape. При ее нажатии просто делается dispose для диалога.
     *
     * @param dialog диалог
     */
    public static void install(final JDialog dialog)
    {
        AbstractAction cancelAction =
                new AbstractAction()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        dialog.dispose();
                    }
                };
        install(dialog.getRootPane(), cancelAction);
    }
    public static void install(final JFrame frame)
    {
        AbstractAction cancelAction =
                new AbstractAction()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        frame.dispose();
                    }
                };
        install(frame.getRootPane(), cancelAction);
    }
    public static void install(final JRootPane rootPane, AbstractAction action)
    {
        String CANCEL_ACTION_KEY = "CANCEL_ACTION_KEY";
        int noModifiers = 0;
        KeyStroke escapeKey = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, noModifiers);
        InputMap inputMap = rootPane.
                getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(escapeKey, CANCEL_ACTION_KEY);
        rootPane.getActionMap().put(CANCEL_ACTION_KEY, action);
    }
}
