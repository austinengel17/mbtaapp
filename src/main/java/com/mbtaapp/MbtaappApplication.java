package com.mbtaapp;

import com.mbtaapp.service.MbtaApiService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class MbtaappApplication {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("\n" +
				"╭╮╭╮╭╮╱╱╭╮╱╱╱╱╱╱╱╱╱╱╱╱╱╱╭╮╱╱╱╱╭━╮╭━╮╱╱╱╱╭━╮╭━┳━━┳━━━━┳━━━╮╭━━━╮╱╱╱╱╱╭╮╱╱╱╱╱╱╱╭╮\n" +
				"┃┃┃┃┃┃╱╱┃┃╱╱╱╱╱╱╱╱╱╱╱╱╱╭╯╰╮╱╱╱┃┃╰╯┃┃╱╱╱╱┃┃╰╯┃┃╭╮┃╭╮╭╮┃╭━╮┃┃╭━╮┃╱╱╱╱╱┃┃╱╱╱╱╱╱╭╯╰╮\n" +
				"┃┃┃┃┃┣━━┫┃╭━━┳━━┳╮╭┳━━╮╰╮╭╋━━╮┃╭╮╭╮┣╮╱╭╮┃╭╮╭╮┃╰╯╰┫┃┃╰┫┃╱┃┃┃┃╱┃┣━━┳━━┫┃╭┳━━┳━┻╮╭╋┳━━┳━╮\n" +
				"┃╰╯╰╯┃┃━┫┃┃╭━┫╭╮┃╰╯┃┃━┫╱┃┃┃╭╮┃┃┃┃┃┃┃┃╱┃┃┃┃┃┃┃┃╭━╮┃┃┃╱┃╰━╯┃┃╰━╯┃╭╮┃╭╮┃┃┣┫╭━┫╭╮┃┃┣┫╭╮┃╭╮╮\n" +
				"╰╮╭╮╭┫┃━┫╰┫╰━┫╰╯┃┃┃┃┃━┫╱┃╰┫╰╯┃┃┃┃┃┃┃╰━╯┃┃┃┃┃┃┃╰━╯┃┃┃╱┃╭━╮┃┃╭━╮┃╰╯┃╰╯┃╰┫┃╰━┫╭╮┃╰┫┃╰╯┃┃┃┃\n" +
				"╱╰╯╰╯╰━━┻━┻━━┻━━┻┻┻┻━━╯╱╰━┻━━╯╰╯╰╯╰┻━╮╭╯╰╯╰╯╰┻━━━╯╰╯╱╰╯╱╰╯╰╯╱╰┫╭━┫╭━┻━┻┻━━┻╯╰┻━┻┻━━┻╯╰╯\n" +
				"╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╭━╯┃╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱┃┃╱┃┃\n" +
				"╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╰━━╯╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╰╯╱╰╯\n" +

				"╭╮╱╱╱╭━━╮╱╭━┳╮╭╮╱╱╱╭━╮" + "\n" +
				"┃╰┳┳╮┃╭╮┣┳┫━┫╰╋╋━┳╮┃┳╋━┳┳━┳━┳╮\n" +
				"┃╋┃┃┃┃┣┫┃┃┣━┃╭┫┃┃┃┃┃┻┫┃┃┃╋┃┻┫╰╮ \n" +
				"╰━╋╮┃╰╯╰┻━┻━┻━┻┻┻━╯╰━┻┻━╋╮┣━┻━╯ \n" +
				"╱╱╰━╯╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╰━╯\n");
//		MbtaApiService mbtaApiService = new MbtaApiService();
//		mbtaApiService.getStops("Red");
//		mbtaApiService.subscribeVehiclesSse(Map.of(
//				"route_type","0,1",
//				"route","Red"
//		));
		SpringApplication.run(MbtaappApplication.class, args);
	}

}
