package co.yiiu.hitbbs.model.vo;

import co.yiiu.hitbbs.model.Comment;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by NPEteam.
 * Cohitright (c) 2023, All Rights Reserved.
 *  
 */
public class CommentsByTopic extends Comment implements Serializable {
    private static final long serialVersionUID = 8082073760910701836L;
    // 话题下面的评论列表单个对象的数据结构

    private String username;
    private String avatar;
    // 评论的层级，直接评论话题的，layer即为0，如果回复了评论的，则当前回复的layer为评论对象的layer+1
    private Integer layer;

    private LinkedHashMap<Integer, List<CommentsByTopic>> children;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getLayer() {
        return layer;
    }

    public void setLayer(Integer layer) {
        this.layer = layer;
    }

    public LinkedHashMap<Integer, List<CommentsByTopic>> getChildren() {
        return children;
    }

    public void setChildren(LinkedHashMap<Integer, List<CommentsByTopic>> children) {
        this.children = children;
    }
}
