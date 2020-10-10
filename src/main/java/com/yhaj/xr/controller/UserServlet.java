package com.yhaj.xr.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.yhaj.xr.domain.User;
import com.yhaj.xr.service.UserService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.*;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/10/9 11:37
 */
@WebServlet("/user/*")
public class UserServlet extends BaseServlet {

    @Override
    public void admin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        request.setAttribute("user", user);
        forward(request, response, "admin/user.jsp");
    }

    @Override
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
                String imgPath = "upload/image/" + fileName;
                String filePath = request.getServletContext().getRealPath(imgPath);
                FileUtils.copyInputStreamToFile(item.getInputStream(), new File(filePath));
                map.put("photo", imgPath);
            }
        }
        User user = new User();
        BeanUtils.populate(user, map);
        User oldUser = (User) request.getSession().getAttribute("user");
        user.setPassword(oldUser.getPassword());
        if (service.save(user)) {
            response.sendRedirect(request.getContextPath() + "/user/admin");
            request.getSession().setAttribute("user", user);
        } else {
            request.setAttribute("error", "获奖成就保存失败！");
            request.getRequestDispatcher("/WEB-INF/page/error.jsp").forward(request, response);
        }
    }

    @Override
    public void remove(HttpServletRequest request, HttpServletResponse response) throws Exception {
    }

    @Override
    public void removeAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
    }

    public void login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String captchaCode = (String) request.getSession().getAttribute("captchaCode");
        String captcha = request.getParameter("captcha");
        if (!captcha.equals(captchaCode)) {
            forwardError(request, response, "验证码错误！");
            return;
        }
        User user = new User();
        BeanUtils.populate(user, request.getParameterMap());
        User loginUser = ((UserService) service).login(user);
        if (loginUser != null) {
            request.getSession().setAttribute("user", loginUser);
            redirect(request, response, "/user/admin");
        } else {
            forwardError(request, response, "账户或密码错误！");
        }
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getSession().removeAttribute("user");
        redirect(request, response, "page/login.jsp");
    }

    public void password(HttpServletRequest request, HttpServletResponse response) throws Exception {
        forward(request, response, "admin/password.jsp");
    }

    public void updatePassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        String oldPassword = request.getParameter("oldPassword");
        if (!user.getPassword().equals(oldPassword)) {
            forwardError(request, response, "旧密码错误！");
        } else {
            user.setPassword(request.getParameter("newPassword"));
            if (service.save(user)) {
                response.sendRedirect(request.getContextPath() + "/user/admin");
                request.getSession().setAttribute("user", user);
            } else {
                request.setAttribute("error", "密码修改失败！");
                request.getRequestDispatcher("/WEB-INF/page/error.jsp").forward(request, response);
            }
        }
    }

    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 创建Katpcha对象
        DefaultKaptcha dk = new DefaultKaptcha();

        // 验证码的配置
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("kaptcha.properties")) {
            Properties properties = new Properties();
            properties.load(is);

            Config config = new Config(properties);
            dk.setConfig(config);
        }

        // 生成验证码字符串
        String code = dk.createText();

        request.getSession().setAttribute("captchaCode", code.toLowerCase());

        // 生成验证码图片
        BufferedImage image = dk.createImage(code);

        // 设置返回数据的格式
        response.setContentType("image/jpeg");

        // 将图片数据写会到客户端
        ImageIO.write(image, "jpg", response.getOutputStream());
    }
}
