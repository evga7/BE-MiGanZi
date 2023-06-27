package com.StreetNo5.StreetNo5.domain;


import com.StreetNo5.StreetNo5.domain.eums.Role;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;


    @Builder
    public Users(Long id, String nickname,String password, Role role, String provider) {
        this.id = id;
        this.nickname = nickname;
        this.password=password;
        this.role = role;
    }

    public Users update(String nickname, String provider) {
        this.nickname = nickname;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}