package com.lorin.continual.model;


import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document
public class NetworkTrafficEntity {
	
	public final static String COLLECTION_NAME="NetworkTrafficData";
	

	public enum Type {
		CALL(Constants.CALLS_DROPPED,Constants.CALLS_TOTAL),
		DATA(Constants.DATA_LEAKAGE,Constants.DATA_TOTAL);


		private String failureName;
		private String totalName;

		private Type (String failureName,String totalName) {
			this.failureName=failureName;
			this.totalName = totalName;
		}

		public String getFailureName() {
			return failureName;
		}

		public String getTotalName() {
			return totalName;
		}

	
		
	}
	
	
	public final static String DEFAULT_TYPE =Type.CALL.name();
	public final static int DEFAULT_ROAD=1;
	public final static int DEFAULT_DAY=1;
	
	@Id
	private String id=UUID.randomUUID().toString();

	private String type=DEFAULT_TYPE;
	private int road   =DEFAULT_ROAD;
	private int day    =DEFAULT_DAY;

	/**
	 * If  CALL is dropped or  DATA is leakage than
	 * the failed flag will be TRUE
	 */
	private boolean failed=false;

	

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public void setType(Type type) {
		this.type = type.name();
	}
	public int getRoad() {
		return road;
	}
	public void setRoad(int road) {
		this.road = road;
	}
	public boolean isFailed() {
		return failed;
	}
	public void setFailed(boolean failed) {
		this.failed = failed;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	
	
	
	

}
