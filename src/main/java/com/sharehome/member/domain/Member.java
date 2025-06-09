package com.sharehome.member.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.sharehome.common.exception.BadRequestException;
import com.sharehome.common.exception.UnauthorizedException;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false)
    private String password;

    private String nickname;

    @Embedded
    private Address address;

    public Member(String email, String name, LocalDate birth, String password) {
        this.email = email;
        this.name = name;
        this.birth = birth;
        this.password = password;
    }

    public void login(String password) {
        if (!this.password.equals(password)) {
            throw new UnauthorizedException("이메일 혹은 비밀번호가 잘못되어 로그인에 실패하였습니다");
        }
    }

    public void update(String nickname, Address address) {
        this.nickname = nickname;
        this.address = address;
    }

    public void changePassword(String oldPassword, String newPassword) {
        checkPassword(oldPassword);
        this.password = newPassword;
    }

    public void checkPassword(String password) {
        if (!password.equals(this.password)) {
            throw new BadRequestException("비밀번호가 틀립니다");
        }
    }
}
