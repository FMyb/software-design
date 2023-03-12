package ru.ilin.actor;

import akka.actor.Props;
import akka.actor.ReceiveTimeout;
import akka.actor.UntypedAbstractActor;
import ru.ilin.MasterSearchActorResponse;
import ru.ilin.SearchResponse;
import scala.concurrent.duration.Duration;

import java.util.ArrayList;
import java.util.List;

public class MasterSearchActor extends UntypedAbstractActor {
    private final List<Class<? extends AbstractSearchActor>> searchers;
    private final List<SearchResponse> responses;
    private int waiting;
    private final Duration duration;
    private final Class<? extends UntypedAbstractActor> returned;

    public MasterSearchActor(
        List<Class<? extends AbstractSearchActor>> searchers,
        Duration duration,
        Class<? extends UntypedAbstractActor> returned
    ) {
        this.searchers = searchers;
        this.responses = new ArrayList<>();
        this.waiting = searchers.size();
        this.returned = returned;
        this.duration = duration;
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof ReceiveTimeout) {
            context().actorOf(Props.create(returned)).tell(new MasterSearchActorResponse(responses), self());
            System.out.println("return timeout");
            context().stop(self());
        } else if (message instanceof SearchResponse) {
            responses.add((SearchResponse) message);
            context().stop(context().sender());
            waiting--;
            if (waiting == 0) {
                context().actorOf(Props.create(returned)).tell(new MasterSearchActorResponse(responses), self());
                System.out.println("return norm");
                context().stop(self());
            }
        } else {
            for (var searcher : searchers) {
                var child = context().actorOf(Props.create(searcher));
                child.tell(message, self());
            }
            context().setReceiveTimeout(duration);
        }
    }
}
