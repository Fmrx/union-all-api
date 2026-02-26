package com.union.config.wxmp.handler;

import com.union.biz.service.message.TextStrategySelector;
import com.union.config.wxmp.builder.MpTextBuilder;
import com.union.enums.UserTypeEnum;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType;

/**
 * 微信公众号消息处理器
 *
 * @author Binary Wang(https://github.com/binarywang)
 */
@Slf4j
@Component
public class MsgHandler extends AbstractHandler {

    @Resource
    private TextStrategySelector textStrategySelector;

    /**
     * 处理微信公众号消息
     *
     * @param wxMessage      微信消息
     * @param context        上下文
     * @param weixinService  微信服务
     * @param sessionManager 会话管理器
     * @return 回复消息
     */
    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) {

        if (!wxMessage.getMsgType().equals(XmlMsgType.EVENT)) {
            String content = wxMessage.getContent();
            String openId = wxMessage.getFromUser();
            log.info("收到微信公众号消息, openId: {}, content: {}", openId, content);

            try {
                Object result = textStrategySelector.process(content, openId, UserTypeEnum.WX_MP.ordinal());
                String replyContent = result != null ? result.toString() : "系统处理中，请稍后...";
                return new MpTextBuilder().build(replyContent, wxMessage, weixinService);
            } catch (Exception e) {
                log.error("处理微信公众号消息失败, openId: {}, content: {}", openId, content, e);
                return new MpTextBuilder().build("系统繁忙，请稍后再试~", wxMessage, weixinService);
            }
        }

        return null;
    }

}
