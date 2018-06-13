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
package cn.fanhub.irelia.server.handler.limit;

import cn.fanhub.irelia.common.utils.ResponseUtil;
import cn.fanhub.irelia.core.handler.AbstractPreHandler;
import cn.fanhub.irelia.core.model.IreliaRequest;
import cn.fanhub.irelia.core.model.IreliaResponse;
import cn.fanhub.irelia.core.model.IreliaResponseCode;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 *
 * @author chengfan
 * @version $Id: AbstractLimitHandler.java, v 0.1 2018年05月13日 下午1:39 chengfan Exp $
 */
public abstract class AbstractLimitHandler extends AbstractPreHandler {

    @Override
    public final void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        IreliaRequest ireliaRequest = (IreliaRequest)msg;
        if (shouldLimit(ireliaRequest)) {
            IreliaResponse response = new IreliaResponse();
            response.setCode(IreliaResponseCode.RPC_BEEN_LIMITED.getCode());
            response.setMessage(IreliaResponseCode.RPC_BEEN_LIMITED.getMessage());
            ResponseUtil.send(ctx, response, HttpResponseStatus.OK);
        } else {
            ctx.fireChannelRead(msg);
        }

    }

    @Override
    public int order() {
        return 110;
    }

    abstract boolean shouldLimit(IreliaRequest ireliaRequest);
}