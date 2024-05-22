package com.metadev.connect.Entity;

public class UserPreferredTopic implements Comparable<UserPreferredTopic> {
    private Long userId;
    private String topic;
    private double weight;

    public UserPreferredTopic (Long userId, String topic, double weight){
        this.userId = userId;
        this.topic = topic;
        this.weight = weight;
    }

    public Long getUserId(){
        return userId;
    }

    public String getTopic(){
        return topic;
    }

    public double getWeight(){
        return weight;
    }

    @Override
    public int compareTo(UserPreferredTopic o) {
        return Double.compare(this.weight, o.weight);
    }

    @Override
    public String toString(){
        return String.format("topic: (%s) weight: (%.2f)",getTopic(),getWeight());
    }
}
