package wen.SmartBPractice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import wen.SmartBPractice.util.Util;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin").password("admin").roles("ADMIN")
                .and()
                .withUser("manager").password("manager").roles("MANAGER")
                .and()
                .withUser("user").password("user").roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling()
                // 管控未登入行為
                .authenticationEntryPoint(
                    (req, resp, auth) -> {
                        resp.setContentType("application/json;charset=UTF-8");
                        resp.setCharacterEncoding("UTF-8");
                        resp.setStatus(resp.SC_UNAUTHORIZED);
                        PrintWriter writer = resp.getWriter();
                        writer.write(Util.mapToJsonString("error", "請先登入才能進行此操作"));
                        writer.flush();
                        writer.close();
                })
                // 管控未取得 role 行為
                .accessDeniedHandler(
                    (req, resp, auth) -> {
                        resp.setContentType("application/json;charset=UTF-8");
                        resp.setCharacterEncoding("UTF-8");
                        resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        PrintWriter out = resp.getWriter();
                        out.write(Util.mapToJsonString("message", "你無權限可執行該動作!"));
                        out.flush();
                        out.close();
                })
                .and()
                .addFilterAt(loginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()

                .antMatchers("/api/company/create/**",   "/api/client/create/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/company/modify/**",  "/api/client/modify/**").hasAnyRole("MANAGER", "ADMIN")
                .antMatchers("/api/company/view",       "/api/client/view").hasAnyRole("USER", "ADMIN", "MANAGER")
                .antMatchers("/api/company/delete/**",  "/api/client/delete/**").hasAnyRole("MANAGER", "ADMIN")

                .and()
                .logout()
                .logoutUrl("/api/logout")
                .invalidateHttpSession(true)
                .logoutSuccessHandler(
                    (req, resp, auth) -> {
                        resp.setContentType("application/json;charset=UTF-8");
                        PrintWriter out = resp.getWriter();
                        resp.setStatus(200);
                        String msg = Util.mapToJsonString("message", "登出成功");
                        out.write(msg);
                        out.flush();
                        out.close();
                })
                .and()
                .csrf()
                .disable();
    }


    // 設定成功及失敗 Handler, 這樣設定是避免 login page
    // 設定 Access Url
    @Bean
    LoginAuthenticationFilter loginAuthenticationFilter() throws Exception {
        LoginAuthenticationFilter filter = new LoginAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        // 成功取得登入設置 role
        filter.setAuthenticationSuccessHandler(
            (req, resp, auth) -> {
                // 透過 session 存取
                // 回傳角色訊息
                String account = auth.getName();
                Collection collection = auth.getAuthorities();
                String authority = collection.iterator().next().toString();
                HttpSession session = req.getSession();
                session.setAttribute("logged_in", account);
                session.setAttribute("user_type", authority);
                Map<String, String> result = new HashMap<>();
                result.put("authority", authority);
                resp.setContentType("application/json;charset=UTF-8");
                PrintWriter out = resp.getWriter();
                resp.setStatus(200);
                ObjectMapper om = new ObjectMapper();
                out.write(om.writeValueAsString(result));
                out.flush();
                out.close();
        });
        // 登入失敗
        filter.setAuthenticationFailureHandler(
            (req, resp, auth) -> {
                resp.setContentType("application/json;charset=UTF-8");
                PrintWriter out = resp.getWriter();
                resp.setStatus(404);
                String msg = Util.mapToJsonString("message", "登入失敗");
                out.write(msg);
                out.flush();
                out.close();
        });
        filter.setFilterProcessesUrl("/api/login");
        return filter;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}