package com.reservation.reserve.reserve.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressVO {
    
    @Column(name = "postal_code", length = 6)
    private String postalCode;          // 우편번호
    
    @Column(name = "province", length = 20)
    private String province;            // 시/도
    
    @Column(name = "city", length = 30)
    private String city;                // 시/군/구
    
    @Column(name = "district", length = 30)
    private String district;            // 읍/면/동
    
    @Column(name = "street_address", length = 100)
    private String streetAddress;       // 상세주소
    
    @Column(name = "building_name", length = 50)
    private String buildingName;        // 건물명
    
    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (province != null) sb.append(province).append(" ");
        if (city != null) sb.append(city).append(" ");
        if (district != null) sb.append(district).append(" ");
        if (streetAddress != null) sb.append(streetAddress);
        if (buildingName != null) sb.append(" (").append(buildingName).append(")");
        return sb.toString().trim();
    }
}
