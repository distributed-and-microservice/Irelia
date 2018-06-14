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

/**
 *
 * @author chengfan
 * @version $Id: IreliaResponseCode.java, v 0.1 2018年05月10日 下午8:51 chengfan Exp $
 */
public enum IreliaResponseCode {

    NO_SIGN_KEY_OR_VALUE(3006, "没有上传公钥或者没有传递签名信息"),
    SIGN_ERROR(3005, "签名验证失败"),
    RPC_BEEN_LIMITED(3004, "RPC 被限流"),
    NOT_OPEN_RPC(3003, "没有开启的 RPC"),
    INVALID_QUERY_URL(3002, "非法的请求路径"),
    NOT_SUPPORT_REQUEST(3001, "不支持的请求类型"),
    SERVER_ERR(2001, "服务端错误"),
    BAD_REQUEST(2002, "请求失败"),
    CUSTOMER_ERR(2000, "业务方错误"),
    SUCCESS(1000, "响应成功");

    private int code;
    private String message;

    IreliaResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public IreliaResponseCode of(int code) {
        switch (code) {
            case 1000:
                return SUCCESS;
            case 2000:
                return CUSTOMER_ERR;
            case 2001:
                return SERVER_ERR;
            default:
                return SUCCESS;
        }
    }


}