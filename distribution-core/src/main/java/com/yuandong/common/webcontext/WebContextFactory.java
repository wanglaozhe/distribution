package com.yuandong.common.webcontext;

import javax.servlet.ServletContext;

public class WebContextFactory {

	private static ServletContext servletContext;

    private static String webRealPath;    //本地web根路径

	private WebContextFactory() {
    }

    private static ThreadLocal<IWebContext> ctxStore = new ThreadLocal<IWebContext>();

    public static void setWebContext(IWebContext ctx) {
        ctxStore.set(ctx);
    }

    public static IWebContext getWebContext() {
        IWebContext ctx =  ctxStore.get();
        if (ctx == null) {
            ctx = new DefaultWebContext();
            setWebContext(ctx);
        }
        return  ctxStore.get();
    }

    @SuppressWarnings("all")
    public void setServletContext(ServletContext servletContext) {
        setContext(servletContext);
    }

    public static synchronized void setContext(ServletContext servletContext) {
        WebContextFactory.servletContext = servletContext;
    }


    public static String getContentPath() {
        return servletContext.getContextPath();
    }

    public static String getWebRealPath() {
        if (webRealPath != null) {
            return webRealPath;
        } else {
            webRealPath =servletContext==null?null: servletContext.getRealPath("/");
            return webRealPath;
        }
    }
}
