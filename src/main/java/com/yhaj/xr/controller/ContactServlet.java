package com.yhaj.xr.controller;

import com.yhaj.xr.domain.Contact;
import com.yhaj.xr.service.UserService;
import com.yhaj.xr.service.WebsiteService;
import com.yhaj.xr.service.impl.UserServiceImpl;
import com.yhaj.xr.service.impl.WebsiteServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/10/12 15:36
 */
@WebServlet("/contact/*")
public class ContactServlet extends BaseServlet {
    private UserService userService = new UserServiceImpl();
    private WebsiteService websiteService = new WebsiteServiceImpl();

    public void front(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("user", userService.list().get(0));
        request.setAttribute("foot", websiteService.list().get(0).getFooter());
        forward(request, response, "front/contact.jsp");
    }

    @Override
    public void admin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("contacts", service.list());
        forward(request, response, "admin/contact.jsp");
    }

    @Override
    public void save(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String captchaCode = (String) request.getSession().getAttribute("captchaCode");
        if (!captchaCode.equals(request.getParameter("captcha"))) {
            forwardError(request, response, "验证码提交错误");
            return;
        }
        Contact contact = new Contact();
        BeanUtils.populate(contact, request.getParameterMap());
        if (service.save(contact)) {
            response.sendRedirect(request.getContextPath() + "/contact/front");
        } else {
            request.setAttribute("error", "留言信息提交失败！");
            request.getRequestDispatcher("/WEB-INF/page/error.jsp").forward(request, response);
        }
    }

    @Override
    public void remove(HttpServletRequest request, HttpServletResponse response) throws Exception {

    }

    @Override
    public void removeAll(HttpServletRequest request, HttpServletResponse response) throws Exception {

    }
}
