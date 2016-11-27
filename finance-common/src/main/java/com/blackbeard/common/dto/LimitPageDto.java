package com.blackbeard.common.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class LimitPageDto implements Serializable{

	@Getter
	@Setter
	private int star;
	@Getter
	@Setter
	private int end;
	@Getter
	@Setter
	private int totalPage;
	
	
	
}
