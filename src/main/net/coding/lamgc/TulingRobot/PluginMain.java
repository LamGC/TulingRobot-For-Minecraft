/**
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
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

/**
 * 插件及程序的主类
 * @author LamGC
 */
public class PluginMain extends JavaPlugin implements Listener {

    //图灵机器人实例
    private static TulingRobot TLR = new TulingRobot();

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
        //getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("插件已就绪");
    }

    /**
     * 载入ApiKey
     */
    private void LoadApiKey() {
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
                        return;
                    }
                    //开始读取
                    InputStreamReader read = new InputStreamReader(
                            new FileInputStream(KeyFile),
                            "UTF-8");//考虑到编码格式
                    BufferedReader bufferedReader = new BufferedReader(read);
                    //因为只需要读取第一行内容，读完就跑
                    TLR.SetApiKey(bufferedReader.readLine());
                    //关闭输入流
                    bufferedReader.close();
                    read.close();
                    getLogger().info("已成功读取ApiKey");
                } else {
                    getLogger().warning("ApiKey不是一个合法的文件！");
                }
            } else {
                //没有文件就创建文件
                if (KeyFile.getParentFile().mkdirs() && KeyFile.createNewFile()) {
                    getLogger().warning("未找到ApiKey文件！已初始化该文件，请在文件内添加机器人ApiKey（注意不要有换行）");
                } else {
                    getLogger().warning("未找到ApiKey文件！尝试初始化文件失败！");
                }
            }
        } catch (IOException e) {
            getLogger().warning("发生了一个严重的异常：");
            e.printStackTrace();
        }
    }

    /**
     * 插件已启用(插件载入完成)
     */
    public void onEnable() {
        getLogger().info("插件已启用");
    }

    /**
     * 插件被停用(服务器关闭时)
     */
    public void onDisable() {
        getLogger().info("插件已停用");
    }

    /**
     * 命令处理方法
     * @param sender 发送者，可强转为 [Player] 类型
     * @param cmd    命令首
     * @param label  命令缩写
     * @param args   命令参数(和main的args参数一样)
     * @return 是否完成处理
     */
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //如果是调用机器人的命令
        if (cmd.getName().equalsIgnoreCase("robot") && args.length == 1) {
            JsonObject rem = null;
            try {
                rem = TLR.Robot(args[0], sender.getName());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
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
        getLogger().info(event.getFormat());
        getLogger().info(event.getMessage());
    }
}
