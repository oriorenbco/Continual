package com.lorin.continual.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lorin.continual.dao.NetworkTrafficDAO;
import com.lorin.continual.model.NetworkTrafficEntity;
import com.lorin.continual.model.NetworkTrafficEntity.Type;
/**
 * Generates network traffic data  given initial parameters
 * @author lorin
 *
 */
@Service
public class NetworkTrafficDataGenerator {

	private final static  Logger logger = LoggerFactory.getLogger(NetworkTrafficDataGenerator.class);

	private static final int DEFAULT_DOCUMENTS_TO_GENERATES = 1000;
	private static final int DEFAULT_ROADS_NUMBER = 10;
	private static final int DEFAULT_DAYS_NUMBER = 7;

	@Autowired
	private NetworkTrafficDAO networkTrafficEntityDAO;
	
	@Autowired
	private DataAnalyser dataAnalyser;

	/*
	 * from road 1 to roads
	 */
	
	private int roads=DEFAULT_ROADS_NUMBER;
	/*
	 * from day 1 to days
	 */
	private int days=DEFAULT_DAYS_NUMBER;

	private int documents=DEFAULT_DOCUMENTS_TO_GENERATES;

	/*
	 * counter to use during generation
	 */
	private static int counter;

	



	public int getRoads() {
		return roads;
	}



	public void setRoads(int roads) {
		this.roads = roads;
	}



	public int getDays() {
		return days;
	}



	public void setDays(int days) {
		this.days = days;
	}




	public int getDocuments() {
		return documents;
	}



	public void setDocuments(int documents) {
		this.documents = documents;
	}



	@SuppressWarnings("unchecked")
	public void configure(JSONObject json) {
		try {
		
			this.setDays( ((Long)json.getOrDefault("days",(long)DEFAULT_DAYS_NUMBER)).intValue());
			this.setRoads( ((Long)json.getOrDefault("roads",(long)DEFAULT_ROADS_NUMBER)).intValue());
			this.setDocuments( ((Long)json.getOrDefault("documents",(long)DEFAULT_DOCUMENTS_TO_GENERATES)).intValue());



		} catch (Throwable e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}

	

	}


   
	@SuppressWarnings("unchecked")
	public JSONObject generate(String jsonString) {
	
	    long startMillis=System.currentTimeMillis();
		
		JSONObject json= CommonUtils.toJson(jsonString);
		boolean drilldown= 	(boolean) json.getOrDefault("drilldown", false);
		this.configure(json);
		counter=0;
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(getRoads());
		for (int road = 1; road <= getRoads(); road++) 
		{
			AddTask task = new AddTask(road);
			logger.info("Launching add network traffic data task for road {}",task.getRoad());
			executor.execute(task);
	
		}
		
		long waitCount=0;
		while (executor.getTaskCount()!=executor.getCompletedTaskCount()) {
			waitCount++;
			if (waitCount % 100000==0) logger.info("*** Wating for tasks to complete...");
		}
		executor.shutdown();
		long endMillis=System.currentTimeMillis();
	
		
		JSONObject jsonResponse=new JSONObject();
		
		
		jsonResponse.put("message","Network traffic data generated succesfully");
		jsonResponse.put("request",json);
		jsonResponse.put("elapsedTime(millis)", endMillis-startMillis);
		
	
		jsonResponse.put("statistics", dataAnalyser.getStatistics(drilldown));
		
		logger.info("generate: response --->{}",jsonResponse);
		return jsonResponse;
	}


	public class AddTask implements Runnable {
		private int road;

		public AddTask(int road) {
			this.road = road;
		}
		public int getRoad() {
			return road;
		}
		public void run() {
			while(counter<getDocuments()) {
//				try {
					NetworkTrafficEntity networkTrafficEntity =generateNetworkTraficEntity(getRoad());
					logger.info("run::AddTask[{}] adding network traffic entity : {}" ,getRoad(),networkTrafficEntity );
					networkTrafficEntityDAO.addNetworkTrafficEntity(networkTrafficEntity);

//				} catch (Throwable e) {
//					e.printStackTrace();
//				}
			}
		}


	}

	private static  synchronized  void count() {
		counter++;
		logger.debug(" *** counter --->"+counter);
	}
/**
 * 
 * @param road
 * @return
 */
	private NetworkTrafficEntity generateNetworkTraficEntity(int road) {
		NetworkTrafficEntity networkTrafficEntity=new NetworkTrafficEntity();
		networkTrafficEntity.setRoad(road);
		networkTrafficEntity.setDay(ThreadLocalRandom.current().nextInt(1, days + 1));
		networkTrafficEntity.setType(ThreadLocalRandom.current().nextInt(1, 1000) % 2==0?Type.CALL:Type.DATA);
		networkTrafficEntity.setFailed(ThreadLocalRandom.current().nextInt(1, 97) % 11==0?true:false);
		count();
		return networkTrafficEntity;
	}



}
