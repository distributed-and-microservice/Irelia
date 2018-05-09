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
package cn.fanhub.irelia.spi.core;

import cn.fanhub.irelia.core.exception.IreliaRuntimeException;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author chengfan
 * @version $Id: IreliaServiceManager.java, v 0.1 2018年04月10日 下午10:52 chengfan Exp $
 */
@Slf4j
public class IreliaServiceManager {

    private static final Map<String, IreliaService> SERVICE_MAP = new ConcurrentHashMap<String, IreliaService>();

    public static void register(String appName, IreliaService service) {
        if (log.isDebugEnabled()) {
            log.debug("register ireliaService ");
        }
        SERVICE_MAP.put(appName, service);
    }

    public static IreliaService getService(String appName) throws IreliaRuntimeException {
        IreliaService ireliaService = SERVICE_MAP.get(appName);
        if (ireliaService == null) {
            log.error("not found this service：" + appName);
            throw new IreliaRuntimeException("not found this service");
        }
        return ireliaService;
    }

}