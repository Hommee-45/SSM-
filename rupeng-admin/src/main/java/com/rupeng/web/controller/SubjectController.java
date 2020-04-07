package com.rupeng.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rupeng.pojo.Subject;
import com.rupeng.service.SubjectService;
import com.rupeng.util.AjaxResult;
/**
 * ����subject����ɾ�Ĳ�
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/page/subject")
public class SubjectController {

	@Autowired
	private SubjectService subjectService;
	
	@RequestMapping("/list.do")
	public String showList(Model model){
		Subject subject=new Subject();
		List<Subject> selectList = subjectService.selectList(subject);
		model.addAttribute("subjectList", selectList);
		return "subject/list";
	}
	
	@RequestMapping("/add.do")
	public String redirectAdd(){
		return "subject/add";
	}
	
	@RequestMapping("/add")
	@ResponseBody
	public AjaxResult add(Subject subject){
		//��Ч�Լ��
		if(subject.getName()==null){
			return AjaxResult.errorInstance("��ӵ�ѧ�Ʋ���Ϊ�գ�");
		}
		
		//Ψһ��У��
		List<Subject> selectList = subjectService.selectList(subject);
		if(selectList.size()>0){
			return AjaxResult.errorInstance("�Ѿ����ڸ�ѧ�ƣ����������룡");
		}
		
		//��������
		subjectService.insert(subject);
		return AjaxResult.successInstance("���ѧ�Ƴɹ���");
	}
	
	@RequestMapping("/delete.do")
	@ResponseBody
	public AjaxResult delete(@RequestParam Long id){
		subjectService.delete(id);
		return AjaxResult.successInstance("ɾ���ɹ���");
	}
	
	@RequestMapping("update.do")
	public String update(Long id,Model model){
		Subject subject = subjectService.selectOne(id);
		model.addAttribute("subject", subject);
		return "subject/update";
	}
	
	@RequestMapping("update")
	@ResponseBody
	public AjaxResult updateSubmit(Subject subject){
		subjectService.update(subject);
		return AjaxResult.successInstance("�޸ĳɹ���");
	}
}
