package exp.bilibili.plugin.bean.ldm;

import java.sql.Connection;
import java.util.List;

import exp.libs.warp.db.sql.DBUtils;

/**
 * <PRE>
 * Table Name : T_ACTIVITY
 * Class Name : TActivity
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-01-23 14:46:29
 * @author    EXP: 272629724@qq.com
 * @since     jdk version : jdk 1.6
 */
public class TActivity  {
    
    /** insert sql */
    public final static String SQL_INSERT = 
            "INSERT INTO T_ACTIVITY(I_PERIOD, S_UID, S_USERNAME, I_COST, I_ROOMID) VALUES(?, ?, ?, ?, ?)";
    
    /** delete sql */
    public final static String SQL_DELETE = 
            "DELETE FROM T_ACTIVITY WHERE 1 = 1 ";
    
    /** update sql */
    public final static String SQL_UPDATE = 
            "UPDATE T_ACTIVITY SET I_PERIOD = ?, S_UID = ?, S_USERNAME = ?, I_COST = ?, I_ROOMID = ? WHERE 1 = 1 ";
    
    /** select sql */
    public final static String SQL_SELECT = 
            "SELECT I_PERIOD AS 'period', S_UID AS 'uid', S_USERNAME AS 'username', I_COST AS 'cost', I_ROOMID AS 'roomid' FROM T_ACTIVITY WHERE 1 = 1 ";

    /** I_PERIOD */
    private Integer period;

    /** S_UID */
    private String uid;

    /** S_USERNAME */
    private String username;

    /** I_COST */
    private Integer cost;

    /** I_ROOMID */
    private Integer roomid;

    /**
     * insert the bean of TActivity to db.
     * 
     * @param conn : the connection of db
     * @param bean : a bean of the data
     * @return effect of row number
     */
    public static boolean insert(Connection conn, TActivity bean) {
        Object[] params = new Object[] {
                bean.getPeriod(),
                bean.getUid(),
                bean.getUsername(),
                bean.getCost(),
                bean.getRoomid()
        };
        return DBUtils.execute(conn, TActivity.SQL_INSERT, params);
    }
    
    /**
     * delete some bean of TActivity from db with some conditions.
     * 
     * @param conn : the connection of db
     * @param where : 
     *      The conditions of sql, must start with "and" (Ignoring the case).
     *      For example: "and id > 1 and name like Exp%"
     * @return effect of row number
     */
    public static boolean delete(Connection conn, String where) {
        StringBuilder sql = new StringBuilder();
        sql.append(TActivity.SQL_DELETE);
        
        if (where != null && !"".equals(where.trim())) {
            if(false == where.toLowerCase().trim().startsWith("and")) {
                sql.append("AND ");
            }
            sql.append(where);
        }
        return DBUtils.execute(conn, sql.toString());
    }
    
    /**
     * update some bean of TActivity to db with some conditions.
     * 
     * @param conn : the connection of db
     * @param bean : a bean of the data
     * @param where :
     *      The conditions of sql, must start with "and" (Ignoring the case).
     *      For example: "and id > 1 and name like Exp%"
     * @return effect of row number
     */
    public static boolean update(Connection conn, TActivity bean, String where) {
        StringBuilder sql = new StringBuilder();
        sql.append(TActivity.SQL_UPDATE);
        
        if (where != null && !"".equals(where.trim())) {
            if(false == where.toLowerCase().trim().startsWith("and")) {
                sql.append("AND ");
            }
            sql.append(where);
        }
        Object[] params = new Object[] {
                bean.getPeriod(),
                bean.getUid(),
                bean.getUsername(),
                bean.getCost(),
                bean.getRoomid()
        };
        return DBUtils.execute(conn, sql.toString(), params);
    }    
    
    /**
     * query all beans of TActivity from db.
     * 
     * @param conn : the connection of db
     * @return all beans of the data set
     */
    public static List<TActivity> queryAll(Connection conn) {
        return querySome(conn, null);
    }
    
    /**
     * query a bean of TActivity from db with some conditions.
     * If the conditions <B>can't</B> lock the range of one record,
     * you will get <B>the first record</B> or <B>null</B>.
     *
     * @param conn : the connection of db
     * @param where :
     *      The conditions of sql, must start with "and" (Ignoring the case).
     *      For example: "and id > 1 and name like Exp%"
     * @return a beans of the data
     */
    public static TActivity queryOne(Connection conn, String where) {
        TActivity bean = null;
        List<TActivity> beans = querySome(conn, where);
        if(beans != null && beans.size() > 0) {
            bean = beans.get(0);
        }
        return bean;
    }
    
