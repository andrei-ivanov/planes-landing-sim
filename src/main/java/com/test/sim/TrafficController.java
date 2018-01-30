package com.test.sim;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.test.sim.model.ControlMessage;
import com.test.sim.model.ControlMessageType;
import com.test.sim.model.PlaneDetails;
import com.test.sim.model.PlaneMessage;
import com.test.sim.model.PlaneMessageType;
import com.test.sim.model.PlaneType;
import com.test.sim.model.Runway;

public class TrafficController implements Runnable {
	private static final Logger LOGGER = LogManager.getLogger(TrafficController.class);

	private final String name;

	private final BlockingQueue<PlaneMessage> planeMessages;

	private final Map<PlaneType, List<Runway>> runwaysByPlaneType;

	public TrafficController(int index, BlockingQueue<PlaneMessage> planeMessages, Map<PlaneType, List<Runway>> runwaysByPlaneType) {
		this.name = getClass().getSimpleName() + ' ' + index;
		this.planeMessages = Objects.requireNonNull(planeMessages, "The planeMessages queue cannot be null");
		this.runwaysByPlaneType = runwaysByPlaneType;
	}

	public String getName() {
		return name;
	}

	@Override
	public void run() {
		while (true) {
			try {
				PlaneMessage message = planeMessages.take();
				Plane plane = message.getPlane();
				PlaneDetails pd = plane.getDetails();
				LOGGER.info("{} → {}: {}", pd.getName(), this.name, message.getType());

				if (message.getType() == PlaneMessageType.READY_TO_LAND) {
					LOGGER.trace("Trying to find an empty runway...");
					Runway runway = runwaysByPlaneType.get(pd.getType()).stream().filter(r -> r.land(pd)).findFirst().orElse(null);

					// TODO: emergency landings
					ControlMessageType cmt = runway == null ? ControlMessageType.CIRCLE : ControlMessageType.LAND;
					plane.message(new ControlMessage(name, cmt, runway));
				} else if (message.getType() == PlaneMessageType.LANDED) {
					message.getRunway().park();
				}
			} catch (InterruptedException e) {
				LOGGER.error("Interrupted: {}", e.getMessage());
			}
		}
	}
}