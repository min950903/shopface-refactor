package com.dreamsecurity.shopface.domain;

import com.dreamsecurity.shopface.response.ApiException;
import com.dreamsecurity.shopface.response.ApiResponseCode;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class Branch extends BaseTimeEntity {
  @PrePersist
  public void setDefaultState() {
    this.state = this.state == null ? "W" : this.state;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long no;

  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(nullable = false, length = 100)
  private String phone;

  @Column(nullable = false, length = 300)
  private String address;

  @Column(nullable = false, length = 300)
  private String detailAddress;

  @Column(nullable = false, length = 5)
  private String zipCode;

  @Column(nullable = true, length = 1000, insertable = false)
  private String businessLicensePath;

  @Column(nullable = false, length = 1)
  private String state;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "branch")
  private List<Role> roles = new ArrayList<Role>();

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "branch")
  private List<Department> departments = new ArrayList<Department>();

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "branch")
  private List<Occupation> occupations = new ArrayList<Occupation>();

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "branch")
  private List<Employ> employs = new ArrayList<Employ>();

  @Builder
  public Branch(
      String name,
      String phone,
      String address,
      String detailAddress,
      String zipCode,
      String businessLicensePath,
      String state,
      Member member) {
    this.name = name;
    this.phone = phone;
    this.address = address;
    this.detailAddress = detailAddress;
    this.zipCode = zipCode;
    this.businessLicensePath = businessLicensePath;
    this.state = state;
    this.member = member;
  }

  public void update(
      String name,
      String address,
      String detailAddress,
      String zipCode,
      String businessLicensePath) {
    this.name = name;
    this.address = address;
    this.detailAddress = detailAddress;
    this.zipCode = zipCode;
    this.businessLicensePath = businessLicensePath;
  }

  public void confirm() {
    if (!this.state.equals("Y")) {
      this.state = "Y";
    } else {
      throw new ApiException(ApiResponseCode.BAD_REQUEST, "이미 확인된 지점입니다");
    }
  }
}
