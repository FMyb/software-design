package client.impl;

import client.PostRateClient;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.newsfeed.responses.SearchResponse;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class VkFeedClient implements PostRateClient {
    private final VkApiClient vkApiClient;
    private final ServiceActor actor;
    private final ZoneId zoneId;

    public VkFeedClient(int appId, String accessToken) {
        TransportClient transportClient = new HttpTransportClient();
        this.vkApiClient = new VkApiClient(transportClient);
        this.actor = new ServiceActor(appId, accessToken);
        this.zoneId = TimeZone.getTimeZone("GMT+3").toZoneId();
    }

    @Override
    public List<Integer> rateList(String hashtag, int n) {
        if (n < 1 || n > 24) {
            throw new IllegalArgumentException("expected n in range [1, 24]");
        }
        List<Integer> result = new ArrayList<>();
        try {
            LocalDateTime finish = LocalDateTime.now().minusHours(n - 1);
            for (LocalDateTime start = LocalDateTime.now(); !start.isBefore(finish); start = start.minusHours(1)) {
                SearchResponse response = vkApiClient
                    .newsfeed()
                    .search(actor)
                    .startTime((int) start.minusHours(1).atZone(zoneId).toEpochSecond())
                    .endTime((int) start.atZone(zoneId).toEpochSecond())
                    .q(hashtag)
                    .execute();
                result.add(response.getTotalCount());
            }
            return result;
        } catch (ApiException e) {
            throw new RuntimeException("api error", e);
        } catch (ClientException e) {
            throw new RuntimeException("client error", e);
        }
    }
}
