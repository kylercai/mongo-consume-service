package com.msl.mongo.consume.message;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.msl.mongo.consume.be.CampaignTarget;
import com.msl.mongo.consume.be.Note;
import com.msl.mongo.consume.ps.PsCampaignTarget;
import com.msl.mongo.consume.ps.PsNote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Receive {

    private static final Logger log = LoggerFactory.getLogger(Receive.class);

    private Gson gson = new Gson();

    @Autowired
    private PsNote psNote;
    @Autowired
    private PsCampaignTarget psCampaignTarget;

    public void receive(String messageJson) {
        Map messageJsonMap = gson.fromJson(messageJson, Map.class);
        String key = (String) messageJsonMap.get("key");
        String className = (String) messageJsonMap.get("className");

        Map<String, Object> orm = getORM(className, messageJson);
        MongoRepository mongoRepository = (MongoRepository) orm.get("repository");
        Object list = orm.get("list");
        if ("save".equals(key) || "update".equals(key)) {
            mongoRepository.save(list);
        }
        if ("remove".equals(key)) {
            mongoRepository.delete(list);
        }
    }

    private Map<String, Object> getORM(String className, String messageJson) {
        Map<String, Object> map = new HashMap<>();
        if (className.equals(CampaignTarget.class.getSimpleName())) {
            map.put("repository", psCampaignTarget);
            Message messages = gson.fromJson(messageJson, new TypeToken<Message<List<CampaignTarget>>>() {
            }.getType());
            map.put("list", messages.getValue());
        }
        if (className.equals(Note.class.getSimpleName())) {
            map.put("repository", psNote);
            Message messages = gson.fromJson(messageJson, new TypeToken<Message<List<CampaignTarget>>>() {
            }.getType());
            map.put("list", messages.getValue());
        }
        return map;
    }

}
