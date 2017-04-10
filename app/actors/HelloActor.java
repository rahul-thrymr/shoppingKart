package actors;

import play.Logger;
import actors.HelloActorProtocol.SayHello;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class HelloActor extends UntypedActor {

    public static Props props = Props.create(HelloActor.class);

    public void onReceive(Object msg) throws Exception {
    	
    	
    	Logger.info(">>>>>>>>>>>>>>>>>  Rajesh");
        if (msg instanceof SayHello) {
            sender().tell("Hello, " + ((SayHello) msg).name, self());
        }
    }
}
