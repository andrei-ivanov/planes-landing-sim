package com.test.sim.model;

import java.util.EventObject;
import java.util.Objects;

import com.test.sim.Plane;

public class PlaneMessage extends EventObject {

	private final PlaneMessageType type;
	private Runway runway;

	public PlaneMessage(Plane plane, PlaneMessageType type) {
		super(plane);
		this.type = Objects.requireNonNull(type, "The type cannot be null");
	}

	public PlaneMessage(Plane plane, PlaneMessageType type, Runway runway) {
		this(plane, type);
		this.runway = Objects.requireNonNull(runway, "The runway cannot be null");
	}

	public Plane getPlane() {
		return (Plane) getSource();
	}

	public PlaneMessageType getType() {
		return type;
	}

	public Runway getRunway() {
		return runway;
	}
}