package com.lorin.continual.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
/**
 * The rate interval representation and logic 
 * @author lorin
 *
 */
public class RankInterval {
	private int rankNumber;
	private int lower;
	private int upper;
	public RankInterval(int rankNumber,int lower, int upper) {
		check(lower,upper);
		this.lower = lower;
		this.upper = upper;
		this.rankNumber =rankNumber;
		
	}
/**
 * Diagnose value
 * @param value
 * @return
 */
	public boolean contains(int value) {
		boolean inside=(upper==100?
				(value >=lower&&value<=upper):
					(value >=lower&&value<upper));
		return inside;

	}
	
	
	
	
	public int getRankNumber() {
		return rankNumber;
	}

	public String toString() {
		return  ReflectionToStringBuilder.toString(this,ToStringStyle.NO_CLASS_NAME_STYLE);
	}
	

	
	private static void check(int lower,int upper) {
		if (lower >=upper) {
			throw new IllegalArgumentException("Lower value cannot be greater or equal to upper value");
		}
	}


}
