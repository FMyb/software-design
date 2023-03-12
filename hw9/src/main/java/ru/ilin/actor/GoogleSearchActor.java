package ru.ilin.actor;

import com.google.gson.JsonParser;
import ru.ilin.SearchResponse;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class GoogleSearchActor extends AbstractSearchActor {
    private final static String queryRef = "https://www.googleapis.com/customsearch/v1?key=AIzaSyCZLHX3QMzpOWjY_rlj02n-kFXaaD_KJa8&num=5&cx=eca2037276f9732da";
    private HttpURLConnection connection;

    @Override
    public void init(HttpsURLConnection connection) {
        this.connection = connection;
    }

    @Override
    public String getQueryRef() {
        return queryRef;
    }

    @Override
    public String getQueryParameterName() {
        return "q";
    }

    @Override
    public SearchResponse getResponse() throws IOException {
        var response = new String(connection.getInputStream().readAllBytes(), Charset.defaultCharset());
        var json = JsonParser.parseString(response).getAsJsonObject();
        var pages = json.get("items").getAsJsonArray();
        var links = new ArrayList<String>();
        for (var page: pages) {
            links.add(page.getAsJsonObject().get("link").getAsString());
        }
        return new SearchResponse(links, "GoogleSearchActor");
    }
}
