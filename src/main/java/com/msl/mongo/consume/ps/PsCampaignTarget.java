package com.msl.mongo.consume.ps;

import com.msl.mongo.consume.be.Note;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface PsCampaignTarget extends MongoRepository<Note, String> {
}
