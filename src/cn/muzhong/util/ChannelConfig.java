package cn.muzhong.util;


import cn.emay.common.XMLConfig;

public class ChannelConfig {
	public static String getcharset() {
		return XMLConfig.GetXMLConfig().AppSettings().getProperty("charset");
	}
	
	public static String getsendmethod() {
		return XMLConfig.GetXMLConfig().AppSettings().getProperty("sendmethod");
	}
	
	public static String getreport() {
		return XMLConfig.GetXMLConfig().AppSettings().getProperty("report");
	}
	
	public static String getmo() {
		return XMLConfig.GetXMLConfig().AppSettings().getProperty("mo");
	}
	
	public static String getregistEx() {
		return XMLConfig.GetXMLConfig().AppSettings().getProperty("registEx");
	}
	
	public static String getRegistDetailInfo() {
		return XMLConfig.GetXMLConfig().AppSettings().getProperty("RegistDetailInfo");
	}
	
	public static String getBalance() {
		return XMLConfig.GetXMLConfig().AppSettings().getProperty("Balance");
	}
	
	public static String getip() {
		return XMLConfig.GetXMLConfig().AppSettings().getProperty("ip");
	}
}