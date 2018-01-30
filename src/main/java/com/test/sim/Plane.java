package com.test.sim;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.test.sim.model.ControlMessage;
import com.test.sim.model.ControlMessageType;
import com.test.sim.model.PlaneDetails;
import com.test.sim.model.PlaneMessage;
import com.test.sim.model.PlaneMessageType;
import com.test.sim.model.Runway;

public class Plane implements Runnable {
	private static final Logger LOGGER = LogManager.getLogger(Plane.class);

	private final PlaneDetails details;

	private final BlockingQueue<ControlMessage> incoming;

	private final BlockingQueue<PlaneMessage> outgoing;

	public Plane(PlaneDetails details, BlockingQueue<PlaneMessage> outgoing) {
		this.details = Objects.requireNonNull(details, "The plane details cannot be null");
		this.incoming = new SynchronousQueue<>();//Objects.requireNonNull(incoming, "The incoming messages queue cannot be null");
		this.outgoing = Objects.requireNonNull(outgoing, "The outgoing messages queue cannot be null");
	}

	@Override
	public void run() {
		try {
			sendInitialMessage();

			for (ControlMessage message = incoming.take(); /*!message.getPlane().getName().equals(details.getName())*/; message = incoming.take()) {
				LOGGER.info("{} → {}: {}", message.getController(), details.getName(), message.getType());

				if (message.getType() == ControlMessageType.LAND) {
					Runway runaway = message.getRunway();
					LOGGER.trace("Landing on runway {}...", runaway.getType());

					sleep(details.getType().getTimeToLand());
					LOGGER.trace("Done.");

					outgoing.put(new PlaneMessage(this, PlaneMessageType.LANDED, runaway));

				} else if (message.getType() == ControlMessageType.CIRCLE) {
					LOGGER.trace("Circling...");
					sleep(details.getType().getTimeToCircle());
					outgoing.put(new PlaneMessage(this, PlaneMessageType.READY_TO_LAND));
				}
			}
		} catch (InterruptedException e) {
		}
	}

	void sendInitialMessage() throws InterruptedException {
		sleep(details.getControllerDialogInitDelay());

		outgoing.put(new PlaneMessage(this, PlaneMessageType.READY_TO_LAND));

		LOGGER.trace("Presence notified to the controller.");
	}

	private void sleep(long delay) throws InterruptedException {
		LOGGER.trace("Waiting {} seconds before sending notification...", delay);
		TimeUnit.SECONDS.sleep(delay);
	}

	public void message(ControlMessage controlMessage) throws InterruptedException {
		incoming.put(controlMessage);
	}

	public void land() {

	}

	public void circle() {

	}

	public PlaneDetails getDetails() {
		return details;
	}
}