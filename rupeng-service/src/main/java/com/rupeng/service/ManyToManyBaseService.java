package com.rupeng.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.rupeng.mapper.IManyToManyMapper;

public class ManyToManyBaseService<F,T,S> extends BaseService<T>{

	@Autowired
	private IManyToManyMapper<F, T, S> manyToManyMapper;
	
	@SuppressWarnings("all")
	public void updateFirst(Long firstId, Long[] secondIds){
		ParameterizedType Superclass = (ParameterizedType) getClass().getGenericSuperclass();
		Type[] actualTypeArguments = Superclass.getActualTypeArguments();
		Class fclass = (Class) actualTypeArguments[0];
		Class tclass = (Class) actualTypeArguments[1];
		Class sclass = (Class) actualTypeArguments[2];
		
		//����cardsubject 
		//1����ѯ��cardid��ѧϰ������������ѧ��
		try {
			T t = (T) tclass.newInstance();
			//��ȡ������T��set(First)Id����
			tclass.getDeclaredMethod("set"+fclass.getSimpleName()+"Id", Long.class).invoke(t, firstId);
			List<T> oldList = selectList(t);
			
			//Ϊ�˷��� ��long[] secondIds����ת����list
			List<Long> secondList=new ArrayList<>();
			for(Long id:secondIds){
				if(id!=null){
					secondList.add(id);
				}
			}
			
			//2���������  �û���ѡ��java ��.net
			for(T c:oldList){
				//�õ�c�����secondId
				Long sId=(Long) tclass.getDeclaredMethod("get"+sclass.getSimpleName()+"Id").invoke(c);
				if(secondList.contains(sId)){
					
				}else{
					delete((Long) tclass.getDeclaredMethod("getId").invoke(c));
				}
				secondList.remove(sId);
			}
			
			//�����û��¹�ѡ��ѧ��id��ӽ����ݿ�
			for(Long id2:secondIds){
				tclass.getDeclaredMethod("set"+sclass.getSimpleName()+"Id", Long.class).invoke(t, id2);
				tclass.getDeclaredMethod("set"+fclass.getSimpleName()+"Id", Long.class).invoke(t, firstId);
				insert(t);
			}
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (IllegalArgumentException e) {
		} catch (InvocationTargetException e) {
		} catch (NoSuchMethodException e) {
		} catch (SecurityException e) {
		}
	} 
	
	@SuppressWarnings("all")
	public void updateSecond(Long secondId, Long[] firstIds){
		
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		Type[] actualTypeArguments = genericSuperclass.getActualTypeArguments();
		Class fclass = (Class) actualTypeArguments[0];
		Class tclass = (Class) actualTypeArguments[1];
		Class sclass = (Class) actualTypeArguments[2];
		
		
		try {
			//1����ѯ���ݿ�
			T t = (T) tclass.newInstance();
			tclass.getDeclaredMethod("set"+sclass.getSimpleName()+"Id", Long.class).invoke(t, secondId);
			List<T> oldList = selectList(t);
			
			//�������е�����ת�Ƶ�list�У���������Ĳ����������Ұ���Ч���ݣ�firstId==null���޳�
			List<Long> firstList=new ArrayList<>();
			for(Long fid:firstIds){
				if(fid!=null){
					firstList.add(fid);
				}
			}
			
 			// 2��ɾ��ԭ�������ݿ��е����ڲ���firstIdList�е�����
			for(T ot:oldList){
				Long otId = (Long) tclass.getDeclaredMethod("get"+fclass.getSimpleName()+"Id").invoke(ot);
				if(firstList.contains(otId)){
					
				}else{
					delete((Long) tclass.getDeclaredMethod("getId").invoke(ot));
				}
				firstList.remove(otId);
			}
			
			//3�� ��firstIdList��ʣ�����ӵ����ݿ�
			for(Long fIdList:firstList){
				tclass.getDeclaredMethod("set"+fclass.getSimpleName()+"Id",Long.class).invoke(t, firstIds);
				tclass.getDeclaredMethod("set"+sclass.getSimpleName()+"Id",Long.class).invoke(t, secondId);
				insert(t);
			}
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (IllegalArgumentException e) {
		} catch (InvocationTargetException e) {
		} catch (NoSuchMethodException e) {
		} catch (SecurityException e) {
		}
		
	}
	
	public int deleteByFirstId(Long firstId){
		if(firstId==null){
			throw new RuntimeException("firstId����Ϊnull");
		}
		return manyToManyMapper.deleteByFirstId(firstId);
	}
	
	public int deleteBySecondId(Long secondId){
		if(secondId==null){
			throw new RuntimeException("secondId����Ϊnull");
		}
		return manyToManyMapper.deleteBySecondId(secondId);
	}
	
	public List<F> selectFirstListBySecondId(Long secondId){
		List<F> list = manyToManyMapper.selectFirstListBySecondId(secondId);
		
		if(list.size()==0||list==null){
			return null;
		}
		return list;
	}
	
	public F selectFirstOneBySecondId(Long secondId){
		List<F> list = selectFirstListBySecondId(secondId);
		if(list==null||list.size()==0){
			return null;
		}
		return list.get(0);
	}
	public List<S> selectSecondListByFirstId(Long firstId){
		 List<S> list = manyToManyMapper.selectSecondListByFirstId(firstId);
		 if(list==null||list.size()==0){
			 return null;
		 }
		 return list;
	}
	
	public S selectSecondOneByFirstId(Long firstId){
		List<S> list = selectSecondListByFirstId(firstId);
		if(list==null||list.size()==0){
			return null;
		}
		return list.get(0);
	}
}
