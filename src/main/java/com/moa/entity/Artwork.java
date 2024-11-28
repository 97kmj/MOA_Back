package com.moa.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import com.moa.entity.User.ApprovalStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Artwork {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long artworkId;
	@ManyToOne
	@JoinColumn(name = "artist_id", nullable = false)
    private User artist;

    @Column(nullable = false)
    private String title;

    @Lob
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private Type type;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    private Boolean isStandardCanvas;
    
    
    @Enumerated(EnumType.STRING)
    private CanvasType canvasType;

    @ManyToOne
    @JoinColumn(name = "canvas_id", nullable = false) // 외래 키 설정
    private Canvas canvas;

    private String width;
    private String length;
    private String height;
    private String imageUrl;

    @Column(nullable = false, updatable = false)
	private Timestamp createAt;
    
    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private Integer stock;

    private Boolean termsAccepted;
    private Integer likeCount;

    @Enumerated(EnumType.STRING)
    private SaleStatus saleStatus;
    
    private Boolean adminCheck;
    
    @PrePersist
	protected void onCreate() {
    	this.createAt = new Timestamp(System.currentTimeMillis());
		this.likeCount = 0;
	}
    

    public enum CanvasType { F, P, M, S }
    
    public enum SaleStatus { AVAILABLE, SOLD_OUT, NOT_SALE, DELETE }



	
	
}
