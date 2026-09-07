package com.sms.sequence.entity;

import com.sms.sequence.enums.ModuleType;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "running_sequences")
@Getter
@Setter
@NoArgsConstructor
public class RunningSequence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 50)
    private ModuleType module;

    @Column(name = "last_number", nullable = false)
    private Long lastNumber;

}