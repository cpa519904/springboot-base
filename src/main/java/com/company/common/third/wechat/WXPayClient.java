package com.company.common.third.wechat;

import com.company.common.third.wechat.domain.UnifiedOrderRequest;
import com.company.common.third.wechat.domain.UnifiedOrderResponse;
import com.company.common.tools.Utils;
import org.apache.http.entity.ContentType;
import org.apache.http.client.fluent.Request;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Component
public class WXPayClient {

    /**
     * 作用：统一下单
     * 场景：公共号支付、扫码支付、APP支付
     * @param request 向wxpay post的请求数据
     */
    public UnifiedOrderResponse unifiedOrder(UnifiedOrderRequest request) throws Exception{
        String requestBody = Utils.beanToXml(request, UnifiedOrderRequest.class);
        requestBody = new String(requestBody.getBytes(), "ISO8859-1");
        String responseStr = Request.Post(WXPayConstants.DOMAIN_API + WXPayConstants.UNIFIEDORDER_URL_SUFFIX).bodyString(requestBody, ContentType.APPLICATION_XML).execute().returnContent().asString(Charset.forName("UTF-8"));
        return Utils.xmlToBean(responseStr, UnifiedOrderResponse.class);
    }

}
