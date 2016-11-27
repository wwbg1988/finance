package com.blackbeard.web.validator;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.blackbeard.common.dto.OrderParam;

/**
 * web 历史订单验证器
 * @author 刘博
 *
 */
public class WebOrderValidator implements Validator {
	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return OrderParam.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		OrderParam orderParam = (OrderParam) obj;
		if (StringUtils.isEmpty(orderParam.getName())) {
			errors.rejectValue("name", null, "userName is empty");
		}
		if (StringUtils.isEmpty(orderParam.getbTime())) {
			errors.rejectValue("bTime", null, "beginTime is empty");
		}
		if (StringUtils.isEmpty(orderParam.geteTime())) {
			errors.rejectValue("eTime", null, "endTime is empty");
		}
		
	}

}
