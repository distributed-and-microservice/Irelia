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
package cn.fanhub.irelia.server.handler.error;

import cn.fanhub.irelia.common.utils.ResponseUtil;
import cn.fanhub.irelia.core.handler.AbstractErrorHandler;
import cn.fanhub.irelia.core.model.IreliaResponse;
import cn.fanhub.irelia.server.future.ErrorChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;

/**
 * 全局异常处理
 * @author chengfan
 * @version $Id: ErrorHandler.java, v 0.1 2018年05月10日 下午8:43 chengfan Exp $
 */
@Sharable
@Slf4j
public class ErrorHandler extends AbstractErrorHandler {
    
    @Setter
    private ErrorChannelFutureListener channelFutureListener;

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
        ctx.connect(remoteAddress, localAddress, promise.addListener(channelFutureListener));
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        ctx.write(msg, promise.addListener(channelFutureListener));
    }

    @Override
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) {
         ctx.bind(localAddress, promise.addListener(channelFutureListener));
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) {
        ctx.close(promise.addListener(channelFutureListener));
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) {
        ctx.disconnect(promise.addListener(channelFutureListener));
    }

    @Override
    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) {
        ctx.deregister(promise.addListener(channelFutureListener));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error(cause.getMessage(), cause);
        IreliaResponse response = new IreliaResponse();
        ResponseUtil.buildExceptionResponse(cause, response);
        response.setContent("错误详情请查看日志");
        ResponseUtil.send(ctx, response, HttpResponseStatus.BAD_GATEWAY);
    }

    @Override
    public int order() {
        return 2000;
    }
}