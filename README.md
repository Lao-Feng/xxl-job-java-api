# xxl-job-java-api

### 一、说明
该项目基于[xl-job](https://github.com/xuxueli/xxl-job)扩展的JavaAPI。<br>
在xxl-job中，没有提供javaAPI，只能在控制台人为操作定时任务，但是在实际在项目中需要通过代码控制调度任务，比如创建订单之后，两个小时不付款就关闭，
因此 开源了一个 `xxl-job-java-api` ，可以在项目动态控制调度任务。

### 二、原理
其实项目原理很简单，控制台通过页面发送http，该项目通过javaAPI生成对应http请求报文，从而通过代码控制调度任务

### 三、使用
项目最主要使用的是 [XxlJobClientImpl](./src/main/java/com/fj/api/manager/XxlJobClientImpl.java)控制调度任务,当然也提供了测试代码,具体查看测试模块<br>

### 四、目录说明
* *config目录:* xxl-job配置文件
* *manager目录:* 具体的API存放目录
* *model目录:* 模型目录,里面的对象是从源码中复制过来的,利于封装数据
* *okhttp目录:* okhttp调用目录

### 五、其他
作者邮箱：2109921940@qq.com，欢迎大家提意见
