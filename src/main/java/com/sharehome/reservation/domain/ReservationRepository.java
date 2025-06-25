package com.sharehome.reservation.domain;

import com.sharehome.place.domain.Place;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("""
            SELECT r FROM Reservation r
            WHERE r.place = :place
              AND r.checkInDate < :checkOutDate
              AND r.checkOutDate > :checkInDate
            """)
    List<Reservation> findAllByPlaceAndReservationDate(
            LocalDate checkInDate, LocalDate checkOutDate, Place place);
}
