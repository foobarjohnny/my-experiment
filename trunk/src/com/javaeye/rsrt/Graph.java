package com.javaeye.rsrt;  
  
import java.util.Arrays;  
import java.util.List;  
import java.util.Vector;  
  
/** 
 *  
 * @author nishiting 
 *  
 */  
  
public class Graph {  
  
    int vertexNum;  
    Vector[] vector;  
    Stack stack;  
    int[] result;  
    int[] in;// ���  
  
    /** 
     *  
     * ����һ��ͼ 
     *  
     * @param num 
     *            ͼ�Ķ����� 
     *  
     */  
    public Graph(int num) {  
  
        vertexNum = num;  
        vector = new Vector[vertexNum];  
        stack = new Stack(num);  
        result = new int[vertexNum];  
        in = new int[vertexNum];  
  
    }  
  
    /** 
     * ��ͼ���������� 
     *  
     * @param I 
     *            �ߵ�һ������ 
     * @param J 
     *            �ߵ���һ������ 
     * @return �Ƿ���ӳɹ� 
     */  
    public boolean addEdge(int I, int J) {  
  
        /** 
         * �ж��û�������Ƿ���һ�����㣬����ǣ��򷵻�flase,��Ӳ��ɹ� 
         */  
        if (J == I) {  
            return false;  
        }  
  
        /** 
         * �ж�������Ķ���ֵ�Ƿ���ͼ�����㷶Χֵ�ڣ�������ڣ�����ʾ���㲻���� 
         *  
         */  
        if (I < vertexNum && J < vertexNum && I >= 0 && J >= 0) {  
  
            /** 
             *  
             * �жϱ��Ƿ���� 
             */  
  
            if (isEdgeExists(I, J)) {  
  
                return false;  
            }  
  
            /** 
             * ��ӱߣ�����ͷ����ȼ�1 
             */  
  
            vector[I].add(J);  
            in[J]++;  
            return true;  
        }  
        return false;  
    }  
  
    /** 
     * �ж�������Ƿ���� 
     *  
     * @param i 
     *            Ҫ��ѯ������ߵ�һ����β 
     * @param j 
     *            Ҫ��ѯ������ߵ���һ����ͷ 
     * @return ���Ƿ���ڣ�false:�����ڣ�true:���� 
     */  
  
    public boolean isEdgeExists(int i, int j) {  
  
        /** 
         * �ж�������Ķ���ֵ�Ƿ���ͼ�����㷶Χֵ�ڣ�������ڣ�����ʾ���㲻���� 
         *  
         */  
        if (i < vertexNum && j < vertexNum && i >= 0 && j >= 0) {  
  
            if (i == j) {  
                return false;  
            }  
  
            /** 
             * �ж�i���ڽӽ�㼯�Ƿ�Ϊ�� 
             */  
  
            if (vector[i] == null) {  
                vector[i] = new Vector(8);  
            }  
  
            /** 
             * �ж��������Ƿ���ڣ�������ڣ�����ʾ���Ѿ����� 
             */  
            for (int q = 0; q < vector[i].size(); q++) {  
  
                if (((Integer) vector[i].get(q)).intValue() == j) {  
                    System.out.println("����" + i + "��" + "����" + j + "������֮����ڱ�");  
                    return true;  
  
                }  
            }  
        }  
        return false;  
    }  
  
    public void TopSort() {  
  
        for (int i = 0; i < vertexNum; i++)  
            if (in[i] == 0)  
                stack.push(i);  
        int k = 0;  
        while (!stack.isEmpty()) {  
  
            result[k] = (Integer) stack.pop();  
  
            if (vector[result[k]] != null) {  
                for (int j = 0; j < vector[result[k]].size(); j++) {  
  
                    int temp = (Integer) vector[result[k]].get(j);  
                    if (--in[temp] == 0) {  
                        stack.push(temp);  
                    }  
  
                }  
  
            }  
            k++;  
  
        }  
  
        if (k < vertexNum) {  
            System.out.println("�л�·");  
            System.exit(0);  
  
        }  
  
    }  
  
    public int[] getResult() {  
        return result;  
  
    }  
  
}  