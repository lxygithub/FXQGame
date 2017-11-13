package cn.muzhong.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class Get {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String param="1586";
		String g=Get.sendGet("13693653460",param);//18911614952
		System.out.println("-返回值:"+g);
		//String g="<?xml version=\"1.0\" encoding=\"UTF-8\"?><response><error>0</error><message></message></response>";
		if(g.contains("<error>")){
			String[] am=g.split("<error>");
			String[] an=am[1].split("</error>");
			String mm=an[0];
			System.out.println(mm);
		}
	}
	//http  get方式
	public static String sendGet(String mobile,String yz) {
		String url="http://sdkhttp.eucp.b2m.cn/sdkproxy/sendsms.action";//只能在正式服务器上下发验证码
		//String url="http://sdk4http.eucp.b2m.cn/sdkproxy/sendsms.action";//如果测试下发验证码就用这个url
		url+="?cdkey=3SDK-EMY-0130-PDWLP&password=276166&phone="+mobile+"&message=【中国支付网】您好，你的验证码是：";
        url+=yz;
        url+="&smspriority=1";
		String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            //connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
           /* connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");*/
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
          /*  for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }*/
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
}
