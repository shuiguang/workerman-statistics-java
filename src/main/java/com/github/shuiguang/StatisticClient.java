package com.github.shuiguang;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

/**
 * Statistic统计java客户端
 * @author shuiguang
 *
 */
public class StatisticClient {
	
	// 时间缓存map
	protected static HashMap<String, HashMap<String, Double>> timeMap = new HashMap<String, HashMap<String, Double>>();
	
	/**
	 * pack编码
	 * @param type
	 * @param data
	 * @return
	 * @throws IOException
	 */
	public static byte[] pack(char type, String data) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
		DataOutputStream dos = new DataOutputStream(baos);
		if(type == 'C') {
			// unsigned char
			dos.write(data.getBytes());
		}else if(type == 'f') {
			// float
			dos.writeFloat(Float.parseFloat(data));
		}else if(type == 'N') {
			// unsigned long
			dos.writeLong(Long.parseLong(data));
		}else if(type == 'n') {
			// unsigned short
			dos.writeShort(Short.parseShort(data));
		}else if(type == 'I') {
			dos.writeInt(Integer.parseInt(data));
		}
		byte[] byteArray = baos.toByteArray();
		dos.close();
		return byteArray;
	}
	
	/**
	 * 模块接口上报消耗时间记时
	 * @param moduleName
	 * @param interfaceName
	 */
	public static void tick(String moduleName, String interfaceName) {
		if(timeMap.get(moduleName) == null) {
			HashMap<String, Double> hm1 = new HashMap<String, Double>();
			hm1.put(interfaceName, microtime(true));
			timeMap.put(moduleName, hm1);
		}else{
			timeMap.get(moduleName).put(interfaceName, microtime(true));
		}
	}
	
	/**
	 * 模块接口上报消耗时间记时
	 */
	public static void tick() {
		tick("", "");
	}
	
    /**
     * UDP上报数据
     * @param moduleName
     * @param interfaceName
     * @param success
     * @param code
     * @param msg
     * @param host
     * @param serverPort
     * @throws Exception 
     */
    public static void report(String moduleName, String interfaceName, int success, int code, String msg, String host, int serverPort) throws Exception {
    	double timeStart = 0f;
    	if(timeMap.get(moduleName) != null && timeMap.get(moduleName).get(interfaceName) != null && timeMap.get(moduleName).get(interfaceName) > 0) {
    		timeStart = timeMap.get(moduleName).get(interfaceName);
    		timeMap.get(moduleName).put(interfaceName, 0.0);
    	}else if(timeMap.get("") != null && timeMap.get("").get("") != null && timeMap.get("").get("") > 0) {
    		timeStart = timeMap.get("").get("");
    		timeMap.get("").put("", 0.0);
    	}else{
    		timeStart = microtime(true);
    	}
    	// 计算耗时
    	double costTime = microtime(true) - timeStart;
    	byte[] buffer = StatisticProtocol.encode(moduleName, interfaceName, costTime, success, code, msg);

    	sendData(host, serverPort, buffer);
    }
	
	/**
	 * UDP发送数据包
	 * @param host
	 * @param serverPort
	 * @param buffer
	 * @throws Exception
	 */
	public static void sendData(String host, int serverPort, byte[] buffer) throws Exception {

		UdpClientSocket client = new UdpClientSocket();
		client.send(host, serverPort, buffer);
		
	}
	
	/**
	 * 返回当前微浮点数
	 * @param flag
	 * @return
	 */
	private static double microtime(boolean flag) {
		double unixtime_ms = (double) new Date().getTime();
		return unixtime_ms/1000;
	}
	
}
