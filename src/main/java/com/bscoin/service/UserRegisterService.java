package com.bscoin.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import org.springframework.stereotype.Service;

import com.bscoin.dto.LoginDTO;
import com.bscoin.dto.UserRegisterDTO;

@Service
public interface UserRegisterService {

	public boolean isMobileNoExist(String mobileNo);

	public boolean isSponserExistCheckBySponserId(String sponser_id);

	public boolean isAccountExistCheckByEmailId(String emailId);

	public boolean saveAccountInfo(UserRegisterDTO userRegisterDTO, String password) throws Exception;

	// public boolean isEmailIdExist(UserRegisterDTO userRegisterDTO) throws
	// Exception;

	public boolean isAccountExistCheck(UserRegisterDTO userRegisterDTO);

	// public boolean isAccHolderIdExist(String user_id);

	public LoginDTO isUserNameAndPasswordExist(UserRegisterDTO userRegisterDTO)
			throws FileNotFoundException, IOException, ParseException;

	public String isAccountHolderActiveOrNot(UserRegisterDTO userRegisterDTO);

	boolean isAccHolderIdExist(UserRegisterDTO userRegisterDTO);

}