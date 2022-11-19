import client.impl.VkFeedClient;
import com.vk.api.sdk.client.VkApiClient;
import com.xebialabs.restito.server.StubServer;
import org.glassfish.grizzly.http.Method;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.header;
import static com.xebialabs.restito.semantics.Action.stringContent;
import static com.xebialabs.restito.semantics.Condition.method;
import static com.xebialabs.restito.semantics.Condition.startsWithUri;

public class VkFeedClientTest {
    private static final int STUB_PORT = 32451;
    private VkFeedClient vkFeedClient;
    private static String resp;

    @BeforeClass
    public static void setResp() throws IOException {
        resp = Files.readString(Path.of("src/test/resources/resp.json"));
    }

    @Before
    public void setUp() {
        System.setProperty("api.host", "localhost:" + STUB_PORT);
        vkFeedClient = new VkFeedClient(1, "");
        disableSsl();
    }

    @Test
    public void simpleAnswerTest() {
        withStubServer(STUB_PORT, s -> {
            whenHttp(s)
                .match(method(Method.POST), startsWithUri("/method"))
                .then(header("Content-Type", "application/json"), stringContent(
                    "{\"response\":{\"count\":1,\"items\":[" + resp + "],\"total_count\":1}}"
                ));
            List<Integer> result = vkFeedClient.rateList("#dota", 1);
            Assert.assertEquals(1, result.size());
            Assert.assertEquals(1, result.get(0).intValue());
        });
    }

    @Test
    public void badAccessTokenTest() {
        try {
            withStubServer(STUB_PORT, s -> {
                whenHttp(s)
                    .match(method(Method.POST), startsWithUri("/method"))
                    .then(header("Content-Type", "application/json"), stringContent(
                        "{\"error\":{\"error_code\":5,\"error_msg\":\"User authorization failed: invalid access_token (4).\",\"request_params\":[{\"key\":\"method\",\"value\":\"newsfeed.search\"},{\"key\":\"oauth\",\"value\":\"1\"},{\"key\":\"start_time\",\"value\":\"1667767523\"},{\"key\":\"q\",\"value\":\"vk\"},{\"key\":\"v\",\"value\":\"5.131\"},{\"key\":\"end_time\",\"value\":\"1667771123\"},{\"key\":\"client_secret\",\"value\":\"*******\"}]}}"
                    ));
                vkFeedClient.rateList("#dota", 1);
            });
            Assert.fail("expected error but not found");
        } catch (RuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("api error"));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void badNTest() {
        vkFeedClient.rateList("#dota", 0);
    }

    private void withStubServer(int port, Consumer<StubServer> callback) {
        StubServer stubServer = null;
        try {
            stubServer = new StubServer(port).run();
            callback.accept(stubServer);
        } finally {
            if (stubServer != null) {
                stubServer.stop();
            }
        }
    }

    private void disableSsl() {
        try {
            Field vkApiClientField = vkFeedClient.getClass().getDeclaredField("vkApiClient");
            vkApiClientField.setAccessible(true);
            VkApiClient vkApiClient = (VkApiClient) vkApiClientField.get(vkFeedClient);
            Field endpoint = vkApiClient.getClass().getDeclaredField("apiEndpoint");
            endpoint.setAccessible(true);
            String s = (String) endpoint.get(vkApiClient);
            endpoint.set(vkApiClient, s.replace("https", "http"));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
