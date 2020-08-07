package top.kingworker.blog.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;
import top.kingworker.blog.entity.Permission;

@Repository
public interface PermissionMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Permission record);

    Permission selectByPrimaryKey(Integer id);

    List<Permission> selectAll();

    int updateByPrimaryKey(Permission record);

}