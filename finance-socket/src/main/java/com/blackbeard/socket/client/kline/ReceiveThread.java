package com.blackbeard.socket.client.kline;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Socket;
import java.nio.CharBuffer;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.blackbeard.common.constant.KlineConstants;
import com.blackbeard.common.dto.CurrencyConfigDto;
import com.blackbeard.common.service.ICurrencyConfigService;
import com.blackbeard.common.util.SocketUtils;
import com.blackbeard.socket.service.BaseThread;
import com.blackbeard.socket.service.KlineHandleService;
import com.blackbeard.socket.vo.ThreadHandleVO;

@Component
public class ReceiveThread extends BaseThread {
	protected static final Log logger = LogFactory.getLog(ReceiveThread.class);
	private Socket socket;

	public ReceiveThread() {

	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			Reader reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream(), "utf-8"));
			CharBuffer charBuffer = CharBuffer.allocate(8192);
			ThreadHandleVO vo = new ThreadHandleVO();
			//查询出显示几种货币
			getListCurrency();
			while (reader.read(charBuffer) != -1) {
				charBuffer.flip();
				// 处理黏包 currencyString为处理后的货币字符串集合
				List<String> currencyList = KlineHandleService.handleStickyPackage(charBuffer.toString());
				if (!CollectionUtils.isEmpty(currencyList)) {
					for (String klineData : currencyList) {
						vo.setMessage(klineData);
						vo.setCurrentTime(System.currentTimeMillis()); // 发送数据到队列
						KlineHandleService.pushDataToMQ(vo);
					}
				}
			}
		} catch (Exception e) {
			e.getStackTrace();
			System.out.println("--ReceiveThread包错:---" + e.getMessage());
			if (!socket.isClosed()) {
				try {
					socket.close();
					// 重连
					reconnect();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}

	}

	public void reconnect() {
		while (true) {
			try {
				socket = new Socket(KlineConstants.MP4_KLINE_ADDRESS,
						KlineConstants.MP4_KLINE_PORT);
				socket.setSoTimeout(9000);
				new SendThread(socket).start();
				ReceiveThread receiveThread = new ReceiveThread();
				receiveThread.setSocket(socket);
				receiveThread.start();
				if (socket != null && !socket.isClosed()) {
					break;
				}
			} catch (Exception e) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					e.printStackTrace();
				}
				e.printStackTrace();
			}
		}
	}

	public void getListCurrency(){
		//查询出显示几种货币
		ICurrencyConfigService currencyConfigService =  SocketUtils.getCurrencyConfigService();
		CurrencyConfigDto currencyConfigDto = new CurrencyConfigDto();
		currencyConfigDto.setIsEnable(1);
		KlineConstants.LIST_CURRENCY = currencyConfigService.findBy(currencyConfigDto);
	}
	
}
