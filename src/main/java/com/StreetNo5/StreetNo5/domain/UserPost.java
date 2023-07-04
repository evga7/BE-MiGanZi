package com.StreetNo5.StreetNo5.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
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
    @Size(min = 2, max = 250,message = "내용은 2~500자 사이로 입력해주세요.")
    private String content;
    private String imageUrl;
    private Double lat;
    private Double lng;
    private String address_name;
    private String tags;
    private Long tagsNum;
    private String music_id;



    @Builder
    public UserPost(String nickname, String content,String imageUrl,Double lat,Double lng,String address_name
            ,String tags,String music_id,Long tags_num) {
        this.nickname=nickname;
        this.content=content;
        this.imageUrl=imageUrl;
        this.lat=lat;
        this.lng=lng;
        this.tags=tags;
        this.tagsNum =tags_num;
        this.address_name=address_name;
        this.music_id=music_id;

    }
    public void addComment(UserComment userComment)
    {
        userComments.add(userComment);
        userComment.setUserPost(this);
    }

    @OneToMany(mappedBy = "userPost",fetch = FetchType.LAZY)
    @OrderBy("comment_id desc")
    @JsonManagedReference
    private List<UserComment> userComments=new ArrayList<>();




}
