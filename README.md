# TulingRobot For Minecraft #
## 什么是图灵机器人 ##
图灵机器人是以语义技术为核心驱动力的人工智能公司，致力于“让机器理解世界”，产品服务包括机器人开放平台、机器人OS和场景方案。<br/>
通过图灵机器人，开发者和厂商能够以高效的方式创建专属的聊天机器人、客服机器人、领域对话问答机器人、儿童/服务机器人等。([来源](https://baike.baidu.com/item/%E5%9B%BE%E7%81%B5%E6%9C%BA%E5%99%A8%E4%BA%BA))
## 我要怎么使用插件 ##
> 安装前请确保你的服务端支持使用插件，并使用BukkitApi作为插件Api

前往[Releases](https://github.com/LamGC/TulingRobot-For-Minecraft/releases)选择你所需要的版本(推荐使用最新版)，下载【Minecraft_TulingRobot.jar】并放到plugins文件夹里即可。<br/>
### 申请一个机器人 ###
>前往[图灵机器人](http://www.tuling123.com/)[注册/登录]帐号，点击左侧[我的机器人](http://www.tuling123.com/member/robot/index.jhtml)进入机器人管理页面，点击【点击创建机器人】后，选择创建【自定义】机器人，然后根据情况选择机器人应用场景分类，点击确定后即可完成机器人创建。
### 设置机器人Key ###
#### 方法1 ####
>运行服务器后，如无异常将会在plugins生成TulingRobot目录，在目录里带有【ApiKey.txt】，将您在图灵机器人官网申请的机器人调用Key复制粘贴到本txt文件并保存，注意，插件只会读取文件第一行内容，其他内容将会忽略！
#### 方法2 ####
>确保插件正常载入后，使用拥有OP权限的玩家 或 在服务器控制台 输入命令:
>`/setrobot setkey [机器人Key] -r` <br/>
>如无异常，插件将会保存新的ApiKey到文件并重载ApiKey。

>注意一下！如果插件在初始化配置时出错，请尝试删除[TulingRobot]目录及目录内所有文件，使用`setrobot reload`命令重新初始化插件！
>如任然无法初始化插件，使用zip解压工具，从插件jar内解压`config.properties`文件到`plugins/TulingRobot`目录中并使用`setrobot reload`命令重载配置文件

至此，插件已配置完成！

## 使用插件 ##
查看命令:`/help TulingRobot`<br/>
与机器人聊天:`/robot [Message(消息)]`<br/>
设置机器人:`/setrobot [Option(选项)] <args(参数)...>`<br/>
其中，机器人设置有以下选项:
 - (选项加太多了，插件中用`setrobot help`命令查看吧QAQ)
 
## 使用图灵机器人Api简易教程 ##
在需要使用Api的类导入包:`import net.lamgc.TulingPlugin.TulingRobot`<br/>
然后new一个对象出来:`new TulingRobot(String ApiKey);` 或 `new TulingRobot();`<br/>
使用`Robot(String info, String UserID)`方法调用机器人(ApiV1)即可

## LICENSE ##
本项目遵循GPLv3协议开源<br/>
如果你需要在商业用途使用(并修改)本项目且(或)不希望开源，请在本项目通过issue向我申请，并等待结果，申请格式如下:
>标题:[项目名 - 商业用途申请]<br/>
>申请方:[个人名(GithubID)/组织名]<br/>
>使用本项目的用途:[rt]<br/>

申请将在3个工作日内被处理，申请被处理后issue会被关闭，结果回复在issue。<br/>
目前已得到允许，不需要开源本项目的项目:
> - (目前还没有呢)

