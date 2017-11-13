package cn.muzhong.main;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import cn.emay.gw.util.ServerListen;
import cn.muzhong.dao.fxqdao;
import cn.muzhong.entity.AnswerRecord;
import cn.muzhong.entity.Company;
import cn.muzhong.entity.Customers;
import cn.muzhong.entity.Topic;
import cn.muzhong.util.ChannelConfig;
import cn.muzhong.util.Get;

public class MtListeningServer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(MtListeningServer.class);
	String charsetString = "utf-8";
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setAttribute("org.eclipse.jetty.server.Request.queryEncoding",
				this.charsetString);
		resp.setContentType("application/json");// text/html
		resp.setStatus(200);
		resp.setCharacterEncoding(this.charsetString);
		req.setCharacterEncoding(this.charsetString);
		if (req.getHeader("Access-Control-Request-Method") != null
				&& "OPTIONS".equals(req.getMethod())) {
			resp.addHeader("Access-Control-Allow-Methods",
					"GET, POST, PUT, DELETE");
			resp.addHeader("Access-Control-Allow-Headers", "Content-Type");
			resp.addHeader("Access-Control-Max-Age", "1");
		}
		// resp.setHeader("Access-Control-Allow-Origin", "*");
		// req.setCharacterEncoding(this.charsetString);
		String resString = process(req, resp);
		PrintWriter pw = resp.getWriter();
		pw.print(resString);
		pw.close();
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	private String process(HttpServletRequest req, HttpServletResponse resp)
			throws UnsupportedEncodingException {
		long kaishi = System.currentTimeMillis();
		String respString = "error";
		String respString2 = "";
		String uri = req.getRequestURI().toLowerCase();
		if (this.logger.isDebugEnabled())
			this.logger.debug("uri:" + uri);

		String findyzm = "findyzm";// 获取验证码
		String findtopic = "findtopic";// 题目
		String findresult = "findresult";// 查看答题结果
		String login = "login";// 登录
		String findcompany = "findcompany";// 所有公司--电脑端
		String findcompanysj = "findcompanysj";// 所有公司--手机端
		String tsx = "tsx";// 判断答案
		String updtgf = "updtgf";// 刷新推广分
		String syqm = "syqm";// 获取邀请码  根据手机号
		String addjl = "addjl";// 提交答题--插入答题
		String findbd = "findbd";// 查看榜单--电脑端
		String findbdsj = "findbdsj";// 查看榜单--手机端
		fxqdao f = new fxqdao();
		
		if (uri.indexOf(findcompany) > -1) {// 查询公司名称，自动补全--电脑端
			try {
				List<Company> li = f.FindCompanydn();
				if (li.size() > 0) {
					String jsoncompany = "[";
					for (Company com : li) {
						jsoncompany += "{\"id\":\"" + com.getId()
								+ "\",\"name\":\"" + com.getName() + "\"},";
					}
					jsoncompany = jsoncompany.substring(0,
							jsoncompany.length() - 1);
					jsoncompany += "]";
					respString = jsoncompany;
					// respString="{\"resultCode\":1,\"detailMsg\":\"获取成功\",\"SelectDetail\":"+jsoncompany+"}";
				} else {
					respString = "{\"resultCode\":2,\"detailMsg\":\"获取失败\"}";
				}
				respString2 = "--自动补全-" + this.sdf.format(new Date()) + "--";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (uri.indexOf(findcompanysj) > -1) {// 查询公司名称，自动补全--手机端
			try {
				List<Company> li = f.FindCompanysj();
				if (li.size() > 0) {
					String jsoncompany = "[";
					for (Company com : li) {
						jsoncompany += "{\"id\":\"" + com.getId()
								+ "\",\"name\":\"" + com.getName() + "\"},";
					}
					jsoncompany = jsoncompany.substring(0,
							jsoncompany.length() - 1);
					jsoncompany += "]";
					respString = jsoncompany;
					// respString="{\"resultCode\":1,\"detailMsg\":\"获取成功\",\"SelectDetail\":"+jsoncompany+"}";
				} else {
					respString = "{\"resultCode\":2,\"detailMsg\":\"获取失败\"}";
				}
				respString2 = "--自动补全" + sdf.format(new Date()) + "--";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (uri.indexOf(findtopic) > -1) {// 查询选题
			try {
				String token = req.getParameter("token") == null ? "" : req
						.getParameter("token");
				if ("".equals(token)) {
					respString = "{\"resultCode\":3,\"detailMsg\":\"没有token\"}";
				} else {
					int tok = f.Stoken(token);
					if (tok > 0) {
						String jsontopic = "[";
						List<Topic> li = f.FindTopic();
						if (li.size() > 0) {
							for (Topic topic : li) {
								jsontopic += "{\"id\":\"" + topic.getId()
										+ "\",\"question\":\""
										+ topic.getQuestion() + "\",\"A\":\""
										+ topic.getOptiona() + "\",\"B\":\""
										+ topic.getOptionb() + "\",\"C\":\""
										+ topic.getOptionc() + "\",\"D\":\""
										+ topic.getOptiond() + "\"},";
							}
							jsontopic = jsontopic.substring(0,
									jsontopic.length() - 1);
							jsontopic += "]";
							respString = "{\"resultCode\":1,\"detailMsg\":\"获取成功\",\"SelectDetail\":"
									+ jsontopic + "}";
						} else {
							respString = "{\"resultCode\":2,\"detailMsg\":\"获取失败\"}";
						}
					} else {
						respString = "{\"resultCode\":7,\"detailMsg\":\"登录信息失效\"}";
					}
				}
				respString2 = respString + "查询选题-" + sdf.format(new Date())
						+ "--";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (uri.indexOf(tsx) > -1) {// 对比答案是否正确
			try {
				String tid = req.getParameter("tid") == null ? "" : req
						.getParameter("tid");// 题号
				String answer = req.getParameter("answer") == null ? "" : req
						.getParameter("answer");// 用户选择的答案
				String token = req.getParameter("token") == null ? "" : req
						.getParameter("token");
				if ("".equals(tid) || "".equals(answer)) {
					respString = "{\"resultCode\":3,\"detailMsg\":\"没有题号或者选择的答案\"}";
				} else if ("".equals(token)) {
					respString = "{\"resultCode\":3,\"detailMsg\":\"没有token\"}";
				} else {
					int tok = f.Stoken(token);
					if (tok > 0) {
						String li = f.FindDaAn(tid);// 根据题号查询出的正确答案
						if (li.equals(answer)) {
							respString = "{\"resultCode\":1,\"detailMsg\":\"答案正确\"}";
						} else {
							String jsond = "\"" + li + "\"";
							respString = "{\"resultCode\":2,\"detailMsg\":\"答案错误\",\"SelectDetail\":"
									+ jsond + "}";
						}
					} else {
						respString = "{\"resultCode\":7,\"detailMsg\":\"登录信息失效\"}";
					}
				}
				respString2 = respString + "根据题号查询出的正确答案-"
						+ sdf.format(new Date()) + "--";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (uri.indexOf(updtgf) > -1) {// 刷新推广分
			try {
				String umobile = req.getParameter("mobile") == null ? "" : req
						.getParameter("mobile");
				String token = req.getParameter("token") == null ? "" : req
						.getParameter("token");
				System.out.println(sdf.format(new Date()) + "-刷新推广分接口的参数(手机号):"
						+ umobile);
				System.out.println(sdf.format(new Date()) + "-刷新推广分接口的参数(token):"
						+ token);
				if (umobile == null || "".equals(umobile)) {
					respString = "{\"resultCode\":2,\"detailMsg\":\"手机号格式错误\"}";
				} else if ("".equals(token)) {
					respString = "{\"resultCode\":2,\"detailMsg\":\"没有token\"}";
				} else {
					int tok = f.Stoken(token);
					if (tok > 0) {
						int uli = f.Updtgf(umobile);
						System.out.println(sdf.format(new Date()) + "-刷新推广分接口返回的结果:"
								+ uli);
						if (uli > -1) {
							int jsoncompany = uli;
							respString = "{\"resultCode\":1,\"detailMsg\":\"刷新成功\",\"SelectDetail\":"
									+ jsoncompany + "}";
						} else {
							respString = "{\"resultCode\":2,\"detailMsg\":\"刷新失败\"}";
						}
					} else {
						respString = "{\"resultCode\":7,\"detailMsg\":\"登录信息失效\"}";
					}
				}
				respString2 = respString + "--刷新-" + sdf.format(new Date())
						+ "--";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (uri.indexOf(syqm) > -1) {// 查询邀请码
			try {
				String umobile = req.getParameter("mobile") == null ? "" : req
						.getParameter("mobile");
				String token = req.getParameter("token") == null ? "" : req
						.getParameter("token");
				System.out.println(sdf.format(new Date()) + "-查询邀请码接口的参数(手机号):"
						+ umobile);
				System.out.println(sdf.format(new Date()) + "-查询邀请码接口的参数(token):"
						+ token);
				if (umobile == null || "".equals(umobile)) {
					respString = "{\"resultCode\":2,\"detailMsg\":\"手机号格式错误\"}";
				} else if ("".equals(token)) {
					respString = "{\"resultCode\":3,\"detailMsg\":\"没有token\"}";
				} else {
					int tok = f.Stoken(token);
					if (tok > 0) {
						String uli = f.Syqm(umobile);// 查询当前推广分
						if (StringUtils.isNotBlank(uli)) {
							// String jsoncompany="\""+xtgf+"\"";
							respString = "{\"resultCode\":1,\"detailMsg\":\"获取成功\",\"SelectDetail\":\""
							+ uli + "\"}";
						} else {
							respString = "{\"resultCode\":2,\"detailMsg\":\"获取失败\"}";
						}
					} else {
						respString = "{\"resultCode\":7,\"detailMsg\":\"登录信息失效\"}";
					}
				}
				respString2 = respString + "--分享-" + sdf.format(new Date())
						+ "--";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (uri.indexOf(addjl) > -1) {// 提交答题--插入答题
			try {
				String umobile = req.getParameter("mobile") == null ? "" : req
						.getParameter("mobile");
				String haoshi = req.getParameter("haoshi") == null ? "" : req
						.getParameter("haoshi");
				String truecount = req.getParameter("truecount") == null ? ""
						: req.getParameter("truecount");
				String token = req.getParameter("token") == null ? "" : req
						.getParameter("token");
				System.out.println(sdf.format(new Date()) + "-提交答题接口的参数(mobile):"
						+ umobile+",耗时:"+haoshi+",正确数:"+truecount);
				if (umobile == null || "".equals(umobile)
						|| umobile.length() != 11) {
					respString = "{\"resultCode\":2,\"detailMsg\":\"手机号格式错误\"}";
				} else if (haoshi == null || "".equals(haoshi)) {
					respString = "{\"resultCode\":2,\"detailMsg\":\"没有耗时时间\"}";
				} else if (truecount == null || "".equals(truecount)) {
					respString = "{\"resultCode\":2,\"detailMsg\":\"没有答对的数量\"}";
				} else if (token == null || "".equals(token)) {
					respString = "{\"resultCode\":3,\"detailMsg\":\"没有token\"}";
				} else {
					int tok = f.Stoken(token);// 验证是否有当前token
					if (tok > 0) {
						int uli = f.Updtgf(umobile);// 查询当前推广分
						int times = Integer.parseInt(haoshi);// 耗时-单位秒
						int tc = Integer.parseInt(truecount);// 答对的数量
						uli = uli + tc * 5;// 总分=推广分+卷面分+耗时折算
						int beyond = f.Findbeyond(uli);// 超过百分人数
						int id = f.Findid(umobile);// 用户id--外键
						int xtgf = f.AddCusRe(tc, times, beyond, id, uli,
									umobile);// 更新总分
							if (xtgf > 0) {
								respString = "{\"resultCode\":1,\"detailMsg\":\"提交成功\"}";
							} else {
								respString = "{\"resultCode\":2,\"detailMsg\":\"提交失败\"}";
							}
						//}
					} else {
						respString = "{\"resultCode\":7,\"detailMsg\":\"登录信息失效\"}";
					}
				}
				respString2 = respString + "--提交答题-" + sdf.format(new Date())
						+ "--";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (uri.indexOf(findbd) > -1) {// 查看榜单---电脑端
			try {
				String jsonbd = "[";
				List<Customers> licust = f.BangDandn();
				if (licust.size() > 0) {
					for (Customers cust : licust) {
						jsonbd += "{\"name\":\"" + cust.getName()
								+ "\",\"dtrs\":\"" + cust.getDaticount()
								+ "\",\"gfrs\":\"" + cust.getId() + "\"},";
					}
					jsonbd = jsonbd.substring(0, jsonbd.length() - 1);
					jsonbd += "]";
					respString = "{\"resultCode\":1,\"detailMsg\":\"获取成功\",\"SelectDetail\":"
							+ jsonbd + "}";
				} else {
					respString = "{\"resultCode\":2,\"detailMsg\":\"获取失败\"}";
				}
				jsonbd = "";
				respString2 = respString + "查询榜单-电脑端-" + sdf.format(new Date())
						+ "--";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (uri.indexOf(findbdsj) > -1) {// 查看榜单---手机端
			try {
				String jsonbd = "[";
				List<Customers> licust = f.BangDansj();
				if (licust.size() > 0) {
					for (Customers cust : licust) {
						jsonbd += "{\"name\":\"" + cust.getName()
								+ "\",\"dtrs\":\"" + cust.getDaticount()
								+ "\",\"gfrs\":\"" + cust.getId() + "\"},";
					}
					jsonbd = jsonbd.substring(0, jsonbd.length() - 1);
					jsonbd += "]";
					respString = "{\"resultCode\":1,\"detailMsg\":\"获取成功\",\"SelectDetail\":"
							+ jsonbd + "}";
				} else {
					respString = "{\"resultCode\":2,\"detailMsg\":\"获取失败\"}";
				}
				jsonbd = "";
				respString2 = respString + "查询榜单-手机端-" + sdf.format(new Date())
						+ "--";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (uri.indexOf(findresult) > -1) {// 查看答题结果
			String umobile = req.getParameter("mobile") == null ? "" : req
					.getParameter("mobile");
			String token = req.getParameter("token") == null ? "" : req
					.getParameter("token");

			try {
				System.out.println(sdf.format(new Date()) + "-查看答题结果接口的参数(mobile):"
						+ umobile);
				System.out.println(sdf.format(new Date()) + "-查看答题结果接口的参数(token):"
						+ token);
				if (umobile == null || "".equals(umobile)
						|| umobile.length() != 11) {
					respString = "{\"resultCode\":2,\"detailMsg\":\"手机号格式错误\"}";
				} else if (token == null || "".equals(token)) {
					respString = "{\"resultCode\":3,\"detailMsg\":\"没有token\"}";
				} else {
					int tok = f.Stoken(token);// 验证是否有当前token
					//System.out.println(tok);
					if (tok > 0) {
					List<AnswerRecord> ar = f.FindRecord(umobile);
					if (ar.size() > 0) {
						String res = "{\"beyond\":" + ar.get(0).getBeyond()
								+ ",\"truecount\":" + ar.get(0).getTruecount()
								+ ",\"haoshi\":" + ar.get(0).getTimes()
								+ ",\"tgf\":" + ar.get(0).getId() + "}";
						respString = "{\"resultCode\":1,\"detailMsg\":\"获取成功\",\"SelectDetail\":"
								+ res + "}";
					} else {
						respString = "{\"resultCode\":2,\"detailMsg\":\"获取失败\"}";
					}
					} else {
						respString = "{\"resultCode\":7,\"detailMsg\":\"登录信息失效\"}";
					}
				}
				respString2 = "--查看答题结果" + sdf.format(new Date()) + "--";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (uri.indexOf(findyzm) > -1) {// 获取验证码
			try {
				String mobile = req.getParameter("mobile") == null ? "" : req
						.getParameter("mobile");
				System.out.println(sdf.format(new Date()) + "-获取验证码接口的参数(手机号):"
						+ mobile);
				//检查phone是否是合格的手机号
				 String str = "[1]{1}(([3]{1}[0-9]{1})|([4]{1}[57]{1})|([5]{1}[0123456789]" +
				 		"{1})|([7]{1}[0135678]{1})|([8]{1}[0123456789]{1}))\\d{8}";
				if (mobile.equals("")) {
					respString = "{\"resultCode\":2,\"detailMsg\":\"手机号为空\"}";
				}else if (!mobile.matches(str)) {
					respString = "{\"resultCode\":2,\"detailMsg\":\"手机号格式错误\"}";
				} /*else if ("13592091866".equals(mobile)||"13693653460".equals(mobile)) {
					int r = f.Updy(9876, mobile);// 把验证码入库
					if (r > 0) {
						respString = "{\"resultCode\":1,\"detailMsg\":\"获取成功\"}";
					} else {
						respString = "{\"resultCode\":2,\"detailMsg\":\"获取失败\"}";
					}
				}*/ else {
					int a = (int) (Math.random() * (9999 - 1000 + 1)) + 1000;
					String yz = a + "";
					String g = Get.sendGet(mobile, yz);
					// String g="<error>0</error>";
					if (g.contains("<error>")) {
						String[] am = g.split("<error>");
						String[] an = am[1].split("</error>");
						String mm = an[0];
						// System.out.println(mm);
						if (mm.equals("0")) {
							System.out.println(sdf.format(new Date()) +"-获取验证码(成功)接口:手机号:"+mobile +",验证码:"+ yz);
							f.Dely(mobile);//删除验证码
							int r = f.Updy(a, mobile);// 把验证码入库
							if (r > 0) {
								respString = "{\"resultCode\":1,\"detailMsg\":\"获取成功\"}";
							} else {
								respString = "{\"resultCode\":2,\"detailMsg\":\"获取失败\"}";
							}

						} else {
							System.out.println(sdf.format(new Date()) +"-获取验证码(失败)接口:手机号:"+mobile +",验证码:"+ yz);
							respString = "{\"resultCode\":2,\"detailMsg\":\"获取失败\"}";
						}
					}else{
						System.out.println(sdf.format(new Date()) +"-获取验证码(失败):手机号:"+mobile +",验证码:"+ yz+"--短信通道商返回值="+g);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (uri.indexOf(login) > -1) {// 注册
			try {
				String cname = req.getParameter("cname") == null ? "" : req
						.getParameter("cname");
				String mobile = req.getParameter("mobile") == null ? "" : req
						.getParameter("mobile");
				String companyid = req.getParameter("companyid") == null ? ""
						: req.getParameter("companyid");
				String yzm = req.getParameter("yzm") == null ? "" : req
						.getParameter("yzm");
				String yqm = req.getParameter("yqm") == null ? "" : req
						.getParameter("yqm");//被推荐码
				System.out.println(sdf.format(new Date()) + "-注册接口的参数(手机号):"
						+ mobile);
				System.out.println(sdf.format(new Date()) + "-注册接口的参数(验证码):"
						+ yzm);
				System.out.println(sdf.format(new Date()) + "-注册接口的参数(邀请码):"
						+ yqm);
				 //检查phone是否是合格的手机号
				 String str = "[1]{1}(([3]{1}[0-9]{1})|([4]{1}[57]{1})|([5]{1}[0123456789]" +
				 		"{1})|([7]{1}[0135678]{1})|([8]{1}[0123456789]{1}))\\d{8}";
				if (yzm.equals("") || yzm.length() != 4) {
					respString = "{\"resultCode\":3,\"detailMsg\":\"验证码格式错误\"}";
				} else if (mobile.equals("")) {
					respString = "{\"resultCode\":2,\"detailMsg\":\"手机号为空\"}";
				}else if (!mobile.matches(str)) {
					respString = "{\"resultCode\":2,\"detailMsg\":\"手机号格式错误\"}";
				}else if (cname.equals("") || cname.length() > 8) {
					respString = "{\"resultCode\":2,\"detailMsg\":\"姓名格式错误\"}";
				} else if (companyid.equals("") || companyid == null) {
					respString = "{\"resultCode\":2,\"detailMsg\":\"没有公司名称错误\"}";
				}else if (!yqm.equals("")&&yqm.length()<5) {//
					respString = "{\"resultCode\":2,\"detailMsg\":\"邀请码格式错误\"}";
				} else {
					int y = f.Findy(mobile);
					if (y > 0) {//||yzm.equals("9876")
						String a1 = y + "";
						/*if(yzm.equals("9876")){
							a1="9876";
						}*/
						// System.out.println("随机的验证码是:--"+a1);
						 if (!yzm.equals(a1)) {
							respString = "{\"resultCode\":4,\"detailMsg\":\"验证码错误\"}";
						} else {
							f.Dely(mobile);//删除验证码
							int dt = f.Finddaticount(mobile);
							if (dt == 0) {// 注册过还未答过题返回mobile和token
								System.out.println("已注册过！");
								String token = f.Updtoken(mobile);
								respString = "{\"resultCode\":6,\"detailMsg\":\"您已注册过\",\"mobile\":\""
										+ mobile
										+ "\",\"token\":\""
										+ token
										+ "\"}";
							} else if (dt == -1) {// 第一次注册
								String a = f.Login(cname, companyid, mobile,yqm);
								if (!"".equals(a)) {// 返回手机号提交题时验证
									respString = "{\"resultCode\":1,\"detailMsg\":\"注册成功\",\"mobile\":\""
											+ mobile
											+ "\",\"token\":\""
											+ a
											+ "\"}";
								} else {
									respString = "{\"resultCode\":2,\"detailMsg\":\"注册失败\"}";
								}
							} else {// 注册过并已答过题返回mobile和token
								String token = f.Updtoken(mobile);
								respString = "{\"resultCode\":5,\"detailMsg\":\"您已答过题\",\"mobile\":\""
										+ mobile
										+ "\",\"token\":\""
										+ token
										+ "\"}";
							}
						}

					} else {
						respString = "{\"resultCode\":8,\"detailMsg\":\"读取验证码失败\"}";// 读取验证码失败
					}
				}
				respString2 = respString + "注册--" + sdf.format(new Date())
						+ "--";
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		long jiesu = System.currentTimeMillis();
		System.out.println(respString2 + "耗时：" + (jiesu - kaishi));
		return respString;
	}

	public String convertNull(Object obj) {
		if (null == obj || "null".equals(obj)) {
			return "";
		}
		return String.valueOf(obj);
	}

	public static boolean Start() {
		boolean result = false;
		try {
			ServerListen.GetServerListen().Start();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void main(String[] args) throws Exception {
		try {
			ServerListen.GetServerListen().Start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
