package com.test.sim;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import com.test.sim.model.LandingType;
import com.test.sim.model.PlaneDetails;
import com.test.sim.model.PlaneType;

public class Config {
	private static final String DEFAULT_CONFIG = "/planes.txt";

	private final String location;

	public Config(String location) {
		this.location = location;
	}

	static List<PlaneDetails> readData(String location) throws IOException {
		return new Config(location).loadData();
	}

	List<PlaneDetails> loadData() throws IOException {
		if (location == null) {
			System.out.println("Loading default config from " + DEFAULT_CONFIG);
			return readConfig(Simulator.class.getResourceAsStream(DEFAULT_CONFIG));
		}

		try {
			URL url = new URL(location);
			return readConfig(url);
		} catch (MalformedURLException e) {
			// not a URL, try to see if it's a file
			Path path = Paths.get(location);
			return readConfig(path);
		}
	}

	List<PlaneDetails> readConfig(URL url) throws IOException {
		System.out.println("Loading config from " + url);
		return readConfig(url.openStream());
	}

	List<PlaneDetails> readConfig(Path path) throws IOException {
		System.out.println("Loading config from " + path);
		return readConfig(Files.newInputStream(path));
	}

	List<PlaneDetails> readConfig(InputStream is) throws IOException {
		if (is == null) {
			throw new IllegalArgumentException("The input stream cannot be null.");
		}

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
			return reader.lines().map(s -> parseLine(s)).collect(Collectors.toList());
		}
	}

	PlaneDetails parseLine(String line) {
		String[] pieces = line.split(", ");
		String name = pieces[0];
		PlaneType planeType = PlaneType.valueOf(pieces[1]);
		LandingType landingType = LandingType.valueOf(pieces[2]);
		int controllerDialogInitDelay = Integer.parseInt(pieces[3]);

		return new PlaneDetails(name, planeType, landingType, controllerDialogInitDelay);
	}
}