package com.test.sim;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.test.sim.model.PlaneDetails;
import com.test.sim.model.PlaneMessage;
import com.test.sim.model.PlaneType;
import com.test.sim.model.Runway;
import com.test.sim.model.Runway.Type;

public class Simulator {
	private static final Logger LOGGER = LogManager.getLogger(Simulator.class);

	private BlockingQueue<PlaneMessage> planeMessages;

	private Map<PlaneType, List<Runway>> runways;

	public static void main(String... args) throws Exception {
		new Simulator().run(args);
	}

	public void run(String... args) throws IOException {
		List<PlaneDetails> planes = getPlaneDetails(args.length > 0 ? args[0] : null);
		planes.forEach(LOGGER::info);

		planeMessages = new SynchronousQueue<>(true);
		//planeMessages = new ArrayBlockingQueue<>(planes.size());

		runways = getRunways();

		LOGGER.info("Starting threads...");

		List<TrafficController> controllers = getControllers();
		controllers.forEach(controller -> new Thread(controller, controller.getName()).start());

		planes.forEach(details -> new Thread(new Plane(details, planeMessages), details.getName()).start());
	}

	List<PlaneDetails> getPlaneDetails(String config) throws IOException {
		LOGGER.info("Loading planes...");
		return Config.readData(config);
	}

	Map<PlaneType, List<Runway>> getRunways() {
		Runway runway1 = new Runway(Type.SHORT);
		Runway runway2 = new Runway(Type.LONG);
		
		Map<PlaneType, List<Runway>> runways = new EnumMap<>(PlaneType.class);
		runways.put(PlaneType.Regular, Arrays.asList(runway1, runway2));
		runways.put(PlaneType.Large, Arrays.asList(runway2));
		return runways;
	}

	List<TrafficController> getControllers() {
		TrafficController c1 = new TrafficController(1, planeMessages, runways);
		TrafficController c2 = new TrafficController(2, planeMessages, runways);

		return Arrays.asList(c1, c2);
	}
}