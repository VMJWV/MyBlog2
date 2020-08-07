package top.kingworker.blog.entity;

import java.io.Serializable;
import java.util.Date;

public class VisitHistory implements Serializable {
    private Integer id;

    private String requestUrl;

    private String classmethod;

    private String ip;

    private Date visitTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl == null ? null : requestUrl.trim();
    }

    public String getClassmethod() {
        return classmethod;
    }

    public void setClassmethod(String classmethod) {
        this.classmethod = classmethod == null ? null : classmethod.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public Date getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(Date visitTime) {
        this.visitTime = visitTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", requestUrl=").append(requestUrl);
        sb.append(", classmethod=").append(classmethod);
        sb.append(", ip=").append(ip);
        sb.append(", visitTime=").append(visitTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}