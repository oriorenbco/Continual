package com.lorin.continual.utils;

import java.util.List;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lorin.continual.dao.CalibratedDataDAO;
import com.lorin.continual.dao.NetworkTrafficDAO;
import com.lorin.continual.model.CalibratedData;
import com.lorin.continual.model.Constants;
import com.lorin.continual.model.RankedData;
import com.lorin.continual.model.NetworkTrafficEntity.Type;

@Service
public class DataAnalyser {
	
	@SuppressWarnings("unused")
	private final static  Logger logger = LoggerFactory.getLogger(NetworkTrafficDataGenerator.class);


	@Autowired
	private NetworkTrafficDAO networkTrafficEntityDAO;
	
	@Autowired
	private CalibratedDataDAO calibratedDataDAO;
	
	/**
	 * Return statics
	 * If drill-down true , will drill down and return also the statistics for each road
	 * @param drilldown
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getStatistics(boolean drilldown) {
		JSONObject statistics=new JSONObject();
		for (Type type:Type.values()) {
			statistics.put(type.name().toLowerCase(), getStatistics( type, drilldown));
		}
		return statistics;
		
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getStatistics(Type type,boolean drilldown) {
		JSONObject statistics=new JSONObject();
		long failures=networkTrafficEntityDAO.getFailuresCount(type);
		long total=networkTrafficEntityDAO.getTotalCount(type);
		statistics.put(type.getFailureName(), failures);
		statistics.put(type.getTotalName(), total);
		if (drilldown) {
			List<Integer> roads=networkTrafficEntityDAO.getRoads();
			JSONObject roadStatistics=new JSONObject();
			for (int road:roads) {
				roadStatistics.put(Constants.ROAD +"["+road+"]",getStatistics( type, road));
			}
			
			statistics.put(Constants.PER_ROAD_STATISTICS,roadStatistics);
			
		}
		return statistics;
		
		}
	
	@SuppressWarnings("unchecked")
	public JSONObject getStatistics(Type type,int road) {
		JSONObject statistics=new JSONObject();
		long failures=networkTrafficEntityDAO.getFailuresCount(type,road);
		long total=networkTrafficEntityDAO.getTotalCount(type,road);
		statistics.put(type.getFailureName(), failures);
		statistics.put(type.getTotalName(), total);
		return statistics;
	}
	

	public JSONObject getStatistics() {
		return  getStatistics(false) ;
		
	}
	
	public RankedData getRankedData(int road) {
		
		CalibratedData calibratedData=calibratedDataDAO.getCalibratedData(road);
		if (calibratedData==null) throw new IllegalArgumentException("No calibraton data found for road "+road);
		RankedData rankedData=new RankedData(calibratedData);
		return rankedData;
		
	}

}
