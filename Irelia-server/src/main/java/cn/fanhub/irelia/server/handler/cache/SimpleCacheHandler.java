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
import com.google.common.collect.Maps;
import io.netty.channel.ChannelHandler.Sharable;

import java.util.Map;

/**
 *
 * @author chengfan
 * @version $Id: SimpleCacheHandler.java, v 0.1 2018年06月14日 下午5:41 chengfan Exp $
 */
@Sharable
public class SimpleCacheHandler extends AbstractCacheHandler {

    private static Map<String, CacheModel> modelMap = Maps.newConcurrentMap();

    @Override
    public boolean hasCache(IreliaRequest request) {
        CacheConfig cacheConfig = request.getRpcConfig().getCacheConfig();
        if (!cacheConfig.isCache()) {
            return false;
        }
        CacheModel cacheModel = modelMap.get(request.getRpcValue());
        if (cacheModel == null) {
            return false;
        }
        long time = System.currentTimeMillis() - cacheModel.getStartTimestamp();

        if (time > cacheConfig.getCacheTime()) {
            modelMap.remove(request.getRpcValue());
            return false;
        }

        return true;
    }

    @Override
    public IreliaResponse cacheValue(IreliaRequest request) {
        CacheModel cacheModel = modelMap.get(request.getRpcValue());
        if (cacheModel == null) {
            return null;
        }
        return cacheModel.getResponse();
    }

    @Override
    public void put(IreliaRequest request, IreliaResponse response) {
        CacheModel cacheModel = new CacheModel();
        cacheModel.setResponse(response);
        cacheModel.setStartTimestamp(System.currentTimeMillis());
        modelMap.put(request.getRpcValue(), cacheModel);
    }

    @Override
    public int order() {
        return 800;
    }
}