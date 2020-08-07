package top.kingworker.blog;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.kingworker.blog.entity.Article;
import top.kingworker.blog.entity.Label;
import top.kingworker.blog.mapper.ArticleLabelsMapper;
import top.kingworker.blog.mapper.LabelMapper;
import top.kingworker.blog.mapper.PermissionMapper;
import top.kingworker.blog.service.ArticleService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SpringBootTest
class BlogApplicationTests {

    @Test
    public void test(){

    }
}
