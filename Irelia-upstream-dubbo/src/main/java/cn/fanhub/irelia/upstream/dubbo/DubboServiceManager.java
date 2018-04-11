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

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.service.GenericService;

/**
 *
 * @author chengfan
 * @version $Id: DubboServiceManager.java, v 0.1 2018年04月11日 下午9:41 chengfan Exp $
 */
public class DubboServiceManager {

    private ReferenceConfig<GenericService> reference;

    public DubboServiceManager(DubboServiceConfig config) {
        connection(config);
    }

    private void connection(DubboServiceConfig config) {
        // 当前应用配置
        ApplicationConfig application = new ApplicationConfig();
        application.setName(config.getName());

        // 连接注册中心配置
        RegistryConfig registry = new RegistryConfig();
        registry.setAddress(config.getAddress());
        registry.setUsername(config.getUsername());
        registry.setPassword(config.getPassword());
        // todo 缓存该实例
        reference = new ReferenceConfig<GenericService>();
    }

    public Object invoke(DubboReqest dubboReqest) {
        // 弱类型接口名
        reference.setInterface(dubboReqest.getInterfaceName());
        reference.setVersion(dubboReqest.getVersion());
        // 声明为泛化接口
        reference.setGeneric(true);
        GenericService genericService = reference.get();
        return genericService.$invoke(dubboReqest.getMethodName(), dubboReqest.getParamTypes(), dubboReqest.getParams());
    }

}