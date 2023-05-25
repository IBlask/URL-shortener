package com.shorty.shorty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
public class ShortyApplication {
	private static String address = "http://";

	public static String getAddress() {
		return address;
	}

	static void setAddress() {
		try {
			address += InetAddress.getLocalHost().getHostName() + "/";
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}


	public static void main(String[] args) {
		SpringApplication.run(ShortyApplication.class, args);

		setAddress();
	}

}
