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
package cn.fanhub.irelia.server.future;

import cn.fanhub.irelia.common.utils.ResponseUtil;
import cn.fanhub.irelia.core.exception.IreliaRuntimeException;
import cn.fanhub.irelia.core.model.IreliaResponse;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;

/**
 * post handler 异常处理
 * @author chengfan
 * @version $Id: ErrorChannelFutureListener.java, v 0.1 2018年05月13日 下午7:38 chengfan Exp $
 */
@Slf4j
public class ErrorChannelFutureListener implements ChannelFutureListener {
    @Override
    public void operationComplete(ChannelFuture future) {
        if (!future.isSuccess()) {
            Throwable cause = future.cause();
            if (cause instanceof IreliaRuntimeException) {
                log.error("post error" , cause);
                future.channel().close();
            }
            log.error("error: " , cause);
            IreliaResponse response = new IreliaResponse();
            ResponseUtil.buildExceptionResponse(cause, response);
            response.setContent("错误详情请查看日志");
            // todo 有瑕疵啊
            ResponseUtil.send(future.channel().pipeline().lastContext(), response, HttpResponseStatus.BAD_GATEWAY);
        }
    }
}