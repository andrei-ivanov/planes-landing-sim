package com.test.sim;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.test.sim.model.PlaneDetails;
import com.test.sim.model.PlaneMessage;

public class Plane implements Runnable {
	private final PlaneDetails details;

	private final BlockingQueue<String> incoming;

	private final BlockingQueue<PlaneMessage> outgoing;

	private boolean initialMessageSent = false;

	private Plane(PlaneDetails details, BlockingQueue<String> incoming, BlockingQueue<PlaneMessage> outgoing) {
		this.details = Objects.requireNonNull(details, "The plane details cannot be null");
		this.incoming = Objects.requireNonNull(incoming, "The incoming messages queue cannot be null");
		this.outgoing = Objects.requireNonNull(outgoing, "The outgoing messages queue cannot be null");
	}

	@Override
	public void run() {
		try {
			if (!initialMessageSent) {
				Thread.sleep(TimeUnit.MILLISECONDS.convert(details.getControllerDialogInitDelay(), TimeUnit.SECONDS));

				outgoing.put(PlaneMessage.READY_TO_LAND);

				initialMessageSent = true;
			}
			for (String message = incoming.take(); !message.equals("DONE"); message = incoming.take()) {
				System.out.format("MESSAGE RECEIVED: %s%n", message);
			}
		} catch (InterruptedException e) {
		}
	}

	public void land() {

	}

	public void circle() {

	}
}