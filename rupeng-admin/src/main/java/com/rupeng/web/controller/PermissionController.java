package com.rupeng.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rupeng.pojo.Permission;
import com.rupeng.service.PermissionsService;
import com.rupeng.util.AjaxResult;

//Ȩ�޹���
@Controller
@RequestMapping("/page/permission")
public class PermissionController {

	@Autowired
	private PermissionsService permissionService;
	
	@RequestMapping("list")
	public String list(Model model){
		List<Permission> selectList = permissionService.selectList();
		model.addAttribute("permissionList", selectList);
		return "permission/list";
	}
	
	
	@RequestMapping("delete")
	@ResponseBody
	public AjaxResult delete(Long id){
		permissionService.delete(id);
		return AjaxResult.successInstance("ɾ���ɹ���");
	}
	
	@RequestMapping("update.do")
	public String updatedo(Long id,Model model){
		Permission selectOne = permissionService.selectOne(id);
		model.addAttribute("permission", selectOne);
		return "permission/update";
	}
	
	@RequestMapping("update")
	@ResponseBody
	public AjaxResult update(Permission permission){
		permissionService.update(permission);
		return	AjaxResult.successInstance("�޸ĳɹ�!");
	}
	
	@RequestMapping("add.do")
	public String adddo(){
		return "permission/add";
	}
	
	@RequestMapping("add")
	@ResponseBody 
	public AjaxResult add(Permission permission){
		//�ǿ�У��ʡ�ԣ�ǰ���Ѿ�У��
		//Ψһ�Լ��
		List<Permission> selectList = permissionService.selectList(permission);
		if(selectList.size()>=1){
			return AjaxResult.errorInstance("�Ѿ����ڸ�Ȩ�ޣ������ظ���ӣ�");
		}
		permissionService.insert(permission);
		return AjaxResult.successInstance("Ȩ����ӳɹ���");
	}
}
