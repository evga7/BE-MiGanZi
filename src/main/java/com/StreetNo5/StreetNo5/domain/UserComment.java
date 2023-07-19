package com.StreetNo5.StreetNo5.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class UserComment extends BaseTimeEntity{

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;
    private String nickname;
    @NotEmpty(message = "댓글을 입력해주세요")
    @Size(min = 1,max = 60,message = "댓글은 1~60자 사이로 입력해주세요.")
    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonBackReference
    private UserPost userPost;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

}
