package com.rupeng.util;

public class AjaxResult {
	private String status; //��ʾ��Ӧ״̬���涨"success"��ʾ�ɹ���"error"��ʾʧ��
    private Object data; //��ʾ��Ӧ��Ϣ���ȿ�������ʾ��Ϣ��Ҳ�����ǿͻ�����Ҫ������
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	public AjaxResult() {

    }

	public AjaxResult(String status){
		this.status=status;
	}
    public AjaxResult(String status, Object data) {
        this.status = status;
        this.data = data;
    }

    public static AjaxResult errorInstance(Object data) {
        return new AjaxResult("error", data);
    }

    public static AjaxResult successInstance(Object data) {
        return new AjaxResult("success", data);
    }
}
