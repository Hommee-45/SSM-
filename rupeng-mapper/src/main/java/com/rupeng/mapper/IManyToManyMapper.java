package com.rupeng.mapper;

import java.util.List;


public interface IManyToManyMapper<F,T,S> extends IMapper<T> {

	/**
	 * Ϊ�˸������˽ӿڣ�ʹ��T_CardSubjects�м���Ӧ��CardSubject����˵��
	 * CardSubject��ʾĳ��ѧϰ����������Щѧ�ƣ�����ĳ��ѧ������Щѧϰ����
	 * @param <T> ��ʾ�м���Ӧ���࣬��CardSubject��
	 * @param <F> ��first����ʾCardSubject��ǰ�벿�ֶ�Ӧ���࣬��Card��
	 * @param <S> ��second����ʾCardSubject�ĺ�벿�ֶ�Ӧ���࣬��Subject��
	 */
	 //��ɾ��ָ��ѧϰ��������ѧ�ƵĹ�ϵ
    int deleteByFirstId(Long firstId);

    //��ɾ��ָ��ѧ�ƺ�����ѧϰ���Ĺ�ϵ
    int deleteBySecondId(Long secondId);

    //���ѯ��ָ��ѧ��ӵ�е�����ѧϰ��
    List<F> selectFirstListBySecondId(Long secondId);

    //���ѯ��ָ��ѧϰ�������ڵ�����ѧ��
    List<S> selectSecondListByFirstId(Long firstId);
}
