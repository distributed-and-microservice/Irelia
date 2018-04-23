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
package cn.fanhub.irelia.demo;

import cn.fanhub.irelia.upstream.IreliaUpstream;
import cn.fanhub.irelia.upstream.UpstreamManager;

import java.io.IOException;
import java.util.Map.Entry;

/**
 *
 * @author chengfan
 * @version $Id: Starter.java, v 0.1 2018年04月18日 下午9:53 chengfan Exp $
 */
public class Starter {
    public static void main(String[] args) throws IOException {
        //ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfig.class);
        for (Entry<String, IreliaUpstream> entry : UpstreamManager.getUpstreamMap().entrySet()) {
            System.out.println(entry.getKey()); // 第一次会加载很久 bio 需要升级
        }

        for (Entry<String, IreliaUpstream> entry : UpstreamManager.getUpstreamMap().entrySet()) {
            System.out.println(entry.getKey()); // 第二次会加载很快
        }
        System.in.read(new byte[2]);
    }
}