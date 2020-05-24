package com.lorin.continual.dao;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.lorin.continual.model.Constants;
import com.lorin.continual.model.NetworkTrafficEntity;
import com.lorin.continual.model.NetworkTrafficEntity.Type;


@Repository
public class NetworkTrafficDAOImpl implements NetworkTrafficDAO {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public String getCollectionName() {
		return NetworkTrafficEntity.COLLECTION_NAME;
	}

	@Override
	public List<NetworkTrafficEntity> getNetworkTrafficEntities() {
		logger.info("Getting all networkTrafficEntities.");
		return mongoTemplate.findAll(NetworkTrafficEntity.class,getCollectionName());
	}
	
	@Override
	public List<NetworkTrafficEntity> getNetworkTrafficEntities(String type) {
		Query query = new Query(); 
		query.addCriteria(Criteria.where(Constants.TYPE).is(type));
		List<NetworkTrafficEntity> list = mongoTemplate.find(query, NetworkTrafficEntity.class,getCollectionName());
		return list;
	}

	@Override
	public NetworkTrafficEntity getNetworkTrafficEntityById(String networkTrafficEntityId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("timestamp").is(networkTrafficEntityId));
		logger.info("Getting networkTrafficEntity with ID: {}.", networkTrafficEntityId);
		return mongoTemplate.findOne(query, NetworkTrafficEntity.class,getCollectionName());
	}

	@Override
	public NetworkTrafficEntity addNetworkTrafficEntity(NetworkTrafficEntity networkTrafficEntity) {
		logger.debug("Saving networkTrafficEntity...");
		mongoTemplate.save(networkTrafficEntity,getCollectionName());
		return networkTrafficEntity;
	}

	@Override
	public List<NetworkTrafficEntity> getNetworkTrafficEntities(String type, boolean failed) {
		Query query = new Query(); 
		query.addCriteria(Criteria.where(Constants.TYPE).is(type).and(Constants.FAILED).is(false));
		List<NetworkTrafficEntity> list = mongoTemplate.find(query, NetworkTrafficEntity.class,getCollectionName());
		return list;
	}
	
	public long getCount(Type type,boolean failed) {
	return getCount( type, failed,0);
		
	}



	@Override
	public long getFailuresCount(Type type) {
		return getFailuresCount( type,0);
	}

	@Override
	public long getTotalCount(Type type) {
		return getTotalCount( type, 0);
	}

	@Override
	public long getCount(Type type, boolean failed, int road) {
		Query query = new Query();
		Criteria criteria=(road==0?Criteria.where(Constants.TYPE).is(type.name())
				  				 .andOperator(Criteria.where(Constants.FAILED).is(failed)):
								Criteria.where(Constants.TYPE).is(type.name())
								  .andOperator(
										  Criteria.where(Constants.FAILED).is(failed),
										  Criteria.where(Constants.ROAD).is(road))
								  );
		query.addCriteria(criteria);
		logger.debug("getCount: query criteria --->{}",
				ToStringBuilder.reflectionToString(criteria,ToStringStyle.MULTI_LINE_STYLE)
			);
		return mongoTemplate.count(query, getCollectionName());
	}

	@Override
	public long getFailuresCount(Type type, int road) {
		return getCount( type,true,road);
	}

	@Override
	public long getTotalCount(Type type, int road) {
		return getCount(type,false,road) +getFailuresCount(type,road);
	
	}
	
	@SuppressWarnings("unchecked")
	public List<Integer> getRoads() {
		List<Integer> roads = mongoTemplate.getCollection(getCollectionName()).distinct(Constants.ROAD);
	    return roads;
	}

	@Override
	public void dropCollection() {
		logger.info("dropCollection {}...",getCollectionName());
		mongoTemplate.dropCollection(getCollectionName());
		
	}

	

}
