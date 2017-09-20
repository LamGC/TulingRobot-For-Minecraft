/*
 * This file is part of TulingRobot_For_Minecraft.
 *
 * TulingRobot_For_Minecraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 *
 * TulingRobot_For_Minecraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.coding.lamgc.TulingRobot;

import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.Arrays;
import java.util.Properties;

/**
 * 插件及程序的主类
 * @author LamGC
 */
public class PluginMain extends JavaPlugin implements Listener {

    //插件相关信息？
    private final String Plugin_Version = "1.1.2";


    //图灵机器人实例
    private static TulingRobot TLR = new TulingRobot();
    //配置项
    private Properties cfg = new Properties();


    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println("请把本Jar文件放入服务器目录下plugins文件夹即可");
        /*
        JsonObject sj = new JsonObject();
        sj.addProperty("key", "6dffba1e83cdb5d7aebc8ad6b320cf5b");
        sj.addProperty("info","Test");
        sj.addProperty("userid","TUD");
        System.out.println(HttpRequest.sendPost(TLR.getApiUrl(true),sj.toString()));
        */

        //JsonObject rj = TLR.Robot("看新闻","Tud");
        /*
        System.out.println(rj.toString());
        System.out.println(rj.get("code").getAsInt());
        System.out.println(rj.get("text").getAsString());
        System.out.println(rj.get("url").getAsString());
        */
    }

    /**
     * 插件载入中(服务器启动时)
     */
    public void onLoad() {
        getLogger().info("插件载入中...");
        try {
            if(LoadConfig()) {
                if (!LoadApiKey()) {
                    getLogger().warning("ApiKey载入失败！");
                }
            }else{
                getLogger().warning("配置载入失败！");
            }
        } catch (IOException e) {
            getLogger().warning("载入配置时发生异常：");
            e.printStackTrace();
        }
        getLogger().info("插件已就绪(Version:V" + Plugin_Version);
    }

    /**
     * 插件已启用(插件载入完成)
     */
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("插件已启用");
    }

    /**
     * 插件被停用(服务器关闭时)
     */
    public void onDisable() {
        try {
            SaveConfig();
        } catch (IOException e) {
            getLogger().warning("保存配置时发生异常：");
            e.printStackTrace();
        }
        //停用插件时注销事件监听器
        HandlerList.unregisterAll((Listener) this);
        getLogger().info("插件已停用");
    }

    /**
     * 命令处理方法
     * @param sender 发送者，可强转为 [Player] 类型
     * @param cmd    命令首
     * @param label  命令缩写(不太清楚怎么用)
     * @param args   命令参数(和main的args参数一样)
     * @return 是否完成处理
     */
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //TODO:2017/09/15: 注意清理 [调试] 代码
        //如果是调用机器人的命令
        if (cmd.getName().equalsIgnoreCase("robot") && args.length == 1) {
            JsonObject rem;
            try {
                rem = TLR.Robot(args[0], sender.getName());
            } catch (IOException e) {
                sender.sendMessage("调用机器人时发生一个异常！详细信息请查看服务器控制台。");
                getLogger().warning("调用机器人时发生了异常，信息如下:");
                e.printStackTrace();
                return true;
            }
            //Null检查
            if (rem == null) {
                getLogger().warning("调用机器人失败！(调用返回不是合法Json)");
                sender.sendMessage("[错误] 机器人调用失败！");
                return true;
            }
            //返回代码判断
            int code = rem.get("code").getAsInt();
            if (code == TulingRobot.TLCode.Text) {
                //文本类消息
                sender.sendMessage(rem.get("text").getAsString());
            } else if (code == TulingRobot.TLCode.Url) {
                //带Url消息
                sender.sendMessage(rem.get("text") + "(Url:" + rem.get("url") + ")");
            } else if (code >= TulingRobot.TLCode.News && code <= TulingRobot.TLCode.children_Poetry) {
                //不支持儿童版和菜谱类
                sender.sendMessage("[插件]本功能咱不支持");
            } else if (code >= TulingRobot.TLCode.Error_KeyError) {
                //错误信息
                sender.sendMessage("[错误]" + TLR.getErrorString(code) + "(" + code + ")");
                getLogger().warning("[错误]" + TLR.getErrorString(code) + "(" + code + ")");
            }
            return true;
        } else
            //------------------------------机器人设置命令------------------------------
            if (cmd.getName().equalsIgnoreCase("setrobot") && args.length >= 1) {
                getLogger().info("[调试] 进入机器人设置");
                getLogger().info("[调试] args:" + Arrays.toString(args));
                //TODO:2017/09/18: 添加【机器人名，自由聊天开关】设置命令
                //两个参数，则为修改ApiKey而不重载
                if (args[0].equalsIgnoreCase("apikey")) {
                    getLogger().info("[调试] 进入修改ApiKey");
                    //标准图灵机器人ApiKey是32位长度的
                    if (args[1].length() == 32) {
                        //更改ApiKey
                        try {
                            SetApiKey(args[1]);
                            sender.sendMessage("已成功修改ApiKey");
                            getLogger().info("ApiKey已修改，新ApiKey:[" + args[1] + "]");
                            if (args.length == 3 && args[2].equalsIgnoreCase("--reload") || args[2].equalsIgnoreCase("-r")) {
                                //重新载入配置
                                getLogger().info("正在载入配置");
                                if (LoadConfig()) {
                                    getLogger().info("已成功载入配置");
                                    sender.sendMessage("已成功载入配置");
                                } else {
                                    getLogger().info("载入配置失败");
                                    sender.sendMessage("载入配置失败");
                                }
                            }
                            return true;
                        } catch (IOException e) {
                            sender.sendMessage("执行操作时发生了一个异常！详细信息请查看服务器控制台。");
                            getLogger().warning("发送了一个严重的问题：");
                            e.printStackTrace();
                            return true;
                        }
                    } else {
                        sender.sendMessage("ApiKey有误(长度错误)");
                        return true;
                    }
                } else
                    if(args.length == 2){
                    //设置聊天前缀
                    if (args[0].equalsIgnoreCase("prefix")) {
                        getLogger().info("[调试] 进入修改聊天前缀");
                        try {
                            //选择清除还是更改
                            putConfig(
                                    "Dialogue.Trigger_Prefix",
                                    args[1].equalsIgnoreCase("-r") ? "" : args[1]
                            );
                        } catch (IOException e) {
                            sender.sendMessage("修改聊天前缀时发生错误！详情请查看服务器控制台。");
                            getLogger().warning("修改前缀时发生错误！信息如下：");
                            e.printStackTrace();
                            return true;
                        }
                        sender.sendMessage("已成功修改聊天前缀！");
                        return true;
                    } else
                        //设置机器人名称
                        if (args[0].equalsIgnoreCase("robotname")) {
                            getLogger().info("[调试] 进入修改机器人名称");
                        try {
                            putConfig("Robot.Name", args[1]);
                        } catch (IOException e) {
                            sender.sendMessage("修改机器人名称时发生错误！详情请查看服务器控制台。");
                            getLogger().warning("修改机器人名称时发生错误！信息如下：");
                            e.printStackTrace();
                            return true;
                        }
                            sender.sendMessage("已成功修改机器人名称！");
                            return true;
                    } else
                        //设置聊天前缀
                        if (args[0].equalsIgnoreCase("chattrigger")) {
                            getLogger().info("[调试] 进入修改聊天模式");
                        try {
                            putConfig(
                                    "Dialogue.Chat_Trigger",
                                    args[1]
                            );
                        } catch (IOException e) {
                            sender.sendMessage("修改设置时发生错误！详情请查看服务器控制台。");
                            getLogger().warning("修改设置时发生错误！信息如下：");
                            e.printStackTrace();
                            return true;
                        }
                        sender.sendMessage("已成功修改自由聊天模式！");
                        return true;
                    }
                } else
                    //重载配置
                    if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                        getLogger().info("正在载入配置");
                        try {
                            //读取配置
                            if (LoadConfig()) {
                                getLogger().info("已成功载入配置");
                                sender.sendMessage("已成功载入配置");
                            } else {
                                getLogger().info("载入配置失败");
                                sender.sendMessage("载入配置失败");
                            }
                        } catch (IOException e) {
                            getLogger().info("载入配置时发生异常：");
                            sender.sendMessage("载入配置出错，详细信息请查看服务器控制台！");
                            e.printStackTrace();
                        }
                        return true;
                    }
                //帮助说明
                String u =
                        "用法:/setrobot [选项] <参数...>"                                                            + "\n" +
                        "    选项(支持小写):"                                                                      + "\n" +
                        "        ApiKey       - 设置新的ApiKey【命令用法：/setrobot setkey {ApiKey} <--reload/-r>】" + "\n" +
                        "        RobotName    - 设置新的机器人名称(自由聊天模式有效)"                                + "\n" +
                        "        Prefix       - 设置自由聊天的前缀(设置为【-r】可删除前缀)"                          + "\n" +
                        "        ChatTrigger  - 设置自由聊天模式开关，true为开启，false或其他字符为关闭"             + "\n" +
                        "        reload       - 重载设置，目前仅重载ApiKey【命令用法：/setrobot reload】"            ;
                sender.sendMessage(u);
                return true;
        }
        return false;
    }

    /**
     * 玩家聊天监听事件
     * 暂时留着，到时候再说233
     * @param event 事件参数
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCharEvent(AsyncPlayerChatEvent event) {
        //TODO:2017/09/15: 注意清理 [调试] 代码
        getLogger().info("[调试] " + "玩家聊天事件被触发");
        if(event.isCancelled()){
            //如果事件被取消，则放弃处理，防止浪费调用次数
            getLogger().info("[调试] " + "事件被取消，放弃处理");
            return;
        }

        //如果停用了聊天对话模式，则忽略事件
        if(!cfg.getProperty("Dialogue.Chat_Trigger","false").equalsIgnoreCase("true")){
            getLogger().info("[调试] " + "聊天对话模式被关闭，放弃处理");
            return;
        }
        //异步处理方法
        new Thread(() -> {
            getLogger().info("[调试] 处理线程已启动，开始异步处理...");
            //前缀，如果需要
            //前缀如果不为空
            String prefix = cfg.getProperty("Dialogue.Trigger_Prefix");
            getLogger().info("[调试] 触发前缀: " + prefix);
            if(!prefix.equalsIgnoreCase("")){
                getLogger().info("[调试] " + "前缀在信息的位置：" + event.getMessage().indexOf(prefix));
                //如果发现了前缀(在开头)
                if(event.getMessage().indexOf(prefix) != 0){
                    //不处理非指定前缀消息
                    getLogger().info("[调试] " + "前缀不正确，放弃处理");
                    return;
                }
            }

            //准备好一个JsonObject变量用来获取机器人返回值
            JsonObject rj;
            try {
                //调用机器人
                getLogger().info("[调试] " + "调用机器人...");
                //清除前缀信息
                rj = TLR.Robot(event.getMessage().replace(prefix,""), event.getPlayer().getName());
                getLogger().info("[调试] " + "调用完毕，开始处理");
            } catch (IOException e) {
                Bukkit.broadcastMessage("执行操作时发生了一个异常！详细信息请查看服务器控制台。");
                getLogger().warning("调用机器人时发生了异常，信息如下:");
                e.printStackTrace();
                return;
            }

            if(rj == null){
                getLogger().warning("调用机器人失败！(调用返回不是合法Json)");
                //sendMsgToOnlinePlayers("[错误] 机器人调用失败！");
                Bukkit.broadcastMessage("[错误] 机器人调用失败！");
                return;
            }

            //前缀设置
            //测试发现有点别扭，所以加个空格- -
            String rs = cfg.getProperty("Robot.Name","").equalsIgnoreCase("") ? "" : cfg.getProperty("Robot.Name") + ": ";
            getLogger().info("[调试] " + "机器人回复前缀：" + rs);

            //根据code设置返回值
            int code = rj.get("code").getAsInt();
            if (code == TulingRobot.TLCode.Text) {
                rs = rs + rj.get("text").getAsString();
            } else if (code == TulingRobot.TLCode.Url) {
                rs = rs + rj.get("text").getAsString() + "(Url: " + rj.get("url").getAsString() + " )";
            } else if (code >= TulingRobot.TLCode.News && code <= TulingRobot.TLCode.children_Poetry) {
                rs = rs + "[插件]本功能咱不支持";
            } else if (code >= TulingRobot.TLCode.Error_KeyError) {
                rs = rs + "[错误]" + TLR.getErrorString(code) + "(" + code + ")";
                getLogger().warning("[错误]" + TLR.getErrorString(code) + "(" + code + ")");
            }

            getLogger().info("[调试] " + "最终消息：" + rs);
            getLogger().info("[调试] " + "开始发送公屏信息");
            //发送公屏信息
            Bukkit.broadcastMessage(rs);
        }).start();
    }

    //---------------功能相关方法---------------

    /**
     * 新的载入
     * @return ApiKey是否载入成功
     */
    private boolean LoadApiKey(){
        String Key = cfg.getProperty("Robot.ApiKey","");
        getLogger().info("[调试] ApiKey:" + Key);
        if(Key.equalsIgnoreCase("")){
            getLogger().warning("ApiKey文件为空，请填入ApiKey！");
        }else if(Key.length() != 32){
            //长度不对
            getLogger().warning("不是一个标准的ApiKey！");
            return false;
        }
        TLR.SetApiKey(Key);
        getLogger().info("ApiKey已成功载入");
        return true;
    }

    /**
     * 载入配置
     * @return 返回是否载入成功
     */
    private boolean LoadConfig() throws IOException {
        //获得插件数据文件夹
        File df = getDataFolder();
        //检查文件夹是否存在，或是否为文件
        if(!df.exists() || df.isFile()){
            //是就删除，然后重新创建
            df.delete();
            if(!df.mkdir()){
                getLogger().warning("插件数据文件夹创建失败！请手动创建【TulingRobot】文件夹");
                return false;
            }
        }
        //指定配置文件路径获取File对象
        File configFile = new File(getDataFolder().getPath() + "/config.properties");
        //检查文件是否存在
        if(configFile.exists()){
            //确保 是一个文件
            if(configFile.isFile()){
                //载入配置
                cfg.load(new InputStreamReader(new FileInputStream(configFile),"UTF-8"));
                return true;
            }else{
                //不是，返回
                getLogger().warning("config.properties不是文件！");
                return false;
            }
        }else{
            //没有文件，生成一个
            InputStream config = this.getClass().getResourceAsStream("/config.properties");
            if(config == null){
                getLogger().warning("获取默认配置文件失败！请使用解压工具打开插件，解压【config.properties】文件到[plugins/TulingRobot]目录！");
                return false;
            }
            byte[] b = new byte[config.available()];
            if(config.read(b) == config.available()){
                FileOutputStream fos = new FileOutputStream(configFile);
                //写入文件
                fos.write(b);
                //刷新缓冲区
                fos.flush();
                //关闭输出流
                fos.close();
                //读入配置
                cfg.load(new InputStreamReader(new FileInputStream(configFile),"UTF-8"));
                getLogger().warning("config.properties文件不存在，插件已自动创建，进行设置后使用【/setRobot reload】重载配置");
                return false;
            }
        }
        return false;
    }

    /**
     * 修改配置<br/>
     * 自动保存
     * @param k 键
     * @param v 值
     * @throws IOException 读取配置时可能发生的异常
     */
    private void putConfig(String k,String v) throws IOException {
        LoadConfig();
        cfg.put(k,v);
        SaveConfig();
    }

    private void SaveConfig() throws IOException {
        cfg.store(new FileOutputStream(new File(getDataFolder().getPath() + "/config.properties")),"TulingRobot_V" + Plugin_Version + " - ConfigFile");
    }

    /**
     * 置ApiKey到文件
     * @param ApiKey 新的ApiKey\n
     * @throws IOException 写入文件时可能发生的异常
     */
    private void SetApiKey(String ApiKey) throws IOException {
        /*
        //获取文件File对象
        File KeyFile = new File(getDataFolder().getPath() + "/ApiKey.txt");
        FileOutputStream fos = new FileOutputStream(KeyFile);
        fos.write(ApiKey.getBytes("UTF-8"));
        fos.flush();
        fos.close();
        */

        //新方法
        putConfig("Robot.ApiKey",ApiKey);
        getLogger().info("机器人ApiKey已修改(New_Key: " + ApiKey + ")");
    }
}
