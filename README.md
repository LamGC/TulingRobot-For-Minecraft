# TulingRobot For Minecraft #
## 什么是图灵机器人 ##
图灵机器人是以语义技术为核心驱动力的人工智能公司，致力于“让机器理解世界”，产品服务包括机器人开放平台、机器人OS和场景方案。<br/>
通过图灵机器人，开发者和厂商能够以高效的方式创建专属的聊天机器人、客服机器人、领域对话问答机器人、儿童/服务机器人等。
## 我要怎么使用插件 ##
> 安装前请确保你的服务端支持使用插件，并使用BukkitApi作为插件Api

前往[Releases](https://github.com/LamGC/TulingRobot-For-Minecraft/releases)选择你所需要的版本(推荐使用最新版)，下载【Minecraft_TulingRobot.jar】并放到plugins文件夹里即可。<br/>
### 设置机器人Key ###
#### 方法1 ####
>运行服务器后，如无异常将会在plugins生成TulingRobot目录，在目录里带有【ApiKey.txt】，将您在[图灵机器人](http://www.tuling123.com/)申请的机器人调用Key复制粘贴到本txt文件并保存，注意，插件只会读取文件第一行内容，其他内容将会忽略

#### 方法2 ####
>确保插件正常载入后，使用拥有OP权限的玩家 或 在服务器控制台 输入命令:
>`/setrobot setkey [机器人Key] -r` <br/>
>如无异常，插件将会保存新的ApiKey到文件并重载Key。