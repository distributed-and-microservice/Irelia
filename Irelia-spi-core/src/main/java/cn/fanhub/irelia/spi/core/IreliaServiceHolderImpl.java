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

import cn.fanhub.irelia.core.model.IreliaBean;
import cn.fanhub.irelia.core.model.MethodInfo;
import cn.fanhub.irelia.core.spi.IreliaServiceHolder;
import cn.fanhub.irelia.spi.core.annotation.Rpc;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 *
 * @author chengfan
 * @version $Id: IreliaServiceHolderImpl.java, v 0.1 2018年04月25日 下午10:40 chengfan Exp $
 */
public class IreliaServiceHolderImpl implements IreliaServiceHolder {

    private final Map<String, IreliaBean> IreliaBeansMap = Maps.newConcurrentMap();


    private final Map<String, List<IreliaBean>> sysTemBeansMap = Maps.newConcurrentMap();

    public void loadRpc(String sysName, Object rpcBean) {
        // todo 多接口
        Method[] methods = MethodUtils.getMethodsWithAnnotation(rpcBean.getClass().getInterfaces()[0], Rpc.class);
        List<IreliaBean> beanList = Lists.newCopyOnWriteArrayList();
        for (Method method : methods) {

            Rpc annotation = method.getAnnotation(Rpc.class);

            Class<?>[] parameterTypes = method.getParameterTypes();
            String[] paramNames = new String[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                paramNames[i] = parameterTypes[i].getName();
            }
            MethodInfo methodInfo = MethodInfo
                    .builder()
                    .paramTypes(parameterTypes)
                    .paramNames(paramNames)
                    .itf(rpcBean.getClass().getInterfaces()[0].getName())
                    .methodName(method.getName())
                    .returnType(method.getReturnType())
                    .build();

            IreliaBean ireliaBean = IreliaBean
                    .builder()
                    .rpcValue(annotation.value())
                    .rpcName(annotation.name())
                    .des(annotation.desc())
                    .methodInfo(methodInfo)
                    .build();

            IreliaBeansMap.put(annotation.value(), ireliaBean);
            beanList.add(ireliaBean);
        }
        sysTemBeansMap.put(sysName, beanList);
    }

    public IreliaBean getIreliaBean(String rpcValue) {
        return IreliaBeansMap.get(rpcValue);
    }

    public List<IreliaBean> getBeansBySysName(String sysName) {
        return sysTemBeansMap.get(sysName);
    }

}