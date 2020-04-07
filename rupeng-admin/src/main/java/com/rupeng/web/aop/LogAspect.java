package com.rupeng.web.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.rupeng.pojo.AdminUser;
import com.rupeng.util.JsonUtils;


//��������
@Aspect
public class LogAspect {
	private final static Logger log=LogManager.getLogger(LogAspect.class);
	
	//�е㶨���ڱ���requestMappingע���
	@Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public void controller(){
	}
	
	//����֪ͨ
	@Before("controller()")
	public void log(JoinPoint joinPoint){//joinPoint �ܻ�ȡ����ǿ�ķ������ͷ�������
		if(!log.isDebugEnabled()){
			return;
		}
		
		//��ȡrequest
	   HttpServletRequest request=((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	  //��ȡ��¼�û�
	   AdminUser adminUser = (AdminUser) request.getSession().getAttribute("adminUser");
	   Long userId=null;
	   if(adminUser!=null){
		  userId = adminUser.getId();
	   }
	   
	   //��ȡ����
	   Object[] args = joinPoint.getArgs();
	   for(int i=0;i<args.length;i++){
		   if(args[i] instanceof HttpServletRequest){
			   args[i]=args[i]+"��request����";
		   }
		   if(args[i] instanceof HttpServletResponse){
			   args[i]=args[i]+"��response����";
		   }
		    if (args[i] instanceof MultipartFile) {
               args[i] = args[i]+"MultipartFilet����";
           } 
		    if (args[i] instanceof BindingResult) {
               args[i] = args[i]+"BindingResult����";
           }
	   }
	   
	   log.info("�û�id��{}������ǩ����{}�������Ĳ����б�{}", userId, joinPoint.getSignature(), JsonUtils.toJson(args));
	}
	

}
