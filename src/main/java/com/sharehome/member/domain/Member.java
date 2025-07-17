package com.sharehome.member.domain;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.sharehome.common.domain.Address;
import com.sharehome.common.exception.BadRequestException;
import com.sharehome.common.exception.UnauthorizedException;
import com.sharehome.place.domain.Place;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false)
    private Password password;

    private String nickname;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "member_city")),
            @AttributeOverride(name = "street", column = @Column(name = "member_street")),
            @AttributeOverride(name = "zipcode", column = @Column(name = "member_zipcode"))
    })
    private Address address;

    @OneToMany(mappedBy = "member", cascade = ALL)
    private List<Place> places = new ArrayList<>();

    public Member(String email, String name, LocalDate birth, String password) {
        this.email = email;
        this.name = name;
        this.birth = birth;
        this.password = Password.hashPassword(password);
    }

    public void login(String password) {
        if (!this.password.checkPassword(password)) {
            throw new UnauthorizedException("이메일 혹은 비밀번호가 잘못되어 로그인에 실패하였습니다");
        }
    }

    public void update(String nickname, Address address) {
        this.nickname = nickname;
        this.address = address;
    }

    public void changePassword(String oldPassword, String newPassword) {
        checkPassword(oldPassword);
        this.password = Password.hashPassword(newPassword);
    }

    private void checkPassword(String password) {
        if (!this.password.checkPassword(password)) {
            throw new BadRequestException("비밀번호가 틀립니다");
        }
    }
}
