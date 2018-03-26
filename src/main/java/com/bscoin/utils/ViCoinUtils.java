package com.bscoin.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bscoin.dto.UserRegisterDTO;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@Service
public class ViCoinUtils {

	static final Logger LOG = LoggerFactory.getLogger(ViCoinUtils.class);

	static final String regex = "[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

	public boolean validateRegistrationParam(UserRegisterDTO userRegisterDTO) {

		if (userRegisterDTO.getSponser_id() != null && StringUtils.isNotBlank(userRegisterDTO.getSponser_id())
				&& userRegisterDTO.getUserName() != null && StringUtils.isNotBlank(userRegisterDTO.getUserName())
				&& userRegisterDTO.getFullName() != null && StringUtils.isNotBlank(userRegisterDTO.getFullName())
				&& userRegisterDTO.getEmail() != null && StringUtils.isNotBlank(userRegisterDTO.getEmail())
				&& userRegisterDTO.getMobile() != null && StringUtils.isNotBlank(userRegisterDTO.getMobile())
				&& userRegisterDTO.getCountry() != null && StringUtils.isNotBlank(userRegisterDTO.getCountry())
				&& userRegisterDTO.getState() != null && StringUtils.isNotBlank(userRegisterDTO.getState())
				&& userRegisterDTO.getCity() != null && StringUtils.isNotBlank(userRegisterDTO.getCity())
				&& userRegisterDTO.getPassword() != null && StringUtils.isNotBlank(userRegisterDTO.getPassword())
				&& userRegisterDTO.getConfirmPassword() != null
				&& StringUtils.isNotBlank(userRegisterDTO.getConfirmPassword())) {
			return true;
		} else {
			return false;
		}
	}

	public boolean validateEmail(String emailId) {
		emailId = emailId.replaceFirst("^ *", "");
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(emailId);
		LOG.info(emailId + " : " + matcher.matches());
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean validateConfirmPassword(UserRegisterDTO userRegisterDTO) {
		if (userRegisterDTO.getPassword().equals(userRegisterDTO.getConfirmPassword())) {
			return true;
		}
		return false;
	}

	public String getWalletAddress(String fileLocation, String fileName)
			throws FileNotFoundException, IOException, ParseException {
		try {
			fileLocation = fileLocation.replace("/", "\\");
			System.out.println("wallet created" + fileLocation);
			JSONParser parser = new JSONParser();
			// Object object = parser.parse(new
			// FileReader(WalletUtils.getMainnetKeyDirectory() + "//" +
			// fileName));
			Object object = parser.parse(new FileReader(fileLocation + "//" + fileName));
			JSONObject jsonObject = (JSONObject) object;
			String address = "0x" + (String) jsonObject.get("address");
			System.out.println("Name: " + fileName);
			return address;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	public boolean validateverifyaccount(UserRegisterDTO userRegisterDTO) {
		// TODO Auto-generated method stub
		if (userRegisterDTO.getUserName() != null && StringUtils.isNotBlank(userRegisterDTO.getUserName())
				&& userRegisterDTO.getUser_id() != null && StringUtils.isNotBlank(userRegisterDTO.getUser_id())
				&& userRegisterDTO.getPassword() != null && StringUtils.isNotBlank(userRegisterDTO.getPassword())) {
			return true;
		} else {
			return false;
		}

	}

	
	public boolean validateLoginParam(UserRegisterDTO userRegisterDTO) {
		if (userRegisterDTO.getUserName() != null && StringUtils.isNotBlank(userRegisterDTO.getPassword())) {
			return true;
		}
		return false;
	}
	
	

}