/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ucb.estudo.dao;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.time.LocalDateTime;

public class AuditoriaMongo {
    private static final String HOST = "localhost";
    private static final int PORT = 27017;
    private static final String DATABASE = "logs_tarefas";

    public static void registrarAcao(String usuario, String acao, String detalhe) {
        try (MongoClient mongoClient = new MongoClient(HOST, PORT)) {
            MongoDatabase db = mongoClient.getDatabase(DATABASE);
            MongoCollection<Document> collection = db.getCollection("auditoria");

            Document log = new Document("usuario", usuario)
                    .append("acao", acao)
                    .append("detalhe", detalhe)
                    .append("data", LocalDateTime.now().toString());

            collection.insertOne(log);
            System.out.println("Log inserido no MongoDB!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
