package org.gaurav.virtualpowerplantsystem.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Table(name = "BATTERY")
public class Battery extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "POST_CODE", nullable = false)
    private String postCode;

    @Column(name = "CAPACITY", nullable = false)
    private Integer capacity;
}
