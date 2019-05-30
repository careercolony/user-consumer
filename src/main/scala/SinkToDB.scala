import config.Application._
import model.ExperienceDto
import mongo.Neo4jConnector.updateNeo4j
import scala.concurrent._
import scala.concurrent.duration._

import java.io._



object SinkToDB {

    def sendToNeo4j(message: ExperienceDto) = {
        //println("parsed message from topic:" + message)
        
 
        //Create a profile node and [r:HAS_PROFILE] relationship 
        val mid = message.memberID 
        val positionVal: String = message.position match {
            case None => ""
            case Some(str) => str
        }
        println(positionVal)

        val descriptionVal: String = message.description match {
            case None => ""
            case Some(str) => str
        }

        val employerVal: String = message.employer match {
            case None => ""
            case Some(str) => str
        }
        val industryVal: String = message.industry match {
            case None => ""
            case Some(str) => str
        }
        val start_monthVal: String = message.start_month match {
            case None => ""
            case Some(str) => str
        }
        val start_yearVal: String = message.start_year match {
            case None => ""
            case Some(str) => str
        }

        
        val script = s"MATCH (u:users) WHERE u.memberID = '${mid}'  CREATE (a:Profile {position:'${positionVal}',description:'${descriptionVal}'}), (c:Company {employername:'${employerVal}', industry:'${industryVal}'}), (u)-[:HAS_PROFILE {conn_type:'profile'}]->(a), (u)-[:WORKS_AT {conn_type:'work', start_month:'${start_monthVal}', start_year:'${start_yearVal}'}]->(c)"
        val response = updateNeo4j(script)


        val result = Await.result(response, Duration.Inf)
        println(s"Result: $result")
    }
}


 

