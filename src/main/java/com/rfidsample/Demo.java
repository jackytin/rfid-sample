package com.rfidsample;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class Demo {

    static class RemoteCall {
        @SuppressWarnings("deprecation")
        private static HttpClient httpClient = new DefaultHttpClient();
        String baseURL = "http://101.200.63.187:8080/naf/goodsManager/";
        Map<String, String> cardGoods        = new HashMap();
        Map<String, String> deviceRemoveCall        = new HashMap();
        public RemoteCall(Map cardGoods,Map deviceRemoveCall){
            this.cardGoods=cardGoods;
            this.deviceRemoveCall=deviceRemoveCall;   
        }
         String getURL(String rfid, String device){
            return baseURL+deviceRemoveCall.get(device)+"/"+cardGoods.get(rfid)+"/"+device;
        }
        public void call(String rfid, String device) {
            String url=getURL(rfid,device);
            System.out.println("call url "+url);
            HttpGet httpget = new HttpGet(url); 
            try {
                HttpResponse httpresponse = httpClient.execute(httpget);
                HttpEntity entity = httpresponse.getEntity();  
                String body = EntityUtils.toString(entity);  
                System.out.println("response: "+body);
            } catch (IOException e) {
                e.printStackTrace();
            } 
        }
    }

    public static void main(String[] args) throws Exception {
        RFIDReader rfidReader = new RFIDReader();
     
        String initialCard = "2BE19935";
        String[] device = { "到达收货区", "整到了出货区", "重复了", "小超接受了" };
        Map<String, String> deviceRemoveCall =  new HashMap();      
        deviceRemoveCall.put("到达收货区", "transferToAnotherReceivingSpace");
        deviceRemoveCall.put("整到了出货区", "transferToAnotherShippingSpace");
        deviceRemoveCall.put("重复了", "transferToAnotherShippingSpace");//TODO
        deviceRemoveCall.put("小超接受了", "transferToAnotherRetailStoreOrder");
       
        Map<String, String> cardGoods = new HashMap();;
        cardGoods.put("2BE2A8F4", "G000001");//no 1
        cardGoods.put("2BE15DC1", "G000002");// no 2
        cardGoods.put("2BE2CBA9", "G000003"); //no 3
        cardGoods.put("2BE0BC65", "G000004"); //no 3
   
        RemoteCall callback = new RemoteCall(cardGoods,deviceRemoveCall);
        rfidReader.doInitial(initialCard, device);
        
        
        rfidReader.start(callback);
    }

}
