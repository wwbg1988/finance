package com.blackbeard.common.mapper.web;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.blackbeard.common.dto.CurrencyConfigDto;

public interface CurrencyConfigExMapper {

	public List<CurrencyConfigDto> findBy(@Param("currencyConfigDto")CurrencyConfigDto currencyConfigDto);
	
}
