package com.jiafancreatezipfile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamDrainer implements Runnable{

	
	   private InputStream ins;

	    public StreamDrainer(InputStream ins) {
	        this.ins = ins;
	    }

	    public void run() {
	        try {
	            BufferedReader reader = new BufferedReader(
	                    new InputStreamReader(ins));
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                System.out.println(line);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
}
