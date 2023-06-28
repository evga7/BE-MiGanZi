package com.StreetNo5.StreetNo5.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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
    @Size(min = 2,max = 30,message = "제목을 2~30자 사이로 입력해주세요.")
    private String title;
    @Size(min = 2, max = 250,message = "내용은 2~500자 사이로 입력해주세요.")
    private String content;
    private String imageUrl;
    private Double lat;
    private Double lng;
    private String address_name;
    private String tags;

    @Builder
    public UserPost(String nickname, String title, String content,String imageUrl,Double lat,Double lng,String address_name,String tags) {
        this.nickname=nickname;
        this.title=title;
        this.content=content;
        this.imageUrl=imageUrl;
        this.lat=lat;
        this.lng=lng;
        this.tags=tags;
        this.address_name=address_name;

    }
    public void addComment(UserComment userComment)
    {
        userComments.add(userComment);
        userComment.setUserPost(this);
    }

    @OneToMany(mappedBy = "userPost",fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<UserComment> userComments=new ArrayList<>();



}
