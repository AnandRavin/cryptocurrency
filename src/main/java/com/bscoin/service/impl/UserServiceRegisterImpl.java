package com.bscoin.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.text.ParseException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.WalletUtils;

import com.bscoin.dto.LoginDTO;
import com.bscoin.dto.UserIdRandomGen;
import com.bscoin.dto.UserRegisterDTO;
import com.bscoin.model.Config;
import com.bscoin.model.Register;
import com.bscoin.repo.ConfigInfoRepository;
import com.bscoin.repo.UserRegisterRepository;
import com.bscoin.service.EmailNotificationService;
import com.bscoin.service.UserRegisterService;
import com.bscoin.utils.ViCoinUtils;

@Service
public class UserRegisterServiceImpl implements UserRegisterService {

	private static final Logger LOG = LoggerFactory.getLogger(UserRegisterServiceImpl.class);

	@Autowired
	private Environment env;

	@Autowired
	private UserRegisterRepository userRegisterRepository;

	@Autowired
	private ConfigInfoRepository configInfoRepository;

	@Autowired
	private EmailNotificationService emailNotificationService;

	@Autowired
	private ViCoinUtils viCoinUtils;

	@Override
	public boolean isMobileNoExist(String mobileNo) {
		Integer accHolderInfoCount = userRegisterRepository.countRegisterByMobile(mobileNo);
		if (accHolderInfoCount > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isSponserExistCheckBySponserId(String sponserId) {
		Integer accHolderInfoCount = userRegisterRepository.countRegisterByUserId(sponserId);
		if (accHolderInfoCount > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isAccountExistCheckByEmailId(String emailId) {
		Integer isEmailExist = userRegisterRepository.countRegisterByEmailIgnoreCase(emailId.trim());
		if (isEmailExist > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isAccountExistCheck(UserRegisterDTO userRegisterDTO) {
		Integer accHolderInfoCount = userRegisterRepository
				.countRegisterByNameAndPassword(userRegisterDTO.getUserName(), userRegisterDTO.getPassword());
		if (accHolderInfoCount > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean saveAccountInfo(UserRegisterDTO userRegisterDTO, String password) throws Exception {

		Config config = configInfoRepository.findConfigByConfigKey("walletfile");
		String userId = UserIdRandomGen.generateRandomString();
		Register registerInfo = new Register();
		registerInfo.setVerify_pan(new BigInteger("1234"));
		registerInfo.setUserId(userId);
		registerInfo.setSponserid(userRegisterDTO.getSponser_id());
		registerInfo.setName(userRegisterDTO.getUserName());
		registerInfo.setEmail(userRegisterDTO.getEmail());
		registerInfo.setMobile(userRegisterDTO.getMobile());
		registerInfo.setPassword(userRegisterDTO.getPassword());
		registerInfo.setWallet_address(" ");
		registerInfo.setWallet_password(userRegisterDTO.getWalletPassword());
		registerInfo.setWalet_qr_code(" ");
		registerInfo.setRoyalty(new BigInteger("0"));
		registerInfo.setGold(new BigInteger("0"));
		registerInfo.setDiamond(new BigInteger("0"));
		registerInfo.setDefaut(0.0);
		registerInfo.setCoin(5.0);
		registerInfo.setStatus(new BigInteger("0"));
		registerInfo.setSignup_date_time(new Date());
		registerInfo.setBuy_amazing_coin(new BigInteger("1"));
		registerInfo.setPre_booking_coin(new BigInteger("1"));
		registerInfo.setPlan("pancard");
		registerInfo.setRemark("NO");
		registerInfo.setKyc_type("D.L");
		registerInfo.setKyc("IMG_20170718_091057.jpg");
		registerInfo.setVerify_kyc(new BigInteger("0"));
		registerInfo.setPan("Pan Card");
		registerInfo.setPan_id("IMG_20170610_220905.jpg");
		registerInfo.setTitle("Mr");
		registerInfo.setAddress(" ");
		registerInfo.setApprove_plan(new BigInteger("0"));
		registerInfo.setLast_login(new Date());
		registerInfo.setCountry(userRegisterDTO.getCountry());
		registerInfo.setCountryIsdCode("+91");
		registerInfo.setState(userRegisterDTO.getState());
		registerInfo.setCity(userRegisterDTO.getCity());
		registerInfo.setProfile_pic("IMG_20170718_091057.jpg");
		registerInfo.setMailveryfication_link(" ");
		registerInfo.setDelete_status(new BigInteger("0"));
		registerInfo.setVl_coin("1679.1296928328");
		registerInfo.setReferral_coin("1679.1296928328");
		registerInfo.setRoi_coin("5000");
		registerInfo.setRoyality_coin(" ");
		registerInfo.setApp_coin("5");

		userRegisterRepository.save(registerInfo);
		if (registerInfo != null) {
			boolean status = isWalletCreate(userRegisterDTO, registerInfo.getId());
			if (status) {
				/*
				 * String encryptWalletAddress =
				 * EncryptDecrypt.encrypt(registerDTO.getWalletAddress());
				 * String encryptWalletPassword =
				 * EncryptDecrypt.encrypt(registerDTO.getWalletPassword());
				 */
				String encryptWalletAddress = userRegisterDTO.getWallet_address();
				String encryptWalletPassword = userRegisterDTO.getWalletPassword();
				registerInfo.setWallet_address(encryptWalletAddress);
				registerInfo.setWallet_password(encryptWalletPassword);
				userRegisterRepository.save(registerInfo);
				String walletAddress = viCoinUtils.getWalletAddress(config.getConfigValue(),
						userRegisterDTO.getWallet_address());

				String verificationLink = env.getProperty("accholderotpverification.url") + registerInfo.getName()
						+ registerInfo.getUserId() + registerInfo.getPassword();

				// Send verification link to user email Id - With subject &
				// content
				boolean isEmailSent = emailNotificationService.sendEmail(userRegisterDTO.getEmail(),
						"WELCOME TO THE VICOIN WALLET !", walletAddress, verificationLink);

				return true;

			}
			userRegisterRepository.delete(registerInfo.getId());
			return false;
		}
		return false;
	}

	public boolean isWalletCreate(UserRegisterDTO userRegisterDTO, Integer Id) {
		try {
			Config configInfo = configInfoRepository.findConfigByConfigKey("walletfile");
			if (configInfo != null) {
				String walletFileLocation = configInfo.getConfigValue();

				walletFileLocation = walletFileLocation.replace("/", "\\");
				File createfolder = new File(walletFileLocation);
				if (!createfolder.exists()) {
					createfolder.mkdirs();
				}

				System.out.println("walletFileLocation " + walletFileLocation);

				String fileName = null;

				fileName = WalletUtils.generateNewWalletFile(userRegisterDTO.getWalletPassword(),
						new File(walletFileLocation), false);

				userRegisterDTO.setWallet_address(fileName);

				/*
				 * String walletAddress =
				 * equocoinUtils.getWalletAddress(walletFileLocation, fileName);
				 * if (walletAddress != null) {
				 * registerDTO.setWalletAddress(walletAddress); } else { return
				 * false; }
				 */

				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return false;
	}

	/*
	 * @Override public boolean isEmailIdExist(UserRegisterDTO userRegisterDTO)
	 * throws Exception {
	 * 
	 * Register viCoinUserInfos =
	 * userRegisterRepository.findRegisterByEmail(userRegisterDTO.getEmail().
	 * trim()); if (viCoinUserInfos != null) { // String decryptPassword = //
	 * EncryptDecrypt.decrypt(viCoinUserInfos.getPassword());
	 * viCoinUserInfos.setPassword(viCoinUserInfos.getPassword()); boolean
	 * isEmailSent =
	 * emailNotificationService.sendEmailforgot(viCoinUserInfos.getEmail(),
	 * "Password info from EQC", viCoinUserInfos.getPassword());
	 * 
	 * return true; }
	 * 
	 * return false; }
	 */
	@Override
	public boolean isAccHolderIdExist(UserRegisterDTO userRegisterDTO) {
		Register registerInfo = userRegisterRepository.findRegisterByUserIdAndNameAndPassword(
				userRegisterDTO.getUser_id(), userRegisterDTO.getUserName(), userRegisterDTO.getPassword());
		if (registerInfo != null) {

			registerInfo.setStatus(new BigInteger("1"));
			userRegisterRepository.save(registerInfo);
			return true;

		}
		return false;
	}

	@Override
	public LoginDTO isUserNameAndPasswordExist(UserRegisterDTO userRegisterDTO)
			throws FileNotFoundException, IOException, ParseException {
		LoginDTO loginDTO = new LoginDTO();
		Register registerInfo = userRegisterRepository.findRegisterByNameAndPassword(userRegisterDTO.getUserName(),
				userRegisterDTO.getPassword());
		Config config = configInfoRepository.findConfigByConfigKey("walletfile");
		if (registerInfo != null && config != null) {
			loginDTO.setId(registerInfo.getId().toString());
			loginDTO.setUserId(registerInfo.getUserId());
			loginDTO.setUserName(registerInfo.getName());
			loginDTO.setMobileNo(registerInfo.getMobile());
			loginDTO.setEmailId(registerInfo.getEmail());
			loginDTO.setIsdCode(registerInfo.getCountryIsdCode());
			loginDTO.setIsActive(registerInfo.getStatus().toString());
			loginDTO.setStatus("success");

			String walletAddress = viCoinUtils.getWalletAddress(config.getConfigValue(),
					registerInfo.getWallet_address());
			if (walletAddress != null) {
				LOG.info("walletAddress " + walletAddress);
				loginDTO.setWalletAddress(walletAddress);
			}

			return loginDTO;

		}
		return loginDTO;
	}

	@Override
	public String isAccountHolderActiveOrNot(UserRegisterDTO userRegisterDTO) {
		LoginDTO loginDTO = new LoginDTO();
		Register registerInfo = userRegisterRepository.findRegisterByNameAndPassword(userRegisterDTO.getUserName(),
				userRegisterDTO.getPassword());
		if (registerInfo != null) {
			BigInteger status = new BigInteger("1");
			int checkStatus = registerInfo.getStatus().compareTo(status);
			if (checkStatus == 0) {
				return "success";
			} else {
				return "Admin cannot active on you please wait";
			}

		}
		return "User not exists";
	}

}