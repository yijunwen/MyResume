package com.yhaj.xr.controller;

import com.yhaj.xr.domain.Award;
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
 * @date 2020/10/2 14:03
 */

@WebServlet("/award/*")
public class AwardServlet extends BaseServlet {
    public void admin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Award> awards = service.list();
        request.setAttribute("awards", awards);
        request.getRequestDispatcher("/WEB-INF/page/admin/award.jsp").forward(request, response);
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
/*                FileOutputStream fos = new FileOutputStream(new File(imgPath));
                byte[] bytes = new byte[1024 * 4];
                int len;
                while ((len = is.read(bytes)) != -1) {
                    fos.write(bytes, 0, len);
                }
                is.close();
                fos.close();*/
            }
        }
        Award award = new Award();
        BeanUtils.populate(award, map);
        if (service.save(award)) {
            response.sendRedirect(request.getContextPath() + "/award/admin");
        } else {
            request.setAttribute("error", "获奖成就保存失败！");
            request.getRequestDispatcher("/WEB-INF/page/error.jsp").forward(request, response);
        }
    }

    public void remove(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        if (service.remove(Integer.valueOf(id))) {
            response.sendRedirect(request.getContextPath() + "/award/admin");
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
            response.sendRedirect(request.getContextPath() + "/award/admin");
        } else {
            request.setAttribute("error", "获奖成就删除失败！");
            request.getRequestDispatcher("/WEB-INF/page/error.jsp").forward(request, response);
        }
    }
}
