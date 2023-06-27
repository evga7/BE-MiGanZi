package com.StreetNo5.StreetNo5.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
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
    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonBackReference
    private UserPost userPost;

}
