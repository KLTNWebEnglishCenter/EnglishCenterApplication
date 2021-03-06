package web.english.application.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import web.english.application.dao.UserDAO;
import web.english.application.security.filter.CustomAuthenticationFilter;
import web.english.application.security.filter.CustomAuthorizationFilter;
import web.english.application.utils.RoleType;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private UserDAO userDAO;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests().antMatchers("/admin/assets/**","/css/**","/img/**","/js/**","/home/**", "/about/**", "/contact/**", "/login/**", "/logout","/info/**",
                "/password/**", "/course/**","/register/**").permitAll();

        http.authorizeRequests().antMatchers("/admin/approvestudent/**", "/admin/classrooms/**", "/admin/classroom/**",
                "/admin/addClassroom/**", "/admin/updateClassroom/**", "/admin/courses/**", "/admin/course/**", "/admin/student/**",
                "/admin/addstudent/**", "/admin/editstudent/**", "/admin/studentinfo/**", "/admin/teacher/**", "/admin/addteacher/**",
                "/admin/editteacher/**", "/admin/teacherinfo/**", "/admin/requestcourse/**").hasAnyAuthority(RoleType.EMPLOYEE);

        http.authorizeRequests().antMatchers("/admin/document/**", "/admin/adddocument/**", "/admin/schedule/**",
                "/admin/exam/**", "/admin/notification/**", "/admin/addnotification/**", "/admin/notificationinfo/**",
                "/admin/editnotification/**").hasAnyAuthority(RoleType.TEACHER);

        http.authorizeRequests().antMatchers(  "/exam/**","/user/exam/**", "/posts/**", "/myPost/**", "/newPost/**",
                "/new/post/**", "/score/**", "/schedule/**", "/classroom/**",
                "/student/classroom/**").hasAnyAuthority(RoleType.STUDENT);

        http.authorizeRequests().antMatchers("/admin/employee/**", "/admin/addemployee/**", "/admin/editemployee/**",
                "/admin/employeeinfo/**").hasAnyAuthority(RoleType.ADMIN);

        http.authorizeRequests().antMatchers("/admin/posts/**", "/admin/addPost/**",
                "/admin/post/**").hasAnyAuthority(RoleType.TEACHER,RoleType.EMPLOYEE);

//        http.authorizeRequests().antMatchers("/exam/**").hasAnyAuthority(RoleType.TEACHER,RoleType.EMPLOYEE,RoleType.STUDENT);

        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/access-denied");

        http.authorizeRequests()
                // C???u h??nh cho Login Form.
                .and().formLogin()
                .loginProcessingUrl("/login") // the URL to submit the username and password to
                .loginPage("/login") // the custom login page
                .usernameParameter("username")
                .passwordParameter("password");

        http.authorizeRequests().and().logout().logoutUrl("/logout").logoutSuccessUrl("/login").deleteCookies("access_token").invalidateHttpSession(true);

        http.authorizeRequests().anyRequest().authenticated();

        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(userDAO), UsernamePasswordAuthenticationFilter.class);

//        http.exceptionHandling().authenticationEntryPoint(new CustomEntryPoint());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return new CustomAuthenticationManager();
    }

    @Bean
    public HttpFirewall allowUrlSemicolonHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowSemicolon(true);
        return firewall;
    }
}
