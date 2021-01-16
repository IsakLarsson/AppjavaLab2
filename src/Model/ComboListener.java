package Model;

import View.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Listener for handling events in the ComboBox (dropdown menu)
 */
public class ComboListener implements ActionListener {
    TableInterface editor;
    GUI gui;

    /**
     * Constructor for the class
     *
     * @param editor The table editor to edit the table with
     * @param gui    the GUI
     */
    public ComboListener(TableInterface editor, GUI gui) {
        this.editor = editor;
        this.gui = gui;
    }

    /**
     * Event that triggers when the Combobox has a new item selected.
     * Starts a new swing worker to avoid locking up the GUI
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String defaultChoice = "P1";
        try {
            editor.updateTable(gui.getSelectedValue().toString());
        } catch (NullPointerException exception) {
            editor.updateTable(defaultChoice);
        }
    }
}
