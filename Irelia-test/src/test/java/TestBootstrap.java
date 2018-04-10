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

import cn.fanhub.irelia.server.Bootstrap;
import org.junit.Test;

/**
 *
 * @author chengfan
 * @version $Id: TestBootstrap.java, v 0.1 2018年04月09日 下午10:33 chengfan Exp $
 */
public class TestBootstrap {

    @Test
    public void test() throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.setPort(8765);

        bootstrap.start();

    }
}