package com.moa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.moa.entity.Artwork.CanvasType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Canvas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long canvasId;

    @Enumerated(EnumType.STRING)
    @Column
    private CanvasNum canvasNum;
    
    @Enumerated(EnumType.STRING)
    private CanvasType canvasType;
    
    private String width;
    private String height;
    
    public enum CanvasType { F, P, M, S }
    public enum CanvasNum {
        ONE("1호"),
        TWO("2호"),
        THREE("3호"),
        FOUR("4호"),
        FIVE("5호"),
        SIX("6호"),
        EIGHT("8호"),
        TEN("10호"),
        TWELVE("12호"),
        FIFTEEN("15호"),
        TWENTY("20호"),
        TWENTY_FIVE("25호"),
        THIRTY("30호"),
        FORTY("40호"),
        FIFTY("50호"),
        SIXTY("60호"),
        EIGHTY("80호"),
        ONE_HUNDRED("100호"),
        ONE_TWENTY("120호"),
        ONE_FIFTY("150호"),
        TWO_HUNDRED("200호"),
        THREE_HUNDRED("300호"),
        FIVE_HUNDRED("500호");


        private final String value;

        // 생성자
        CanvasNum(String value) {
            this.value = value;
        }

        // 문자열 값 가져오기
        public String getValue() {
            return value;
        }

        // 문자열로부터 Enum 가져오기
        public static CanvasNum fromValue(String value) {
            for (CanvasNum num : CanvasNum.values()) {
                if (num.value.equals(value)) {
                    return num;
                }
            }
            throw new IllegalArgumentException("Invalid value: " + value);
        }
    }



}
