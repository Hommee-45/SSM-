package com.rupeng.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rupeng.mapper.ClassesUsersMapper;
import com.rupeng.mapper.SettingsMapper;
import com.rupeng.pojo.Card;
import com.rupeng.pojo.Settings;
import com.rupeng.pojo.User;
import com.rupeng.pojo.UserCard;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;

@Service
public class UserCardsService extends ManyToManyBaseService<User, UserCard, Card> {
	@Autowired
	private ClassesUsersMapper classesUsersMapper;

	@Autowired
	private SettingService settingService;
	public AjaxResult activateFirstCard(Long classesId, Long cardId) {

		// �ð༶�µ����г�Ա
		List<User> userList = classesUsersMapper.selectSecondListByFirstId(classesId);
		// �п���û�г�Ա
		if (CommonUtils.isEmpty(userList)) {
			return AjaxResult.errorInstance("�ð༶�»�û�г�Ա��");
		}

		// �����ж��Ƿ�����ʦ
		for (User user : userList) {
			if (user.getIsTeacher()) {
				continue;// ����ִ����һ��ѭ��
			}
			// �ж�ѧ���Ƿ��Ѿ�ӵ�е�һ��ѧϰ��
			UserCard uc = new UserCard();
			uc.setUserId(user.getId());
			uc.setCardId(cardId);
			if (isExisted(uc)) {
				continue;//����һ������ѧϰ��
			}
			
			Settings setting = new Settings();
	        setting.setName("card_valid_days");
	        setting = settingService.selectOne(setting);

	        int validDays = Integer.parseInt(setting.getValue());
			uc.setCreateTime(new Date());

            Date endTime = new Date(uc.getCreateTime().getTime() + validDays * 1000 * 60 * 60 * 24);
            uc.setEndTime(endTime);

            insert(uc);
		}
		
		return AjaxResult.successInstance("ѧϰ���Ѿ�����");
	}

}
