package com.lorin.continual.dao;

import java.util.List;

import com.lorin.continual.model.NetworkTrafficEntity;
import com.lorin.continual.model.NetworkTrafficEntity.Type;



public interface NetworkTrafficDAO {
	String getCollectionName();
	List<NetworkTrafficEntity> getNetworkTrafficEntities();
	List<NetworkTrafficEntity> getNetworkTrafficEntities(String type);
	List<NetworkTrafficEntity> getNetworkTrafficEntities(String type,boolean failed);
	NetworkTrafficEntity getNetworkTrafficEntityById(String networkTrafficEntityId);
	NetworkTrafficEntity addNetworkTrafficEntity(NetworkTrafficEntity networkTrafficEntity);
	long getCount(Type type,boolean failed,int road);
	long getCount(Type type,boolean failed);
	long getFailuresCount(Type type,int road);
	long getFailuresCount(Type type);
	long getTotalCount(Type type,int road);
	long getTotalCount(Type type);
	List<Integer> getRoads();
	void dropCollection();
	

	

	

}
