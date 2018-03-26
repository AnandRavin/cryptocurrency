package com.bscoin.repo;

import org.springframework.data.repository.CrudRepository;

import com.bscoin.model.Config;

public interface ConfigInfoRepository extends CrudRepository<Config, Integer> {

	public Config findConfigByConfigKey(String configKey);

}