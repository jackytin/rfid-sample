package com.rfidsample;

import com.rfidsample.Demo.RemoteCall;

public class RFIDReader {
    String              initialCard      = null;
    String           deviveCode      = null;
    Device dev=null;

    public void doInitial(String initialCard,String deviveCode) {
     this.initialCard=initialCard;
     this.deviveCode=deviveCode;
         dev = new Device( (int)(Math.random()*1000));
        bindDevice2Code();
        System.out.println(" devices was initialed");
    }



    public void start(RemoteCall callback) throws InterruptedException {
        System.out.println("Service starting, wait 5s");
        Thread.sleep(5 * 1000);
        System.out.println("Service was start");
        while (true) {
                String sn = dev.readSN();
                if (sn != null) {
                    System.out.println("send message \"card id is " + sn + "\" from device " + dev.getDeviceCode());
                    System.out.println("operator is "+initialCard);
                    callback.call(sn,dev.getDeviceCode(),initialCard);
                }
            
        }
    }



    private void bindDevice2Code() {
        System.out.println("initial devices, will map the device to : " +deviveCode);
        while (true) {
                // if the device is not initial, read card and initial it
                String sn = dev.readSN();
                if (sn == null) {
                    continue; // next device
                }
                System.out.println("read sn " + sn + " from device port " + dev.getDevicePort());
                if (!initialCard.equalsIgnoreCase(sn)) {
                    System.out.println("please use " + initialCard + " to initial the device");
                    continue;
                }
                dev.setDeviceCode(deviveCode);
                dev.beep(3, 10);
                break;
        }
    }

}
