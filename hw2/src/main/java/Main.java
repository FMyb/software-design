import client.PostRateClient;
import client.impl.VkFeedClient;
import manager.PostRateManager;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        int appId = 51469317;
        String accessToken = System.getenv("VK_ACCESS_TOKEN");
        if (accessToken == null) {
            throw new IllegalArgumentException("don't find vk access token");
        }
        PostRateClient postRateClient = new VkFeedClient(appId, accessToken);
        PostRateManager postRateManager = new PostRateManager(postRateClient);
        List<Integer> result = postRateManager.rateList("", 2);
        for (int x : result) {
            System.out.print(x + " ");
        }
    }
}
