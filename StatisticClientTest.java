/**
 * Statistic统计java客户端
 * @author shuiguang
 *
 */
public class StatisticClientTest {
	
	public static void main(String[] args) {
		// 模块名称
		String moduleName = "TestModule";
		// 接口名称
		String interfaceName = "TestInterface";
		// 执行状态
		int success = rand(0,1);
		// 状态码
		int code = rand(300, 400);
		// 日志消息
		String msg = "这个是测试消息";
		// 远程主机IP
		String host = "127.0.0.1";
		// 远程主机端口,默认值为55656
		int serverPort = 55656;
		
		// 开始计时
		StatisticClient.tick(moduleName, interfaceName);
		
		try {
			// 执行耗时任务
			Thread.sleep(rand(10, 600));
			
			// 发送数据
			StatisticClient.report(moduleName, interfaceName, success, code, msg, host, serverPort);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 
	 * 返回[min, max]之间的整型，包含边界。如果只传入一个参数n，则返回[0-n]。
	 * @author Z 
	 * @param m 起始数字
	 * @return n 结束数字
	 */
	public static int rand(int m, int n) {
		java.util.Random random = new java.util.Random();
		return random.nextInt(n - m + 1) + m;
	}
	
}
