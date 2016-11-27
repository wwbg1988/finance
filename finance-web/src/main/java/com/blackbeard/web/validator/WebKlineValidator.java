package com.blackbeard.web.validator;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.blackbeard.common.dto.KlineRecordParam;

public class WebKlineValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return KlineRecordParam.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		KlineRecordParam klineRecordParam = (KlineRecordParam) obj;
		if (StringUtils.isEmpty(klineRecordParam.getName())) {
			errors.rejectValue("userName", null, "userName is empty");
		}
		if (StringUtils.isEmpty(klineRecordParam.getPwd())) {
			errors.rejectValue("pwd", null, "password is empty");
		}
		if (StringUtils.isEmpty(klineRecordParam.getAll())) {
			errors.rejectValue("all", null, "all is empty");
		}
		if (klineRecordParam.getAll() == 0) {// 如果是查某种货币
			if (StringUtils.isEmpty(klineRecordParam.getCury())) {
				errors.rejectValue("cury", null, "currency is empty");
			}
		}
		if (StringUtils.isEmpty(klineRecordParam.getbTime())) {
			errors.rejectValue("bTime", null, "beginTime is empty");
		}
		if (StringUtils.isEmpty(klineRecordParam.geteTime())) {
			errors.rejectValue("eTime", null, "endTime is empty");
		}
		if (StringUtils.isEmpty(klineRecordParam.getType())) {
			errors.rejectValue("type", null, "type is empty");
		}
	}

}
