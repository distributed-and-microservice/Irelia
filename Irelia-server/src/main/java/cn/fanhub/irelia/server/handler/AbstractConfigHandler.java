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

import cn.fanhub.irelia.core.handler.AbstractPreHandler;
import cn.fanhub.irelia.core.model.IreliaRequest;
import cn.fanhub.irelia.core.model.RpcConfig;
import cn.fanhub.irelia.core.upstream.UpstreamConfig;
import cn.fanhub.irelia.server.http.HeaderKey;
import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 获得 rpc 配置的 handler， 需要使用方自行配置数据。
 * @author chengfan
 * @version $Id: AbstractConfigHandler.java, v 0.1 2018年04月30日 下午9:59 chengfan Exp $
 */
@Sharable
@Slf4j
public abstract class AbstractConfigHandler extends AbstractPreHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest httpRequest = (FullHttpRequest)msg;
        HttpHeaders headers = httpRequest.headers();
        String rpcValue = headers.get(HeaderKey.rpcValue.name());
        String body = getBody(httpRequest);

        RpcConfig config = getRpcConfig(rpcValue);

        IreliaRequest ireliaRequest = new IreliaRequest();
        ireliaRequest.setRpcValue(rpcValue);

        // todo 参数支持的不够完善
        ireliaRequest.setRequestArgs(JSON.parseArray(body));
        ireliaRequest.setRpcConfig(config);


        ireliaRequest.setUpstreamConfig(getUpstreamConfig(config.getAppName()));

        ctx.fireChannelRead(ireliaRequest);

    }

    @Override
    public int order() {
        return 10;
    }

    /**
     * 获取body参数
     * @param request
     * @return
     */
    private String getBody(FullHttpRequest request){
        ByteBuf buf = request.content();
        return buf.toString(CharsetUtil.UTF_8);
    }

    abstract public RpcConfig getRpcConfig(String rpcValue);

    abstract public UpstreamConfig getUpstreamConfig(String rpcValue);
}