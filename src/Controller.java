import View.GUI;

import javax.swing.*;

public class Controller {

    public Controller(){
        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI("SvEriGeS RadIO");
        /*buttonListener =
                new ButtonListener(gui.getOutputText(),gui.getInputText());
        gui.setupListeners(buttonListener);*/
            gui.show();
        });

        APIHandler apiHandler = new APIHandler();
    }

}
