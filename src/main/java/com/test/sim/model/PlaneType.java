package com.test.sim.model;

public enum PlaneType {
	Regular(5, 3), Large(7, 4);

	private final long timeToLand;

	private final long timeToCircle;

	private PlaneType(long timeToLand, long timeToCircle) {
		this.timeToLand = timeToLand;
		this.timeToCircle = timeToCircle;
	}

	/**
	 * @return
	 * 	Time to land, in seconds.
	 */
	public long getTimeToLand() {
		return timeToLand;
	}

	/**
	 * @return
	 * 	Time to circle, in seconds.
	 */
	public long getTimeToCircle() {
		return timeToCircle;
	}
}