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
package cn.fanhub.irelia.server.handler.cache;

import cn.fanhub.irelia.common.utils.ResponseUtil;
import cn.fanhub.irelia.core.handler.AbstractPreHandler;
import cn.fanhub.irelia.core.model.IreliaRequest;
import cn.fanhub.irelia.core.model.IreliaResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.springframework.beans.factory.InitializingBean;

/**
 *
 * @author chengfan
 * @version $Id: AbstractCacheHandler.java, v 0.1 2018年06月11日 下午3:21 chengfan Exp $
 */
public abstract class AbstractCacheHandler extends AbstractPreHandler implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        CacheHelper.register(this);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        IreliaRequest ireliaRequest = (IreliaRequest) msg;
        if (!hasCache(ireliaRequest)) {
            ctx.fireChannelRead(msg);
            return;
        }
        IreliaResponse response = cacheValue(ireliaRequest);
        if (response == null) {
            ctx.fireChannelRead(msg);
        } else {
            // 直接响应
            ResponseUtil.send(ctx, response, HttpResponseStatus.OK);
        }
    }


    abstract public boolean hasCache(IreliaRequest request);

    abstract public IreliaResponse cacheValue(IreliaRequest request);

    abstract public void put(IreliaRequest request, IreliaResponse response);

}