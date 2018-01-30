package com.test.sim.model;

import java.util.EventObject;
import java.util.Objects;

public class ControlMessage extends EventObject {

	private final ControlMessageType type;
	private final Runway runway;

	public ControlMessage(String controller, ControlMessageType type, Runway runway) {
		super(controller);
		this.runway = runway;
		this.type = Objects.requireNonNull(type, "The type cannot be null");
	}

	public String getController() {
		return (String) getSource();
	}

	public ControlMessageType getType() {
		return type;
	}

	public Runway getRunway() {
		return runway;
	}
}