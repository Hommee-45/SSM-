package com.rupeng.web.interceptor;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.rupeng.pojo.AdminUser;
import com.rupeng.service.AdminUserRoleService;
import com.rupeng.util.CommonUtils;
import com.rupeng.util.JsonUtils;

public class PermissionInterceptor extends HandlerInterceptorAdapter{

	@Autowired
	private AdminUserRoleService adminUserRoleService;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//1������û��Ƿ��¼
		AdminUser adminUser = (AdminUser) request.getSession().getAttribute("adminUser");
		if(adminUser==null){
			//�ж�����������
			if(CommonUtils.isEmpty(request.getHeader("X-Request-With"))){
				//ajax����
				response.getWriter().print(JsonUtils.toJson("����û�е�¼�����ȵ�¼��"));
			}else{
				//��ͨ����
				response.getWriter().print("����û�е�¼�����ȵ�¼��");
			}
			return false;
		}
		//2�����Ȩ��
		//����·��  Ϊ�˿������㣬������Ȩ�޵Ĺ��ܹر�
		String servletPath = request.getServletPath();
		HashMap<String, Object> map=new HashMap<>();
		map.put("adminUserId", adminUser.getId());
		map.put("path",servletPath);
		boolean checkPermission = adminUserRoleService.checkPermission(map);
		if(!checkPermission){
			//�ж�����������
			if(CommonUtils.isEmpty(request.getHeader("X-Request-With"))){
				//ajax����
				response.getWriter().print(JsonUtils.toJson("����û��Ȩ�ޣ�"));
			}else{
				//��ͨ����
				response.getWriter().print("����û��Ȩ�ޣ�");
			}
			return false;
		}
		return true;
	}
}
