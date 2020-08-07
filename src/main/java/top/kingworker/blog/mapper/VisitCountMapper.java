package top.kingworker.blog.mapper;

import java.util.List;

import org.springframework.stereotype.Component;
import top.kingworker.blog.entity.VisitCount;

@Component
public interface VisitCountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(VisitCount record);

    VisitCount selectByPrimaryKey(Integer id);

    List<VisitCount> selectAll();

    int updateByPrimaryKey(VisitCount record);

    VisitCount selectByDayString(String dayString);

    void visitCountAddOne(String dayString);

    Integer getSumVisitCount();
}