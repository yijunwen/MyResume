package com.yhaj.xr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.yhaj.xr.domain.Award;
import com.yhaj.xr.domain.Skill;
import com.yhaj.xr.domain.User;
import com.yhaj.xr.service.*;
import com.yhaj.xr.service.impl.AwardServiceImpl;
import com.yhaj.xr.service.impl.SkillServiceImpl;
import com.yhaj.xr.service.impl.WebsiteServiceImpl;
import com.yhaj.xr.util.Uploads;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/10/9 11:37
 */
@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    private SkillService skillService = new SkillServiceImpl();
    private AwardService awardService = new AwardServiceImpl();
    private WebsiteService websiteService = new WebsiteServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String uri = request.getRequestURI();
        String[] cmps = uri.split("/");
        String methodName = "/" + cmps[cmps.length - 1];
        if (methodName.equals(request.getContextPath())) {
            try {
                front(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            super.doGet(request, response);
        }
    }

    public void front(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = (User) service.list().get(0);
        String[] trait = user.getTrait().split(",");
        String[] Interests = user.getInterests().split(",");
        List<Skill> skills = skillService.list();
        List<Award> awards = awardService.list();
        String footer = websiteService.list().get(0).getFooter();
        request.setAttribute("user", user);
        request.setAttribute("trait", trait);
        request.setAttribute("Interests", Interests);
        request.setAttribute("skills", skills);
        request.setAttribute("awards", awards);
        request.setAttribute("footer", footer);
        forward(request, response, "front/user.jsp");
    }

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
        User oldUser = (User) request.getSession().getAttribute("user");
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
                Uploads.deleteOldImage(request, oldUser.getPhoto());
            }
        }
        User user = new User();
        user.setPassword(oldUser.getPassword());
        BeanUtils.populate(user, map);
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
        // 设置编码
        response.setContentType("text/json; charset=UTF-8");
        String captchaCode = (String) request.getSession().getAttribute("captchaCode");
        String captcha = request.getParameter("captcha");
        Map<String, Object> result = new HashMap<>();
        if (!captcha.equals(captchaCode)) {
            //forwardError(request, response, "验证码错误！");
            result.put("success", false);
            result.put("msg", "验证码不正确");
        } else {
            User user = new User();
            Map<String, String[]> parameterMap = request.getParameterMap();
            BeanUtils.populate(user, parameterMap);
            User loginUser = ((UserService) service).login(user);
            if (loginUser != null) {
                request.getSession().setAttribute("user", loginUser);
                String rememberme = request.getParameter("rememberme");
                if ("true".equals(rememberme)) {
                    Cookie cookie = new Cookie("JSESSIONID", request.getSession().getId());
                    cookie.setPath(getServletContext().getContextPath());
                    cookie.setMaxAge(3600 * 24 * 7);
                    response.addCookie(cookie);
                }
                //redirect(request, response, "/user/admin");
                result.put("success", true);
            } else {
                //forwardError(request, response, "账户或密码错误！");
                result.put("success", false);
                result.put("msg", "邮箱或密码不正确");
            }
        }
        response.getWriter().write(new ObjectMapper().writeValueAsString(result));
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
