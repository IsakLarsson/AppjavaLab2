import Model.ChannelHandler;
import Model.TableauHandler;
import View.GUI;

import javax.swing.*;
import java.util.HashMap;

public class Controller {
    HashMap channelMap;

    public Controller(){
        channelMap = new HashMap();
        ChannelHandler channelHandler = new ChannelHandler(channelMap);
        TableauHandler tableauHandler = new TableauHandler(channelMap);
        tableauHandler.loadEpisodes("164");

        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI("SvEriGeS RadIO");
        /*buttonListener =
                new ButtonListener(gui.getOutputText(),gui.getInputText());
            gui.setupListeners(buttonListener);*/
            gui.show();
        });


    }

}
