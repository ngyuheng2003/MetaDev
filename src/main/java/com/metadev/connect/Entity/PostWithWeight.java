package com.metadev.connect.Entity;

public class PostWithWeight implements Comparable<PostWithWeight>{
    private Post post;
    private double weight;

    public PostWithWeight(Post post){
        this.post = post;
        this.weight = 0;
    }

    public PostWithWeight(Post post,double weight){
        this.post = post;
        this.weight = weight;
    }

    public double getWeight(){
        return this.weight;
    }

    public Post getPost(){
        return this.post;
    }

    @Override
    public int compareTo(PostWithWeight o) {
        return Double.compare(o.getWeight(),this.weight);
    }
}
