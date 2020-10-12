package com.yhaj.xr.controller;

import com.yhaj.xr.domain.Company;
import com.yhaj.xr.service.CompanyService;
import com.yhaj.xr.service.impl.CompanyServiceImpl;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/10/1 17:18
 */

@WebServlet("/company/*")
public class CompanyServlet extends BaseServlet {

    public void admin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Company> companies = service.list();
        request.setAttribute("companies", companies);
        request.getRequestDispatcher("/WEB-INF/page/admin/company.jsp").forward(request, response);
    }

    public void save(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> map = new HashMap<>();
        ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
        upload.setHeaderEncoding("UTF-8");
        List<FileItem> fileItems = upload.parseRequest(request);
        for (FileItem item : fileItems) {
            if (item.isFormField()){
                map.put(item.getFieldName(), item.getString("UTF-8"));
            }else {
                String name = item.getName();
                if (name == null || name.equals(""))
                    continue;
                String fileName = UUID.randomUUID() + "." + FilenameUtils.getExtension(name);
                String imgPath = "upload/image/"+ fileName;
                String filePath = request.getServletContext().getRealPath(imgPath);
                FileUtils.copyInputStreamToFile(item.getInputStream(), new File(filePath));
                map.put("logo", imgPath);
            }
        }
        Company company = new Company();
        BeanUtils.populate(company,map);
        if (service.save(company)){
            response.sendRedirect(request.getContextPath() + "/company/admin");
        }else {
            request.setAttribute("error","公司信息保存失败！");
            request.getRequestDispatcher("/WEB-INF/page/error.jsp").forward(request, response);
        }
    }

    public void remove(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        if (service.remove(Integer.valueOf(id))) {
            response.sendRedirect(request.getContextPath() + "/company/admin");
        } else {
            request.setAttribute("error","公司信息删除失败！");
            request.getRequestDispatcher("/WEB-INF/page/error.jsp").forward(request, response);
        }
    }

    public void removeAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] strId = request.getParameterValues("id");
        List<Integer> ids = new ArrayList<>();
        for (String id : strId) {
            ids.add(Integer.valueOf(id));
        }
        if (service.remove(ids)) {
            response.sendRedirect(request.getContextPath() + "/company/admin");
        } else {
            request.setAttribute("error","公司信息删除失败！");
            request.getRequestDispatcher("/WEB-INF/page/error.jsp").forward(request, response);
        }
    }

}
