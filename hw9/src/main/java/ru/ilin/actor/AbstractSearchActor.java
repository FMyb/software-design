package ru.ilin.actor;

import akka.actor.UntypedAbstractActor;
import ru.ilin.SearchResponse;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public abstract class AbstractSearchActor extends UntypedAbstractActor {
    public abstract SearchResponse getResponse() throws IOException;

    public abstract void init(HttpsURLConnection connection);
    public abstract String getQueryRef();
    public abstract String getQueryParameterName();

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof String) {
            var url = new URL(String.format("%s&%s=%s", getQueryRef(), getQueryParameterName(), URLEncoder.encode(
                (String) message,
                StandardCharsets.UTF_8
            )));
            init((HttpsURLConnection) url.openConnection());
            SearchResponse response = getResponse();
            context().parent().tell(response, self());
        }
    }
}
