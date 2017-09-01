/**
 This file is part of TulingRobot_For_Minecraft.

 TulingRobot_For_Minecraft is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 TulingRobot_For_Minecraft is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.coding.lamgc.TulingRobot;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import wzh.http.HttpRequest;

import java.io.UnsupportedEncodingException;

/**
 * 图灵机器人Api
 * @author LamGC
 */
public class TulingRobot {

    private String ApiKey;

    public TulingRobot(){}

    /**
     * 置Key - 构造方法
     * @param ApiKey Api调用Key
     */
    public TulingRobot(String ApiKey){
        SetApiKey(ApiKey);
    }

    /**
     * 设置/修改 Api调用Key
     * @param ApiKey 调用Api所需Key，请确保Key有效
     */
    public void SetApiKey(String ApiKey){
        this.ApiKey = ApiKey;
    }


    /**
     * 取图灵机器人Api地址
     * @param NoApiV2 是否不使用ApiV2
     * @return 地址
     */
    public String getApiUrl(boolean NoApiV2){
        if(NoApiV2){
            return "http://www.tuling123.com/openapi/api";
        }else{
            return "http://openapi.tuling123.com/openapi/api/v2";
        }
    }

    /**
     * 使用机器人（Api_V1）
     * @param msg 消息
     * @param UserID 用户标识，用于上下文答复
     * @return 返回HashMap对象
     *          请注意检查HashMap，以免造成因无对应值导致的错误
     */
    public JsonObject Robot(String msg, String UserID) throws UnsupportedEncodingException {
        JsonObject sj = new JsonObject();
        sj.addProperty("key", ApiKey);
        sj.addProperty("info",msg);
        sj.addProperty("userid",UserID);
        //String re = HttpRequest.sendGet(getApiUrl(true),"key=" + ApiKey + "&info=" + URLEncoder.encode(msg,"UTF-8") + "&userid=" + URLEncoder.encode(UserID,"UTF-8"));
        String re = HttpRequest.sendPost(getApiUrl(true),new String(sj.toString().getBytes("UTF-8")),"application/json");
        return new JsonParser().parse(re).getAsJsonObject();
    }

    /**
     * 根据错误代码获取文本，如果不是错误文本，将返回空
     * @param Code 返回码
     * @return 如果返回码为错误码，返回错误文本，否则返回空文本( != Null)
     */
    public String getErrorString(int Code){
        switch (Code) {
            case TLCode.Error_KeyError:
                return "Key错误";
            case TLCode.Error_InfoIsEmpty:
                return "参数Info为空";
            case TLCode.Error_RTHBU:
                return "当天调用次数已用完";
            case TLCode.Error_DataFormatException:
                return "数据格式异常";
            default:
                return "";
        }
    }

    /**
     * 返回信息的Code
     */
    public static class TLCode{
        //文字类返回
        final static int Text = 100000;
        //链接类返回
        final static int Url  = 200000;
        //新闻类返回
        final static int News = 302000;
        //菜谱类返回
        final static int Menu = 308000;

        //儿童版 儿歌类返回
        final static int children_Song = 313000;
        //儿童版 诗词类返回
        final static int children_Poetry = 314000;

        // Key错误
        final static int Error_KeyError = 40001;
        //Info为空
        final static int Error_InfoIsEmpty = 40002;
        //当天请求次数已用完
        final static int Error_RTHBU = 40003;
        //数据格式异常
        final static int Error_DataFormatException = 40007;
    }

    /**
     * Json对象
     */
    public static class TLJsonObject{
        /**
         * V1信息
         */
        public static class V1Info{
            String key;
            String info;
            String userid;
            public V1Info(String Key,String Info,String UserID){
                this.key = Key;
                this.info = Info;
                this.userid = UserID;
            }
        }
    }
}
