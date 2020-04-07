package com.rupeng.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.rupeng.pojo.Card;
import com.rupeng.pojo.CardSubject;
import com.rupeng.pojo.Subject;

@Service
public class CardSubjectService extends ManyToManyBaseService<Card, CardSubject, Subject>{
	
	public void order(Long[] cardSubjectIds,Integer[] seqNums){
		for(int i=0;i<cardSubjectIds.length;i++){
			CardSubject cs=new CardSubject();
			cs.setId(cardSubjectIds[i]);
			CardSubject selectOne = selectOne(cs);
			selectOne.setSeqNum(seqNums[i]);
			update(selectOne);
		}
	}

	public List<Card> selectFirstListBySecondId(Long subjectId, String string) {
		PageHelper.orderBy(string);
		return	selectFirstListBySecondId(subjectId);
	}

/*	public void updateCard(Long cardId,Long[] secondIds){
		//1����ѯ��cardid��ѧϰ������������ѧ��
		CardSubject cardSubject=new CardSubject();
		cardSubject.setId(cardId);
		List<CardSubject> oldList = selectList(cardSubject);
		
		//Ϊ�˷��� ��long[] secondIds����ת����list
		List<Long> secondList=new ArrayList<>();
		for(Long ids:secondIds){
			if(ids!=null){
				secondList.add(ids);
			}
		}
		
		//2���������  �û���ѡ��java ��.net
		if(oldList!=null){
			for(CardSubject cardSub:oldList){
				if(secondList.contains(cardSub.getSubjectId())){
					//1������ԭ������ѧϰ������java ,�����û����ǰ�java�򹳣����ǾͲ���ɾ�����κβ���
				}else{
					//2��ԭ��������ѧϰ��������java, �����û�ȡ����java,�������ݿ��а�������¼ɾ��
					delete(cardSub.getId());
				}
				//3��������java,��������������Ҫɾ��sceondList������java�ľ�Id.���ݿ����Ѿ����java�ˣ�����û��Ҫ��ڶ��Ρ�
				secondList.remove(cardSub.getSubjectId());
			}
		}
		//�����û��¹�ѡ��ѧ��id��ӽ����ݿ�
		for(Long id:secondList){
			CardSubject cs=new CardSubject();
			cs.setSubjectId(id);
			cs.setCardId(cardId);
			insert(cs);
		}
	}*/
}
