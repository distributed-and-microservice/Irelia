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
package cn.fanhub.irelia.spi.dubbo;

import cn.fanhub.irelia.spi.core.IreliaService;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;

/**
 *
 * @author chengfan
 * @version $Id: DubboClient.java, v 0.1 2018年04月30日 下午1:29 chengfan Exp $
 */
public class DubboClient {

    public void appServiceRegister(String appName, IreliaService ireliaService, String url, Integer port, Integer threads) {
        /** 当前应用配置, 配置应用名*/
        ApplicationConfig application = new ApplicationConfig();
        application.setName(appName);

        /** 连接注册中心配置*/
        RegistryConfig registry = new RegistryConfig();
        registry.setAddress(url);

        /** 服务提供者协议配置  threads线程池大小*/
        ProtocolConfig protocol = new ProtocolConfig();
        protocol.setName("dubbo");
        protocol.setThreads(threads);
        protocol.setPort(port);

        // 服务提供者暴露服务配置
        ServiceConfig<IreliaService> service = new ServiceConfig<IreliaService>();
        service.setApplication(application);
        service.setRegistry(registry); // 多个注册中心可以用setRegistries()
        service.setProtocol(protocol); // 多个协议可以用setProtocols()
        service.setInterface(IreliaService.class);
        service.setRef(ireliaService);
        service.setVersion("1.0.0");
        service.setGroup(appName);

        // 暴露及注册服务
        service.export();

    }
}