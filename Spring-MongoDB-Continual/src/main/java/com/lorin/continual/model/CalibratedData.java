package com.lorin.continual.model;

import java.util.UUID;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

public class CalibratedData {
	
	public final static String COLLECTION_NAME="CalibratedData";

	public enum Event {
		CALL_DROP("callDropPercent","EVENT_1"),
		DATA_LEAKAGE("dataLeakagePercent","EVENT_2"),
		CALL_QUALITY("callQuality","EVENT_3"),
		DATA_QUALITY("dataQuality","EVENT_4");

		private String field;
		private String type;
		private Event (String field,String type) {
			this.field = field;
			this.type = type;
		}
		public String getField() {
			return field;
		}
	
		public String getType() {
			return type;
		}
	
		
	}
		
		
		@Id
		private String id=UUID.randomUUID().toString();
		private int road;
		/* 
		 * All the below fields (EVENT_1 to 4) are either percents or scores 
		 * with integer values between 1 and 100 ,inclusive
		 */
		
	
		//callDropPercent
		private int EVENT_1;
		//dataLeakagePercent
		private int EVENT_2;
		//callQualityScore
		private int EVENT_3;
		//dataQualityScore
		private int EVENT_4;
		
		
		/*for iteration purposes*/
		@Transient
		private transient  int[] events;
		
		
		
		public CalibratedData(int road,int EVENT_1, int EVENT_2) {
		
			this.road = road;
			this.EVENT_1 = EVENT_1;
			this.EVENT_2 = EVENT_2;
			this.EVENT_3 = computeQualityScore(EVENT_1);
			this.EVENT_4 = computeQualityScore(EVENT_2);
			events=new int[] {EVENT_1,EVENT_2,EVENT_3,EVENT_4};
			
			
		}
		public String getId() {
			return id;
		}
	
	
		public int getEVENT_1() {
			return EVENT_1;
		}
	
		public int getEVENT_2() {
			return EVENT_2;
		}
	
		public int getEVENT_3() {
			return EVENT_3;
		}
	
		public int getEVENT_4() {
			return EVENT_4;
		}
	
		public int getRoad() {
			return road;
		}
		
		public int getEvent(int eventNumber) {
			if (eventNumber <1||eventNumber >4) {
				throw new IllegalArgumentException("Permitted values are 1,2,3,4 ...");
			}
			return events[eventNumber-1];
		}

		
		private int computeQualityScore(int failurePercent) {
			return (100 -failurePercent);
			
		}
		@Transient
		public int[] getEvents() {
			return events;
		}
		
		public String toString() {
			return   ReflectionToStringBuilder.toString(this,ToStringStyle.NO_CLASS_NAME_STYLE,false);
		}
		
		
		
	
	
}
