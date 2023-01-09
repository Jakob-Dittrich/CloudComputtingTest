package com.example.REST.HelloUser;

import java.util.LinkedList;

import org.springframework.web.bind.annotation.*;
import com.google.cloud.datastore.*;
import com.google.cloud.Timestamp;

@RestController
public class GreetingControler {
 
    private int respCount;
    private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    private final KeyFactory keyFactory = datastore.newKeyFactory().setKind("greeting");

    @GetMapping("/addSimpleGreetings")
    public void SimpleGreeting() {
        for (int i = 0; i <10; i++){
            Func("testuser"+i);
        }
    }

    @GetMapping("/greeting")
    public Greeting Func(@RequestParam(name="name",required = false)String n){
        String out_name = (n==null) ? "World" : n;
        Greeting greeting = new Greeting(respCount++, out_name);

        storeGreeting(greeting);

        return greeting;
    }

    public Key storeGreeting(Greeting g){
        Key key = datastore.allocateId(keyFactory.newKey());		
        
        Entity newTask = Entity.newBuilder(key)
        .set("name",g.getName())
        .set("date",g.getTimestamp())
        .build();
        datastore.put(newTask);

        return key;
    }

    @GetMapping("/list")
    public Greeting[] list() {
        Greeting[] greetings=listGreetings();
        return greetings;
    }

    public Greeting[] listGreetings() {
        Query<Entity> orderLimitQuery = Query
            .newGqlQueryBuilder(Query.ResultType.ENTITY ,"SELECT * FROM greeting ORDER BY date DESC LIMIT @lit")
            .setBinding("lit", 10)
            .build();

        QueryResults<Entity> queryResult =datastore.run(orderLimitQuery);

        LinkedList<Greeting> greetingList = new LinkedList<>();
        while(queryResult.hasNext()) {
            Entity ent=queryResult.next();
            Greeting g=new Greeting(ent.getKey().getId(),ent.getString("name"),ent.getTimestamp("date"));
            greetingList.add(g);
        }
        return greetingList.toArray(new Greeting[0]);
    }

    @GetMapping("/cleanup")
    public String cleanUp() {
        long x=deleteOldFromDatastore();
        String msg="Cleaned up "+x+" entries.";
        System.out.println(msg);
        return msg;
    }

    public long deleteOldFromDatastore() {
        Timestamp t=Timestamp.now();
        t=Timestamp.ofTimeSecondsAndNanos(t.getSeconds()-60*2,t.getNanos()); //2 minutes
        Query<Key> q= Query
            .newGqlQueryBuilder(Query.ResultType.KEY ,
                "SELECT __key__ FROM greeting WHERE date<@time")
            .setBinding("time",t)
            .build();
        QueryResults<Key> rs =datastore.run(q);
        long cnt=0;
        while(rs.hasNext()) {
            Key k=rs.next();
            datastore.delete(k);
            cnt++;
        }
        return cnt;
    }
    
}
