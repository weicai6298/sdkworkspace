package com.kkgame.sdk.bean;

/**
 * 保存礼包信息
 * @author Administrator
 *
 */
public class GiftInfo {

	public String gift_id; // 礼包id
	public String game_id; // 游戏id
	public String name; // 礼包名
	public String description; // 礼包介绍
	public String howto; // 礼包使用方法
	public String create_time; // 礼包创建时间
	public String end_time; // 礼包截止时间
	public String cdkey;	//礼包cdkey
	
	public GiftInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GiftInfo(String gift_id, String game_id, String name,
			String description, String howto, String create_time,
			String end_time) {
		super();
		this.gift_id = gift_id;
		this.game_id = game_id;
		this.name = name;
		this.description = description;
		this.howto = howto;
		this.create_time = create_time;
		this.end_time = end_time;
	}

    @Override
    public String toString() {
        return "GiftInfo [gift_id=" + gift_id + ", game_id=" + game_id
                + ", name=" + name + ", description=" + description
                + ", howto=" + howto + ", create_time=" + create_time
                + ", end_time=" + end_time + ", cdkey=" + cdkey + "]";
    }

	

}
