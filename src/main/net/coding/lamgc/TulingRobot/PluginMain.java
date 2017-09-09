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
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.Collection;
import java.util.Properties;

/**
 * 插件及程序的主类
 * @author LamGC
 */
public class PluginMain extends JavaPlugin implements Listener {

    //图灵机器人实例
    private static TulingRobot TLR = new TulingRobot();
    Properties cfg;


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
        LoadApiKey();
        getLogger().info("插件已就绪");
    }

    /**
     * 载入ApiKey
     * @return 是否成功载入
     */
    private boolean LoadApiKey() {
        try {
            //获取文件File对象
            File KeyFile = new File(getDataFolder().getPath() + "/ApiKey.txt");
            //检查文件是否存在
            if (KeyFile.exists()) {
                //检查是否为一个文件
                if (KeyFile.isFile()) {
                    //确定文件是否为空
                    if (KeyFile.length() == 0L) {
                        getLogger().warning("ApiKey文件为空，请填入ApiKey！");
                        return false;
                    }
                    //开始读取
                    InputStreamReader read = new InputStreamReader(
                            new FileInputStream(KeyFile),
                            "UTF-8");//考虑到编码格式
                    BufferedReader bufferedReader = new BufferedReader(read);
                    //因为只需要读取第一行内容，读完就跑
                    String k = bufferedReader.readLine();
                    if(k.length() == 32){
                        TLR.SetApiKey(k);
                    }else{
                        getLogger().warning("不是一个标准的ApiKey！");
                        bufferedReader.close();
                        read.close();
                        return false;
                    }
                    //关闭输入流
                    bufferedReader.close();
                    read.close();
                    getLogger().info("已成功读取ApiKey");
                } else {
                    getLogger().warning("ApiKey不是文件！");
                    return false;
                }
            } else {
                //没有文件就创建文件
                if (KeyFile.getParentFile().mkdirs() && KeyFile.createNewFile()) {
                    getLogger().warning("未找到ApiKey文件！已初始化该文件，请在文件内添加机器人ApiKey（注意不要有换行）");
                    return false;
                } else {
                    getLogger().warning("未找到ApiKey文件！尝试初始化文件失败！");
                    return false;
                }
            }
        } catch (IOException e) {
            getLogger().warning("发生了一个严重的异常：");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 载入配置
     * @return 返回是否载入成功
     */
    private boolean LoadConfig() throws IOException {
        File configFile = new File(getDataFolder().getPath() + "/config.properties");
        if(configFile.exists()){
            if(configFile.isFile()){
                cfg.load(new InputStreamReader(new FileInputStream(configFile),"UTF-8"));
            }else{
                getLogger().warning("config.properties不是文件！");
                return false;
            }
        }else{
            this.getClass().getResourceAsStream("config.properties");
        }
        return false;
    }


    /**
     * 置ApiKey到文件
     * @param ApiKey 新的ApiKey\n
     * @throws IOException 写入文件时可能发生的异常
     */
    private void SetApiKey(String ApiKey) throws IOException {
        //获取文件File对象
        File KeyFile = new File(getDataFolder().getPath() + "/ApiKey.txt");
        FileOutputStream fos = new FileOutputStream(KeyFile);
        fos.write(ApiKey.getBytes("UTF-8"));
        fos.flush();
        fos.close();
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
        //如果是调用机器人的命令
        if (cmd.getName().equalsIgnoreCase("robot") && args.length == 1) {
            JsonObject rem;
            try {
                rem = TLR.Robot(args[0], sender.getName());
            } catch (UnsupportedEncodingException e) {
                sender.sendMessage("调用机器人时发生一个异常！详细信息请查看服务器控制台。");
                e.printStackTrace();
                return true;
            }
            assert rem != null;
            int code = rem.get("code").getAsInt();
            if (code == TulingRobot.TLCode.Text) {
                sender.sendMessage(rem.get("text").getAsString());
            } else if (code == TulingRobot.TLCode.Url) {
                sender.sendMessage(rem.get("text") + "(Url:" + rem.get("url") + ")");
            } else if (code >= TulingRobot.TLCode.News && code <= TulingRobot.TLCode.children_Poetry) {
                sender.sendMessage("[插件]本功能咱不支持");
            } else if (code >= TulingRobot.TLCode.Error_KeyError) {
                sender.sendMessage("[错误]" + TLR.getErrorString(code) + "(" + code + ")");
                getLogger().warning("[错误]" + TLR.getErrorString(code) + "(" + code + ")");
            }
            return true;
        }else if(cmd.getName().equalsIgnoreCase("setrobot")){
            //两个参数，则为修改ApiKey而不重载
            if(args[0].equalsIgnoreCase("setkey") && args.length == 2 || args.length == 3){
                //标准图灵机器人ApiKey是32位长度的
                if(args[1].length() == 32){
                    //更改ApiKey
                    try {
                        SetApiKey(args[1]);
                        sender.sendMessage("已成功修改ApiKey");
                        getLogger().info("ApiKey已修改，新ApiKey:[" + args[1] + "]");
                        if(args.length == 3 && args[2].equalsIgnoreCase("--reload") || args[2].equalsIgnoreCase("-r")){
                            getLogger().info("正在载入ApiKey");
                            if(LoadApiKey()){
                                sender.sendMessage("已成功载入ApiKey");
                            }else{
                                getLogger().info("载入ApiKey失败");
                                sender.sendMessage("载入ApiKey失败");
                            }
                        }
                        return true;
                    } catch (IOException e) {
                        sender.sendMessage("执行操作时发生了一个异常！详细信息请查看服务器控制台。");
                        getLogger().warning("发送了一个严重的问题：");
                        e.printStackTrace();
                        return true;
                    }
                }
            }else
                //或者重载Key
                //以后会重载配置
                if(args.length == 1 && args[0].equalsIgnoreCase("reload")){
                    getLogger().info("正在载入ApiKey");
                    if(LoadApiKey()){
                        getLogger().info("已成功载入ApiKey");
                        sender.sendMessage("已成功载入ApiKey");
                    }else{
                        getLogger().info("载入ApiKey失败");
                        sender.sendMessage("载入ApiKey失败");
                    }
                return true;
            }

            //帮助说明
            String u =
                    "用法:/setrobot [选项] <参数...>" +
                            "   选项:" +
                            "setkey - 设置新的ApiKey【命令用法：/setrobot setkey {ApiKey} <--reload/-r>】" +
                            "reload - 重载设置，目前仅重载ApiKey【命令用法：/setrobot reload】";

            sender.sendMessage(u);
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
        //TODO:2017/09/08 - 玩家聊天事件，获取消息后发送公屏信息
        if(event.isCancelled()){
            //如果事件被取消，则放弃处理，防止浪费调用次数
            return;
        }
        //开发所留下的调试代码
        getLogger().info("PlayerCharEventInfo:");
        getLogger().info(event.getFormat());
        getLogger().info(event.getMessage());
        //前缀，如果需要
        if(event.getMessage().indexOf("*") == 1){

        }
    }

    /**
     * 发送公屏信息<br/>
     * 其实是发给所有玩家一条消息而已233
     * @param Msg 信息
     */
    private void sendMsgToOnlinePlayer(String Msg){
        Collection<? extends Player> onlinePlayers = getServer().getOnlinePlayers();
        Player[] op = new Player[onlinePlayers.size()];
        Player[] players = onlinePlayers.toArray(op);
        for(int i = 0;i < players.length;i++){
            players[i].sendMessage(Msg);
        }
    }
}
