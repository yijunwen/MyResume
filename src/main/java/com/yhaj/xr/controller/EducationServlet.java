package com.yhaj.xr.controller;

import com.yhaj.xr.domain.Education;
import org.apache.commons.beanutils.BeanUtils;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/9/29 10:14
 */
@WebServlet("/education/*")
public class EducationServlet extends BaseServlet {
    //private EducationserviceImpl service = new EducationserviceImpl();

    public void admin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Education> educations = service.list();
        request.setAttribute("educations", educations);
        request.getRequestDispatcher("/WEB-INF/page/admin/education.jsp").forward(request, response);
    }

    public void save(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Education education = new Education();
        BeanUtils.populate(education,request.getParameterMap());
        if (service.save(education)){
            response.sendRedirect(request.getContextPath()+"/education/admin");
        }else{
            request.setAttribute("error","教育信息保存失败！");
            request.getRequestDispatcher("/WEB-INF/page/error.jsp").forward(request,response);
        }
    }

    public void remove(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        if (service.remove(Integer.valueOf(id))){
            response.sendRedirect(request.getContextPath()+"/education/admin");
        }else{
            request.setAttribute("error","教育信息删除失败！");
            request.getRequestDispatcher("/WEB-INF/page/error.jsp").forward(request,response);
        }
    }

    public void removeAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] idStrs = request.getParameterValues("id");
        List<Integer> ids = new ArrayList<>();
        for (String idStr : idStrs) {
            ids.add(Integer.valueOf(idStr));
        }
        if (service.remove(ids)){
            response.sendRedirect(request.getContextPath()+"/education/admin");
        }else{
            request.setAttribute("error","教育信息删除失败！");
            request.getRequestDispatcher("/WEB-INF/page/error.jsp").forward(request,response);
        }
    }
}
