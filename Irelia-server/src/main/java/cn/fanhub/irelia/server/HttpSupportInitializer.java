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
package cn.fanhub.irelia.server;

import cn.fanhub.irelia.server.handler.HttpInboundHandler;
import cn.fanhub.irelia.server.handler.RouteHandler;
import cn.fanhub.irelia.server.handler.SecurityHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author chengfan
 * @version $Id: HttpSupportInitializer.java, v 0.1 2018年04月09日 下午10:25 chengfan Exp $
 */
@Slf4j
public class HttpSupportInitializer extends ChannelInitializer<Channel> {
    protected void initChannel(Channel channel) throws Exception {
        log.info("init pipeline");
        channel.pipeline()
                .addLast("codec", new HttpServerCodec())
                .addLast("aggregator", new HttpObjectAggregator(512 * 1024))
                .addLast("compressor", new HttpContentCompressor())
                .addLast("http",new HttpInboundHandler())
                .addLast("security", new SecurityHandler())
                .addLast("route", new RouteHandler())
                ;
        //for (String s : channel.pipeline().names()) {
        //    log.info("add pipeline: {}", s);
        //}
    }
}