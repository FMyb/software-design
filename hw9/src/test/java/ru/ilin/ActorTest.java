package ru.ilin;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.ilin.actor.AbstractSearchActor;
import ru.ilin.actor.MasterSearchActor;
import scala.concurrent.duration.Duration;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ActorTest {
    static boolean[] f;

    @Before
    public void setUp() {
        f = new boolean[]{true};
    }

    private static class CheckAnswerActor extends UntypedAbstractActor {
        public CheckAnswerActor() {
        }

        private final List<SearchResponse> expected = List.of(
            new SearchResponse(List.of("test", "test"), "test"),
            new SearchResponse(List.of("test", "test"), "test")
        );

        @Override
        public void onReceive(Object message) throws Throwable {
            Assert.assertEquals(MasterSearchActorResponse.class, message.getClass());
            Assert.assertEquals(expected, ((MasterSearchActorResponse) message).responses());
            f[0] = false;
        }
    }

    private static class LongTimeActor extends AbstractSearchActor {
        public LongTimeActor() {
        }

        @Override
        public SearchResponse getResponse() throws IOException {
            return new SearchResponse(List.of("test", "test"), "test");
        }

        @Override
        public void init(HttpsURLConnection connection) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String getQueryRef() {
            return "https://test.ru/test?";
        }

        @Override
        public String getQueryParameterName() {
            return "";
        }
    }

    private static class StubTestActor extends AbstractSearchActor {
        public StubTestActor() {
        }

        @Override
        public SearchResponse getResponse() throws IOException {
            return new SearchResponse(List.of("test", "test"), "test");
        }

        @Override
        public void init(HttpsURLConnection connection) {}

        @Override
        public String getQueryRef() {
            return "https://test.ru/?test";
        }

        @Override
        public String getQueryParameterName() {
            return "";
        }
    }

    @Test(timeout = 1000)
    public void testNormalAnswer() throws InterruptedException {

        ActorSystem system = ActorSystem.create("test");
        List<Class<? extends AbstractSearchActor>> actors = List.of(StubTestActor.class, StubTestActor.class);
        ActorRef actor = system.actorOf(
            Props.create(MasterSearchActor.class, actors, Duration.create(5, TimeUnit.SECONDS), CheckAnswerActor.class),
            "test"
        );
        actor.tell("itmo", ActorRef.noSender());
        Thread.sleep(500);
        if (f[0]) {
            Assert.fail();
        }
    }

    @Test
    public void testLongAnswer() throws InterruptedException {
        ActorSystem system = ActorSystem.create("test");
        List<Class<? extends AbstractSearchActor>> actors = List.of(LongTimeActor.class, StubTestActor.class, StubTestActor.class);
        ActorRef actor = system.actorOf(
            Props.create(MasterSearchActor.class, actors, Duration.create(500, TimeUnit.MILLISECONDS), CheckAnswerActor.class),
            "test"
        );
        actor.tell("itmo", ActorRef.noSender());
        Thread.sleep(700);
        if (f[0]) {
            Assert.fail();
        }
    }


}
