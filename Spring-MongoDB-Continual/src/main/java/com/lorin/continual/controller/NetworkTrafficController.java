package com.lorin.continual.controller;

import java.util.List;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lorin.continual.dao.CalibratedDataDAO;
import com.lorin.continual.dao.NetworkTrafficDAO;
import com.lorin.continual.model.CalibratedData;
import com.lorin.continual.model.Constants;
import com.lorin.continual.model.NetworkTrafficEntity;
import com.lorin.continual.model.RankDefinition;
import com.lorin.continual.model.RankedData;
import com.lorin.continual.utils.DataAnalyser;
import com.lorin.continual.utils.DataCalibrator;
import com.lorin.continual.utils.NetworkTrafficDataGenerator;


@RestController
@RequestMapping(value = "/")
public class NetworkTrafficController {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private NetworkTrafficDAO networkTrafficEntityDAO;
	@Autowired
	private CalibratedDataDAO calibratedDataDAO;
	@Autowired
	private NetworkTrafficDataGenerator networkTrafficDataGenerator;
	@Autowired
	private DataCalibrator dataCalibrator;
	@Autowired
	private DataAnalyser dataAnalyser;


	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public NetworkTrafficEntity addNetworkTrafficEntity(@RequestBody NetworkTrafficEntity networkTrafficEntity) {
		return networkTrafficEntityDAO.addNetworkTrafficEntity(networkTrafficEntity);
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<NetworkTrafficEntity> getNetworkTrafficEntities() {
		return networkTrafficEntityDAO.getNetworkTrafficEntities();
	}


	@RequestMapping(value = "/generate", method = RequestMethod.POST)
	public JSONObject  generateNetworkTrafficData(@RequestBody String jsonString) {
		return networkTrafficDataGenerator.generate(jsonString);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/clear", method = RequestMethod.POST)
	public JSONObject  clearNetworkTrafficData() {
		JSONObject json=new JSONObject();
		networkTrafficEntityDAO.dropCollection();
		json.put(Constants.SUCCESS+" [step 1]",NetworkTrafficEntity.COLLECTION_NAME +" dropped!");
		calibratedDataDAO.dropCollection();
		json.put(Constants.SUCCESS +" [step 2]",CalibratedData.COLLECTION_NAME +" dropped!");

		return json;
	}


	@RequestMapping(value = "/calibrate", method = RequestMethod.POST)
	public JSONObject  calibrateNetworkTrafficData() {
		return dataCalibrator.calibrateNetworkTrafficData();
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/setRankDefinition", method = RequestMethod.POST)
	public JSONObject  setRanking(@RequestBody String jsonString) {
		JSONObject json=new JSONObject();
		 RankDefinition.getInstance().setRanking(jsonString);
		 json.put("Ranks",RankDefinition.toJson(RankDefinition.getInstance().getRanking()));
		 return json;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getRankDefinition", method = RequestMethod.POST)
	public JSONObject  getRanking() {
		JSONObject json=new JSONObject();
		json.put("Ranks",RankDefinition.toJson(RankDefinition.getInstance().getRanking()));
		return json;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/resetRankDefinition", method = RequestMethod.POST)
	public JSONObject  resetRanking() {
		JSONObject json=new JSONObject();
		RankDefinition.getInstance().resetRanking();
		json.put("Ranks",RankDefinition.toJson(RankDefinition.getInstance().getRanking()));
		return json;
	}
	
	@RequestMapping(value = "/rankRoad/{road}", method = RequestMethod.GET)
	public RankedData getRoadRank(@PathVariable int road) {
		return dataAnalyser.getRankedData(road);
	}
	
	@RequestMapping(value = "/calibratedDataForRoad/{road}", method = RequestMethod.GET)
	public CalibratedData getCalibratedDataForRoad(@PathVariable int road) {
		return calibratedDataDAO.getCalibratedData(road);
	}







}