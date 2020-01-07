import java.io.IOException;
import java.net.URLConnection;

public class APIHandler {

    public APIHandler(){
        URLConnection connection =
                new URLConnection("http://api.sr.se/api/v2/scheduledepisodes"){
            @Override
            public void connect() throws IOException {

            }
        }
    }
}
