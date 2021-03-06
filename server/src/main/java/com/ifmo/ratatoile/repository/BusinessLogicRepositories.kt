package com.ifmo.ratatoile.repository

import com.ifmo.ratatoile.dao.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.Instant

interface EatingTableRepository : JpaRepository<EatingTable, Int>
interface ReservationRepository : JpaRepository<Reservation, Int> {

  @Query("SELECT r FROM Reservation r WHERE ((r.reservedFrom >= ?1) AND (r.reservedFrom <= ?2)) OR ((r.reservedTo >= ?1) AND (r.reservedTo <= ?2)) OR ((r.reservedFrom <= ?1) AND (r.reservedTo >= ?2))")
  fun findAllWithinTimeRange(from: Instant, to: Instant): List<Reservation>
}

interface ReservationRequestRepository : JpaRepository<ReservationRequest, Int>

interface GuestRepository : JpaRepository<Guest, Int> {
  fun findAllByLeavedAtIsNull(): List<Guest>
}

interface DishRepository : JpaRepository<Dish, Int>
interface GuestOrderItemRepository : JpaRepository<GuestOrderItem, Int> {
  fun findAllByGuestId(guestId: Int): List<GuestOrderItem>

  @Query("select a from GuestOrderItem a where a.guestId in (select g.id from Guest g where g.waiter.id = ?1 and g.leavedAt is null)")
  fun findForWaiter(waiterId: Long): List<GuestOrderItem>
}

interface IngredientRepository : JpaRepository<Ingredient, Int>

interface DishIngredientRepository : JpaRepository<DishIngredient, Int> {
  fun findByDishId(dishId: Int): List<DishIngredient>
  fun findByIngredientId(ingredientId: Int): List<DishIngredient>
}

interface DishPhotoRepository : JpaRepository<DishPhoto, Long>
interface MenuEntryRepository : JpaRepository<MenuEntry, Int>

interface CriticDonationRepository : JpaRepository<CriticDonation, Int>
interface InspectorDonationRepository : JpaRepository<InspectorDonation, Int>

interface GuestCardRepository: JpaRepository<GuestCard, Int>

interface KitchenQueueRepository: JpaRepository<KitchenQueueEntry, Int> {
  fun findAllByOrderItemIn(items: List<GuestOrderItem>): List<KitchenQueueEntry>
}

interface PaidOrderItemRepository: JpaRepository<PaidOrderItem, Int>