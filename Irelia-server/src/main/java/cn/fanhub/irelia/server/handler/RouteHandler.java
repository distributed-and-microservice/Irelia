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
import cn.fanhub.irelia.core.exception.IreliaRuntimeException;
import cn.fanhub.irelia.core.handler.AbstractRouterHandler;
import cn.fanhub.irelia.core.model.IreliaRequest;
import cn.fanhub.irelia.core.model.IreliaResponse;
import cn.fanhub.irelia.core.model.IreliaResponseCode;
import cn.fanhub.irelia.server.handler.cache.CacheHelper;
import cn.fanhub.irelia.upstream.IreliaUpstream;
import cn.fanhub.irelia.upstream.UpstreamManager;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpResponseStatus;
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
        IreliaUpstream upstream = UpstreamManager.getUpstream(ireliaRequest.getUpstreamConfig().getName());

        IreliaResponse ireliaResponse;
        try {
            ireliaResponse = upstream.invoke(ireliaRequest);
        } catch (Exception e) {
            throw new IreliaRuntimeException(IreliaResponseCode.BAD_REQUEST, "请求失败，请检查业务系统是否正常运行");
        }

        if (((IreliaRequest) msg).getRpcConfig().getCacheConfig().isCache()) {
            CacheHelper.setValue(ireliaRequest, ireliaResponse);
            if (log.isInfoEnabled()) {
                log.info("{} response cached", ireliaRequest.getRpcValue());
            }
        }
        ireliaResponse.setCode(IreliaResponseCode.SUCCESS.getCode());
        ireliaResponse.setMessage(IreliaResponseCode.SUCCESS.getMessage());
        ResponseUtil.send(ctx, ireliaResponse, HttpResponseStatus.OK);


    }

    public int order() {
        return 1000;
    }
}