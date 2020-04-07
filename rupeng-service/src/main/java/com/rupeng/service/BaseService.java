package com.rupeng.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rupeng.mapper.IMapper;
/**
 * ϵͳ�쳣��ò�Ҫֱ���׳�runtimexception ��Ϊruntimeexception���Բ�ʹ��try...catch���д�������������쳣
 * ���쳣��JVM���д����ҳ����ж�ִ�С�Ϊ�˱�֤����������Ȼ����ִ�У��ڿ��������ʱ�����ʹ��try...catch���쳣���ƴ���
 * @author Administrator
 *
 * @param <T>
 */
public class BaseService<T>{

	//�����Զ����ݷ���T�ľ���ֵע���Ӧ��mapper������
	//���T��Subject����ôע���mapper����SubjectMapper�Ķ���
	@Autowired
	private IMapper<T> mapper;
	
	@SuppressWarnings("all")
	private T createInstanceAndSetId(Long id){
		if(id==null){
			throw new  RuntimeException("id����Ϊ�գ�");
		}
		//1�����������ķ��Ͷ��� BaseService<T> ,��Ϊ�˴���������T�����࣬����getclass()Ҳ�����ࡣ
		Type type = getClass().getGenericSuperclass();
		//2���������ʱT��ʵ����
		ParameterizedType t=(ParameterizedType)type;//ת���ɲ���������
		Type[] actualTypeArguments = t.getActualTypeArguments();//���紫������Tֻ��һ��subject,�����м����һ��
		Class tclass = (Class) actualTypeArguments[0];
		T instance=null;
		try {
			instance = (T) tclass.newInstance();//����subject��
			tclass.getDeclaredMethod("setId",Long.class ).invoke(instance, id);
		} catch (IllegalAccessException e) {
		} catch (IllegalArgumentException e) {
		} catch (InvocationTargetException e) {
		} catch (NoSuchMethodException e) {
		} catch (SecurityException e) {
		} catch (InstantiationException e) {
		}
		
		return instance;
	}
	
	public int insert(T t){
		return mapper.insert(t);
	}
	
	public int update(T t){
		return mapper.update(t);
	}
	
	public int delete(Long id){
		return mapper.delete(id);
	}
	
	public T selectOne(T t){
		 List<T> list = mapper.select(t);
		 if(list.size()==0||list.size()>1){
			 throw new RuntimeException("������ݳ���һ�����ϣ�");
		 }
		 return list.get(0);
		 
	}
	
	public List<T> selectList(){
		return mapper.select(null);
	}
	
	public List<T> selectList(T t){
		return mapper.select(t);
	}
	
	/**
	 * ���������ѯ
	 * @return
	 */
	public List<T> selectList(T t, String orderBy){
		PageHelper.orderBy(orderBy);
		return mapper.select(t);
	} 
	
	/**
	 * ����id���в�ѯ
	 * @param id
	 * @return
	 */
	public T selectOne(Long id){
		T t = createInstanceAndSetId(id);
		return selectOne(t);
	}
	
	    public PageInfo<T> page(int pageNum, int pageSize, T pojo) {
	        PageHelper.startPage(pageNum, pageSize);//ע��pageNum��ʾҳ�룬��1��ʼ
	        List<T> list = mapper.select(pojo);//����ִ���Լ���Mapper�Ĳ�ѯ����
	        return new PageInfo<T>(list);
	    }

	    public PageInfo<T> page(int pageNum, int pageSize, T pojo, String orderBy) {
	        PageHelper.startPage(pageNum, pageSize);//ע��pageNum��ʾҳ�룬��1��ʼ
	        PageHelper.orderBy(orderBy);
	        List<T> list = mapper.select(pojo);//����ִ���Լ���Mapper�Ĳ�ѯ����
	        return new PageInfo<T>(list);
	    }
	
	/**
	 *  �ж��Ƿ����
	 * @param pojo
	 * @return
	 */
	public boolean isExisted(T pojo){
		List<T> selectOne = selectList(pojo);
		if(selectOne.size()>=1){
			return false;
		}
		return true;
	}
}
