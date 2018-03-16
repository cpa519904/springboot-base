package com.company.common.third.wechat.domain;

/**
 * 微信订单交易状态
 */
public enum TradeState {
    SUCCESS,
    REFUND,
    NOTPAY,
    CLOSED,
    REVOKED,
    USERPAYING,
    PAYERROR
}
