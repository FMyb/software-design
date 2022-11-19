package client;

import java.util.List;

public interface PostRateClient {
    List<Integer> rateList(String hashtag, int n);
}
