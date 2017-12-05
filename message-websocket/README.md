### 1.采用netty4实现的websocket,单点连接数可达六十万，做过测试;

### 2.集成springboot提供restapi,接入信息内部转给websocket发送；

### 3.支持集群。全部采用jar包与脚本启动



说明：脚本首次运行请操作如下    start

​           直接执行sed -i "s/\r//" start.sh来转化， 然后就可以执行./start.sh运行脚本了。（我亲自试过， 是ok的）

###4.请阅读pdf架构设计图，rpc方式请忽略因为不方便关联rpc框架

