package com.company.common.third.wechat.domain;

import com.company.common.third.wechat.WXPayConstants;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "xml")
public class UnifiedOrderResponse {
    //返回状态码 SUCCESS/FAIL
    private String return_code;
    //返回信息
    private String return_msg;
    //公众号id
    private String appid;
    //商户号
    private String mch_id;
    //随机串
    private String nonce_str;
    //签名
    private String sign;
    //业务结果 SUCCESS/FAIL
    private String result_code;
    //预支付id
    private String prepay_id;
    //二维码链接
    private String code_url;
    //错误描述
    private String err_code_des;

    public boolean isSuccess() {

        if (getReturn_code().equals(WXPayConstants.SUCCESS) && this.result_code.equals(WXPayConstants.SUCCESS)) {
            return true;
        }

        return false;
    }
}
