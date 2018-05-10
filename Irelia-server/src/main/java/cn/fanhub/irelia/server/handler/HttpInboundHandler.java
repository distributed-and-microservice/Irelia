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

import cn.fanhub.irelia.common.utils.ResponseUtil;
import cn.fanhub.irelia.core.handler.AbstractPreHandler;
import cn.fanhub.irelia.core.model.IreliaResponse;
import cn.fanhub.irelia.core.model.IreliaResponseCode;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author chengfan
 * @version $Id: HttpInboundHandler.java, v 0.1 2018年04月10日 下午9:00 chengfan Exp $
 */
@Slf4j
@Sharable
public class HttpInboundHandler extends AbstractPreHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(! (msg instanceof FullHttpRequest)){
            IreliaResponse response = new IreliaResponse();
            response.setCode(IreliaResponseCode.NOT_SUPPORT_REQUEST.getCode());
            response.setMessage(IreliaResponseCode.NOT_SUPPORT_REQUEST.getMessage());
            ResponseUtil.send(ctx, response, HttpResponseStatus.BAD_REQUEST);
            return;
        }
        FullHttpRequest httpRequest = (FullHttpRequest)msg;
        try{
            String path=httpRequest.uri();          //获取路径
            HttpMethod method = httpRequest.method();//获取请求方法
            //如果不是这个路径，就直接返回错误
            if(!"/irelia".equalsIgnoreCase(path) || !HttpMethod.POST.equals(method)){
                IreliaResponse response = new IreliaResponse();
                response.setCode(IreliaResponseCode.INVALID_QUERY_URL.getCode());
                response.setMessage(IreliaResponseCode.INVALID_QUERY_URL.getMessage());
                ResponseUtil.send(ctx, response, HttpResponseStatus.BAD_REQUEST);
                return;
            }
            //如果是POST请求 todo
            ctx.fireChannelRead(msg);

        } catch(Exception e) {
            log.error("处理请求失败!", e);
        } finally {
            //释放请求
            httpRequest.release();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("连接的客户端地址: {}", ctx.channel().remoteAddress());
        super.channelActive(ctx);
    }

    public int order() {
        return 0;
    }
}
