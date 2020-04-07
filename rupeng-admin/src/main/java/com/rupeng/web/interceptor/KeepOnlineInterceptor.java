package com.rupeng.web.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import com.rupeng.pojo.AdminUser;
import com.rupeng.service.AdminUserService;
import com.rupeng.web.redis.JedisClient;

public class KeepOnlineInterceptor extends HandlerInterceptorAdapter{

	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private AdminUserService adminUserService;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//���жϵ�ǰsession����û��adminUser����
		AdminUser adminUser = (AdminUser) request.getSession().getAttribute("adminUser");
	
		//���û��adminUser , ��cookie��ȡ��ԭ����sessionId
		if(adminUser==null){
			Cookie sessionCookie = WebUtils.getCookie(request, "JSESSIONID");
			if(sessionCookie==null){
				return true;
			}
			
			String oldSessionId = sessionCookie.getValue();
			
			//��redis���������õ���Ӧ��userId
			String userId = jedisClient.get("keepOnline_"+oldSessionId);
			if(userId==null){
				return true;
			}
			
			//�����ݿ��и���userid��adminUser��ѯ���������õ�session����
			adminUser = adminUserService.selectOne(Long.parseLong(userId));
			if(adminUser==null){
				return true;
			}
			
			request.getSession().setAttribute("adminUser", adminUser);
		}
		
		//�ѵ�ǰsessionid��userid��ŵ�redis������
		if(adminUser!=null){
			jedisClient.setex("keepOnline_"+request.getSession().getId(),24*24*60,adminUser.getId()+"");
		}
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}

}
