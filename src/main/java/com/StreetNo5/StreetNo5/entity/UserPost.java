package com.StreetNo5.StreetNo5.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPost extends BaseTimeEntity{
    @Id
    @Column(name="post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ColumnDefault("0")
    private int viewCount;
    @Size(min = 2, max = 250,message = "내용은 2~500자 사이로 입력해주세요.")
    private String content;
    private String detailImageUrl;
    private String thumbnailImageUrl;
    private Double lat;
    private Double lng;
    private String address_name;
    private String tags;
    private Long tagsNum;
    private String profileImage;
    private String music_id;




    public void addComment(UserComment userComment)
    {
        userComments.add(userComment);
        userComment.setUserPost(this);
    }
    public void increase(){
        this.viewCount+=1;
    }

    @OneToMany(mappedBy = "userPost",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<UserComment> userComments=new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;


}
