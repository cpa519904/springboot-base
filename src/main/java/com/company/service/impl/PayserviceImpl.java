package com.company.service.impl;

import com.company.common.exception.ExceptionCode;
import com.company.common.exception.ServiceException;
import com.company.common.third.ThridType;
import com.company.common.third.wechat.WXPayClient;
import com.company.common.third.wechat.WXPayConstants;
import com.company.common.third.wechat.WXPayUtil;
import com.company.common.third.wechat.domain.TradeType;
import com.company.common.third.wechat.domain.UnifiedOrderRequest;
import com.company.common.third.wechat.domain.UnifiedOrderResponse;
import com.company.common.tools.Time;
import com.company.common.tools.Utils;
import com.company.pojo.entity.AppConfig;
import com.company.service.IPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class PayserviceImpl implements IPayService {
    @Value("${wechat.notify-url}")
    private String notifyUrl;

    @Autowired
    private WXPayClient wxPayClient;

    public String getPayUrl() {
        try {
            UnifiedOrderResponse unifiedOrderResponse = wxPayClient.unifiedOrder(fillRequestData());
            if (unifiedOrderResponse.isSuccess()){
                //TODO 封装返回对象
                //TODO 数据缓存
            }
        } catch (Exception e) {
            throw new ServiceException(ExceptionCode.SERVICE_ERROR.getCode(), "");
        }

        return "";
    }

    private UnifiedOrderRequest fillRequestData() throws Exception {
        UnifiedOrderRequest request = new UnifiedOrderRequest();
        // pre.支付方式判断
        //AppConfig appConfig = appConfigService.getConfig(requestParameters.getAppId(), ThridType.WECHATPAY);
        AppConfig appConfig = null;
        if (appConfig == null) {
            throw new ServiceException(ExceptionCode.NO_FOUND.getCode(), "该应用不支持这种支付方式");
        }

        request.setAppid(appConfig.getThirdAppId());
        request.setMch_id(appConfig.getThirdMerchantNo());
        request.setNonce_str(WXPayUtil.generateNonceStr());
        request.setBody("");
        request.setOut_trade_no("");
        //request.setTotal_fee(requestParameters.getTotalAmount().multiply(new BigDecimal(100)).toBigInteger());
        //request.setSpbill_create_ip(StringUtils.isEmpty(requestParameters.getClientIp()) ? Utils.getIp() : requestParameters.getClientIp());
        request.setNotify_url(notifyUrl);
        request.setTrade_type(TradeType.JSAPI);
        request.setOpenid("");
        request.setTime_start(Time.getTime(LocalDateTime.now(), "yyyyMMddHHmmss"));
        request.setTime_expire(Time.getTime(LocalDateTime.now(), "yyyyMMddHHmmss"));

        String sign = WXPayUtil.generateSignature(Utils.objectToMap(request), appConfig.getSecretKey(), WXPayConstants.SignType.MD5);

        request.setSign(sign);

        return request;
    }
}
