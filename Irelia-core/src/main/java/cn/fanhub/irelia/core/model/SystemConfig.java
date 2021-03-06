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
package cn.fanhub.irelia.core.model;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author chengfan
 * @version $Id: SystemConfig.java, v 0.1 2018年06月13日 下午11:19 chengfan Exp $
 */
@Data
public class SystemConfig implements Serializable {
    private String publicKey;
}