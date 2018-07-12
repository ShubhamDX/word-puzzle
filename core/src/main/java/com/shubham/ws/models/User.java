package com.shubham.ws.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : shubham
 */
@Entity
@Table(name = "user")
@Getter
@Setter
@Builder
public class User {

  @Id
  @Column(name = "id")
  private String id;

  @Column(name = "name")
  private String name;

  @Temporal(TemporalType.TIMESTAMP)
  @CreationTimestamp
  @Column(name = "created_on")
  private Date createdOn;

  @Temporal(TemporalType.TIMESTAMP)
  @UpdateTimestamp
  @Column(name = "updated_on")
  private Date updatedOn;
}
