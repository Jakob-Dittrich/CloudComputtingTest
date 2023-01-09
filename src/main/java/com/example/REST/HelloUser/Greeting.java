package com.example.REST.HelloUser;


import com.google.cloud.*;

public class Greeting {
	private long id;
	private String name;
    private Timestamp timestamp;

    public Greeting(long _id, String _name,Timestamp _timestamp){
        this.id = _id;
        this.name = "Hello, " + _name + "!";
        this.timestamp = _timestamp;
    }

    public Greeting(long _id, String _name){
        this(_id,_name,Timestamp.now());
    }

    public long getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
    
}
