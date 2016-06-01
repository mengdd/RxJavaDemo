package com.ddmeng.rxjavademo.network;

public class MockFetcher {

    public static String getString(String input) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Hello World response: " + input;
    }
}
