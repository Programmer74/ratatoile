package com.ifmo.ratatoile.repository

import com.ifmo.ratatoile.dao.EatingTable
import com.ifmo.ratatoile.dao.Reservation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.Instant

interface EatingTableRepository : JpaRepository<EatingTable, Int>
interface ReservationRepository : JpaRepository<Reservation, Int> {

  @Query("SELECT r FROM Reservation r WHERE ((r.reservedFrom >= ?1) AND (r.reservedFrom <= ?2)) OR ((r.reservedTo >= ?1) AND (r.reservedTo <= ?2)) OR ((r.reservedFrom <= ?1) AND (r.reservedTo >= ?2))")
  fun findAllWithinTimeRange(from: Instant, to: Instant): List<Reservation>
}