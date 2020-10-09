package com.yhaj.xr.controller;

import com.yhaj.xr.domain.Education;
import com.yhaj.xr.domain.Skill;
import com.yhaj.xr.service.SkillService;
import com.yhaj.xr.service.impl.SkillServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/9/30 17:39
 */
@WebServlet("/skill/*")
public class SkillServlet extends BaseServlet {

    //private SkillService service = new SkillServiceImpl();

    public void admin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Skill> skills = service.list();
        request.setAttribute("skills", skills);
        request.getRequestDispatcher("/WEB-INF/page/admin/skill.jsp").forward(request, response);
    }

    public void save(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Skill skill = new Skill();
        BeanUtils.populate(skill, request.getParameterMap());
        if (service.save(skill)) {
            response.sendRedirect(request.getContextPath() + "/skill/admin");
        } else {
            request.setAttribute("error", "专业技能保存失败！");
            request.getRequestDispatcher("/WEB-INF/page/error.jsp").forward(request, response);
        }
    }

    public void remove(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        if (service.remove(Integer.valueOf(id))) {
            response.sendRedirect(request.getContextPath() + "/skill/admin");
        } else {
            request.setAttribute("error", "专业技能信息删除失败！");
            request.getRequestDispatcher("/WEB-INF/page/error.jsp").forward(request, response);
        }
    }

    public void removeAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] strIds = request.getParameterValues("id");
        List<Integer> ids = new ArrayList<>();
        for (String strId : strIds) {
            ids.add(Integer.valueOf(strId));
        }
        if (service.remove(ids)) {
            response.sendRedirect(request.getContextPath() + "/skill/admin");
        } else {
            request.setAttribute("error", "专业技能信息删除失败！");
            request.getRequestDispatcher("/WEB-INF/page/error.jsp").forward(request, response);
        }
    }
}