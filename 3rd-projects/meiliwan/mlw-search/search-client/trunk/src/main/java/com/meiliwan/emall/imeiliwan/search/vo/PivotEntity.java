package com.meiliwan.emall.imeiliwan.search.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author lgn-mop
 * 二三级类目树
 * Date: 13-6-7
 * Time: 上午9:19
 */
public class PivotEntity implements Serializable {


    private static final long serialVersionUID = -2280991707756365050L;

    public String name;//类目名
    public int count; //数量
    public Object id;  // 类目id
    public List<PivotEntity> tail;   //子类目 集合

    public PivotEntity() {
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<PivotEntity> getTail() {
        return tail;
    }

    public void setTail(List<PivotEntity> tail) {
        this.tail = tail;
    }
    
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("{\"id\":").append(this.id).append(",\"counts\":").append(count)
        .append(",\"name\":\"").append(name).append("\",\"children\":[ ");
        if (this.tail != null){ 
        	for (PivotEntity pe : this.tail) {
        		builder.append(pe.toString()).append(",");
        	}
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("]}");
        return builder.toString();
    }
    
}