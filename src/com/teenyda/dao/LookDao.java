package com.teenyda.dao;
/**
 * ��λ����Ϣ���޸�
 * @author Administrator
 *
 */
public interface LookDao {
	/**
	 * ����ʹ��״̬
	 */
	int updateState(int lookId,int state);
	/**
	 * ��ѯʹ��״̬
	 * 1-��λ 2-��ʹ�� 3-ά��
	 * @param lookId
	 * @return
	 */
	int isInUse(int lookId);
}
