


import java.util.concurrent.TimeUnit;

import play.Application;
import play.GlobalSettings;
import play.libs.Akka;
import scala.concurrent.duration.Duration;
import actors.HelloActor;
import akka.actor.ActorRef;
import akka.actor.Props;



public class Global extends GlobalSettings{
	
	
	  public void onStart(Application app) {

		
	
		  final ActorRef HelloActor = Akka.system().actorOf(Props.create(HelloActor.class));
		  Akka.system().scheduler().schedule(
				  Duration.create(0, TimeUnit.MILLISECONDS), //Initial delay 0 milliseconds
				  Duration.create(1, TimeUnit.MINUTES),     //Frequency 30 minutes
				  HelloActor,
				  "tick",
				  Akka.system().dispatcher(),
				  null
				  );
	  } 
}