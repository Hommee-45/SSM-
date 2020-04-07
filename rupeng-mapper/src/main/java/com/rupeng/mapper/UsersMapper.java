package com.rupeng.mapper;

import java.util.List;
import java.util.Map;

import com.rupeng.pojo.User;

public interface UsersMapper extends IMapper<User>{
	
	//�ɸ���ע��ʱ�䡢���������䡢�ֻ��Ƚ���ģ����ѯ
    public List<User> search(Map<String, Object> params);

	public int count(User user);
   
}