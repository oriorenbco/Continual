package com.lorin.continual.utils;

import com.lorin.continual.model.CalibratedData;
import com.lorin.continual.model.RankDefinition;
import com.lorin.continual.model.RankedData;

public class RankingUtils {

	public static void main(String[] args) {
		RankDefinition.getInstance().setRanking( new int[]{10,50,70,90});
		System.out.println(RankDefinition.print(RankDefinition.getInstance().getRanking()));
		RankDefinition.getInstance().resetRanking();
		System.out.println(RankDefinition.print(RankDefinition.getInstance().getRanking()));
		
		CalibratedData calibratedData=new CalibratedData(8,15,34);
		System.out.println(calibratedData);
		RankedData rankedData=new RankedData(calibratedData);
		
		
//		System.out.println(rankedData);

		
		System.out.println(rankedData.toJson());

	}

}
