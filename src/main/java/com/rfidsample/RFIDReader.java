package com.rfidsample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RFIDReader {
	List<Device>	list				= null;
	String			initialCard			= "2BE19935";
	String[]		deviveCodes			= { "北京", "上海", "成都", "广州" };
	Set<Device>		initialedDevices	= new HashSet();



	public RFIDReader() {
		list = new ArrayList();
		for (int i = 1; i <= deviveCodes.length; i++) {
			Device dev = new Device(i);
			if (dev.getDevicePort() > 0) {
				list.add(dev);
			}
		}
		System.out.println(list.size() + " devices was found");
		initialDeviceCode();
		System.out.println(list.size() + " devices was initialed");
	}



	public void start() throws InterruptedException {
		System.out.println("Service starting, wait 5s");
		Thread.sleep(5 * 1000);
		System.out.println("Service was start");
		while (true) {
			for (Device dev : list) {
				String sn = dev.readSN();
				if (sn != null) {
					System.out.println("send message \"card id is " + sn + "\" from device " + dev.getDeviceCode());
				}
			}
		}
	}



	private void initialDeviceCode() {
		System.out.println("initial devices, will map the devices in order: " + Arrays.toString(deviveCodes));
		int hardwareid = 0;
		// initial
		while (true) {
			for (Device dev : list) {
				if (!initialedDevices.contains(dev)) {
					String sn = dev.readSN();
					if (sn == null) {
						continue;
					}
					System.out.println("read sn " + sn + " from device port " + dev.getDevicePort());
					if (initialCard.equalsIgnoreCase(sn)) {
						if (!initialedDevices.contains(dev))
							if (dev.getDeviceCode() == null) {
								String deviceCode = deviveCodes[hardwareid];
								dev.setDeviceCode(deviceCode);
								hardwareid++;
								initialedDevices.add(dev);
								System.out.println(dev.getDeviceCode() + " was initialed");
							}
					} else {
						System.out.println("please use " + initialCard + " to initial the device");
					}
				}
				if (initialedDevices.size() == list.size()) {
					for (Device beepDev : list) {
						beepDev.beep(5, 10);
					}
					return;
				}
			}

		}

	}



	public static void main(String[] args) throws Exception {
		new RFIDReader().start();
	}

}
