package top.kingworker.blog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.kingworker.blog.entity.VisitCount;
import top.kingworker.blog.entity.VisitHistory;
import top.kingworker.blog.mapper.VisitCountMapper;
import top.kingworker.blog.mapper.VisitHistoryMapper;
import top.kingworker.blog.service.VisitRecordService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class VisitRecordServiceImpl implements VisitRecordService {

    @Autowired
    VisitHistoryMapper visitHistoryMapper;

    @Autowired
    VisitCountMapper visitCountMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public void insertVisitHistory(String ip, String url, String methodName, Date currentDate) {
        VisitHistory visitHistory = new VisitHistory();
        visitHistory.setIp(ip);
        visitHistory.setClassmethod(methodName);
        visitHistory.setRequestUrl(url);
        visitHistory.setVisitTime(currentDate);
        visitHistoryMapper.insert(visitHistory);
    }

    @Override
    public void insertNewDayVisitCount(String dayString) {
        VisitCount visitCount = new VisitCount();
        visitCount.setToday(dayString);
        visitCount.setCount(0);
        visitCountMapper.insert(visitCount);
    }

    @Override
    @Caching(
            evict = {@CacheEvict(value = "todayVisitCount",allEntries = true),
                    @CacheEvict(value = "sumVisitCount",allEntries = true)}
    )
    public void addToDayVisitCount(String dayStr) {
        VisitCount visitCount = visitCountMapper.selectByDayString(dayStr);
        if(visitCount == null){
            insertNewDayVisitCount(dayStr);
        }
        visitCountMapper.visitCountAddOne(dayStr);
    }

    @Override
    public void deleteVisitHistory(Integer historyId) {
        visitHistoryMapper.deleteByPrimaryKey(historyId);
    }

    @Override
    public List<VisitHistory> getAllVisitHistory() {
        return visitHistoryMapper.selectAll();
    }

    @Override
    @Cacheable(value = "todayVisitCount")
    public Integer toDayVisitCount() {
        String toDayStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        VisitCount visitCount = visitCountMapper.selectByDayString(toDayStr);
        if(visitCount != null)
            return visitCount.getCount();
        return 1;
    }

    @Override
    @Cacheable(value="sumVisitCount")
    public Integer sumVisitCount() {
        return visitCountMapper.getSumVisitCount();
    }
}
