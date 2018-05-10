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
package cn.fanhub.irelia.spi.core.impl;

import cn.fanhub.irelia.core.model.IreliaRequest;
import cn.fanhub.irelia.core.model.IreliaResponse;
import cn.fanhub.irelia.spi.core.model.IreliaBean;
import cn.fanhub.irelia.spi.core.IreliaService;
import cn.fanhub.irelia.spi.core.IreliaServiceHolder;
import cn.fanhub.irelia.spi.core.model.MethodInfo;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author chengfan
 * @version $Id: IreliaServiceImpl.java, v 0.1 2018年04月24日 下午10:37 chengfan Exp $
 */
public class IreliaServiceImpl implements IreliaService {

    private IreliaServiceHolder ireliaServiceHolder;


    public IreliaResponse invoke(IreliaRequest request) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        IreliaResponse response = new IreliaResponse();
        IreliaBean ireliaBean = ireliaServiceHolder.getIreliaBean(request.getRpcValue());
        MethodInfo methodInfo = ireliaBean.getMethodInfo();
        //Object o = MethodUtils.invokeExactMethod(ireliaBean.getImpl(), methodInfo.getMethodName(), request.getRequestArgs(),
        //
        //  methodInfo.getParamTypes());

        Method method = ireliaBean.getImpl().getClass().getMethod(methodInfo.getMethodName(), methodInfo.getParamTypes());
        //Method method = methodInfo.getMethod();
        ReflectionUtils.makeAccessible(method);
        Object[] params = new Object[request.getRequestArgs().size()];

        for (int i = 0; i < request.getRequestArgs().size(); i++) {
            params[i] = request.getRequestArgs().getString(i);
        }
        Object o = method.invoke(ireliaBean.getImpl(), params);
        response.setContent(o);
        return response;
    }

    public IreliaServiceHolder getIreliaServiceHolder() {
        return ireliaServiceHolder;
    }

    public void setIreliaServiceHolder(IreliaServiceHolder ireliaServiceHolder) {
        this.ireliaServiceHolder = ireliaServiceHolder;
    }
}