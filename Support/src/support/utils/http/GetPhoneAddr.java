package support.utils.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @CopyRight：http://www.netrust.cn/
 *
 * @Description:获取手机号归属地   
 * @Author: lazite 
 * @CreateTime: 2015年9月29日 上午10:01:23   
 * @ModifyBy: lazite 
 * @ModeifyTime: 2015年9月29日 上午10:01:23   
 * @ModifyDescription:
 * @Version:   V1.0
 */
public class GetPhoneAddr {	
	private static String url="http://www.ip138.com:8080/search.asp?action=mobile&mobile=%s";
	
	private static String httpUrl = "http://apis.baidu.com/showapi_open_bus/mobile/find";
	private static String APIKEY_VALUE = "8debae6e48612fecc892dcf7e589bfbb";
	private static String APIKEY = "apikey";
	private static String BAIDU_CHARSET = "UTF-8";
	
	/**
	 * @param httpArg
	 *            :参数
	 * @return 返回结果
	 */
	public static String request(String httpArg) {
	    BufferedReader reader = null;
	    String result = null;
	    StringBuffer sbf = new StringBuffer();
	    HttpURLConnection connection=null;
	    try {
	        URL url = new URL(httpUrl + "?" + httpArg);
	        connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("GET");
	        // 填入apikey到HTTP header
	        connection.setRequestProperty(APIKEY,APIKEY_VALUE);
	        connection.connect();
	        InputStream is = connection.getInputStream();
	        reader = new BufferedReader(new InputStreamReader(is,BAIDU_CHARSET));
	        String strRead = null;
	        while ((strRead = reader.readLine()) != null) {
	            sbf.append(strRead);
	            sbf.append("\r\n");
	        }
	        reader.close();
	        //请求返回的json串
	        result = sbf.toString();
	        
	        //返回归属地
	        result =parseJson2Area(result);
	        
	    } catch (Exception e) {
	    	System.out.println("get phone address fail please check netWork connect");
	        e.printStackTrace();
	    }finally{
	    	if(connection!=null)
	    		connection.disconnect();
	    }
	    
	    return result;
	}
	
	private static String parseJson2Area(String result){
		JSONObject jo = JSON.parseObject(result);
        String body=jo.getString("showapi_res_body");
        
        if(JSON.parseObject(body).getString("ret_code").equals("0")){
        	String prov=JSON.parseObject(body).getString("prov");
        	if(Arrays.asList(Municipality).contains(prov))
        		return prov+"市";
        	else
        		return 	JSON.parseObject(body).getString("prov")
        				+"省"
        				+JSON.parseObject(body).getString("city")
        				+"市";
        } 
        return null;
	}
	
	//ip138通过手机号码，返回归属地
	public String getBelong(String mobile){
		Elements els=null;
		try {
			url = String.format(url, mobile);
			Document doc = Jsoup.connect(url).get();
			els= doc.getElementsByClass("tdc2");
		} catch (IOException e) {
			System.out.println("get phone address fail please check netWork connect");
			e.printStackTrace();
		}
		return els.get(1).text();
	}
	
	private static String[] Municipality={"北京","天津","上海","重庆"};
	
	//将返回的手机归属地转化数据库可存储的地址
	public static String RegExAddr(String mobile){
		if(StringUtils.isNotBlank(mobile))
			return request("num="+mobile);
		return mobile;
	}
	
	
	public static void main(String[] ss) throws InterruptedException{
		System.out.println(RegExAddr("num=18511770020"));
		System.out.println(RegExAddr("num=18913707832"));
		/*Thread.sleep(5000);
		System.out.println(gtr.RegExAddr("13658475487"));
		System.out.println(gtr.RegExAddr("18913707807"));
		System.out.println(gtr.RegExAddr("null"));
		System.out.println(gtr.RegExAddr("13705600849"));*/
	}
}
