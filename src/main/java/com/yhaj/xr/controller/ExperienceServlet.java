package com.yhaj.xr.controller;

import com.yhaj.xr.domain.Company;
import com.yhaj.xr.domain.Experience;
import com.yhaj.xr.service.CompanyService;
import com.yhaj.xr.service.ExperienceService;
import com.yhaj.xr.service.UserService;
import com.yhaj.xr.service.WebsiteService;
import com.yhaj.xr.service.impl.CompanyServiceImpl;
import com.yhaj.xr.service.impl.ExperienceServiceImpl;
import com.yhaj.xr.service.impl.UserServiceImpl;
import com.yhaj.xr.service.impl.WebsiteServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/10/2 10:35
 */

@WebServlet("/experience/*")
public class ExperienceServlet extends BaseServlet {

    private ExperienceService service = new ExperienceServiceImpl();
    private CompanyService companyService = new CompanyServiceImpl();
    private UserService userService = new UserServiceImpl();
    private WebsiteService websiteService = new WebsiteServiceImpl();

    public void front(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("user", userService.list().get(0));
        request.setAttribute("footer", websiteService.list().get(0).getFooter());
        request.setAttribute("experiences", service.list());
        forward(request, response, "front/experience.jsp");
    }


    public void admin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Experience> experiences = service.list();
        List<Company> companies = companyService.list();
        request.setAttribute("experiences", experiences);
        request.setAttribute("companies", companies);
        request.getRequestDispatcher("/WEB-INF/page/admin/experience.jsp").forward(request, response);
    }

    public void save(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Experience experience = new Experience();
        BeanUtils.populate(experience, request.getParameterMap());
        Company company = new Company();
        company.setId(Integer.valueOf(request.getParameter("companyId")));
        experience.setCompany(company);
        if (service.save(experience)) {
            response.sendRedirect(request.getContextPath() + "/experience/admin");
        } else {
            request.setAttribute("error", "工作经验信息保存失败！");
            request.getRequestDispatcher("/WEB-INF/page/error.jsp").forward(request, response);
        }
    }

    public void remove(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        if (service.remove(Integer.valueOf(id))) {
            response.sendRedirect(request.getContextPath() + "/experience/admin");
        } else {
            request.setAttribute("error", "工作经验信息删除失败！");
            request.getRequestDispatcher("/WEB-INF/page/error.jsp").forward(request, response);
        }
    }

    public void removeAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] idStrs = request.getParameterValues("id");
        List<Integer> ids = new ArrayList<>();
        for (String idStr : idStrs) {
            ids.add(Integer.valueOf(idStr));
        }
        if (service.remove(ids)) {
            response.sendRedirect(request.getContextPath() + "/experience/admin");
        } else {
            request.setAttribute("error", "工作经验信息删除失败！");
            request.getRequestDispatcher("/WEB-INF/page/error.jsp").forward(request, response);
        }
    }

}
