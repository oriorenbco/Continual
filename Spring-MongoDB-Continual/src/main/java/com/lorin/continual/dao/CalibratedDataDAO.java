package com.lorin.continual.dao;

import com.lorin.continual.model.CalibratedData;



public interface CalibratedDataDAO {
	String getCollectionName();
	CalibratedData addCalibratedData(CalibratedData calibratedData);
	CalibratedData getCalibratedData(int road);
	void dropCollection();



	

	

}
