package com.yhaj.xr.controller;

import com.yhaj.xr.domain.Website;
import org.apache.commons.beanutils.BeanUtils;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/9/28 15:04
 */
@WebServlet("/website/*")
public class WebsiteServlet extends BaseServlet {
    //private WebsiteDaoImpl dao = new WebsiteDaoImpl();

    public void admin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Website> websites = service.list();
        Website website = (websites == null || websites.isEmpty()) ? null : websites.get(0);
        request.setAttribute("website", website);
        request.getRequestDispatcher("/WEB-INF/page/admin/website.jsp").forward(request, response);
    }

    public void save(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Website website = new Website();
        BeanUtils.populate(website,request.getParameterMap());
        if (service.save(website)){
            response.sendRedirect(request.getContextPath()+"/website/admin");
        }else {
            request.setAttribute("error","网站底部信息保存失败！");
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
