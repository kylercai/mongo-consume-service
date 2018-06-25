package com.msl.common.utilities.mongo;

import com.google.gson.Gson;
import com.msl.common.utilities.message.MessageSender;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class PsDataMongoByRedis {

    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private MessageSender messageSender;

    private String topic = "cosmos_topic";
    private Integer pageSize = 20;
    private Integer stepCount = 20;

    private Gson gson = new Gson();

    private <T> List<List<T>> splitList(List<T> sourceList) {
        List<List<T>> targetLists = new ArrayList<>();
        List<T> targetList = new ArrayList();
        targetLists.add(targetList);
        int progress = 0;
        int step = 0;
        while (progress < sourceList.size()) {
            if (step == stepCount) {
                targetList = new ArrayList();
                targetLists.add(targetList);
                step = 0;
            }
            targetList.add(sourceList.get(progress));
            step++;
            progress++;
        }
        return targetLists;
    }

    private <T> void send(String key, List<T> sourceList) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return;
        }
        Class clazz = sourceList.get(0).getClass();
        List<List<T>> targetLists = splitList(sourceList);
        for (List targetList : targetLists) {
            List<T> list = targetList;
            Message message = new Message();
            message.setKey(key);
            message.setValue(list);
            message.setClassName(clazz.getSimpleName());
            messageSender.send(topic, gson.toJson(message));
        }
    }

    private <T> List<List<T>> stepQuery(Query query, Class<T> clazz) {
        List<List<T>> lists = new ArrayList<>();
        List<T> list = null;
        Integer skip = 0;
        do {
            query.skip(skip);
            query.limit(stepCount);
            list = mongoTemplate.find(query, clazz);
            if (!CollectionUtils.isEmpty(list)) {
                lists.add(list);
            }
            skip += stepCount;
        } while (!CollectionUtils.isEmpty(list));
        return lists;
    }

    public <T> List<T> findByPage(Query query, Integer pageNum, Class<T> clazz) {
        query.skip((pageNum - 1) * pageSize);
        query.limit(pageSize);
        List<T> list = mongoTemplate.find(query, clazz);
        return list;
    }

    public <T> void insert(T t) {
        List<T> list = new ArrayList<>();
        list.add(t);
        send("save", list);
    }

    public <T> void insert(List<T> list) {
        send("save", list);
    }

    public <T> void update(T t) {
        List<T> list = new ArrayList<>();
        list.add(t);
        send("update", list);
    }

    public <T> void update(List<T> lists) {
        for (List<T> list : splitList(lists)) {
            send("update", list);
        }
    }


    public <T> void remove(Query query, Class<T> clazz) {
        List<List<T>> lists = stepQuery(query, clazz);
        for (List<T> list : lists) {
            send("remove", list);
        }
    }
}
