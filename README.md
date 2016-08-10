Maven
=======
```xml
<dependency>
  <groupId>com.github.shuiguang</groupId>
  <artifactId>workerman-statistics-java</artifactId>
  <version>1.0.0</version>
</dependency>
```

RELEASE版本已经提交到Maven仓库，数据同步中……。

特色功能
=======

添加支持Java客户端数据搜集，通过UDP发送数据到服务端。

添加支持关键配置参数修改，可自定义探测数据周期和数据落地周期。

```php
参数1：默认300秒统计一次探测数据，可修改为60秒
参数所在文件：Applications\Statistics\Bootstrap\StatisticProvider.php
关键代码：$time = ceil($time/300)*300;
修改代码：$time = ceil($time/60)*60;

参数2：默认300秒分组一次探测数据，可修改为60秒
参数所在文件：Applications\Statistics\Modules\main.php
关键代码：$time_line = ceil($time_line/300)*300;
修改代码：$time_line = ceil($time_line/60)*60;

参数3：默认300秒展示一次探测数据，可修改为60秒
参数所在文件：Applications\Statistics\Views\main.tpl.php
关键代码：pointInterval: 300*1000
修改代码：pointInterval: 60*1000

参数4：默认300秒展示一次探测数据，可修改为60秒
参数所在文件：Applications\Statistics\Views\statistic.tpl.php
关键代码：pointInterval: 300*1000
修改代码：pointInterval: 60*1000

参数5：默认统计信息60秒存盘一次，可修改为1秒
参数所在文件：Applications\Statistics\Bootstrap\StatisticWorker.php
关键代码：const WRITE_PERIOD_LENGTH = 60;
修改代码：const WRITE_PERIOD_LENGTH = 1;

```

Windows服务端所需环境
========

需要PHP版本不低于5.3，只需要安装PHP的Cli即可，无需安装PHP-FPM、nginx、apache

示例
========
[Live Demo](http://www.workerman.net:55757/)

安装
=========

以windows为例

安装PHP，双击start_for_win.bat启动监控服务端

如果启动start_for_win.bat报错说明未添加pthreads扩展，其中PHP各个版本扩展dll的均已提供下载。

该压缩包有2个dll文件：

php_pthreads.dll需要复制到php的ext目录下，然后在php.ini中添加extension=php_pthreads.dll；

pthreadVC2是windows系统的动态库文件，32位系统复制到C:\Windows\System32下，64位系统复制到C:\Windows\SysWOW64下。

最后重启PHP即可。

访问[http://127.0.0.1:55757/](http://127.0.0.1:55757/)

Linux服务端所需环境
======
只需要安装PHP的Cli即可，无需安装PHP-FPM、nginx、apache。PHP Cli以及扩展安装文档[http://doc3.workerman.net/install/install.html](http://doc3.workerman.net/install/install.html)

上传workerman-statistics-linux.zip到服务器，解压。

感谢walkor长期以来给予的支持，版本升级网址：[https://github.com/walkor/workerman-statistics](https://github.com/walkor/workerman-statistics)

启动  
`php start.php start -d`

重启启动  
`php start.php restart`

平滑重启/重新加载配置  
`php start.php reload`

查看服务状态  
`php start.php status`

停止  
`php start.php stop`

默认需要开启防火墙55858、55757、55656这三个端口。


权限验证
=======

  *  管理员用户名密码默认都为空，即不需要登录就可以查看监控数据
  *  如果需要登录验证，在applications/Statistics/Config/Config.php里面设置管理员密码

