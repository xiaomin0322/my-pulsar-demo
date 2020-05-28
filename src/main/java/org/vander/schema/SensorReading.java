package org.vander.schema;

public class SensorReading {
	public String temperature;
	
	public long addStr;
	

	public long getAddStr() {
		return addStr;
	}

	public void setAddStr(long addStr) {
		this.addStr = addStr;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	// A no-arg constructor is required
	public SensorReading() {
	}
}