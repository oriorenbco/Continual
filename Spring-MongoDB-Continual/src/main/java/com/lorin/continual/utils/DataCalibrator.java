package com.lorin.continual.utils;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lorin.continual.dao.CalibratedDataDAO;
import com.lorin.continual.dao.NetworkTrafficDAO;
import com.lorin.continual.model.CalibratedData;
import com.lorin.continual.model.Constants;
import com.lorin.continual.model.NetworkTrafficEntity.Type;

@Service
public class DataCalibrator {
	@Autowired
	private NetworkTrafficDAO networkTrafficEntityDAO;
	
	@Autowired
	private CalibratedDataDAO calibratedDataDAO;
	
	@SuppressWarnings("unchecked")
	public JSONObject  calibrateNetworkTrafficData() {
		JSONObject json=new JSONObject();
		
		List<Integer> roads=networkTrafficEntityDAO.getRoads();
		for (int road:roads) {
			json.put(Constants.ROAD+"["+road+"]",calibratedDataDAO.addCalibratedData(getCalibratedData(road)));
		}
		return json;
	}
	
	private int percentFailure(Type type,int road) {
		long failures=networkTrafficEntityDAO.getFailuresCount(type,road);
		long total=networkTrafficEntityDAO.getTotalCount(type,road);
		return (int)((failures*100)/total);
		
	}
	
	public CalibratedData getCalibratedData(int road) {

		return new CalibratedData(
				 road,
				 percentFailure(Type.CALL,road), 
				 percentFailure(Type.DATA,road)
					);
		
	}
	
	

}
