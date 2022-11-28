package com.travelog.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter @Setter
public class EntityInfo {
	
	@CreatedDate
	@Column(name="register_date", nullable = false, updatable = false)
	private LocalDateTime registerDate;
	@CreatedBy
	@Column(name="register_id", nullable = false, updatable = false)
	private String registerId;
	@LastModifiedDate
	@Column(name="update_date")
	private LocalDateTime updateDate;
	@LastModifiedBy
	@Column(name="update_id")
	private String updateId;

}
