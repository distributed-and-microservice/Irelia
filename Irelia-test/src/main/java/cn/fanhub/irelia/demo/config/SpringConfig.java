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
package cn.fanhub.irelia.demo.config;

import cn.fanhub.irelia.server.Bootstrap;
import cn.fanhub.irelia.server.handler.HttpInboundHandler;
import cn.fanhub.irelia.server.handler.RouteHandler;
import cn.fanhub.irelia.server.handler.SecurityHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author chengfan
 * @version $Id: SpringConfig.java, v 0.1 2018年04月18日 下午9:53 chengfan Exp $
 */

@Configuration
@ComponentScan(basePackages = "cn.fanhub.irelia.demo")
public class SpringConfig {

    // 使用 http 服务
    @Bean
    public HttpInboundHandler httpInboundHandler() {
        return new HttpInboundHandler();
    }

    // 添加默认路由组件
    @Bean
    public RouteHandler routeHandler() {
        return new RouteHandler();
    }

    // 添加默认身份验证组件
    @Bean
    public SecurityHandler securityHandler() {
        return new SecurityHandler();
    }

    // 启动网关
    @Bean(initMethod = "start")
    public Bootstrap bootstrap() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.setPort(9876);
        return bootstrap;
    }
}