package com.rfidsample;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class Demo {

    static class RemoteCall {
        @SuppressWarnings("deprecation")
        private static HttpClient httpClient       = new DefaultHttpClient();
        String                    baseURL          = "http://101.200.63.187:8080/naf/goodsManager/";
        Map<String, String>       cardGoods        = new HashMap();
        Map<String, String>       deviceRemoveCall = new HashMap();
        String[]                  devices          = null;



        public RemoteCall(Map cardGoods, Map deviceRemoveCall, String[] devices) {
            this.cardGoods = cardGoods;
            this.deviceRemoveCall = deviceRemoveCall;
            this.devices = devices;
        }



        String getURL(String rfid, String device) {
            // return baseURL + deviceRemoveCall.get(device) + "/" +
            // cardGoods.get(rfid) + "/" + device;
            return baseURL + deviceRemoveCall.get(device) + "/" + rfid + "/" + device + "/";
        }



        String pack(String rfid, String device, String operator) {
            // pack/goodsId/packageName/rfid/packageTime/description/
            String description = "打包员" + operator + "在" + device + "进行的打包完成的操作";
            return baseURL + "/" + cardGoods.get(rfid) + "/这是一箱很贵重的物品" + rfid + "/"
                    + new SimpleDateFormat("YYYY-MM-DD").format(new Date()) + "/" + description + "/";
        }



        public void call(String rfid, String device, String operator) {
            String url = getURL(rfid, device);
            if (device == devices[0]) {
                url = pack(rfid, device, operator);
            }
            System.out.println("call url " + url);
            HttpGet httpget = new HttpGet(url);
            try {
                HttpResponse httpresponse = httpClient.execute(httpget);
                HttpEntity entity = httpresponse.getEntity();
                String body = EntityUtils.toString(entity);
                System.out.println("response: " + body);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public static void main(String[] args) throws Exception {
        RFIDReader rfidReader = new RFIDReader();

        String initialCard = "2BE19935";
        String dev1 = "package";
        String dev2 = "RS000001";
        String dev3 = "SS000001";
        String dev4 = "RS000001";

        String[] device = { dev1, dev2, dev3, dev4 };
        Map<String, String> deviceRemoveCall = new HashMap();
        deviceRemoveCall.put(dev1, "pack");
        // we receive the package
        deviceRemoveCall.put(dev2, "transferToAnotherReceivingSpace");
        // move the package here, prepare to send to store
        deviceRemoveCall.put(dev3, "transferToAnotherShippingSpace");
        // Store receive the goods
        deviceRemoveCall.put(dev4, "transferToAnotherRetailStoreOrder");

        Map<String, String> cardGoods = new HashMap();
        cardGoods.put("2BE2A8F4", "G000001");// card no 1
        cardGoods.put("2BE15DC1", "G000002");// card no 2
        cardGoods.put("2BE2CBA9", "G000003"); // card no 3
        cardGoods.put("2BE0BC65", "G000004"); // card no 3

        RemoteCall callback = new RemoteCall(cardGoods, deviceRemoveCall, device);

        rfidReader.doInitial(initialCard, device[0]);

        rfidReader.start(callback);
    }

}
