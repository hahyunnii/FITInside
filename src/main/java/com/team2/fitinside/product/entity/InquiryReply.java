//package com.team2.fitinside.product.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.annotation.LastModifiedDate;
//
//import java.time.LocalDateTime;
//
//@Entity
//@NoArgsConstructor
//@AllArgsConstructor
//@Data
//@Builder
//@Table(name = "inquiry_reply")
//public class InquiryReply {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "inquiry_id", nullable = false)
//    private Inquiry inquiry;
//
//    @Column(name = "reply_content", length = 500, nullable = false)
//    private String content;
//
//    @CreatedDate
//    @Column(name = "created_at", nullable = false, updatable = false)
//    private LocalDateTime createdAt;
//
//    @LastModifiedDate
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
//
//    @Column(name = "deleted_at")
//    private LocalDateTime deletedAt;
//
//    @Column(name = "is_deleted", nullable = false)
//    private boolean isDeleted;
//
//    @PrePersist
//    public void prePersist() {
//        this.isDeleted = false;
//        this.createdAt = LocalDateTime.now();
//    }
//
//    @PreUpdate
//    public void preUpdate() {
//        this.updatedAt = LocalDateTime.now();
//        if (this.deletedAt != null) {
//            this.isDeleted = true;
//        }
//    }
//}