package com.project.airbnb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "invalidated_tokens")
public class InvalidatedToken {
    @Id
    private String id;
    Date expiryTime;
}
//Lưu thời gian token hết hạn -> lập lịch để remove những token đã hết hạn
