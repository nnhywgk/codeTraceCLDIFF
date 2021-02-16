/**
 * @description:
 * @author: fancying
 * @create: 2019-05-25 11:04
 **/
package main.GraphBuild;

import org.neo4j.driver.v1.*;

import static org.neo4j.driver.v1.Values.parameters;
import java.util.List;
import java.util.Map;

public class GraphBuilder implements AutoCloseable{

    private final Driver driver;

    public GraphBuilder(String uri, String user, String password) {
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
    }

    public void executeBatchCql(List<String> cqlList) {
        //Driver driver = getNeo4jDriver();
        Session session = driver.session();
        for (String cql : cqlList) {
            try {
//                System.out.println(cql);
                session.run(cql);
            }catch (Exception e) {
                System.out.println("start");
                System.out.println(cql);
                System.out.println("end");
                e.printStackTrace();
            }

        }
        session.close();
       // driver.close();
    }

    public void executeBatchCqlStat(Map<String,String> cqlListPara) {
        ///Driver driver = getNeo4jDriver();
        Session session = driver.session();
        for (Map.Entry<String, String > cql : cqlListPara.entrySet()) {
            try {
                session.run(cql.getKey(),parameters("body", cql.getValue()));
                //session.run("",parameters(cqlListPara));
            }catch (Exception e) {
                System.out.println("START");
                System.out.println(cql);
                System.out.println("END");
                e.printStackTrace();
            }
        }
        session.close();
       // driver.close();


/*        try ( Session session = driver.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    StatementResult result = tx.run( "CREATE (a:Greeting) " +
                                    "SET a.message = $message " +
                                    "RETURN a.message + ', from node ' + id(a)",
                            parameters( "message", message ) );
                    return result.single().get( 0 ).asString();
                }
            } );
            System.out.println( greeting );
        }*/
    }


    //判断package 是否存在

/*    private Driver getNeo4jDriver() {
        return GraphDatabase.driver( "bolt://10.141.221.84:7687", AuthTokens.basic( "neo4j", "1" ) );
    }*/

    @Override
    public void close() throws Exception {
        driver.close();
    }
}