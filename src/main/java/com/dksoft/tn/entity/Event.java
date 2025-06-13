package com.dksoft.tn.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
@Table(name = "events")
public class Event {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        @ElementCollection
        @CollectionTable(name = "event_images", joinColumns = @JoinColumn(name = "event_id"))
        @Column(name = "image_url")
        private List<String> imageUrls = new ArrayList<>();

        @Column(nullable = false)
        private String title;

        @Column(nullable = false, length = 1000)
        private String description;

        @Column(nullable = false, length = 255)
        private String shortDescription;

        @Column(nullable = false)
        private boolean isPublished = true;

        @ManyToMany
        @JoinTable(
                name = "event_categories",
                joinColumns = @JoinColumn(name = "event_id"),
                inverseJoinColumns = @JoinColumn(name = "category_id")
        )
        private List<Category> categories = new ArrayList<>();

        @ManyToOne
        @JoinColumn(name = "organizer_id", nullable = false)
        private User organizer;

        @ElementCollection
        @CollectionTable(name = "event_tags", joinColumns = @JoinColumn(name = "event_id"))
        @Column(name = "tag")
        private List<String> tags = new ArrayList<>();

        @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonManagedReference("event-eventday")
        private List<EventDay> eventDays = new ArrayList<>();

        @Column(nullable = false, updatable = false)
        @Temporal(TemporalType.TIMESTAMP)
        private LocalDateTime createdAt;

        @Column(nullable = false)
        @Temporal(TemporalType.TIMESTAMP)
        private LocalDateTime updatedAt;

        @PrePersist
        protected void onCreate() {
                createdAt = LocalDateTime.now();
                updatedAt = LocalDateTime.now();
        }

        @PreUpdate
        protected void onUpdate() {
                updatedAt = LocalDateTime.now();
        }

        public void setIsPublished(boolean active) {
                this.isPublished = active;
        }
}