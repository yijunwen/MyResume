package com.yhaj.xr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yhaj.xr.domain.Contact;
import com.yhaj.xr.domain.ContactListParam;
import com.yhaj.xr.domain.ContactListResult;
import com.yhaj.xr.service.ContactService;
import com.yhaj.xr.service.UserService;
import com.yhaj.xr.service.WebsiteService;
import com.yhaj.xr.service.impl.UserServiceImpl;
import com.yhaj.xr.service.impl.WebsiteServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        ContactListParam param = new ContactListParam();
        BeanUtils.populate(param, request.getParameterMap());
        request.setAttribute("result", ((ContactService) service).list(param));
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

    public void read(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer id = Integer.valueOf(request.getParameter("id"));
        Map<String, Object> result = new HashMap<>();
        if (((ContactService) service).read(id)) {
            result.put("success", true);
            result.put("msg", "查看成功");
        } else {
            result.put("success", false);
            result.put("msg", "查看失败");
        }
        response.setContentType("text/json; charset=UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(result));
    }

    @Override
    public void remove(HttpServletRequest request, HttpServletResponse response) throws Exception {

    }

    @Override
    public void removeAll(HttpServletRequest request, HttpServletResponse response) throws Exception {

    }
}
