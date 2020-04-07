package com.rupeng.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rupeng.pojo.Chapter;
import com.rupeng.service.ChaptersSevice;
import com.rupeng.util.AjaxResult;

@Controller
@RequestMapping("/page/chapter")
public class ChapterController {

	@Autowired
	private ChaptersSevice chaptersSevice;
	@RequestMapping(value="/list.do" ,method=RequestMethod.GET)
	public String listdo(@RequestParam(value="id") Long cardId,Model model){
		Chapter chapter=new Chapter();
		chapter.setCardId(cardId);
		List<Chapter> selectOne = chaptersSevice.selectList(chapter);
		model.addAttribute("chapterList", selectOne);
		model.addAttribute("cardId", cardId);
		return "chapter/list";
	}
	
	@RequestMapping(value="/add.do{cardId}" ,method=RequestMethod.GET)
	public String adddo(@PathVariable("cardId") Long id,Model model){
		model.addAttribute("cardId", id);
		return "chapter/add";
	}
	
	@RequestMapping(value="/add.do" ,method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult add(Chapter chapter){
		//Ψһ�Ժͷǿ�У��ʡ��
		chaptersSevice.insert(chapter);
		return AjaxResult.successInstance("��ӳɹ���");
	}
	
	@RequestMapping(value="/delete.do" ,method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult add(@RequestParam("id") Long chapterId){
		//�ǿ�У��ʡ��
		chaptersSevice.delete(chapterId);
		return AjaxResult.successInstance("ɾ���ɹ���");
	}
	
	@RequestMapping(value="/update.do" ,method=RequestMethod.GET)
	public String updatedo(@RequestParam("id") Long chapterId,Model model){
		Chapter selectOne = chaptersSevice.selectOne(chapterId);
		model.addAttribute("chapter", selectOne);
		return "chapter/update";
	}
	
	
	@RequestMapping(value="/update.do" ,method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult update(Chapter chapter){
		chaptersSevice.update(chapter);
		return AjaxResult.successInstance("�޸ĳɹ���");
	}
}
