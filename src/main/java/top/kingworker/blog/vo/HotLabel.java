package top.kingworker.blog.vo;

import java.io.Serializable;

public class HotLabel implements Serializable {
    private Integer id;
    private String labelName;
    private String labelColor;
    private Integer count;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(String labelColor) {
        this.labelColor = labelColor;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "HotLabel{" +
                "id=" + id +
                ", labelName='" + labelName + '\'' +
                ", labelColor='" + labelColor + '\'' +
                ", count=" + count +
                '}';
    }
}
