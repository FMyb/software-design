package ru.ilin;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import ru.ilin.actor.AbstractSearchActor;
import ru.ilin.actor.YandexSearchActor;
import ru.ilin.actor.GoogleSearchActor;
import ru.ilin.actor.MasterSearchActor;
import scala.concurrent.duration.Duration;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {
    private static class PrintActor extends UntypedAbstractActor {

        @Override
        public void onReceive(Object message) throws Throwable {
            if (message instanceof MasterSearchActorResponse m) {
                for (var resp : m.responses()) {
                    System.out.println(resp.searchEngineName() + ":");
                    resp.response().forEach(System.out::println);
                }
            }
        }
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("SearchSystem");
        List<Class<? extends AbstractSearchActor>> actors = List.of(YandexSearchActor.class, GoogleSearchActor.class);
        ActorRef actor = system.actorOf(
            Props.create(MasterSearchActor.class, actors, Duration.create(5, TimeUnit.SECONDS), PrintActor.class),
            "mastersearchactor"
        );
        // Send message
        actor.tell("itmo", ActorRef.noSender());
    }

}
