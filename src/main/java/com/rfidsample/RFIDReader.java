package com.rfidsample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.rfidsample.Demo.RemoteCall;

public class RFIDReader {
    List<Device>        list             = null;
    String              initialCard      = null;
    String[]            deviveCodes      = null;
  
    Set<Device>         initialedDevices = new HashSet();


    public void doInitial(String initialCard,String[] deviveCodes) {
     this.initialCard=initialCard;
     this.deviveCodes=deviveCodes;
        list = new ArrayList();
        for (int i = 1; i <= deviveCodes.length; i++) {
            Device dev = new Device(i);
            if (dev.getDevicePort() > 0) {
                list.add(dev);
            }
        }
        System.out.println(list.size() + " devices was found");
        bindDevice2Code();
        System.out.println(list.size() + " devices was initialed");
    }



    public void start(RemoteCall callback) throws InterruptedException {
        System.out.println("Service starting, wait 5s");
        Thread.sleep(5 * 1000);
        System.out.println("Service was start");
        while (true) {
            for (Device dev : list) {
                String sn = dev.readSN();
                if (sn != null) {
                    System.out.println("send message \"card id is " + sn + "\" from device " + dev.getDeviceCode());
                    System.out.println("operator is "+initialCard);
                    callback.call(sn,dev.getDeviceCode(),initialCard);
                }
            }
        }
    }



    private void bindDevice2Code() {
        System.out.println("initial devices, will map the devices in order: " + Arrays.toString(deviveCodes));
        int hardwareid = 0;

        while (true) {
            for (Device dev : list) {
                // if the device is not initial, read card and initial it
                String sn = null;
                if (initialedDevices.contains(dev) || (sn = dev.readSN()) == null) {
                    continue; // next device
                }
                System.out.println("read sn " + sn + " from device port " + dev.getDevicePort());
                if (!initialCard.equalsIgnoreCase(sn)) {
                    System.out.println("please use " + initialCard + " to initial the device");
                    continue;
                }
                hardwareid = processCard(hardwareid, dev);
                if (initialedDevices.size() == list.size()) {
                    for (Device beepDev : list) {
                        beepDev.beep(5, 10);
                    }
                    return;
                }
            }
        }
    }



    private int processCard(int hardwareid, Device dev) {
        if (dev.getDeviceCode() == null) {
            String deviceCode = deviveCodes[hardwareid];
            dev.setDeviceCode(deviceCode);
            hardwareid++;
            initialedDevices.add(dev);
            System.out.println("Authentication passed, employee "+initialCard+" login into "+dev.getDeviceCode()+" successfull");
        }
        return hardwareid;
    }

}
