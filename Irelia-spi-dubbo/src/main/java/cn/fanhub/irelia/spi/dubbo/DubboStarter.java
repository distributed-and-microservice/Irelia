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

import cn.fanhub.irelia.core.spi.IreliaService;
import cn.fanhub.irelia.core.spi.IreliaServiceHolder;
import cn.fanhub.irelia.spi.core.IreliaServiceHolderImpl;
import cn.fanhub.irelia.spi.core.IreliaServiceImpl;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author chengfan
 * @version $Id: DubboStarter.java, v 0.1 2018年04月30日 上午11:18 chengfan Exp $
 */
@Setter
@Slf4j
public class DubboStarter implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private String registryUrl;

    private String appName;

    private Integer protocolPort;

    private Integer serviceThreads;

    public void init() {
        if (log.isDebugEnabled()) {
            log.debug("init dubbo irelia service");
        }
        IreliaServiceHolder holder = new IreliaServiceHolderImpl();
        IreliaService service = new IreliaServiceImpl();
        service.setIreliaServiceHolder(holder);

        for (String springBeanName : this.applicationContext.getBeanDefinitionNames()) {
            holder.loadRpc(appName, this.applicationContext.getBean(springBeanName));
        }
    }

}