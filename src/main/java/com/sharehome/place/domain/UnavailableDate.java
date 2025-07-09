package com.sharehome.place.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "unavailable_date",
        uniqueConstraints = @UniqueConstraint(columnNames = {"place_id", "date"})
)
public class UnavailableDate {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    private LocalDate date;

    public UnavailableDate(Place place, LocalDate date) {
        this.place = place;
        this.date = date;
    }
}
