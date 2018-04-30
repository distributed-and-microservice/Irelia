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

import cn.fanhub.irelia.core.spi.IreliaService;
import cn.fanhub.irelia.core.spi.IreliaServiceHolder;
import cn.fanhub.irelia.core.model.IreliaRequest;
import cn.fanhub.irelia.core.model.IreliaResponse;
import cn.fanhub.irelia.core.model.MethodInfo;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author chengfan
 * @version $Id: IreliaServiceImpl.java, v 0.1 2018年04月24日 下午10:37 chengfan Exp $
 */
public class IreliaServiceImpl implements IreliaService {

    private IreliaServiceHolder serviceHolder;


    public IreliaResponse invoke(IreliaRequest request) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        IreliaResponse response = new IreliaResponse();

        MethodInfo methodInfo = serviceHolder.getIreliaBean(request.getRpcType()).getMethodInfo();
        Object o = MethodUtils.invokeExactMethod(methodInfo.getItf(), methodInfo.getMethodName(), request.getRequestArgs(),
                methodInfo.getParamTypes());

        response.setContent(o);
        return response;
    }

    public void setIreliaServiceHolder(IreliaServiceHolder serviceHolder) {
        this.serviceHolder = serviceHolder;
    }
}