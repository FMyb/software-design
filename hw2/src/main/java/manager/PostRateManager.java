package manager;

import client.PostRateClient;

import java.util.List;

public class PostRateManager {
    private final PostRateClient postRateClient;


    public PostRateManager(PostRateClient postRateClient) {this.postRateClient = postRateClient;}

    public List<Integer> rateList(String hashtag, int n) {
        return postRateClient.rateList(hashtag, n);
    }
}
