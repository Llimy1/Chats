package org.project.chats.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String postCode;
    private String mainAddress;
    private String detailAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Address(String postCode, String mainAddress, String detailAddress, User user) {
        this.postCode = postCode;
        this.mainAddress = mainAddress;
        this.detailAddress = detailAddress;
        this.user = user;
    }

    public static Address createAddress(String postCode, String mainAddress, String detailAddress, User user) {
        return Address.builder()
                .postCode(postCode)
                .mainAddress(mainAddress)
                .detailAddress(detailAddress)
                .user(user)
                .build();
    }
}
