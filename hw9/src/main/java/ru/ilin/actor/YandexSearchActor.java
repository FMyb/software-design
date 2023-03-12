package ru.ilin.actor;

import com.google.gson.JsonParser;
import org.jsoup.Jsoup;
import ru.ilin.SearchResponse;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class YandexSearchActor extends AbstractSearchActor {
    private final static String queryRef = "https://yandex.ru/yandsearch?";
    private HttpURLConnection connection;

    public void init(HttpsURLConnection connection) {
        this.connection = connection;
    }

    @Override
    public String getQueryRef() {
        return queryRef;
    }

    @Override
    public String getQueryParameterName() {
        return "text";
    }

    @Override
    public SearchResponse getResponse() throws IOException {
        String response = new String(connection.getInputStream().readAllBytes(), Charset.defaultCharset());
        //var json = JsonParser.parseString(response).getAsJsonObject();
        var html = Jsoup.parse(response);
        var pages = html.select("a.OrganicTitle-Link");
        //var links = new ArrayList<String>();
        //for (var page: pages) {
        //    links.add(page.getAsJsonObject().get("url").getAsString());
        //}
        return new SearchResponse(
            pages.stream().map(element -> element.attr("href")).limit(5).collect(Collectors.toList()),
            "YandexSearchActor"
        );
    }
}
