package com.bscoin.repo;

import org.springframework.data.repository.CrudRepository;

import com.bscoin.model.Register;

public interface UserRegisterRepository extends CrudRepository<Register, Integer> {

	public Integer countRegisterByMobile(String mobileNo);

	//public Integer countRegisterBySponserid(String sponserId);

	public Integer countRegisterByEmailIgnoreCase(String trim);

	public Register findRegisterByEmail(String trim);

	public Integer countRegisterByNameAndPassword(String userName, String password);

	public Register findRegisterByUserId(String userId);

	public Register findRegisterByNameAndPassword(String userName, String password);

	public Integer countRegisterByUserId(String sponserId);

	public Register findRegisterByUserIdAndNameAndPassword(String user_id, String userName, String password);

}