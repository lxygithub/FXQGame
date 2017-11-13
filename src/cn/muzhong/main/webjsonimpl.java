package cn.muzhong.main;

public class webjsonimpl {

	public String convertNull(Object obj) {
		if (null == obj || "null".equals(obj)) {
			return "";
		}
		return String.valueOf(obj);
	}

	
	public static boolean Start() {
		boolean result = false;
		try {
			MtListeningServer.Start();
			result = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
	
	public static void main(String[] args) {
		webjsonimpl.Start();
	}
}
