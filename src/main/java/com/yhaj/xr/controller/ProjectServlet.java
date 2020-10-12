package com.yhaj.xr.controller;

import com.yhaj.xr.domain.Award;
import com.yhaj.xr.domain.Company;
import com.yhaj.xr.domain.Project;
import com.yhaj.xr.domain.User;
import com.yhaj.xr.service.CompanyService;
import com.yhaj.xr.service.UserService;
import com.yhaj.xr.service.WebsiteService;
import com.yhaj.xr.service.impl.CompanyServiceImpl;
import com.yhaj.xr.service.impl.UserServiceImpl;
import com.yhaj.xr.service.impl.WebsiteServiceImpl;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/10/8 12:39
 */
@WebServlet("/project/*")
public class ProjectServlet extends BaseServlet {

    private CompanyService companyService = new CompanyServiceImpl();
    private UserService userService = new UserServiceImpl();
    private WebsiteService websiteService = new WebsiteServiceImpl();

    public void front(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("user", userService.list().get(0));
        request.setAttribute("projects", service.list());
        request.setAttribute("footer", websiteService.list().get(0).getFooter());
        request.getRequestDispatcher("/WEB-INF/page/front/project.jsp").forward(request, response);
    }

    public void admin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Project> projects = service.list();
        List<Company> companies = companyService.list();
        request.setAttribute("projects", projects);
        request.setAttribute("companies", companies);
        request.getRequestDispatcher("/WEB-INF/page/admin/project.jsp").forward(request, response);
    }
    public void save(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> map = new HashMap<>();
        ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
        upload.setHeaderEncoding("UTF-8");
        List<FileItem> fileItems = upload.parseRequest(request);
        for (FileItem item : fileItems) {
            if (item.isFormField()) {
                map.put(item.getFieldName(), item.getString("UTF-8"));
            } else {
                String name = item.getName();
                if (name == null || name.equals(""))
                    continue;
                String fileName = UUID.randomUUID() + "." + FilenameUtils.getExtension(name);
                String imgPath = "upload/image/"+ fileName;
                String filePath = request.getServletContext().getRealPath(imgPath);
                FileUtils.copyInputStreamToFile(item.getInputStream(), new File(filePath));
                map.put("image", imgPath);
            }
        }
        Project project = new Project();
        Company company = new Company();
        project.setCompany(company);
        company.setId(Integer.valueOf(map.get("CompanyId")));
        BeanUtils.populate(project, map);
        if (service.save(project)) {
            response.sendRedirect(request.getContextPath() + "/project/admin");
        } else {
            request.setAttribute("error", "项目经验保存失败！");
            request.getRequestDispatcher("/WEB-INF/page/error.jsp").forward(request, response);
        }
    }

    public void remove(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        if (service.remove(Integer.valueOf(id))) {
            response.sendRedirect(request.getContextPath() + "/project/admin");
        } else {
            request.setAttribute("error", "获奖成就删除失败！");
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
            response.sendRedirect(request.getContextPath() + "/project/admin");
        } else {
            request.setAttribute("error", "获奖成就删除失败！");
            request.getRequestDispatcher("/WEB-INF/page/error.jsp").forward(request, response);
        }
    }
}
