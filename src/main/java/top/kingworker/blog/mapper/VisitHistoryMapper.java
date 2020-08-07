package top.kingworker.blog.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;
import top.kingworker.blog.entity.VisitHistory;

@Repository
public interface VisitHistoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(VisitHistory record);

    VisitHistory selectByPrimaryKey(Integer id);

    List<VisitHistory> selectAll();

    int updateByPrimaryKey(VisitHistory record);
}