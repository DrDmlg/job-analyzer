package com.example.analyzer.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Vacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "job_title")
    String jobTitle;

    @Column(name = "companyName")
    String companyName;

    String location;

    Integer salary;

    String experience;

    @ElementCollection
    @CollectionTable(name = "vacancy_technologies", joinColumns = @JoinColumn(name = "vacancy_id"))
    @Column(name = "technology")
    Set<String> technologies = new HashSet<>();

    @CreationTimestamp
    @Column(name = "create_date")
    Date createDate;

    @UpdateTimestamp
    @Column(name = "modify_date")
    Date updateDate;
}
