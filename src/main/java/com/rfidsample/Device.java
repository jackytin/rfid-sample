package com.rfidsample;

import jcumf.umf_javacall;

public class Device {
	private int			devicePort;

	protected short		findCardMode	= 1;

	protected String	emptyId			= String.valueOf(new char[8]);

	protected String	deviceCode;



	public int getDevicePort() {
		return devicePort;
	}



	public void setDevicePort(int pDevicePort) {
		devicePort = pDevicePort;
	}



	public String getDeviceCode() {
		return deviceCode;
	}



	public void setDeviceCode(String pDeviceCode) {
		deviceCode = pDeviceCode;
	}



	public Device(int deviceNO) {
		devicePort = umf_javacall.fw_init(100, deviceNO);
		System.out.println("Device number is " + devicePort);
	}



	protected String readSN() {
		return readSN(true);
	}



	protected String readSNWithoutBeep() {
		return readSN(false);
	}



	protected String readSN(boolean beep) {
		// M1 card serial number string
		char[] snChars = new char[8];
		umf_javacall.fw_card_hex(this.devicePort, (short) findCardMode, snChars); // card
		String sn = String.valueOf(snChars);
		if (emptyId.equalsIgnoreCase(sn)) {
			sn = null;
		}
		if (beep) {
			if (sn == null) {
//				 beep(3, 10);
			} else {
				beep(1, 100);
			}
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return sn;
	}



	protected void beep(int times, int duration) {
		for (int i = 0; i < times; i++) {
			umf_javacall.fw_beep(this.devicePort, (short) duration);
			try {
				Thread.sleep(duration + 10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + devicePort;
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Device other = (Device) obj;
		if (devicePort != other.devicePort)
			return false;
		return true;
	}

}
