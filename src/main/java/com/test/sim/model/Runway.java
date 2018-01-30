package com.test.sim.model;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class Runway {
	public enum Type {
		SHORT, LONG;
	}

	private final Type type;

	private final AtomicReference<PlaneDetails> plane = new AtomicReference<>();

	public Runway(Type type) {
		this.type = Objects.requireNonNull(type, "The type cannot be null");
	}

	public Type getType() {
		return type;
	}

	public boolean isBusy() {
		return plane.get() != null;
	}

	public boolean land(PlaneDetails plane) {
		PlaneDetails prev = this.plane.get();
		if (prev != null) {
			return false;
		}
		// updateAndGet((p) -> plane);
		return this.plane.compareAndSet(prev, plane);
	}

	public boolean park() {
		PlaneDetails prev = this.plane.get();
		//updateAndGet((p) -> null);
		return this.plane.compareAndSet(prev, null);
	}
}