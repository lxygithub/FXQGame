package cn.muzhong.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import cn.muzhong.entity.AnswerRecord;
import cn.muzhong.entity.Company;
import cn.muzhong.entity.Customers;
import cn.muzhong.entity.Topic;
import cn.muzhong.util.ConnectionPool;
import cn.muzhong.util.ShareCodeUtil;

public class fxqdao
{
    ConnectionPool c = ConnectionPool.getInstance();

    // 查询公司名称，自动补全--电脑端
    public List<Company> FindCompanydn()
    {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List<Company> listcompany = new ArrayList<Company>();
        try
        {
            conn = c.getConnection();
            String sql = "select id,name from company ";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next())
            {
                Company c = new Company();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                listcompany.add(c);
            }
            // System.out.println(listcompany.size());
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return listcompany;
    }

    // 查询公司名称，自动补全--手机端
    public List<Company> FindCompanysj()
    {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List<Company> listcompany = new ArrayList<Company>();
        try
        {
            conn = c.getConnection();
            String sql = "select id,namejc from company ";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next())
            {
                Company c = new Company();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("namejc"));
                listcompany.add(c);
            }
            // System.out.println(listcompany.size());
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return listcompany;
    }

    // 判断是否答过题
    public int Finddaticount(String mobile)
    {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        int cu = -1;
        try
        {
            conn = c.getConnection();
            String sql = "select daticount from customers where mobile=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, mobile);
            rs = ps.executeQuery();
            while (rs.next())
            {
                cu = rs.getInt("daticount");
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return cu;
    }

    // 根据手机号来修改验证码
    public int Updy(int yzm, String mobile)
    {
        int ym = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        try
        {
            conn = c.getConnection();
            String sql = "insert into ym(yam,mobile,cdtime) values(?,?,now())";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, yzm);
            ps.setString(2, mobile);
            ym = ps.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return ym;
    }


    // 查询验证码
    public int Findy(String mobile)
    {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        int cu = 0;
        try
        {
            conn = c.getConnection();
            String sql = "select yam from ym where mobile=? order by cdtime desc";
            ps = conn.prepareStatement(sql);
            ps.setString(1, mobile);
            rs = ps.executeQuery();
            while (rs.next())
            {
                cu = rs.getInt("yam");
                break;
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return cu;
    }

    // 根据手机号来删除验证码
    public int Dely(String mobile)
    {
        int ym = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        try
        {
            conn = c.getConnection();
            String sql = "delete from ym where mobile=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, mobile);
            ym = ps.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }

        }
        return ym;
    }

    // 如果已答过题就显示上次的答题结果
    public List<AnswerRecord> FindRecord(String mobile)
    {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List<AnswerRecord> listar = new ArrayList<AnswerRecord>();
        try
        {
            conn = c.getConnection();
            String sql = "select beyond,truecount,haoshi,fenxiangcount from customers c,answerrecord a where c.id=a.customerid and mobile=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, mobile);
            rs = ps.executeQuery();
            while (rs.next())
            {
                AnswerRecord c = new AnswerRecord();
                c.setBeyond(rs.getInt("beyond"));// 超过人数的百分比
                c.setTruecount(rs.getInt("truecount"));// 答对题数
                c.setTimes(rs.getInt("haoshi"));// 耗时
                c.setId(rs.getInt("fenxiangcount"));// 为了显示推广分
                listar.add(c);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }

        }
        return listar;
    }

    // 注册用户
    public String Login(String name, String company, String mobile, String yqm)
    {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatement ps3 = null;
        PreparedStatement ps2 = null;
        int a = 0;
        String b = "";
        Customers cc = null;
        try
        {
            //System.out.println("yqm="+yqm);
            conn = c.getConnection();
            if (StringUtils.isNotBlank(yqm))
            {
                int tgf = 0;
                //先查询当前推广分，然后根据推荐码来更新推广分
                String sql3 = "select * from customers where yqm=?";
                ps3 = conn.prepareStatement(sql3);
                ps3.setString(1, yqm);
                rs = ps3.executeQuery();
                while (rs.next())
                {
                    cc = new Customers();
                    cc.setId(rs.getInt("id"));
                    cc.setFenxiangcount(rs.getInt("fenxiangcount"));
                    cc.setFena(rs.getInt("fena"));
                    cc.setFxr(rs.getString("fxr"));
                    cc.setYqm(rs.getString("yqm"));
                }
                if (cc != null)
                {
                    tgf = cc.getFenxiangcount() + 1;//分享成功+1
                    int fena = cc.getFena() + 1;
                    String sql2 = "update customers set fenxiangcount=?,fena=? where yqm=?";
                    ps2 = conn.prepareStatement(sql2);
                    ps2.setInt(1, tgf);
                    ps2.setInt(2, fena);
                    ps2.setString(3, yqm);
                    ps2.executeUpdate();
                }
            }

            String tok = UUID.randomUUID().toString();
            String s = ShareCodeUtil.jiami(mobile);
            // yqm邀请码(不为空)自己用
            String sql = "insert into customers(cname,mobile,companyid,fenxiangcount,daticount,"
                    + "fena,fenb,createtime,token,yqm";
            if (StringUtils.isNotBlank(yqm))
            {
                sql += ",fxr";
            }
            sql += ") values(?,?,?,0,0,0,0,now(),'" + tok + "','" + s + "'";
            if (StringUtils.isNotBlank(yqm))
            {
                sql += "," + cc.getId();
            }
            sql += ")";
            //System.out.println(sql);
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, mobile);
            ps.setString(3, company);
            a = ps.executeUpdate();
            if (a > 0)
            {
                b = tok;
            } else
            {
                b = "";
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (rs != null) rs.close();
                if (ps3 != null) ps3.close();
                if (ps2 != null) ps2.close();
                if (ps != null) ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }

        }
        return b;
    }

    // 第二次登录刷新token
    public String Updtoken(String mobile)
    {
        String tok = "";
        Connection conn = null;
        PreparedStatement ps = null;
        try
        {
            conn = c.getConnection();
            tok = UUID.randomUUID().toString();
            String sql = "update customers set token='" + tok
                    + "' where mobile=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, mobile);
            int ym = ps.executeUpdate();
            if (ym == 0)
            {
                tok = "";
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }

        }
        return tok;
    }

    // 查询当前token，验证token是否一致
    public int Stoken(String token)
    {
        int v = 0;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try
        {
            conn = c.getConnection();
            String sql = "select id from customers where token=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, token);
            rs = ps.executeQuery();
            while (rs.next())
            {
                v = rs.getInt("id");
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }

        }
        return v;
    }


    // 查询20道题
    public List<Topic> FindTopic()
    {
        // 400以内随机20个不重复的数
        String li = "";
        Set<Integer> set = new HashSet<Integer>();
        Random random = new Random();
        int num = 0;
        for (; true; )
        {
            num = random.nextInt(390);
            set.add(num);
            if (set.size() >= 20)
            {
                break;
            }
        }
        for (int a : set)
        {
            li += a + ",";
        }
        li = li.substring(0, li.length() - 1);
        //System.out.println("获取的题的数量="+li);
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List<Topic> listtopic = new ArrayList<Topic>();
        try
        {
            conn = c.getConnection();
            int h = 0;
            String sql = "select * from topic where id in(" + li + ")";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next())
            {
                h++;
                Topic c = new Topic();
                c.setId(rs.getInt("id"));
                c.setQuestion(h + "、" + rs.getString("question").trim());
                c.setOptiona(rs.getString("optiona").trim());
                c.setOptionb(rs.getString("optionb").trim());
                c.setOptionc(rs.getString("optionc").trim());
                c.setOptiond(rs.getString("optiond").trim());
                // c.setAnswer(rs.getString("answer"));
                listtopic.add(c);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }

        }
        return listtopic;
    }

    // 刷新推广分
    public int Updtgf(String mobile)
    {
        int tgf = -1;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try
        {
            conn = c.getConnection();
            String sql = "select fenxiangcount from customers where mobile=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, mobile);
            rs = ps.executeQuery();
            while (rs.next())
            {
                tgf = rs.getInt("fenxiangcount");
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }

        }
        return tgf;
    }

    // 查询邀请码，加在注册url后---分享加分功能
    public String Syqm(String mobile)
    {
        String v = "";
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try
        {
            conn = c.getConnection();
            String sql = "select yqm from customers where mobile=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, mobile);
            rs = ps.executeQuery();
            while (rs.next())
            {
                v = rs.getString(1);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }

        }
        return v;
    }

    // 分享--添加更新推广分
    public int Updatetgf(int tgf, String mobile)
    {
        Connection conn = null;
        PreparedStatement ps = null;
        int b = 0;
        try
        {
            conn = c.getConnection();
            String sql = "update customers set fenxiangcount=? where mobile=?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, tgf);
            ps.setString(2, mobile);
            b = ps.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }

        }
        return b;
    }

    // 查找插入AnswerRecord所用的customerid
    public int Findid(String mobile)
    {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        int cu = -1;
        try
        {
            conn = c.getConnection();
            String sql = "select id from customers where mobile=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, mobile);
            rs = ps.executeQuery();
            while (rs.next())
            {
                cu = rs.getInt("id");
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }

        }
        return cu;
    }

    // 提交答题--插入答题
    public synchronized int AddCusRe(int truecount, int times, int beyond, int customerid,
                                     int fena, String mobile)
    {
        Connection conn = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        PreparedStatement ps3 = null;
        int b = 0;
        int d = 0;
        try
        {
            conn = c.getConnection();
            //防止并发量过大重复提交，先验证是否已经有答题记录，如果有，什么都不执行
            String sql2 = "select a.id as id from answerrecord a,customers c where a.customerid=c.id and mobile=?";
            ps3 = conn.prepareStatement(sql2);
            ps3.setString(1, mobile);
            rs = ps3.executeQuery();
            while (rs.next())
            {
                d = rs.getInt("id");
            }
            if (d < 1)
            {//无答题记录
                conn.setAutoCommit(false);
                String sql = "update customers set fena=?,daticount=1 where mobile=?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, fena);
                ps.setString(2, mobile);
                ps.executeUpdate();
                String sql1 = "insert into answerrecord(truecount,haoshi,beyond,customerid,tijiaotime)"
                        + " values(?,?,?,?,now())";
                ps2 = conn.prepareStatement(sql1);
                ps2.setInt(1, truecount);
                ps2.setInt(2, times);
                ps2.setInt(3, beyond);
                ps2.setInt(4, customerid);
                b = ps2.executeUpdate();
                conn.commit();
            } else
            {
                b = 1;
            }
        } catch (SQLException e)
        {
            try
            {
                conn.rollback();
            } catch (SQLException e1)
            {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally
        {
            try
            {
                if (rs != null)
                    rs.close();
                if (ps3 != null)
                    ps3.close();
                if (ps != null)
                    ps.close();
                if (ps2 != null)
                    ps2.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }

        }
        return b;
    }

    // 根据选题id得出正确答案和用户选的选项做判断是否正确
    public String FindDaAn(String id)
    {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String daan = "";
        try
        {
            conn = c.getConnection();
            String sql = "select answer from topic where id=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();
            while (rs.next())
            {
                daan = rs.getString("answer");
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }

        }
        return daan;
    }

    // 查询榜单-电脑端
    public List<Customers> BangDandn()
    {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        ResultSet rs2 = null;
        PreparedStatement ps2 = null;
        List<Customers> listcu = new ArrayList<Customers>();
        try
        {
            conn = c.getConnection();
            int maxrenshu = 0;
            StringBuffer s1 = new StringBuffer();
            s1.append("SELECT a.zrs+a.cj as count FROM (SELECT co.name AS cname,co.cj as cj,COUNT(*) AS zrs FROM customers cu ");
            s1.append("INNER JOIN company co ON cu.companyid=co.id GROUP BY co.name) a ");
            s1.append("WHERE a.zrs=(SELECT MAX(b.zrs) FROM (SELECT co.name AS cname,COUNT(*) AS zrs ");
            s1.append("FROM customers cu INNER JOIN company co ON cu.companyid=co.id GROUP BY co.name) b)");
            ps2 = conn.prepareStatement(s1.toString());
            rs2 = ps2.executeQuery();
            while (rs2.next())
            {
                maxrenshu = rs2.getInt(1);// 参加答题的某个公司的最多的人数
            }
            // System.out.println("--最多的人数--"+maxrenshu);
            StringBuffer s = new StringBuffer();
            /*s.append("SELECT a.cname as gsm,a.zrs as czrs,b.gfs as gfrs FROM (SELECT co.name AS cname,COUNT(*) AS zrs ");
			s.append("FROM customers cu INNER JOIN company co ON cu.companyid=co.id ");
			s.append("GROUP BY co.name) a LEFT JOIN ");
			s.append("(SELECT co.name AS cname,COUNT(*) AS gfs FROM customers cu ");
			s.append("INNER JOIN company co ON cu.companyid=co.id WHERE ");
			s.append("cu.fena>=80 GROUP BY co.name) b ON a.cname=b.cname order BY a.zrs desc limit 0,30");*/

            s.append("SELECT ab.gsm as gsm,ab.czrs+ab.cj as czrs,ab.gfrs+ab.gf as gfrs from (SELECT a.cname as gsm,a.zrs as czrs,IFNULL(b.gfs,0) as gfrs,a.cj,a.gf FROM (SELECT co.name AS cname,co.cj,co.gf,COUNT(*) AS zrs ");
            s.append("FROM customers cu INNER JOIN company co ON cu.companyid=co.id ");
            s.append("GROUP BY co.name) a LEFT JOIN ");
            s.append("(SELECT co.name AS cname,COUNT(*) AS gfs FROM customers cu ");
            s.append("INNER JOIN company co ON cu.companyid=co.id WHERE ");
            s.append("cu.fena>=80 GROUP BY co.name) b ON a.cname=b.cname order BY a.zrs desc) ab order BY czrs desc limit 0,30");
            // String
            // s="select name,fena from Company c,Customers d where fena=0 and d.companyid=c.id limit 0,30";
            ps = conn.prepareStatement(s.toString());
            rs = ps.executeQuery();
            int baifenbi = 0;
            while (rs.next())
            {
                Customers c = new Customers();
                c.setName(rs.getString("gsm"));// 公司全称
                baifenbi = (int) (((double) rs.getInt("czrs") / (double) maxrenshu) * 100);
                c.setDaticount(baifenbi);// 参加人数--在电脑端显示百分数
                c.setId(rs.getInt("gfrs"));// 高分人数
                listcu.add(c);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (rs2 != null)
                    rs2.close();
                if (rs != null)
                    rs.close();
                if (ps2 != null)
                    ps2.close();
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }

        }
        return listcu;
    }

    // 查询榜单-手机端
    public List<Customers> BangDansj()
    {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        ResultSet rs2 = null;
        PreparedStatement ps2 = null;
        List<Customers> listcu = new ArrayList<Customers>();
        try
        {
            conn = c.getConnection();
            int maxrenshu = 0;
            StringBuffer s1 = new StringBuffer();
            s1.append("SELECT a.zrs+a.cj as count FROM (SELECT co.name AS cname,co.cj as cj,COUNT(*) AS zrs FROM customers cu ");
            s1.append("INNER JOIN company co ON cu.companyid=co.id GROUP BY co.name) a ");
            s1.append("WHERE a.zrs=(SELECT MAX(b.zrs) FROM (SELECT co.name AS cname,COUNT(*) AS zrs ");
            s1.append("FROM customers cu INNER JOIN company co ON cu.companyid=co.id GROUP BY co.name) b)");
            ps2 = conn.prepareStatement(s1.toString());
            rs2 = ps2.executeQuery();
            while (rs2.next())
            {
                maxrenshu = rs2.getInt(1);// 参加答题的某个公司的最多的人数
            }
            // System.out.println("--最多的人数--"+maxrenshu);
            StringBuffer s = new StringBuffer();
			/*s.append("SELECT a.cname as gsm,a.zrs as czrs,b.gfs as gfrs FROM (SELECT co.name AS cname,COUNT(*) AS zrs ");
			s.append("FROM customers cu INNER JOIN company co ON cu.companyid=co.id ");
			s.append("GROUP BY co.name) a LEFT JOIN ");
			s.append("(SELECT co.name AS cname,COUNT(*) AS gfs FROM customers cu ");
			s.append("INNER JOIN company co ON cu.companyid=co.id WHERE ");
			s.append("cu.fena>=80 GROUP BY co.name) b ON a.cname=b.cname order BY a.zrs desc limit 0,30");*/

            s.append("SELECT ab.gsm as gsm,ab.czrs+ab.cj as czrs,ab.gfrs+ab.gf as gfrs from ( ");
            s.append("SELECT a.cname as gsm,a.zrs as czrs,IFNULL(b.gfs,0) as gfrs,a.cj,a.gf FROM (SELECT co.namejc AS cname,co.cj,");
            s.append("co.gf,COUNT(*) AS zrs FROM customers cu INNER JOIN company co ON cu.companyid=co.id ");
            s.append("GROUP BY co.namejc) a LEFT JOIN (SELECT co.namejc AS cname,COUNT(*) AS gfs FROM customers cu ");
            s.append("INNER JOIN company co ON cu.companyid=co.id WHERE ");
            s.append("cu.fena>=80 GROUP BY co.namejc) b ON a.cname=b.cname order BY a.zrs desc) ab order BY czrs desc limit 0,30");
            // String
            // s="select name,fena from Company c,Customers d where fena=0 and d.companyid=c.id limit 0,30";
            ps = conn.prepareStatement(s.toString());
            rs = ps.executeQuery();
            int baifenbi = 0;
            while (rs.next())
            {
                Customers c = new Customers();
                c.setName(rs.getString("gsm"));// 公司简称
                baifenbi = (int) (((double) rs.getInt("czrs") / (double) maxrenshu) * 100);
                c.setDaticount(baifenbi);// 参加人数--在手机端显示百分数
                c.setId(rs.getInt("gfrs"));// 高分人数
                listcu.add(c);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (rs2 != null)
                    rs2.close();
                if (rs != null)
                    rs.close();
                if (ps2 != null)
                    ps2.close();
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }

        }
        return listcu;
    }

    // 查询榜单-手机端
/*	public List<Customers> BangDansj() {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		List<Customers> listcu = new ArrayList<Customers>();
		try {
			conn = c.getConnection();
			StringBuffer s = new StringBuffer();
			公司全称，参加人数，高分人数
			 * s.append("SELECT ab.gsm as gsm,ab.czrs+ab.cj as czrs,ab.gfrs+ab.gf as gfrs from " +
					"(SELECT a.cname as gsm,a.zrs as czrs,IFNULL(b.gfs,0) as gfrs,a.cj,a.gf" +
					" FROM (SELECT co.name AS cname,co.cj,co.gf,COUNT(*) AS zrs ");
			s.append("FROM customers cu INNER JOIN company co ON cu.companyid=co.id ");
			s.append("GROUP BY co.name) a LEFT JOIN ");
			s.append("(SELECT co.name AS cname,COUNT(*) AS gfs FROM customers cu ");
			s.append("INNER JOIN company co ON cu.companyid=co.id WHERE ");
			s.append("cu.fena>=80 GROUP BY co.name) b ON a.cname=b.cname order BY a.zrs desc limit 0,30) ab");
			
			//公司简称，参加人数，高分人数
			s.append("SELECT ab.gsm as gsm,ab.czrs+ab.cj as czrs,ab.gfrs+ab.gf as gfrs from ");
			s.append("(SELECT a.cname as gsm,a.zrs as czrs,IFNULL(b.gfs,0) as gfrs,a.cj,a.gf ");
			s.append(" FROM (SELECT co.namejc AS cname,co.cj,co.gf,COUNT(*) AS zrs ");
			s.append("FROM customers cu INNER JOIN company co ON cu.companyid=co.id ");
			s.append("GROUP BY co.namejc) a LEFT JOIN ");
			s.append("(SELECT co.namejc AS cname,COUNT(*) AS gfs FROM customers cu ");
			s.append("INNER JOIN company co ON cu.companyid=co.id WHERE ");
			s.append("cu.fena>=80 GROUP BY co.name) b ON a.cname=b.cname order BY a.zrs desc limit 0,30) ab");
			ps = conn.prepareStatement(s.toString());
			rs = ps.executeQuery();
			while (rs.next()) {
				Customers c = new Customers();
				c.setName(rs.getString("gsm"));// 公司全称
				c.setDaticount(rs.getInt("czrs"));// 参加人数
				// System.out.println(c.getDaticount());
				c.setId(rs.getInt("gfrs"));// 高分人数
				listcu.add(c);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		return listcu;
	}*/

    // 查找总人数和比我得分低的人，计算出超过的百分人数在结果界面显示--比我得分低的人数/总人数
    public int Findbeyond(int fenshu)
    {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        ResultSet rs2 = null;
        PreparedStatement ps2 = null;
        int zongrenshu = 0;
        int difenrenshu = 0;
        int beyong = 0;
        try
        {
            conn = c.getConnection();
            String sql = "select count(*) from customers";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next())
            {
                zongrenshu = rs.getInt(1);
            }
            if (zongrenshu > 0)
            {
                String sql2 = "select count(*) from customers where fena<?";
                ps2 = conn.prepareStatement(sql2);
                ps2.setInt(1, fenshu);
                rs2 = ps2.executeQuery();
                while (rs2.next())
                {
                    difenrenshu = rs2.getInt(1);
                }
                beyong = (int) (((double) difenrenshu / (double) zongrenshu) * 100);
                System.out.println("百分比=" + beyong + "--总人数=" + zongrenshu
                        + "--低分人数=" + difenrenshu);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (rs2 != null)
                    rs2.close();
                if (rs != null)
                    rs.close();
                if (ps2 != null)
                    ps2.close();
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }

        }
        return beyong;
    }
}
