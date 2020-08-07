package top.kingworker.blog.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;
import top.kingworker.blog.entity.QqToken;

@Repository
public interface QqTokenMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(QqToken record);

    QqToken selectByPrimaryKey(Integer id);

    List<QqToken> selectAll();

    int updateByPrimaryKey(QqToken record);

    QqToken selectByOpenId(String openId);

    int deleteByUserId(Integer userId);
}