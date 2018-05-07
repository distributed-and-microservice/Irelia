/**
 *    Copyright 2018 chengfan(fanhub.cn)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.fanhub.irelia.server.handler;

import cn.fanhub.irelia.core.handler.AbstractRouterHandler;
import cn.fanhub.irelia.core.model.IreliaRequest;
import cn.fanhub.irelia.core.model.IreliaResponse;
import cn.fanhub.irelia.upstream.UpstreamManager;
import com.alibaba.fastjson.JSON;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author chengfan
 * @version $Id: RouteHandler.java, v 0.1 2018年04月09日 下午10:41 chengfan Exp $
 */
@Slf4j
@Sharable
public class RouteHandler extends AbstractRouterHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        IreliaRequest ireliaRequest = (IreliaRequest)msg;

        IreliaResponse ireliaResponse = null;

        ireliaResponse = UpstreamManager.getUpstream(ireliaRequest.getAppName()).invoke(ireliaRequest);


        send(ctx, ireliaResponse);
    }

    /**
     * 发送的返回值
     * @param ctx     返回
     * @param ireliaResponse 消息
     */
    private void send(ChannelHandlerContext ctx, IreliaResponse ireliaResponse) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(JSON.toJSONString(ireliaResponse), CharsetUtil.UTF_8)
        );
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }


    public int order() {
        return 1000;
    }
}