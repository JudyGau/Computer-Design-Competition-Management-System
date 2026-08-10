package cqu.jsjds.Filter;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {"/student/*", "/teacher/*", "/manager/*", "/schoolManager/*", "/reviewer/*", "/judge/*"})
public class AuthenticationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession(false);
        System.out.println("session为" + session);
        // 获取请求的 URI
        String requestURI = request.getRequestURI();
        System.out.println("检查请求" + requestURI);

        // 检查是否为 OPTIONS 请求（预检请求）
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            // 直接返回响应，避免重定向
            response.setStatus(HttpServletResponse.SC_OK);
            response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            return;
        }

        // 设置允许跨域的来源
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000"); // 允许的前端地址
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
        response.setHeader("Access-Control-Allow-Credentials", "true"); // 允许携带 cookies

        // 检查用户是否已登录，如果未登录则重定向到登录页面
        if (requestURI.contains("/student/")) {
            System.out.println("检查是否已有学生登录");
            if (session == null || session.getAttribute("student") == null) {
                System.out.println("学生未登录,请求被拦截");
                response.sendRedirect("http://localhost:3000/logins/student-login.html");
                System.out.println("重定向请求已发送");
                return;
            }
        } else if (requestURI.contains("/teacher/")) {
            System.out.println("检查是否已有教师登录");
            if (session == null || session.getAttribute("teacher") == null) {
                System.out.println("教师未登录,请求被拦截");
                response.sendRedirect("http://localhost:3000/logins/teacher-login.html");
                System.out.println("重定向请求已发送");
                return;
            }
        } else if (requestURI.contains("/manager/")) {
            System.out.println("检查是否有管理员登录");
            if (session == null || session.getAttribute("manager") == null) {
                System.out.println("管理员未登录，请求被拦截");
                return;
            }
        } else if (requestURI.contains("/schoolManager/")) {
            System.out.println("检查是否有校管理员登录");
            if (session == null || session.getAttribute("schoolManager") == null) {
                System.out.println("校管理员未登录，请求被拦截");
                return;
            }
        } else if (requestURI.contains("/reviewer/")) {
            System.out.println("检查是否有审核登录");
            if (session == null || session.getAttribute("reviewer") == null) {
                System.out.println("审核未登录，请求被拦截");
                return;
            }
        } else if (requestURI.contains("/judge/")) {
            System.out.println("检查是否有评委登录");
            if (session == null || session.getAttribute("judge") == null) {
                System.out.println("评委未登录，请求被拦截");
                return;
            }
        }
        // 用户已经登录，允许请求继续执行
        chain.doFilter(req, resp);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        // 过滤器初始化方法，在这里可以进行一些初始化操作
    }

    @Override
    public void destroy() {
        // 过滤器销毁方法，在这里可以进行一些资源释放操作
    }
}