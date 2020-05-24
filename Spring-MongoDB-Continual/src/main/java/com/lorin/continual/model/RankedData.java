package com.lorin.continual.model;

import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the rankings for network traffic events
 * Contains the rankings as static data that can be changed or reset 
 * 
 */

public class RankedData {

	private final static  Logger logger = LoggerFactory.getLogger(RankedData.class);

	private SortedMap<Integer, RankInterval> eventRanking=new TreeMap<Integer, RankInterval>();

	private int road;

	public RankedData(CalibratedData calibratedData) {
		this.road=calibratedData.getRoad();
		this.eventRanking=computeRanking(calibratedData);

	}
	/**
	 * Compute event ranking for calibrated data for a certain road
	 * Note: Ranks are the same for event_1 and event_3 or event_2 and event_4
	 * so it's enough to compute 2 of 4 ranks 
	 * @param calibratedData
	 * @return
	 */
	private SortedMap<Integer, RankInterval> computeRanking(CalibratedData calibratedData) {
		SortedMap<?, ?> ranking=RankDefinition.getInstance().getRanking();
	
		for (int eventNumber=3;eventNumber<=4;eventNumber++) {
			int event=calibratedData.getEvent(eventNumber);
			boolean found=false;
			int rankNumber;
			RankInterval rankInterval=null;
			for (rankNumber=1;rankNumber<=ranking.keySet().size();rankNumber++) {
				 rankInterval= (RankInterval) ranking.get(rankNumber);
				logger.info("computeRanking:: eventNumber --->{} , event --->{} ,rankNumber --->{} ,rankInterval --->{}",eventNumber,event,rankNumber,rankInterval);
				if (rankInterval.contains(event) ) {
					logger.info("computeRanking:: *** found rank count {} for event number {} and {} where event value is {} and rank value {}",rankNumber,eventNumber-2,eventNumber,event,rankInterval); 
					found=true;
					break;
				}else if  (rankNumber==ranking.keySet().size()){
					found=true;
				}
			
			}
			if (found) {
				eventRanking.put(eventNumber,rankInterval);
				eventRanking.put(eventNumber-2,rankInterval);
			}
			
		}
		return eventRanking;
		
	}

	public String toString() {
		return new StringBuilder()
				.append( ReflectionToStringBuilder.toString(this,ToStringStyle.MULTI_LINE_STYLE))
				.append("\n")
				.append(RankDefinition.print(this.getEventRanking(),"Event","Rank"))
				.toString();
	}

	public SortedMap<Integer, RankInterval> getEventRanking() {
		return eventRanking;
	}

	public int getRoad() {
		return road;
	}
	
	public JSONObject toJson() {
		
		return RankDefinition.toJson(this.getEventRanking(),"Event","Rank");
	
		
	}
	



}
