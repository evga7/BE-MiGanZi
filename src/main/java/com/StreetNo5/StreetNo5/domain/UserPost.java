package com.StreetNo5.StreetNo5.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class UserPost extends BaseTimeEntity{
    @Id
    @Column(name="post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickname;
    private Long viewCount=0L;
    private Long commentCount=0L;
    private String title;
    private String content;
    private String imageUrl;
    private String lat;
    private String lng;
    private String address_name;

    @Builder
    public UserPost(String nickname, String title, String content,String imageUrl) {
        this.nickname=nickname;
        this.title=title;
        this.content=content;
        this.imageUrl=imageUrl;
    }
    public void addComment(UserComment userComment)
    {
        userComments.add(userComment);
        userComment.setUserPost(this);
    }

    public void addTag(PostTag postTag)
    {
        postTags.add(postTag);
        postTag.setUserPost(this);
    }

    @OneToMany(mappedBy = "userPost",fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<UserComment> userComments=new ArrayList<>();

    @OneToMany(mappedBy = "userPost",fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<PostTag> postTags=new ArrayList<>();


}
