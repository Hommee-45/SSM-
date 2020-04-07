package com.rupeng.mapper;

import java.util.List;

public interface IMapper<T> {

    public int insert(T pojo);

    public int update(T pojo); //����id����

    public int delete(Long id); //����idɾ��

    public List<T> select(T pojo); //�Էǿ��ֶ�ֵ��Ϊ��ѯ�������в�ѯ
}
