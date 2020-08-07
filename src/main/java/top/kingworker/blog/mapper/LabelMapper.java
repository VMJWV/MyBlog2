package top.kingworker.blog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.kingworker.blog.entity.Label;

@Repository
public interface LabelMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Label record);

    Label selectByPrimaryKey(Integer id);

    List<Label> selectAll();

    int updateByPrimaryKey(Label record);
    //这是一开始手动写的 后面偷懒用了逆向工程
    public List<Label> getAllLabelsByUserId(int UserId);

    public int updateLabel(@Param("label") Label label);

    public int deleteLabel(@Param("id") Integer id,@Param("userId") int userId);

    public int addLabel(Label label);
}