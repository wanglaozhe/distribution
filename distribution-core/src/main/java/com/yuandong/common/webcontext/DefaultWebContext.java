package com.yuandong.common.webcontext;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.yuandong.util.CookieUtils;
import com.yuandong.vo.SecurityUser;

public class DefaultWebContext implements IWebContext {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;



    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public void setSessionAttr(String key, Object value) {
        if (session != null) {
            session.setAttribute(key, value);
        }
    }

    public Object getSessionAttr(String key) {
        if (session != null) {
            return session.getAttribute(key);
        }
        return null;
    }

    public void removeSessionAttr(String key) {
        if (session != null) {
            session.removeAttribute(key);
        }
    }

    public void setRequestAttr(String key, Object value) {
        if (request != null) {
            request.setAttribute(key, value);
        }
    }

    public Object getRequestAttr(String key) {
        if (request != null) {
            request.getAttribute(key);
        }
        return null;
    }

    public void removeRequestAttr(String key) {
        if (request != null) {
            request.removeAttribute(key);
        }
    }

    @Override
    public String getHost() {
        if (request != null) {
            return request.getRemoteHost();
        }
        return null;
    }

    @Override
    public String getRemoteAddr() {
        if (request != null) {
            return request.getRemoteAddr();
        }
        return null;
    }

    @Override
    public Cookie getCookie(String cookieName) {
        if (getCookies() != null) {
            for (Cookie c : getCookies()) {
                if (c.getName().equalsIgnoreCase(cookieName)) {
                    return c;
                }
            }
        }
        return null;
    }

    @Override
    public String getCookieValue(String cookieName) {
        Cookie cookie = getCookie(cookieName);
        return cookie == null ? "" : cookie.getValue();
    }

    @Override
    public Cookie[] getCookies() {
        return request.getCookies();
    }

    @Override
    public void setCookie(Cookie c) {
        response.addCookie(c);
    }

    @Override
    public void expireCookie(String cookieName) {
        Cookie cookie = getCookie(cookieName);
        if (cookie != null) {
            cookie.setMaxAge(-1);
            response.addCookie(cookie);
        }
    }


    @Override
    public String getAccount() {
        Cookie cookie = getCookie("LOGIN_ACCOUNT");
        if (cookie != null) {
            if (cookie.getValue() != null) {
                try {
                    return URLDecoder.decode(cookie.getValue(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return null;
            //return cookie.getValue();
        }
        return null;
    }

    @Override
    public void setAccount(HttpServletRequest request, SecurityUser user) {
        Cookie cookie = null;
        try {
            cookie = new Cookie("LOGIN_ACCOUNT", URLEncoder.encode(user.getUsername(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //Cookie cookie = new Cookie(Global.LOGIN_ACCOUNT, CommonUtil.getName(cuUserExtend));
        cookie.setMaxAge(60 * 60 * 24 * 30 * 1);
        String path = request.getContextPath();
        cookie.setPath("/");
        if (null != request) {// 设置域名的cookie
			String domainName = CookieUtils.getDomainName(request);
			//				System.out.println(domainName);
			if (!"localhost".equals(domainName)) {
				cookie.setDomain(domainName);
			}
		}
//        cookie.setDomain("gzl.com.cn");
        response.addCookie(cookie);
    }

}
