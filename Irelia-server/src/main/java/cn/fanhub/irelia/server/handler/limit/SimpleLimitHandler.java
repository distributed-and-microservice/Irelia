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

import cn.fanhub.irelia.core.model.IreliaRequest;
import cn.fanhub.irelia.core.model.LimitConfig;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import io.netty.channel.ChannelHandler.Sharable;

import java.util.Map;

/**
 *
 * @author chengfan
 * @version $Id: SimpleLimitHandler.java, v 0.1 2018年05月13日 下午1:41 chengfan Exp $
 */
@Sharable
public class SimpleLimitHandler extends AbstractLimitHandler {

    private static Map<String, RateLimiter> limiterMap = Maps.newConcurrentMap();
    
    @Override
    boolean shouldLimit(IreliaRequest ireliaRequest) {
        LimitConfig limitConfig = ireliaRequest.getRpcConfig().getLimitConfig();
        if (limitConfig.isLimit()) {
            if (limitConfig.getFrequency() <= 0) {
                return true;
            }

            RateLimiter limiter = limiterMap.get(ireliaRequest.getRpcValue());
            if (limiter == null) {
                limiter = RateLimiter.create(limitConfig.getFrequency());;
                limiterMap.put(ireliaRequest.getRpcValue(), limiter);
            }
            return !limiter.tryAcquire();
        }
        return false;
    }
}