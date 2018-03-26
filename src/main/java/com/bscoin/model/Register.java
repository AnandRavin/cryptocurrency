package com.bscoin.model;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "Register")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Register {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "verify_pan")
	@NotNull
	private BigInteger verify_pan;

	@Column(name = "user_id")
	@NotNull
	private String userId;

	@Column(name = "sponser_id")
	@NotNull
	private String sponserid;

	@Column(name = "name")
	@NotNull
	private String name;

	@Column(name = "email")
	@NotNull
	private String email;

	@Column(name = "mobile")
	@NotNull
	private String mobile;

	@Column(name = "password")
	@NotNull
	private String password;

	@Column(name = "wallet_address")
	@NotNull
	private String wallet_address;

	@Column(name = "wallet_password")
	@NotNull
	private String wallet_password;

	@Column(name = "walet_qr_code")
	@NotNull
	private String walet_qr_code;

	@Column(name = "royalty")
	@NotNull
	private BigInteger royalty;

	@Column(name = "gold")
	@NotNull
	private BigInteger gold;

	@Column(name = "diamond")
	@NotNull
	private BigInteger diamond;

	@Column(name = "defaut")
	@NotNull
	private Double defaut;

	@Column(name = "coin")
	@NotNull
	private Double coin;

	@Column(name = "status")
	@NotNull
	private BigInteger status;

	@Column(name = "signup_date_time")
	@NotNull
	private Date signup_date_time;

	@Column(name = "buy_amazing_coin")
	@NotNull
	private BigInteger buy_amazing_coin;

	@Column(name = "pre_booking_coin")
	@NotNull
	private BigInteger pre_booking_coin;

	@Column(name = "plan")
	@NotNull
	private String plan;

	@Column(name = "remark")
	@NotNull
	private String remark;

	@Column(name = "kyc_type")
	@NotNull
	private String kyc_type;

	@Column(name = "kyc")
	@NotNull
	private String kyc;

	@Column(name = "verify_kyc")
	@NotNull
	private BigInteger verify_kyc;

	@Column(name = "pan")
	@NotNull
	private String pan;

	@Column(name = "pan_id")
	@NotNull
	private String pan_id;

	@Column(name = "title")
	@NotNull
	private String title;

	@Column(name = "address")
	@NotNull
	private String address;

	@Column(name = "approve_plan")
	@NotNull
	private BigInteger approve_plan;

	@Column(name = "last_login")
	@NotNull
	private Date last_login;

	@Column(name = "country")
	@NotNull
	private String country;

	@Column(name = "country_isdCode")
	@NotNull
	private String countryIsdCode;

	@Column(name = "state")
	@NotNull
	private String state;

	@Column(name = "city")
	@NotNull
	private String city;

	@Column(name = "profile_pic")
	@NotNull
	private String profile_pic;

	@Column(name = "mailveryfication_link")
	@NotNull
	private String mailveryfication_link;

	@Column(name = "delete_status")
	@NotNull
	private BigInteger delete_status;

	@Column(name = "vl_coin")
	@NotNull
	private String vl_coin;

	@Column(name = "referral_coin")
	@NotNull
	private String referral_coin;

	@Column(name = "roi_coin")
	@NotNull
	private String roi_coin;

	@Column(name = "royality_coin")
	@NotNull
	private String royality_coin;

	@Column(name = "app_coin")
	private String app_coin;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigInteger getVerify_pan() {
		return verify_pan;
	}

	public void setVerify_pan(BigInteger verify_pan) {
		this.verify_pan = verify_pan;
	}

	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getSponserid() {
		return sponserid;
	}

	public void setSponserid(String sponserid) {
		this.sponserid = sponserid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getWallet_address() {
		return wallet_address;
	}

	public void setWallet_address(String wallet_address) {
		this.wallet_address = wallet_address;
	}

	public String getWalet_qr_code() {
		return walet_qr_code;
	}

	public void setWalet_qr_code(String walet_qr_code) {
		this.walet_qr_code = walet_qr_code;
	}

	public BigInteger getRoyalty() {
		return royalty;
	}

	public void setRoyalty(BigInteger royalty) {
		this.royalty = royalty;
	}

	public BigInteger getGold() {
		return gold;
	}

	public void setGold(BigInteger gold) {
		this.gold = gold;
	}

	public BigInteger getDiamond() {
		return diamond;
	}

	public void setDiamond(BigInteger diamond) {
		this.diamond = diamond;
	}

	public Double getDefaut() {
		return defaut;
	}

	public void setDefaut(Double defaut) {
		this.defaut = defaut;
	}

	public Double getCoin() {
		return coin;
	}

	public void setCoin(Double coin) {
		this.coin = coin;
	}

	public BigInteger getStatus() {
		return status;
	}

	public void setStatus(BigInteger status) {
		this.status = status;
	}

	public Date getSignup_date_time() {
		return signup_date_time;
	}

	public void setSignup_date_time(Date signup_date_time) {
		this.signup_date_time = signup_date_time;
	}

	public BigInteger getBuy_amazing_coin() {
		return buy_amazing_coin;
	}

	public void setBuy_amazing_coin(BigInteger buy_amazing_coin) {
		this.buy_amazing_coin = buy_amazing_coin;
	}

	public BigInteger getPre_booking_coin() {
		return pre_booking_coin;
	}

	public void setPre_booking_coin(BigInteger pre_booking_coin) {
		this.pre_booking_coin = pre_booking_coin;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getKyc_type() {
		return kyc_type;
	}

	public void setKyc_type(String kyc_type) {
		this.kyc_type = kyc_type;
	}

	public BigInteger getVerify_kyc() {
		return verify_kyc;
	}

	public void setVerify_kyc(BigInteger verify_kyc) {
		this.verify_kyc = verify_kyc;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public BigInteger getApprove_plan() {
		return approve_plan;
	}

	public void setApprove_plan(BigInteger approve_plan) {
		this.approve_plan = approve_plan;
	}

	public Date getLast_login() {
		return last_login;
	}

	public void setLast_login(Date last_login) {
		this.last_login = last_login;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public BigInteger getDelete_status() {
		return delete_status;
	}

	public void setDelete_status(BigInteger delete_status) {
		this.delete_status = delete_status;
	}

	public String getVl_coin() {
		return vl_coin;
	}

	public void setVl_coin(String vl_coin) {
		this.vl_coin = vl_coin;
	}

	public String getReferral_coin() {
		return referral_coin;
	}

	public void setReferral_coin(String referral_coin) {
		this.referral_coin = referral_coin;
	}

	public String getRoi_coin() {
		return roi_coin;
	}

	public void setRoi_coin(String roi_coin) {
		this.roi_coin = roi_coin;
	}

	public String getRoyality_coin() {
		return royality_coin;
	}

	public void setRoyality_coin(String royality_coin) {
		this.royality_coin = royality_coin;
	}

	public String getApp_coin() {
		return app_coin;
	}

	public void setApp_coin(String app_coin) {
		this.app_coin = app_coin;
	}

	public String getKyc() {
		return kyc;
	}

	public void setKyc(String kyc) {
		this.kyc = kyc;
	}

	public String getPan_id() {
		return pan_id;
	}

	public void setPan_id(String pan_id) {
		this.pan_id = pan_id;
	}

	public String getProfile_pic() {
		return profile_pic;
	}

	public void setProfile_pic(String profile_pic) {
		this.profile_pic = profile_pic;
	}

	public String getMailveryfication_link() {
		return mailveryfication_link;
	}

	public void setMailveryfication_link(String mailveryfication_link) {
		this.mailveryfication_link = mailveryfication_link;
	}

	public String getWallet_password() {
		return wallet_password;
	}

	public void setWallet_password(String wallet_password) {
		this.wallet_password = wallet_password;
	}

	public String getCountryIsdCode() {
		return countryIsdCode;
	}

	public void setCountryIsdCode(String countryIsdCode) {
		this.countryIsdCode = countryIsdCode;
	}

}