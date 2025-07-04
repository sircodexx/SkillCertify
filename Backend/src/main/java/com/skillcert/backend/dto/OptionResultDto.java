package com.skillcert.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptionResultDto {

    private Long optionId;
    private String optionText;
    private Boolean isCorrect;
    private Boolean wasSelected;
}