package com.blackbeard.common.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * MT4注册用户请求dto
 * 
 * @author 刘博
 *
 */
public class Mt4RegisterDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4341645242202778566L;

	@Getter
	@Setter
	private String username;

	@Getter
	@Setter
	private String userpassword;

	@Getter
	@Setter
	private String useremail;

	@Getter
	@Setter
	private int userlever;

	@Getter
	@Setter
	private String ip;

	@Getter
	@Setter
	private int lgmanage;

	@Getter
	@Setter
	private String lgpword;

	@Getter
	@Setter
	private String mid;

	@Getter
	@Setter
	private String usergroup;

}
