package com.kkgame.sdk.bean;

/**
 * 保存攻略信息
 * 
 * @author Administrator
 * 
 */
public class StrategyInfo {
	public String id;
	public String name;
	public String game_id;
	public String time;

	public StrategyInfo() {
	}

	public StrategyInfo(String id, String name, String game_id, String time) {
		super();
		this.id = id;
		this.name = name;
		this.game_id = game_id;
		this.time = time;
	}

	@Override
	public String toString() {
		return "StrategyInfo [id=" + id + ", name=" + name + ", game_id="
				+ game_id + ", time=" + time + "]";
	}

}
