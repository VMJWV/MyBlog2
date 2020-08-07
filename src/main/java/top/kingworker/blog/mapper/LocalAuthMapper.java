package top.kingworker.blog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import top.kingworker.blog.entity.LocalAuth;

@Repository
public interface LocalAuthMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(LocalAuth record);

    LocalAuth selectByPrimaryKey(Integer id);

    List<LocalAuth> selectAll();

    int updateByUserId(LocalAuth record);

    LocalAuth findByEmail(String email);

    LocalAuth findByUsername(String username);

    Integer getUserIdByAccount(@Param("email") String email, @Param("username") String username);

    LocalAuth findByUserId(Integer userId);
}