package com.lorin.continual.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.lorin.continual.model.CalibratedData;
import com.lorin.continual.model.NetworkTrafficEntity;

@Repository
public class CalibratedDataDAOImpl implements CalibratedDataDAO {
	
	private final static  Logger logger = LoggerFactory.getLogger(CalibratedDataDAOImpl.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public CalibratedData addCalibratedData(CalibratedData calibratedData) {
		logger.debug("Saving calibratedData...");
		mongoTemplate.save(calibratedData,getCollectionName());
		return calibratedData;
	}
	
	@Override
	public CalibratedData getCalibratedData(int road) {
	
		Query query = new Query();
		query.addCriteria(Criteria.where("road").is(road));
		logger.debug("Get calibratedData for road {}...",road);
		return mongoTemplate.findOne(query, CalibratedData.class,getCollectionName());
		
	}


	@Override
	public String getCollectionName() {
		return CalibratedData.COLLECTION_NAME;
	}

	@Override
	public void dropCollection() {
		logger.info("dropCollection {}...",getCollectionName());
		mongoTemplate.dropCollection(getCollectionName());
		
	}

}
