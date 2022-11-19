import client.PostRateClient;
import manager.PostRateManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

public class PostRateManagerTest {
    private PostRateManager postRateManager;
    private PostRateClient postRateClient;

    @Before
    public void setUp() {
        postRateClient = Mockito.mock(PostRateClient.class);
        postRateManager = new PostRateManager(postRateClient);
    }

    @Test
    public void listRatesTest() {
        Mockito.when(postRateClient.rateList("#test", 10)).thenReturn(createAnswer());

        List<Integer> result = postRateManager.rateList("#test", 10);
        Assert.assertEquals(List.of(5, 4, 3, 2, 1), result);
    }

    private List<Integer> createAnswer() {
        return List.of(5, 4, 3, 2, 1);
    }
}
