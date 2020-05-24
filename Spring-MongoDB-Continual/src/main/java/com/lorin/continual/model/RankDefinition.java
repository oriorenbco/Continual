package com.lorin.continual.model;

import java.util.Arrays;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.ArrayUtils;
import org.json.simple.JSONObject;

import com.lorin.continual.utils.CommonUtils;

/**
 * Holds the ranking definition
 * Can be changed and reset to default values
 * Ranking rules:  lowest (failure) value , highest rank
 * Highest the rank , lowest the rank number
 * E.g: prize one is the highest on the podium and has the lowest number
 * @author lorin
 *
 */

public class RankDefinition {
	
	private static final RankDefinition _instance=new RankDefinition();

	private static final String ENCLOSE_START = "[";
	private static final String ENCLOSE_END = "]";

	
	/**
	 * Default ranking steps
	 * Interpretation
	 * Rank 1 : values [0,80]
	 * Rank 2 : values (80,90]
	 * Rank 3 : values (90,95]
	 * Rank 4 : values (95,100]
	 * 
	 */
	private static int[] DEFAULT_RANKING_STEPS= {80,90,95};
	
	private static SortedMap<Integer, RankInterval> ranking=toRanking(DEFAULT_RANKING_STEPS);
	
	private RankDefinition() {
		
	}
	
	public static RankDefinition getInstance() {
		return _instance;
	}
	
	private static SortedMap<Integer, RankInterval> toRanking(int[] rankingValues) {
		SortedMap<Integer, RankInterval> ranking=new TreeMap<Integer, RankInterval>();
		Arrays.sort(rankingValues);
		ArrayUtils.reverse(rankingValues);
		rankingValues=checkExtremities (rankingValues);
		int rankNumber=1;
	
		for (int i=0;i<rankingValues.length-1;i++) {
			
			int upper=rankingValues[i];
			int lower=rankingValues[i+1];
			ranking.put(rankNumber, new RankInterval(rankNumber,lower,upper));
			rankNumber++;
		}
	
		return ranking;
	}
	
	/**
	 * Complete values 0 and 100 if not present
	 * @param rankingValues
	 * @return
	 */
	private static int[] checkExtremities (int[] rankingValues) {
		if (rankingValues[0] !=100) {
			rankingValues=ArrayUtils.insert(0, rankingValues, 100);
		}
		if (rankingValues[rankingValues.length-1] !=0) {
			rankingValues=ArrayUtils.insert(rankingValues.length, rankingValues, 0);
		}
		return rankingValues;
		
	}
	
	public void setRanking(String jsonString) {
		JSONObject json=CommonUtils.toJson(jsonString);
		String[] tokens = ((String) json.get("ranks")).trim().split(",");
		int[]  rankingArray=new int[tokens.length];
		int index=0;
		for (String token:tokens) {
			rankingArray[index++]=Integer.parseInt(token);
		}
		setRanking(rankingArray);
		
	}
	
	public  void setRanking(int[] rankingArray) {
		ranking=toRanking(rankingArray);
	}
	/**
	 * Reset ranking to default values
	 */
	public void resetRanking() {
		ranking=toRanking(DEFAULT_RANKING_STEPS);
	}
	
	public int getRanksNumber(){
		return ranking.keySet().size();
	}
	
	public  SortedMap<Integer, RankInterval> getRanking() {
		return ranking;
	}

	private static String enclose(Object o) {
		StringBuilder sb=new StringBuilder();
		return sb.append(ENCLOSE_START).append(o).append(ENCLOSE_END).toString();
	}
	
	public static String print(SortedMap<Integer, RankInterval> sortedMap,String keyDescription,String valueDescription) {
		StringBuilder sb=new StringBuilder();
		for(Iterator<Integer> iterator=sortedMap.keySet().iterator();iterator.hasNext();) {
			Integer key= iterator.next();
			RankInterval value=sortedMap.get(key);
			sb.append(keyDescription).append(enclose(key)).append("--->").append(valueDescription).append(enclose(value));
			sb.append("\n");
		}
		return sb.toString();
	}
	
	

	public static String print(SortedMap<Integer, RankInterval> ranking) {
		return print( ranking,"rank","score");
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject toJson(SortedMap<Integer, RankInterval> sortedMap,String keyDescription,String valueDescription) {
		JSONObject json=new JSONObject();
		StringBuilder sb=new StringBuilder();
		for(Iterator<Integer> iterator=sortedMap.keySet().iterator();iterator.hasNext();) {
			Integer key= iterator.next();
			RankInterval value=sortedMap.get(key);
			json.put(keyDescription+enclose(key),valueDescription+enclose(value));
			
		}
		return json;
		
	}
	
	public static  JSONObject toJson(SortedMap<Integer, RankInterval> ranking) {
		return toJson( ranking,"rank","");
	}
	
	
	

}
