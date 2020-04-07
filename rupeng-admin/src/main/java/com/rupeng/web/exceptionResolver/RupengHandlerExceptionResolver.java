package com.rupeng.web.exceptionResolver;


import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.util.AjaxResult;
import com.rupeng.util.JsonUtils;
//ע����spring��ע���bean
@Component
public class RupengHandlerExceptionResolver implements HandlerExceptionResolver{

	private final static Logger log=LogManager.getLogger(RupengHandlerExceptionResolver.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		
		if(request.getHeader("X-Requested-With")!=null){
			//��ajax����
			try {
				ex.printStackTrace();
				AjaxResult ajaxResult = AjaxResult.errorInstance(ex.getStackTrace());
				response.getWriter().println(JsonUtils.toJson(ajaxResult));
			} catch (IOException e) {
				e.printStackTrace();
				log.error(e);
			}

			//�˴����ؿղ�����modelandview���쳣����������Ϊ�����Լ��Ѿ���ӡ�쳣
			return new ModelAndView();
		}else{
			//��ͨ����
			return new ModelAndView("500");
		}
	}

}
