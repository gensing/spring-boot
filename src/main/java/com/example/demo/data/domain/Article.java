package com.example.demo.data.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "articles")
public class Article extends Common {

	@Column(name = "subject", length = 10, nullable = false)
	String subject;

	@Column(name = "content", length = 500, nullable = false)
	String content;

	@ColumnDefault("0")
	@Column(name = "read_count", insertable = false)
	Integer readCount;

	@ManyToOne(targetEntity = Member.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_aritcles_member_id"))
	Member member;

	public void update(Article article) {
		this.subject = article.getSubject();
		this.content = article.getContent();
	}

}

//@DynamicInsert // null 값 필드는 무시하고 insert , 미사용 시 null값을 넣는다.
//@DynamicUpdate // null 값 필드는 무시하고 update , 미사용 시 null값을 넣는다.