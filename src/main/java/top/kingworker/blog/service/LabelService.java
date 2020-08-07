package top.kingworker.blog.service;

import com.github.pagehelper.PageInfo;
import top.kingworker.blog.entity.Label;

public interface LabelService {
    PageInfo<Label> getAllLabelsByUserId(int pageNum, int pageSize,int userId);
    PageInfo<Label> getAllLabelsAdmin(int pageNum, int pageSize);
    boolean updateLabel(Label label);
    boolean addLabel(Label label);
    void deleteLabel(Integer labelId,Integer userId);
    void deleteLabelAdmin(Integer labelId);
    Label selectLabelByLabelId(Integer labelId);
}
