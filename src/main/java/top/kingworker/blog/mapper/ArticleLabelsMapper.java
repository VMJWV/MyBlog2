package top.kingworker.blog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.kingworker.blog.entity.ArticleLabels;
import top.kingworker.blog.vo.HotLabel;

@Repository
public interface ArticleLabelsMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(ArticleLabels record);

    ArticleLabels selectByPrimaryKey(Integer id);

    List<ArticleLabels> selectAll();

    int updateByPrimaryKey(ArticleLabels record);

    int insertBatch(@Param("articleId") Integer articleId, @Param("labels") List<Integer> labels);

    int deleteLabels(Integer articleId);

    List<HotLabel> getHotLabels();
}