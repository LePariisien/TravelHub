package com.example.TravelHub.Services;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Neo4jService {

    private final Driver neo4jDriver;

    public Neo4jService(Driver neo4jDriver) {
        this.neo4jDriver = neo4jDriver;
    }

    public List<String> findRelatedCities(String cityCode, int limit) {
        List<String> relatedCities = new ArrayList<>();
        String cypherQuery = "MATCH (c:City {code: $cityCode})-[:NEAR]->(n:City) RETURN n.code AS city ORDER BY n.weight DESC LIMIT $limit";

        try (Session session = neo4jDriver.session()) {
            Result result = session.run(cypherQuery,
                    org.neo4j.driver.Values.parameters("cityCode", cityCode, "limit", limit));

            while (result.hasNext()) {
                Record record = result.next();
                relatedCities.add(record.get("city").asString());
            }
        } catch (Exception e) {
            System.err.println("Error querying Neo4j for related cities: " + e.getMessage());
            e.printStackTrace();
        }

        return relatedCities;
    }
}
