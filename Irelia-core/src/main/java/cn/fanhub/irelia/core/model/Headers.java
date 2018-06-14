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

import com.google.common.collect.Maps;
import io.netty.handler.codec.http.HttpHeaders;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author chengfan
 * @version $Id: Headers.java, v 0.1 2018年06月14日 下午6:54 chengfan Exp $
 */
public class Headers implements Serializable {

    private Map<String, String> headerMap = Maps.newConcurrentMap();

    public void put(String key, String value) {
        headerMap.put(key, value);
    }

    public String get(String key) {
        return headerMap.get(key);
    }

    public Set<Entry<String, String>> entries() {
        return headerMap.entrySet();
    }

    public void putAll(HttpHeaders httpHeaders) {
        for (Entry<String, String> entry : httpHeaders.entries()) {
            headerMap.put(entry.getKey(), entry.getValue());
        }
    }
}