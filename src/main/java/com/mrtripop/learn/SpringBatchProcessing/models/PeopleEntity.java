package com.mrtripop.learn.SpringBatchProcessing.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "peoples")
public class PeopleEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long personId;
  private String firstName;
  private String lastName;
}
