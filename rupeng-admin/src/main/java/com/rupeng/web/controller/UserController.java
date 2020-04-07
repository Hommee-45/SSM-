package com.rupeng.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.rupeng.pojo.User;
import com.rupeng.service.UsersService;
import com.rupeng.util.AjaxResult;

@Controller
@RequestMapping("/page/user")
public class UserController {

	@Autowired
	private UsersService usersService;
	
	//�ֶ����������ڵ��ַ���ת����������ʽ
	 @InitBinder
	    public void initBinder(WebDataBinder binder) {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        //true ��ʾ���ڿ���Ϊ��
	        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	    }
	 
	 
	 //��ҳ��ѯ
	@RequestMapping(value="/list.do",method = RequestMethod.GET)
	public String listdo(Date beginTime,Date endTime,Integer curr,String param,Model model){
		if(curr==null){
			curr=1;
		}
		//Ĭ�ϵ���00��00��00 ������Ҫ��� 23��59��59
		if(endTime!=null){
			endTime.setTime((endTime.getTime()+1000*60*60*24-1));
		}
		Map<String, Object> params=new HashMap<>();
		params.put("beginTime", beginTime);
		params.put("endTime", endTime);
		if (param != null && param.length() > 0) {
            params.put("param", "%" + param + "%");
        }else{
        	param=null;
        }
		PageInfo<User> search = usersService.search(curr,10,params);
		model.addAttribute("pageInfo", search);
		model.addAttribute("param", params);
		return "user/list";
	}
	
	@RequestMapping(value="update.do",method=RequestMethod.GET)
	public String updatedo(Long id,Model model){
		User selectOne = usersService.selectOne(id);
		model.addAttribute("user", selectOne);
		return "user/update";
	}
	
	@RequestMapping(value="update.do",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult update(User user){
		User selectOne = usersService.selectOne(user.getId());
		selectOne.setName(user.getName());
		selectOne.setIsMale(user.getIsMale());
		selectOne.setEmail(user.getEmail());
		selectOne.setPhone(user.getPhone());
		selectOne.setSchool(user.getSchool());
		usersService.update(selectOne);
		return AjaxResult.successInstance("�޸ĳɹ�");
	}
	@RequestMapping(value="isTeacher")
	@ResponseBody
	public AjaxResult isTeacher(Long id){
		User selectOne = usersService.selectOne(id);
		selectOne.setIsTeacher(!selectOne.getIsTeacher());
		int update = usersService.update(selectOne);
		return AjaxResult.successInstance(update>0?"�ѳɹ�����Ϊ��ʦ":"�ѳɹ�����Ϊѧ��");
	}
}
