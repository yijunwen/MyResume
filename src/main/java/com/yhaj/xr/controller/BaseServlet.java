package com.yhaj.xr.controller;

import com.yhaj.xr.service.BaseService;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/9/28 15:09
 */
public abstract class BaseServlet extends HttpServlet {
    static {
        // null参数表示允许值为null
        DateConverter dateConverter = new DateConverter(null);
        dateConverter.setPatterns(new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"});
        ConvertUtils.register(dateConverter, Date.class);
    }

    protected BaseService service = newService();

    protected BaseService newService() {
        // com.mj.xr.servlet.WebsiteServlet
        // com.mj.xr.service.impl.WebsiteServiceImpl
        try {
            String clsName = getClass().getName()
                    .replace(".controller.", ".service.impl.")
                    .replace("Servlet", "ServiceImpl");
            return (BaseService) Class.forName(clsName).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public abstract void admin(HttpServletRequest request, HttpServletResponse response) throws Exception;

    public abstract void save(HttpServletRequest request, HttpServletResponse response) throws Exception;

    public abstract void remove(HttpServletRequest request, HttpServletResponse response) throws Exception;

    public abstract void removeAll(HttpServletRequest request, HttpServletResponse response) throws Exception;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            String uri = request.getRequestURI();
            String[] cmps = uri.split("/");
            String methodName = cmps[cmps.length - 1];
            Method method = getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            method.invoke(this, request, response);
        } catch (Exception e) {
            e.printStackTrace();
            Throwable cause = e;
            while (cause.getCause() != null) {
                cause = cause.getCause();
            }
            request.setAttribute("error", cause.getClass().getName() + ": " + cause.getMessage());
            request.getRequestDispatcher("/WEB-INF/page/error.jsp").forward(request, response);
        }
    }

    protected void forward(HttpServletRequest request, HttpServletResponse response, String path) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/page/" + path).forward(request, response);
    }

    protected void redirect(HttpServletRequest request, HttpServletResponse response, String path) throws IOException {
        response.sendRedirect(request.getContextPath() + "/" + path);
    }

    protected void forwardError(HttpServletRequest request, HttpServletResponse response, String error) throws ServletException, IOException {
        request.setAttribute("error", error);
        request.getRequestDispatcher("/WEB-INF/page/error.jsp").forward(request, response);
    }
}
