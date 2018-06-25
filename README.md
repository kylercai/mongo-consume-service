"# mongo-consume-service" 

用于测试ASB的性能

使用方法：

1. 用ASB服务的ConnectionString替换代码中的connectionString
2. 用实际的Queue名称替换代码中相应的队列名
3. 在eclipse中export项目为“Runnable JAR”，package required libraries，存成本地jar文件，例如“sbtest.jar”
4. 上传sbtest.jar到Azure虚拟机，加压 sbtest.jar 到目录下，例如 sbtest/
5. 进入sbtest目录，在命令行下，使用java命令执行：
    --- 发送消息：
	    例如： java -Djava.ext.dirs=./ com.msl.mongo.consume.message.sb.ServiceBusMessageSender 1 10240 280000
		 
		执行过程控制台打印输出，显示消息发送过程，指定数量的消息发送完后，打印信息显示总发送消息数，以及耗时（单位毫秒），例如：
			sent message 100197
			sent message 100198
			sent message 100199
			message size = 1024 bytes; message counts = 100200
			send elapse time = 55645 milliSec
			send messages done
		
		参数说明：
		参数1：保留不用
		参数2：消息字节大小，上面的示例命令，使用10K字节的消息
		参数3：发送的消息数，上面的示例命令，发送280000条消息
		 
    --- 接收消息
	    java -Djava.ext.dirs=./ com.msl.mongo.consume.message.sb.MongoSubscriberByAzure 1 20

		执行过程控制台打印输出，显示消息接收过程，已接收的消息数和总用时（单位毫秒），例如：
			Fetched msg... count= 100182, cost 587710 millSec. concurrent=20
			Fetched msg... count= 100183, cost 587710 millSec. concurrent=20
			Fetched msg... count= 100184, cost 587711 millSec. concurrent=20

		参数说明：
		参数1：保留不用
		参数2：接收消息并发数
		参数3：可选（true/false）。true时打印接收消息的内容（只前30个字节）
		
注意事项：
可能执行会出现找不到加密类的异常:
java.lang.RuntimeException: java.security.NoSuchAlgorithmException: SunTls12MasterSecret KeyGenerator not available
这时把java目录下lib/ext/sunjce_provider.jar 拷贝到 sbtest/ 目录下即可，或在执行的命令中指明需要查找sunjce_provider.jar的路径，即类似以下命令：
java -Djava.ext.dirs=./:/usr/local/jre1.8.0_171/lib/ext  com.msl.mongo.consume.message.sb.ServiceBusMessageSender 1 1024 3
其中"/usr/local/jre1.8.0_171" 是java的安装路径
