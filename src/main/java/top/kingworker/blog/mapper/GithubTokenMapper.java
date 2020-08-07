package top.kingworker.blog.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;
import top.kingworker.blog.entity.GithubToken;

@Repository
public interface GithubTokenMapper {
    int deleteByUserId(Integer userId);

    int insert(GithubToken record);

    GithubToken selectByToken(String token);

    List<GithubToken> selectAll();

    int updateByPrimaryKey(GithubToken record);
}