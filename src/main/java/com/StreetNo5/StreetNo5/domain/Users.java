package com.StreetNo5.StreetNo5.domain;


import com.StreetNo5.StreetNo5.domain.eums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.common.aliasing.qual.Unique;


@Entity
@Getter
@NoArgsConstructor
public class Users extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Unique
    @NotNull
    private String nickname;
    @NotNull
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    
    @Builder
    public Users(Long id, String nickname,String password, Role role) {
        this.id = id;
        this.nickname = nickname;
        this.password=password;
        this.role = role;
    }

    public Users update(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

}