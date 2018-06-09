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
package cn.fanhub.irelia.upstream;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author chengfan
 * @version $Id: UpstreamManager.java, v 0.1 2018年04月21日 下午4:23 chengfan Exp $
 */
@Slf4j
public class UpstreamManager {

    private static final Map<String, IreliaUpstream>  UPSTREAM_MAP = new ConcurrentHashMap<String, IreliaUpstream>();

    static {
        try {
            if (log.isInfoEnabled()) {
                log.info("start load upstreams");
            }
            loadInitialUpstreams();

        } catch (IOException e) {
            log.error("load upstream error : io exception", e);
        } catch (ClassNotFoundException e) {
            log.error("load upstream error : class not found", e);
        }
    }

    private static void loadInitialUpstreams() throws IOException, ClassNotFoundException {
        Enumeration<URL> resources = UpstreamManager.class.getClassLoader().getResources(UpstreamConfig.UPSTREAM_FILE);

        while (resources.hasMoreElements()) {
            File file = new File(resources.nextElement().getFile());
            FileReader fileReader = new FileReader(file);
            BufferedReader bf = new BufferedReader(fileReader);
            String upstreamStr = bf.readLine();
            String[] upstreams = StringUtils.split(upstreamStr, ",");

            for (String upstream : upstreams) {
                Class.forName(upstream, true, Thread.currentThread().getContextClassLoader());
                if (log.isInfoEnabled()) {
                    log.info("load upstreams {}", upstream);
                }
            }

            bf.close();
            fileReader.close();
        }

    }

    public static void register(IreliaUpstream upstream) {
        if (log.isInfoEnabled()) {
            log.info("register upstream ");
        }
        UPSTREAM_MAP.put(upstream.name(), upstream);
    }

    public static Map<String, IreliaUpstream> getUpstreamMap() {
        return UPSTREAM_MAP;
    }

    public static IreliaUpstream getUpstream(String name) {
        return UPSTREAM_MAP.get(name);
    }
}