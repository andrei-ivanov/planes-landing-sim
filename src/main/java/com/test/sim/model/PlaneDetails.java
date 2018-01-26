package com.test.sim.model;

public class PlaneDetails {
	private final String name;

	private final PlaneType type;

	private final LandingType landingType;

	private final int controllerDialogInitDelay;

	public PlaneDetails(String name, PlaneType type, LandingType landingType, int controllerDialogInitDelay) {
		this.name = name;
		this.type = type;
		this.landingType = landingType;
		this.controllerDialogInitDelay = controllerDialogInitDelay;
	}

	public String getName() {
		return name;
	}

	public PlaneType getType() {
		return type;
	}


	public LandingType getLandingType() {
		return landingType;
	}

	/**
	 * After how many seconds it should initiate the dialog with a traffic controller (as not all the planes are ready to land in the same time)
	 */
	public int getControllerDialogInitDelay() {
		return controllerDialogInitDelay;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + controllerDialogInitDelay;
		result = prime * result + (landingType == null ? 0 : landingType.hashCode());
		result = prime * result + (name == null ? 0 : name.hashCode());
		result = prime * result + (type == null ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PlaneDetails)) {
			return false;
		}
		PlaneDetails other = (PlaneDetails) obj;
		if (controllerDialogInitDelay != other.controllerDialogInitDelay) {
			return false;
		}
		if (landingType != other.landingType) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Plane [name=").append(name).append(", type=").append(type).append(", landingType=").append(landingType)
				.append(", controllerDialogInitDelay=").append(controllerDialogInitDelay).append("]");
		return builder.toString();
	}
}