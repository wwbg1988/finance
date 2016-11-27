/**
 * 
 */
package com.blackbeard.service.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.blackbeard.common.service.IKlineRecordService;
import com.blackbeard.test.BaseTestCase;

/**
 * <p>
 * Title: AreaInfoServiceTest
 * </p>
 * <p>
 * Description: 类描述
 * </p>
 * <p>
 * Copyright (c) 2016
 * </p>
 * <p>
 * Company: 上海天坊信息科技有限公司
 * </p>
 * 
 * @author刘博
 * @date 2016年6月21日 下午4:49:06
 * @version 1.0
 *          <p>
 *          修改人：刘博
 *          </p>
 *          <p>
 *          修改时间：2016年3月15日 下午4:49:06
 *          </p>
 *          <p>
 *          修改备注：
 *          </p>
 */
public class IKlineRecordServiceTest extends BaseTestCase {

	@Autowired
	private IKlineRecordService klineRecordService;
	protected static final Log logger = LogFactory
			.getLog(IKlineRecordServiceTest.class);
}
