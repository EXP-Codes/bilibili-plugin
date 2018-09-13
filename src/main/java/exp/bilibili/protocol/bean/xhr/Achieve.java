package exp.bilibili.protocol.bean.xhr;


/**
 * <PRE>
 * 
 * 普通成就对象：
 * 
   {
	  "code": 0,
	  "msg": "OK",
	  "data": {
	    "user": {
	      "achieve": 70,
	      "progress": 7,
	      "total": 48,
	      "done": 3,
	      "can_receive_normal": 1,
	      "can_receive_legend": 0
	    },
	    "info": [
	      {
	        "tid": 30,
	        "css": "task-30",
	        "title": "挥金如土",
	        "award": "<i class=\"silver-seed\">银瓜子<\/i> × 15,000",
	        "awards": [
	          {
	            "type": "silver",
	            "num": "15000"
	          }
	        ],
	        "achieve": 30,
	        "descript": "达成条件：累计消费100,000瓜子",
	        "status": true,
	        "finished": false,
	        "progress": {
	          "now": 0,
	          "max": 0
	        }
	      },
	      {
	        "tid": 5,
	        "css": "task-5",
	        "title": "脱非入欧",
	        "award": "<p>弹幕颜色：<i class=\"dot red\"><\/i>红（需保持月费老爷状态）<\/p>",
	        "awards": [
	          {
	            "type": "dannmakuColor",
	            "color": "red",
	            "text": "红（需保持月费老爷状态）"
	          }
	        ],
	        "achieve": 50,
	        "descript": "达成条件：成为月费老爷",
	        "status": true,
	        "finished": true,
	        "progress": {
	          "now": 0,
	          "max": 0
	        }
	      },
	      {
	        "tid": 29,
	        "css": "task-29",
	        "title": "土豪潜质",
	        "award": "<i class=\"silver-seed\">银瓜子<\/i> × 1,000",
	        "awards": [
	          {
	            "type": "silver",
	            "num": "1000"
	          }
	        ],
	        "achieve": 20,
	        "descript": "达成条件：累计消费10,000瓜子",
	        "status": true,
	        "finished": true,
	        "progress": {
	          "now": 0,
	          "max": 0
	        }
	      }
	    ],
	    "page": {
	      "total": 3,
	      "totalPage": 1,
	      "current": "1",
	      "info": [
	        
	      ],
	      "pageSize": "100"
	    }
	  }
	}
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Achieve {

	private String id;
	
	private String name;
	
	public Achieve(String id, String name) {
		this.id = (id == null ? "" : id);
		this.name = (name == null ? "" : name);
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}

