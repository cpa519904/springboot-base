package com.company.common.third.wechat.domain;

import lombok.Data;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigInteger;

@Data
@XmlRootElement(name = "xml")
public class UnifiedOrderRequest {
    //公众账号ID
    private String appid;
    //商户号
    private String mch_id;
    //随机串
    private String nonce_str;
    //签名
    private String sign;
    //商品描述
    private String body;
    //商户订单号
    private String out_trade_no;
    //标价金额
    private BigInteger total_fee;
    //终端IP
    private String spbill_create_ip;
    //后台通知地址 (不可带参)
    private String notify_url;
    //交易类型
    private TradeType trade_type;
    //用户标识
    private String openid;

//============= not require ============//
    private String product_id;
    private String limit_pay;
    private String time_start;
    private String goods_tag;
    private String time_expire;
    private String fee_type;
    private String detail;
    private String attach;
    private String device_info;
}
