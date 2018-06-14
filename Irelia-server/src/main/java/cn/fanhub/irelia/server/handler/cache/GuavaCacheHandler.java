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

import cn.fanhub.irelia.core.model.IreliaRequest;
import cn.fanhub.irelia.core.model.IreliaResponse;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import io.netty.channel.ChannelHandler.Sharable;

import java.util.Map;

/**
 *
 * @author chengfan
 * @version $Id: GuavaCacheHandler.java, v 0.1 2018年06月11日 下午3:34 chengfan Exp $
 */
@Sharable
public class GuavaCacheHandler extends AbstractCacheHandler {

    private static Map<String, LoadingCache> cacheMap = Maps.newConcurrentMap();

    @Override
    public boolean hasCache(IreliaRequest request) {
        return request.getRpcConfig().getCacheConfig().isCache();
    }

    @Override
    public IreliaResponse cacheValue(IreliaRequest request) {
        LoadingCache loadingCache = cacheMap.get(request.getRpcValue());
        if (loadingCache == null) {
            return null;
        }
        return (IreliaResponse) loadingCache.getIfPresent(request.getRpcValue());
    }

    @Override
    public void put(IreliaRequest request, final IreliaResponse response) {
        // 有性能问题
        LoadingCache<String, IreliaResponse> ireliaResponseLoadingCache = CacheBuilder.newBuilder()
                .maximumSize(request.getRpcConfig().getCacheConfig().getCacheTime())
                .build(new CacheLoader<String, IreliaResponse>() {
                    @Override
                    public IreliaResponse load(String rpcValue) {
                        return response;
                    }
                });
        cacheMap.put(request.getRpcValue(), ireliaResponseLoadingCache);
    }

    @Override
    public int order() {
        return 800;
    }

}