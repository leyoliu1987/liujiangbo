package com.sunlands.handler;

import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
/**
 * 
 * Created on 2017年11月15日 下午7:17:39
 * Description: [处理http请求]
 * Company:     [尚德机构]
 * @author         [liujiangbo]
 *
 */
public class HttpRequestForWsHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	private static final Logger logger = LoggerFactory.getLogger(HttpRequestForWsHandler.class);
	
    private final String wsUri;
    public HttpRequestForWsHandler(String wsUri) {
        super();
        this.wsUri = wsUri;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req)
            throws Exception {
    	// Handle a bad request.
        if (!req.decoderResult().isSuccess()) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
            return;
        }

        // Allow only GET methods.
        if (req.method() != GET) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN));
            return;
        }

        String requestUri = req.uri();
        if (logger.isInfoEnabled()) {
			logger.info("当前请求的url为：{}", requestUri);
		}

        String realUri = null;
        String requestParam = null;
        if (requestUri.contains("?")) {
            realUri = requestUri.substring(0, requestUri.lastIndexOf("?"));
            requestParam = requestUri.substring(requestUri.lastIndexOf("?") + 1);
        } else {
            realUri = requestUri;
        }

        if(wsUri.equalsIgnoreCase(realUri)){
            req.setUri(wsUri);
            if (StringUtils.isNotBlank(requestParam)) {
                String[] params = requestParam.split("&");
                for (String param : params) {
                    String[] strs = param.split("=");
                    ctx.channel().attr(AttributeKey.valueOf(strs[0])).set(strs[1]);
                }
            }
            ctx.fireChannelRead(req.retain());
        }
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
        // Generate an error page if response getStatus code is not OK (200).
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            HttpUtil.setContentLength(res, res.content().readableBytes());
        }

        // Send the response and close the connection if necessary.
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpUtil.isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
    	logger.error("创建websocket连接失败：{}", cause);
    	super.exceptionCaught(ctx, cause);
    }

}
