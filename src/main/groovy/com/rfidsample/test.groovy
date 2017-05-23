package com.rfidsample;

import groovyx.net.http.RESTClient;
//
//http://klipkloud.net:8080/naf/goodsManager/transferToAnotherReceivingSpace/G000007/
RESTClient client = new RESTClient("http://klipkloud.net:8080");
def resp = client.get(
        path:"naf/goodsManager/transferToAnotherRetailStore/G000007/RS000006/"
        )
println resp.responseData;