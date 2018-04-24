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
package cn.fanhub.irelia.upstream.dubbo;

import cn.fanhub.irelia.core.IreliaService;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 *
 * @author chengfan
 * @version $Id: DubboServiceManager.java, v 0.1 2018年04月11日 下午9:41 chengfan Exp $
 */
public class DubboServiceManager {

    // todo  缓存待改造
    private final Map<String, IreliaService> serviceMap = Maps.newConcurrentMap();

    public static DubboServiceManager getInstance() {
        return DubboServiceManagerHolder.INSTANCE;
    }

    private DubboServiceManager() {

    }
    private static class DubboServiceManagerHolder {
        private final static DubboServiceManager INSTANCE = new DubboServiceManager();
    }

    private IreliaService createService(DubboServiceConfig config) {
        // 当前应用配置
        ApplicationConfig application = new ApplicationConfig();
        application.setName(config.getName());

        // 连接注册中心配置
        RegistryConfig registry = new RegistryConfig();
        registry.setAddress(config.getAddress());
        registry.setUsername(config.getUsername());
        registry.setPassword(config.getPassword());
        registry.setGroup(config.getAppId());
        ReferenceConfig<IreliaService> reference = new ReferenceConfig<IreliaService>();
        IreliaService service = reference.get();
        // 缓存
        serviceMap.put(config.getAppId(), service);
        return service;
    }

    public IreliaService getService(DubboServiceConfig config) {
        IreliaService service = serviceMap.get(config.getAppId());
        if (service == null) {
            service = createService(config);
        }
        return service;
    }

}