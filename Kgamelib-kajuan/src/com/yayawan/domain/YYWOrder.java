package com.yayawan.domain;

public class YYWOrder {

    public String id; // 订单id(丫丫玩)

    public String goods; // 商品名称

    public String transNum; //

    public String goods_id; // 商品id

    public Long money; // 金额

    public String orderId; // 订单编号(厂家)

    public Long gameId; // 游戏id

    public long serverId; // 服务id

    public String ext; // 厂家自定义字段

    public int mentId; // 支付方式

    public String time; // 订单时间

    public int status; // 订单状态0等待付款,1等待游戏到账2充值成功

    public int paytype;

    public YYWOrder() {
    }

    public YYWOrder(String orderId, String goods, Long money, String ext) {
        super();
        this.orderId = orderId;
        this.goods = goods;
        this.money = money;
        this.ext = ext;
    }

    public YYWOrder(String orderId, Long money, int mentId, String ext) {
        super();
        this.orderId = orderId;
        this.money = money;
        this.mentId = mentId;
        this.ext = ext;
    }

    @Override
    public String toString() {
        return "Order [id=" + id + ", goods=" + goods + ", transNum="
                + transNum + ", money=" + money + ", orderId=" + orderId
                + ", gameId=" + gameId + ", serverId=" + serverId + ", ext="
                + ext + ", mentId=" + mentId + ", time=" + time + ", status="
                + status + "]";
    }

}
