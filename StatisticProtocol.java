import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Statistic编码协议
 * struct statisticPortocol
 * {
 *     unsigned char moduleNameLen;
 *     unsigned char interfaceNameLen;
 *     float costTime;
 *     unsigned char success;
 *     int code;
 *     unsigned short msgLen;
 *     unsigned int time;
 *     char[moduleNameLen] moduleName;
 *     char[interfaceName_len] interfaceName;
 *     char[msgLen] msg;
 * }
 *
 * @author shuiguang
*/
public class StatisticProtocol {

	// 包头长度
	public static int PACKAGE_FIXED_LENGTH = 17;
	
	// udp 包最大长度
	public static int MAX_UDP_PACKGE_SIZE = 65507;

	// char类型能保存的最大数值
	public static int MAX_CHAR_VALUE = 255;

	// usigned short 能保存的最大数值
	public static int MAX_UNSIGNED_SHORT_VALUE = 65535;
	
	/**
	 * 编码
	 * @param moduleName
	 * @param interfaceName
	 * @param costTime
	 * @param success
	 * @param code
	 * @param msg
	 * @return
	 * @throws IOException
	 */
	public static byte[] encode(String moduleName, String interfaceName, double costTime, int success, int code, String msg) throws IOException {
		
		// 防止模块名过长
		if(moduleName.length() > MAX_CHAR_VALUE) {
			moduleName = moduleName.substring(0, MAX_CHAR_VALUE);
		}
		
		// 防止接口名过长
		if(interfaceName.length() > MAX_CHAR_VALUE) {
			interfaceName = interfaceName.substring(0, MAX_CHAR_VALUE);
		}
		
		// 防止msg过长
		int avalibleSize = MAX_UDP_PACKGE_SIZE - PACKAGE_FIXED_LENGTH - moduleName.length() - interfaceName.length();
		if(msg.length() > avalibleSize) {
			msg = msg.substring(0, avalibleSize);
		}
		
		// 分开打包
		byte[] moduleLenBin = new byte[]{(byte) moduleName.length()};
		byte[] interfaceLenBin = new byte[]{(byte) interfaceName.length()};
		byte[] costTimeBin = pack('f', costTime+"");
		byte[] successBin = new byte[]{(byte) (success > 0 ? 1: 0)};
		byte[] codeBin = pack('I', code+"");    // php中的unsigned long对应java中的writeInt
		byte[] msgLenBin = pack('n', msg.length()+"");
		byte[] timeBin = pack('I', time()+"");  // php中的unsigned long对应java中的writeInt
		// 合并为一个byte[]数组
		ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
		DataOutputStream dos = new DataOutputStream(baos);
		dos.write(moduleLenBin);
		dos.write(interfaceLenBin);
		// 修正writeFloat顺序和问题
		byte[] newByte = new byte[4];
		newByte[0] = costTimeBin[3];
		newByte[1] = costTimeBin[2];
		newByte[2] = costTimeBin[1];
		newByte[3] = costTimeBin[0];
		dos.write(newByte);
		dos.write(successBin);
		dos.write(codeBin);
		dos.write(msgLenBin);
		dos.write(timeBin);
		// 追加moduleName、interfaceName、msg
		dos.write(moduleName.getBytes());
		dos.write(interfaceName.getBytes());
		dos.write(msg.getBytes());
		byte[] byteArray = baos.toByteArray();
		dos.close();
		return byteArray;
	}
	
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
	 * 返回当前秒
	 * @return
	 */
	private static int time() {
		long time = new Date().getTime()/1000;
		return (int) time;
	}
}
