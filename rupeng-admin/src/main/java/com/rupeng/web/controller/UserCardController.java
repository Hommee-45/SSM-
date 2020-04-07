package com.rupeng.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rupeng.pojo.CardSubject;
import com.rupeng.pojo.Classes;
import com.rupeng.service.CardSubjectService;
import com.rupeng.service.ClassesService;
import com.rupeng.service.ClassesUsersService;
import com.rupeng.service.UserCardsService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;

@Controller
@RequestMapping("/page/userCard")
public class UserCardController {

	@Autowired
	private UserCardsService userCardsService;
	@Autowired
	private ClassesService classesService;
	@Autowired
	private CardSubjectService cardSubjectService;
	@RequestMapping("activateFirstCard.do")
	@ResponseBody
	public AjaxResult activateFirstCard(Long classesId) {
		 //����ָ����classesId��ѯ��������ѧ��
		Classes classes = classesService.selectOne(classesId);
		Long subjectId = classes.getSubjectId();

		if (subjectId == null) {
			return AjaxResult.errorInstance("�˰༶��û��ָ��ѧ��");
		}

		// ������ѯ�����ѧ�Ƶĵ�һ��ѧϰ��
		CardSubject cardSubject = new CardSubject();
		cardSubject.setSubjectId(subjectId);
		List<CardSubject> cardSubjectList = cardSubjectService.selectList(cardSubject, "seqNum asc");

		if (CommonUtils.isEmpty(cardSubjectList)) {
			return AjaxResult.errorInstance("�˰༶������ѧ�ƻ�û��ָ��ѧϰ��");
		}
		// ��ѧ�Ƶ�һ��ѧϰ����id
		Long cardId = cardSubjectList.get(0).getCardId();

		// ������༶��������е�ѧ������ѧϰ��
		AjaxResult result = userCardsService.activateFirstCard(classesId, cardId);
		return result;
	}
}
