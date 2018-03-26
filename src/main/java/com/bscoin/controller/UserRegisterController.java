package com.bscoin.controller;

import java.io.File;
import java.io.FileReader;
import java.math.BigInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;

import com.google.gson.Gson;
import com.vlcoin.dto.LoginDTO;
import com.vlcoin.dto.StatusResponseDTO;
import com.vlcoin.dto.UserRegisterDTO;
import com.vlcoin.service.UserRegisterService;
import com.vlcoin.utils.ViCoinUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/vicoin/api/vicoinuser")
@Api(value = "RegistrationController", description = "RegistrationController API")
@CrossOrigin
public class UserRegisterController {

	private static final Logger LOG = LoggerFactory.getLogger(UserRegisterController.class);

	EthGetBalance ethGetBalance;

	@Autowired
	private Environment env;

	@Autowired
	private ViCoinUtils viCoinUtils;

	@Autowired
	private UserRegisterService userRegisterService;

	@CrossOrigin
	@RequestMapping(value = "/userregister", method = RequestMethod.POST, produces = { "application/json" })
	@ApiOperation(value = "register account", notes = "Need to get user details and send email verification link and otp to mobile")
	public synchronized ResponseEntity<String> registerUser(

			@ApiParam(value = "Required user details", required = true) @RequestBody UserRegisterDTO userRegisterDTO) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {

			boolean isValid = viCoinUtils.validateRegistrationParam(userRegisterDTO);
			if (!isValid) {
				// User registration validation failed
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			boolean isValidEmailId = viCoinUtils.validateEmail(userRegisterDTO.getEmail());
			if (!isValidEmailId) {

				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("register.validate.email"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

			}

			boolean isConformPassword = viCoinUtils.validateConfirmPassword(userRegisterDTO);
			if (!isConformPassword) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("password.exist"));

				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			boolean isAccountExistCheckByMobileNo = userRegisterService.isMobileNoExist(userRegisterDTO.getMobile());
			if (isAccountExistCheckByMobileNo) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("mobileno.exist"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			boolean isAccountExistCheck = userRegisterService.isAccountExistCheck(userRegisterDTO);
			if (isAccountExistCheck) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage("UserName and password already eExist");
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			boolean isSponserExistCheckBySponserId = userRegisterService
					.isSponserExistCheckBySponserId(userRegisterDTO.getSponser_id());
			if (!isSponserExistCheckBySponserId) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage("Sponser is not exist");
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			boolean isAccountExistCheckByEmailId = userRegisterService
					.isAccountExistCheckByEmailId(userRegisterDTO.getEmail().toLowerCase());
			if (isAccountExistCheckByEmailId) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("email.exist"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			// String encryptedPassword =
			// EncryptDecrypt.encrypt(registerDTO.getPassword());

			// Save user registration & verification details in db
			boolean isRegister = userRegisterService.saveAccountInfo(userRegisterDTO, userRegisterDTO.getPassword());
			if (isRegister) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("register.success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			} else {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("register.failed"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("Problem in registration  : ", e);
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}

	@CrossOrigin
	@RequestMapping(value = "/verifyaccount", method = RequestMethod.POST, consumes = {
			"application/json" }, produces = { "application/json" })
	@ApiOperation(value = "login user", notes = "Email verification")
	public ResponseEntity<String> userLoginVerification(
			@ApiParam(value = "Required user mobile no and password ", required = true) @RequestBody UserRegisterDTO userRegisterDTO,
			HttpServletRequest request, HttpServletResponse response) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();

		try {

			if (!viCoinUtils.validateverifyaccount(userRegisterDTO)) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			if (!userRegisterService.isAccHolderIdExist(userRegisterDTO)){
				statusResponseDTO.setMessage("user not valid");
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage("Account verified successfully");
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("Problem in login  : ", e);
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}

	@CrossOrigin
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = { "application/json" }, produces = {
			"application/json" })
	@ApiOperation(value = "login user", notes = "Validate user and allow user to login. return auth token as response")
	public ResponseEntity<String> userLogin(
			@ApiParam(value = "Required user mobile no and password ", required = true) @RequestBody UserRegisterDTO UserRegisterDTO,
			HttpServletRequest request, HttpServletResponse response) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();

		try {

			if (!viCoinUtils.validateLoginParam(UserRegisterDTO)) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("validate.faild"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			String checkStatus = userRegisterService.isAccountHolderActiveOrNot(UserRegisterDTO);
			if (!checkStatus.equalsIgnoreCase("success")) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(checkStatus);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			LoginDTO responseDTO = userRegisterService.isUserNameAndPasswordExist(UserRegisterDTO);
			if (!responseDTO.getStatus().equalsIgnoreCase("success")) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("login.failure"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("login.success"));
			statusResponseDTO.setLoginInfo(responseDTO);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("Problem in login  : ", e);
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}

}