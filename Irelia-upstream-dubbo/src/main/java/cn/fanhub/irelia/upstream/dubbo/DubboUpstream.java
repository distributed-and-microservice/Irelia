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
import cn.fanhub.irelia.core.spi.IreliaService;
import cn.fanhub.irelia.core.model.IreliaRequest;
import cn.fanhub.irelia.core.model.IreliaResponse;
import cn.fanhub.irelia.upstream.IreliaUpstream;
import cn.fanhub.irelia.upstream.UpstreamManager;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author chengfan
 * @version $Id: DubboUpstream.java, v 0.1 2018年04月23日 下午9:32 chengfan Exp $
 */
@Slf4j
public class DubboUpstream implements IreliaUpstream {

    static {
        if (log.isInfoEnabled()) {
            log.info("DubboUpstream start register");
        }
        UpstreamManager.register(DubboUpstreamHolder.INSTANCE);
    }

    private static class DubboUpstreamHolder {
        private static final DubboUpstream INSTANCE = new DubboUpstream();
    }

    private DubboUpstream() {

    }

    public IreliaResponse invoke(IreliaRequest request)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IreliaRuntimeException {
        IreliaService service = DubboServiceManager.getInstance().getService((DubboUpstreamConfig) request.getUpstreamConfig());
        return service.invoke(request);
    }

    public String name() {
        return "dubbo";
    }
}