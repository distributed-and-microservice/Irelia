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

import cn.fanhub.irelia.core.model.CacheConfig;
import cn.fanhub.irelia.core.model.IreliaRequest;
import cn.fanhub.irelia.core.model.IreliaResponse;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 *
 * @author chengfan
 * @version $Id: GuavaCacheHandler.java, v 0.1 2018年06月11日 下午3:34 chengfan Exp $
 */
public class GuavaCacheHandler extends AbstractCacheHandler {

    LoadingCache<String, IreliaResponse> ireliaResponseLoadingCache = CacheBuilder.newBuilder()
    .maximumSize(1000)
    .build(new CacheLoader<String, IreliaResponse>() {
        @Override
        public IreliaResponse load(String rpcValue) {
            return null;
        }
    });


    @Override
    public boolean shouldCache(CacheConfig cacheConfig) {
        return cacheConfig.isCache();
    }

    @Override
    public IreliaResponse cacheValue(IreliaRequest request) {
        return ireliaResponseLoadingCache.getIfPresent(request.getRpcValue());
    }

    @Override
    public void put(String rpcValue, IreliaResponse response) {
        ireliaResponseLoadingCache.put(rpcValue, response);
    }

    @Override
    public int order() {
        return 800;
    }

}