    /**
     * query some beans of TActivity from db with some conditions.
     * 
     * @param conn : the connection of db
     * @param where :
     *      The conditions of sql, must start with "and" (Ignoring the case).
     *      For example: "and id > 1 and name like Exp%"
     * @return some beans of the data set
     */
    public static List<TActivity> querySome(Connection conn, String where) {
        StringBuilder sql = new StringBuilder();
        sql.append(TActivity.SQL_SELECT);
        
        if (where != null && !"".equals(where.trim())) {
            if(false == where.toLowerCase().trim().startsWith("and")) {
                sql.append("AND ");
            }
            sql.append(where);
        }
        return DBUtils.query(TActivity.class, conn, sql.toString());
    }
    
    /**
     * getPeriod
     * @return Integer
     */
    public Integer getPeriod() {
        return this.period;
    }

    /**
     * setPeriod
     * @param period period to set
     */
    public void setPeriod(Integer period) {
        this.period = period;
    }

    /**
     * getUid
     * @return String
     */
    public String getUid() {
        return this.uid;
    }

    /**
     * setUid
     * @param uid uid to set
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * getUsername
     * @return String
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * setUsername
     * @param username username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * getCost
     * @return Integer
     */
    public Integer getCost() {
        return this.cost;
    }

    /**
     * setCost
     * @param cost cost to set
     */
    public void setCost(Integer cost) {
        this.cost = cost;
    }

    /**
     * getRoomid
     * @return Integer
     */
    public Integer getRoomid() {
        return this.roomid;
    }

    /**
     * setRoomid
     * @param roomid roomid to set
     */
    public void setRoomid(Integer roomid) {
        this.roomid = roomid;
    }

    /**
     * get column name
     * @return I_PERIOD
     */
    public final static String CN$I_PERIOD() {
        return "I_PERIOD";
    }

    /**
     * get column name
     * @return S_UID
     */
    public final static String CN$S_UID() {
        return "S_UID";
    }

    /**
     * get column name
     * @return S_USERNAME
     */
    public final static String CN$S_USERNAME() {
        return "S_USERNAME";
    }

    /**
     * get column name
     * @return I_COST
     */
    public final static String CN$I_COST() {
        return "I_COST";
    }

    /**
     * get column name
     * @return I_ROOMID
     */
    public final static String CN$I_ROOMID() {
        return "I_ROOMID";
    }

    /**
     * get java name
     * @return period
     */
    public final static String JN$I_PERIOD() {
        return "period";
    }

    /**
     * get java name
     * @return uid
     */
    public final static String JN$S_UID() {
        return "uid";
    }

    /**
     * get java name
     * @return username
     */
    public final static String JN$S_USERNAME() {
        return "username";
    }

    /**
     * get java name
     * @return cost
     */
    public final static String JN$I_COST() {
        return "cost";
    }

    /**
     * get java name
     * @return roomid
     */
    public final static String JN$I_ROOMID() {
        return "roomid";
    }

    /**
     * get all column names
     * @return String
     */
    public final static String ALL_COLUMN_NAMES() {
        return "I_PERIOD, S_UID, S_USERNAME, I_COST, I_ROOMID";
    }

    /**
     * get all java names
     * @return String
     */
    public final static String ALL_JAVA_NAMES() {
        return "period, uid, username, cost, roomid";
    }

    /**
     * get table name
     * @return String
     */
    public final static String TABLE_NAME() {
        return "T_ACTIVITY";
    }

    /**
     * get class name
     * @return String
     */
    public final static String CLASS_NAME() {
        return "TActivity";
    }
    
    /**
     * toString
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("T_ACTIVITY/TActivity: {\r\n");
        sb.append("\tI_PERIOD/period").append(" = ").append(getPeriod()).append("\r\n");
        sb.append("\tS_UID/uid").append(" = ").append(getUid()).append("\r\n");
        sb.append("\tS_USERNAME/username").append(" = ").append(getUsername()).append("\r\n");
        sb.append("\tI_COST/cost").append(" = ").append(getCost()).append("\r\n");
        sb.append("\tI_ROOMID/roomid").append(" = ").append(getRoomid()).append("\r\n");
        sb.append("}\r\n");
        return sb.toString();
    }
}
