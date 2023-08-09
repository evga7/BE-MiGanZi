package com.StreetNo5.StreetNo5.domain;


import com.StreetNo5.StreetNo5.domain.eums.Role;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.common.aliasing.qual.Unique;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
public class User extends BaseTimeEntity{

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Unique
    @NotNull
    @Column(unique = true)
    private String nickname;
    @NotNull
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<UserPost> userPosts=new ArrayList<>();

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<UserComment> userComments=new ArrayList<>();

    public void addPost(UserPost userPost)
    {
        userPosts.add(userPost);
        userPost.setUser(this);
    }

    public void addComment(UserComment userComment)
    {
        userComments.add(userComment);
        userComment.setUser(this);
    }

    @Builder
    public User(Long id, String nickname, String password, Role role) {
        this.id = id;
        this.nickname = nickname;
        this.password=password;
        this.role = role;
    }

    public void nicknameUpdate(String nickname) {
        this.nickname = nickname;
    }
    public void passwordUpdate(String newPassword) {
        this.password = newPassword;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

}