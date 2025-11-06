/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ucb.estudo.services;

import org.springframework.stereotype.Service;
import ucb.estudo.repositories.SessionRepository;
import ucb.estudo.entities.SessionDoc;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.bson.Document;
import java.time.Instant;

@Service
public class LogService {
    private final MongoTemplate mongoTemplate;

    public LogService(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    public void log(String userId, String action, String details){
        Document doc = new Document();
        doc.append("userId", userId);
        doc.append("action", action);
        doc.append("details", details);
        doc.append("timestamp", Instant.now().toString());
        mongoTemplate.getCollection("app_logs").insertOne(doc);
    }
}
