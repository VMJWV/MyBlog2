package top.kingworker.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.kingworker.blog.aspect.ClearAllCache;
import top.kingworker.blog.entity.Label;
import top.kingworker.blog.mapper.LabelMapper;
import top.kingworker.blog.service.LabelService;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class LabelServiceImpl implements LabelService {

    @Autowired
    LabelMapper labelMapper;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "userLabels",unless="#result == null")
    public PageInfo<Label> getAllLabelsByUserId(int pageNum, int pageSize,int userId) {
        PageHelper.startPage(pageNum, pageSize);
        List<Label> list = labelMapper.getAllLabelsByUserId(userId);
        PageInfo<Label> pageInfo = new PageInfo<Label>(list);
        return pageInfo;
    }

    @Override
    @Cacheable(value = "allLabel",unless="#result == null")
    public PageInfo<Label> getAllLabelsAdmin(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Label> list = labelMapper.selectAll();
        PageInfo<Label> pageInfo = new PageInfo<Label>(list);
        return pageInfo;
    }

    @Override
    @ClearAllCache
    public boolean updateLabel(Label label) {
        return labelMapper.updateLabel(label)>0;
    }

    @Override
    @ClearAllCache
    public boolean addLabel(Label label) {
        return labelMapper.addLabel(label) > 0;
    }

    @Override
    @ClearAllCache
    public void deleteLabel(Integer labelId,Integer userId) {
        labelMapper.deleteLabel(labelId,userId);
    }

    @Override
    @ClearAllCache
    public void deleteLabelAdmin(Integer labelId) {
        labelMapper.deleteByPrimaryKey(labelId);
    }

    @Override
    @Cacheable(value = "label",unless="#result == null")
    public Label selectLabelByLabelId(Integer labelId) {
        return labelMapper.selectByPrimaryKey(labelId);
    }
}
