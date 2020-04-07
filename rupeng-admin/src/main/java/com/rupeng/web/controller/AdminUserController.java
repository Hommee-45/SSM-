package com.rupeng.web.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rupeng.pojo.AdminUser;
import com.rupeng.pojo.AdminUserRole;
import com.rupeng.pojo.Role;
import com.rupeng.service.AdminUserService;
import com.rupeng.service.RolesService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;
import com.rupeng.util.ImageCodeUtils;

@Controller
@RequestMapping("/page/adminUser")
public class AdminUserController {

	@Autowired
	private AdminUserService adminUserService;
	@Autowired
	private RolesService rolesService;
	@RequestMapping("list.do")
	public String listdo(Model model){
		List<AdminUser> selectList = adminUserService.selectList();
		model.addAttribute("adminUserList", selectList);
		return "adminUser/list";
	}
	
	@RequestMapping("add.do")
	public String adddo(Model model){
		List<Role> selectList = rolesService.selectList();
		model.addAttribute("roleList", selectList);
		return "adminUser/add";
	}
	
	@RequestMapping("add")
	@ResponseBody
	public AjaxResult add(AdminUser adminUser,Long[] roleIds){
		//�ǿ�У��ʡ��
		//Ψһ�Լ��
		if(!adminUserService.isExisted(adminUser)){
			return AjaxResult.errorInstance("�Ѿ����ڸ��û���");
		}
		if (!CommonUtils.isLengthEnough(adminUser.getPassword(), 6)) {
	            return AjaxResult.errorInstance("���볤������6λ");
	    }
		String salt=UUID.randomUUID().toString();
		adminUser.setPasswordSalt(salt);
		String calculateMD5 = CommonUtils.calculateMD5(adminUser.getPassword()+salt);
		adminUser.setPassword(calculateMD5);
		adminUserService.insert(adminUser);
		AdminUser selectOne = adminUserService.selectOne(adminUser);
		for(Long id:roleIds){
			AdminUserRole aur=new AdminUserRole();
			aur.setRoleId(id);
			aur.setAdminuserid(selectOne.getId());
		}
		return AjaxResult.successInstance("����Ա��ӳɹ���");
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public AjaxResult delete(Long id){
		adminUserService.delete(id);
		return AjaxResult.successInstance("ɾ���ɹ�!");
	}
	
	@RequestMapping("resetPassword.do")
	public String resetPassworddo(Long id,Model model){
		AdminUser selectOne = adminUserService.selectOne(id);
		model.addAttribute("adminUser", selectOne);
		return "adminUser/resetPassword";
	}
	
	@RequestMapping("resetPassword")
	@ResponseBody
	public AjaxResult resetPassword(AdminUser adminUser){
		AdminUser selectOne = adminUserService.selectOne(adminUser.getId());
		String passwordSalt=UUID.randomUUID().toString();
		adminUser.setPasswordSalt(passwordSalt);
		adminUser.setPassword(CommonUtils.calculateMD5(adminUser.getPassword()+passwordSalt));
		adminUser.setAccount(selectOne.getAccount());
		adminUser.setIsDisabled(true);
		adminUserService.update(adminUser);
		return AjaxResult.successInstance("�������óɹ���");
	}
	
	@RequestMapping("isDesabled")
	@ResponseBody
	public AjaxResult isDesabled(Long id){
		AdminUser selectOne = adminUserService.selectOne(id);
		Boolean isDisabled = selectOne.getIsDisabled();
		selectOne.setIsDisabled(!isDisabled);
		adminUserService.update(selectOne);
		return new AjaxResult("success");
	}
	
	@RequestMapping("login.do")
	public String loginDo(){
		return "adminUser/login";
	}
	
	
	@RequestMapping("login")
	public String loginSubmit(String imageCode,String account, String password,HttpServletRequest request,Model model){
		//��֤��У��
		if(!ImageCodeUtils.checkImageCode(request.getSession(), imageCode)){
			model.addAttribute("message", "��֤�����");
			return "adminUser/login";
		}
		
		//�û��˻�������У��
		AdminUser au=new AdminUser();
		au.setAccount(account);
		int count = adminUserService.count(au);//��countֵȡ�����ǲ�ѯ��������������ֵ
		if(count<=0){
			model.addAttribute("message", "�˻����������");
			return "adminUser/login";
		}
		AdminUser selectOne = adminUserService.selectOne(au);
		if(!(selectOne.getPassword()).equalsIgnoreCase(CommonUtils.calculateMD5(password+selectOne.getPasswordSalt()))){
			//���벻һ��
			model.addAttribute("message", "�˻����������");
			return "adminUser/login";
		}
		
		//���û��Ƿ����
		if(selectOne.getIsDisabled()){
			model.addAttribute("message", "���û��Ѿ�����");
			return "adminUser/login";
		}
		
		//��½�ɹ�
		request.getSession().setAttribute("adminUser", selectOne);
		model.addAttribute("adminUser", selectOne);
		return "redirect:/index";	
	}
	
	@RequestMapping("updatePassword.do")
	public String updatePassworddo(Long id,Model model){
		model.addAttribute("id", id);
		return "adminUser/updatePassword";
	}
	
	@RequestMapping("update")
	@ResponseBody
	public AjaxResult updatePassword(Long id,String password,String newpassword,String renewpassword){
		AdminUser selectOne = adminUserService.selectOne(id);
		if(!selectOne.getPassword().equalsIgnoreCase(CommonUtils.calculateMD5(password+selectOne.getPasswordSalt()))){
			return AjaxResult.errorInstance("ԭʼ�������!");
		}
		if(!CommonUtils.isLengthEnough(newpassword, 6)){
			return AjaxResult.errorInstance("���볤�Ȳ�������6λ");
		}
		
		if(!newpassword.equals(renewpassword)){
			return AjaxResult.errorInstance("ǰ�����벻һ�£�");
		}
		
		AdminUser adminUser=new AdminUser();
		String passwordSalt = UUID.randomUUID().toString();
		adminUser.setPassword(CommonUtils.calculateMD5(password+passwordSalt));
		adminUser.setPasswordSalt(passwordSalt);
		adminUser.setId(id);
		adminUser.setAccount(selectOne.getAccount());
		adminUser.setIsDisabled(true);
		adminUserService.update(adminUser);
		return AjaxResult.successInstance("�����޸ĳɹ���");
	}
	
	@RequestMapping("loginout")
	public String loginout(HttpServletRequest request){
		//����session
		request.getSession().invalidate();
		return "redirect:/page/adminUser/login.do";
	}
}
