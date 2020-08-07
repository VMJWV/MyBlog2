package top.kingworker.blog.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;
import top.kingworker.blog.entity.Profile;

@Repository
public interface ProfileMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Profile record);

    Profile selectByPrimaryKey(Integer id);

    List<Profile> selectAll();

    int updateByPrimaryKey(Profile record);

}