package top.kingworker.blog.mapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.kingworker.blog.entity.UserPermission;

@Repository
public interface UserPermissionMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(UserPermission record);

    UserPermission selectByPrimaryKey(Integer id);

    List<UserPermission> selectAll();

    int updateByPrimaryKey(UserPermission record);

    Set<String> getPermissionsByUserId(int userId);

    void deletePermissions(Integer userId);

    int insertBatch(@Param("userId") Integer userId, @Param("permsId") List<Integer> permsId);

}