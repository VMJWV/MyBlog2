package top.kingworker.blog.service;

import top.kingworker.blog.entity.VisitHistory;

import java.util.Date;
import java.util.List;

public interface VisitRecordService {
    void insertVisitHistory(String ip, String url, String methodName, Date currentDate);
    void insertNewDayVisitCount(String dayString);
    void addToDayVisitCount(String dayStr);
    void deleteVisitHistory(Integer historyId);
    List<VisitHistory> getAllVisitHistory();
    Integer toDayVisitCount();
    Integer sumVisitCount();
}
