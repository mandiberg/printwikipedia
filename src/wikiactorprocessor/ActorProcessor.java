/**
 * Copyright (C) 2009-2012 Typesafe Inc. <http://www.typesafe.com>
 */

package wikiactorprocessor;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;


import wikitopdf.Processor;
import wikitopdf.SQLProcessor;
import wikitopdf.WikiProcessor;
import wikitopdf.utils.WikiSettings;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.routing.RoundRobinRouter;
import akka.util.Duration;

public class ActorProcessor {

	public static void main(String[] args) {
		ActorProcessor actorProcessor = new ActorProcessor();
		actorProcessor.act();
	}

	static class Calculate {
	}

	static class Work {
		private final int start;
		private final int nrOfElements;

		public Work(int start, int nrOfElements) {
			this.start = start;
			this.nrOfElements = nrOfElements;
		}

		public void work() {
			
			
		}
		
		public int getStart() {
			return start;
		}

		public int getNrOfElements() {
			return nrOfElements;
		}
	}

	static class Result {
		private final int value;

		public Result(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	static class PDFTimeApproximation {
		private final double pi;
		private final Duration duration;

		public PDFTimeApproximation(double pi, Duration duration) {
			this.pi = pi;
			this.duration = duration;
		}

		public double getPi() {
			return pi;
		}

		public Duration getDuration() {
			return duration;
		}
	}

	public static class Worker extends UntypedActor {

		private int processPDF(int start) {
			SQLProcessor sqlReader = null;
			try {
				sqlReader = new SQLProcessor();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		// removed 5/21 rebuild b/c wikiprocessor.java not importing correctly
                        Processor processor = new Processor(start, sqlReader); //this is where it uses the wikiprocessor.java?

			processor.run();
			return 1;
		}

		public void onReceive(Object message) {
			if (message instanceof Work) {
				Work work = (Work) message;
				int result = processPDF(work.getStart());
				getSender().tell(new Result(result), getSelf());
			} else {
				unhandled(message);
			}
		}
	}

	public static class Master extends UntypedActor {
		private final int nrOfElements;
		private final int nrOfWorkers;
		private int nbrOfEntries;
		private double pi;
		private int nrOfResults;
		private final long start = System.currentTimeMillis();

		private final ActorRef listener;
		private final ActorRef workerRouter;

		public Master(ActorRef listener) {
			this.nrOfElements = WikiSettings.getInstance().getThreadLimit();
			this.nrOfWorkers  = WikiSettings.getInstance().getThreadLimit();
			this.listener     = listener;
			/**
			 * We have to figure out how many entries we have to process
			 *
			 */
			workerRouter      = this.getContext().actorOf(
					new Props(Worker.class).withRouter(new RoundRobinRouter(
							nrOfWorkers)), "workerRouter");
				
			
		}
		
		public void onReceive(Object message) {
			
			int pageBunch =  WikiSettings.getInstance().getArticleBunch();
			int startLimit = WikiSettings.getInstance().getStartPage();
			SQLProcessor sqlProcessor = null;
			try {
				sqlProcessor = new SQLProcessor();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
			nbrOfEntries   = sqlProcessor.getArticlesCount();
			if (message instanceof Calculate) {
				//giving each worker work todo - in a round-robin fashion
				for (int start = 0; start < nbrOfEntries; start++) {
					workerRouter.tell(new Work(startLimit, nrOfElements), getSelf());
					startLimit += pageBunch;
				}
			} else if (message instanceof Result) {
				Result result = (Result) message;
				pi += result.getValue();
				nrOfResults += 1;
				if (nrOfResults == nbrOfEntries) {
					// Send the result to the listener
					Duration duration = Duration.create(
							System.currentTimeMillis() - start,
							TimeUnit.MILLISECONDS);
					listener.tell(new PDFTimeApproximation(pi, duration), getSelf());
					// Stops this actor and all its supervised children
					getContext().stop(getSelf());
				}
			} else {
				unhandled(message);
			}
		}
	}

	public static class Listener extends UntypedActor {
		public void onReceive(Object message) {
			if (message instanceof PDFTimeApproximation) {
				PDFTimeApproximation approximation = (PDFTimeApproximation) message;
				System.out
						.println(String
								.format("\n\tPi approximation: \t\t%s\n\tCalculation time: \t%s",
										approximation.getPi(),
										approximation.getDuration()));
				getContext().system().shutdown();
			} else {
				unhandled(message);
			}
		}
	}

	public void act() {
		ActorSystem system = ActorSystem.create("WikiSystem");
		final ActorRef listener = system.actorOf(new Props(Listener.class),
				"listener");
		ActorRef master = system.actorOf(new Props(new UntypedActorFactory() {
			public UntypedActor create() {
				return new Master(listener);
			}
		}), "master");
		master.tell(new Calculate());
	}
}