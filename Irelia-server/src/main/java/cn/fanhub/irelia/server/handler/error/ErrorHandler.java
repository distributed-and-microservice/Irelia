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
import cn.fanhub.irelia.core.exception.IreliaRuntimeException;
import cn.fanhub.irelia.core.handler.AbstractErrorHandler;
import cn.fanhub.irelia.core.model.IreliaResponse;
import cn.fanhub.irelia.core.model.IreliaResponseCode;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author chengfan
 * @version $Id: ErrorHandler.java, v 0.1 2018年05月10日 下午8:43 chengfan Exp $
 */
@Sharable
@Slf4j
public class ErrorHandler extends AbstractErrorHandler {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        IreliaResponse response = new IreliaResponse();
        if (cause instanceof IreliaRuntimeException) {
            response.setCode(((IreliaRuntimeException) cause).getResponseCode().getCode());
            response.setMessage(((IreliaRuntimeException) cause).getResponseCode().getMessage());
        } else {
            response.setCode(IreliaResponseCode.SERVER_ERR.getCode());
        }
        response.setContent(cause.getMessage());
        ResponseUtil.send(ctx, response, HttpResponseStatus.BAD_GATEWAY);
    }

    @Override
    public int order() {
        return 2000;
    }
}