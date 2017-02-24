/*
 * Copyright (c) 2002-2012 Alibaba Group Holding Limited.
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.citrus.webx.impl;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.citrus.service.requestcontext.RequestContext;
import com.alibaba.citrus.util.ServletUtil;
import com.alibaba.citrus.webx.WebxComponent;
import com.alibaba.citrus.webx.support.AbstractWebxRootController;
import com.alibaba.citrus.webx.util.WebxUtil;

/**
 * 对<code>WebxRootController</code>的默认实现。
 *
 * @author Michael Zhou
 */
public class WebxRootControllerImpl extends AbstractWebxRootController {
    @Override
    protected boolean handleRequest(RequestContext requestContext) throws Exception {
        HttpServletRequest request = requestContext.getRequest();

        // Servlet mapping有两种匹配方式：前缀匹配和后缀匹配。
        // 对于前缀匹配，例如：/servlet/aaa/bbb，servlet path为/servlet，path info为/aaa/bbb
        // 对于前缀匹配，当mapping pattern为/*时，/aaa/bbb，servlet path为""，path info为/aaa/bbb
        // 对于后缀匹配，例如：/aaa/bbb.html，servlet path为/aaa/bbb.html，path info为null
        //
        // 对于前缀匹配，取其pathInfo；对于后缀匹配，取其servletPath。
        String path = ServletUtil.getResourcePath(request);
        /**
         * Webx首先通过这行代码获取绝对路径，也就是port之后到?之前的这一段字符串。在上面的例子中，这个path就是/search。
         * 如果是http://aaa.bbb.com/xxx/yyy/zzz.jsonp?key=value，那么这个path就是/xxx/yyy/zzz.jsonp。得到绝对路径之后，
         * 就按照该路径去寻找匹配的组件
         * */

        // 再根据path查找component，component 就是类似与user，store，home这种，
        // 到目前，就是根据url，获取component，比如我可以知道是user组件下的，如果没找到就是默认的组件
        WebxComponent component = getComponents().findMatchedComponent(path);
        boolean served = false;


        if (component != null) {
            try {
                // request.setAttribute(CURRENT_WEBX_COMPONENT_KEY, component);
                WebxUtil.setCurrentComponent(request, component);
                served = component.getWebxController().service(requestContext);
                // WebxControllerImpl 下面的service，
                // getWebxController可以用默认的，就是上面这个，也可以另外自己写
            } finally {
                WebxUtil.setCurrentComponent(request, null);
            }
        }

        return served;
    }
}
