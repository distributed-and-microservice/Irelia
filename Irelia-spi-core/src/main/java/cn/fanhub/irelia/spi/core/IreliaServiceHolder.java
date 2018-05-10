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

import cn.fanhub.irelia.core.model.RpcConfig;
import cn.fanhub.irelia.spi.core.model.IreliaBean;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author chengfan
 * @version $Id: IreliaServiceHolder.java, v 0.1 2018年04月29日 下午11:17 chengfan Exp $
 */
public interface IreliaServiceHolder extends Serializable {
    void loadRpc(String sysName, Object rpcBean);

    IreliaBean getIreliaBean(String rpcValue);

    List<IreliaBean> getBeansBySysName(String sysName);

    List<RpcConfig> getRpcConfig(String sysName);
}