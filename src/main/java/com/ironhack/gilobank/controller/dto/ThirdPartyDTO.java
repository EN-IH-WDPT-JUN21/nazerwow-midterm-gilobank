package com.ironhack.gilobank.controller.dto;

import com.ironhack.gilobank.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
public class ThirdPartyDTO {

    private Long id;

    @Size(min = 1, max = 28)
    private String name;

    @Size(min = 1, max = 28)
    private String hashedKey;

    @Enumerated(EnumType.STRING)
    private final Role role = Role.THIRDPARTY;

    public ThirdPartyDTO(String name, String hashedKey) {
        this.name = name;
        this.hashedKey = hashedKey;
    }
}
