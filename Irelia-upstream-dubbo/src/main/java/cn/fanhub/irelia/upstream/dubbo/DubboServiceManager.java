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

import cn.fanhub.irelia.core.exception.IreliaRuntimeException;
import cn.fanhub.irelia.spi.core.IreliaService;
import cn.fanhub.irelia.spi.core.IreliaServiceManager;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author chengfan
 * @version $Id: DubboServiceManager.java, v 0.1 2018年04月11日 下午9:41 chengfan Exp $
 */
@Slf4j
public class DubboServiceManager {

    public static DubboServiceManager getInstance() {
        return DubboServiceManagerHolder.INSTANCE;
    }

    private DubboServiceManager() {

    }
    private static class DubboServiceManagerHolder {
        private final static DubboServiceManager INSTANCE = new DubboServiceManager();
    }

    private IreliaService createService(DubboUpstreamConfig config) {
        // 当前应用配置
        ApplicationConfig application = new ApplicationConfig();
        application.setName(config.getAppName());

        // 连接注册中心配置
        RegistryConfig registry = new RegistryConfig();
        registry.setAddress(config.getAddress());
        registry.setUsername(config.getUsername());
        registry.setPassword(config.getPassword());
        registry.setGroup(config.getAppName());

        ReferenceConfig<IreliaService> reference = new ReferenceConfig<IreliaService>();
        reference.setApplication(application);
        reference.setRegistry(registry); // 多个注册中心可以用setRegistries()
        reference.setInterface(IreliaService.class);
        reference.setGroup(config.getAppName());
        reference.setVersion("1.0.0");

        IreliaService service = reference.get();
        // 缓存
        IreliaServiceManager.register(config.getAppName(), service);
        return service;
    }

    public IreliaService getService(DubboUpstreamConfig config) throws IreliaRuntimeException {
        IreliaService service = null;
        try {
            service = IreliaServiceManager.getService(config.getAppName());
        } catch (IreliaRuntimeException e) {
            service = createService(config);
            if (log.isInfoEnabled()) {
                log.info("try load service");
            }

            if (service == null) {
                log.error("load error");
                throw new IreliaRuntimeException("try load fail!");
            }
        }
        return service;
    }

    public IreliaService register(DubboUpstreamConfig config) {
        return createService(config);
    }

}