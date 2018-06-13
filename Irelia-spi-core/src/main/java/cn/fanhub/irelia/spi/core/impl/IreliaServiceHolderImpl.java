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

import cn.fanhub.irelia.core.model.CacheConfig;
import cn.fanhub.irelia.core.model.LimitConfig;
import cn.fanhub.irelia.core.model.RpcConfig;
import cn.fanhub.irelia.spi.core.model.IreliaBean;
import cn.fanhub.irelia.spi.core.IreliaServiceHolder;
import cn.fanhub.irelia.spi.core.model.MethodInfo;
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

    private final Map<String, List<RpcConfig>> rpcConfigMap = Maps.newConcurrentMap();


    private final Map<String, List<IreliaBean>> sysTemBeansMap = Maps.newConcurrentMap();

    public void loadRpc(String sysName, Object rpcBean) {

        List<IreliaBean> beanList = Lists.newCopyOnWriteArrayList();
        List<RpcConfig> configList = Lists.newCopyOnWriteArrayList();
        for (Class<?> intf : rpcBean.getClass().getInterfaces()) {
            Method[] methods = MethodUtils.getMethodsWithAnnotation(intf, Rpc.class);

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
                        //.intf(intf)
                        .methodName(method.getName())
                        //.method(method)
                        .returnType(method.getReturnType())
                        .build();

                IreliaBean ireliaBean = IreliaBean
                        .builder()
                        .rpcValue(annotation.value())
                        .rpcName(annotation.name())
                        .des(annotation.desc())
                        .impl(rpcBean)
                        .methodInfo(methodInfo)
                        .build();

                RpcConfig rpcConfig = new RpcConfig();
                rpcConfig.setAppName(sysName);
                rpcConfig.setRpcName(annotation.name());
                rpcConfig.setRpcValue(annotation.value());
                rpcConfig.setDes(annotation.desc());
                rpcConfig.setMethodName(method.getName());
                rpcConfig.setItfName(intf.getName());
                rpcConfig.setCacheConfig(new CacheConfig());
                rpcConfig.setLimitConfig(new LimitConfig());


                IreliaBeansMap.put(annotation.value(), ireliaBean);
                configList.add(rpcConfig);
                beanList.add(ireliaBean);
            }
        }
        if (beanList.size() > 0) {
            sysTemBeansMap.put(sysName, beanList);
            rpcConfigMap.put(sysName, configList);
        }

    }

    public IreliaBean getIreliaBean(String rpcValue) {
        return IreliaBeansMap.get(rpcValue);
    }

    public List<IreliaBean> getBeansBySysName(String sysName) {
        return sysTemBeansMap.get(sysName);
    }


    public List<RpcConfig> getRpcConfig(String sysName) {
        return rpcConfigMap.get(sysName);
    }


}