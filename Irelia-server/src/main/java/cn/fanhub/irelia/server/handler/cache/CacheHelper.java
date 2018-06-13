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

import cn.fanhub.irelia.core.model.IreliaResponse;
import org.springframework.util.Assert;

/**
 *
 * @author chengfan
 * @version $Id: CacheHelper.java, v 0.1 2018年06月11日 下午5:26 chengfan Exp $
 */
public class CacheHelper {

    private static AbstractCacheHandler CACHE_HANDLER;

    public static void register(AbstractCacheHandler cacheHandler) {
        CACHE_HANDLER = cacheHandler;
    }

    public static void setValue(String rpcValue, IreliaResponse response) {
        Assert.notNull(CACHE_HANDLER, "NO CACHE_HANDLER ");
        CACHE_HANDLER.put(rpcValue, response);
    }
}