package com.ifmo.ratatoile.service

import com.ifmo.ratatoile.dao.EatingTable
import com.ifmo.ratatoile.dao.toDto
import com.ifmo.ratatoile.dto.*
import com.ifmo.ratatoile.repository.EatingTableRepository
import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class EatingTableService(
  private val tableRepository: EatingTableRepository
) {

  @Autowired
  lateinit var reservationsService: ReservationsService

  @Autowired
  lateinit var guestsService: GuestService

  @PostConstruct
  fun postConstruct() {
    if (tableRepository.findAll().isEmpty()) {
      logger.warn { "Tables table is empty; generating mock eating tables..." }
      val tables = TableLayoutService.createArrangedTables()
      tables.forEach { tableRepository.saveAndFlush(it) }
    }
  }

  fun getTables(): TablesDto = TablesDto(tableRepository.findAll().map { it.toDto() })

  fun getTableAsEntity(id: Int) = tableRepository.findById(id).get()

  private fun getTablesWithReservations(reservations: TableReservationsDto): TablesWithStateDto {
    val reservationsGrouped = reservations.reservations.groupBy { it.assignedTableId }
    val tablesCurrentlyBusy = guestsService.currentBusyTables()
    val guestsUnderMyControl = guestsService.currentGuestsForCurrentUser()
    return TablesWithStateDto(getTables().tables.map {
      val reservationsForTable = reservationsGrouped[it.id] ?: emptyList()
      val reservedNow = reservationsForTable
          .firstOrNull {
            (it.from <= System.currentTimeMillis()) &&
                (it.to >= System.currentTimeMillis())
          } != null
      val tableCurrentlyBusy = tablesCurrentlyBusy.contains(it.id)
      val guestsForTable = guestsUnderMyControl[it.id] ?: GuestsDto(emptyList())
      val state = when {
        tableCurrentlyBusy && guestsForTable.guests.isNotEmpty() -> TableWithStateDtoState.BUSY_BY_YOU
        tableCurrentlyBusy && guestsForTable.guests.isEmpty() -> TableWithStateDtoState.BUSY
        reservedNow -> TableWithStateDtoState.SUPPOSED_TO_BE_BUSY
        reservationsForTable.isNotEmpty() -> TableWithStateDtoState.FREE_BUT_BOOKED
        else -> TableWithStateDtoState.FREE
      }
      TableWithStateDto(
          it.id,
          it.guiX,
          it.guiY,
          it.guiW,
          it.guiH,
          it.maxSeats,
          it.type,
          reservationsForTable,
          tableCurrentlyBusy,
          guestsForTable,
          state)
    })
  }

  fun getTablesWithReservations(): TablesWithStateDto {
    val allReservations = reservationsService.getReservations()
    return getTablesWithReservations(allReservations)
  }

  fun getTablesWithReservationsForToday(): TablesWithStateDto {
    val reservationsForToday = reservationsService.getReservationsForToday()
    return getTablesWithReservations(reservationsForToday)
  }

  fun filterAvailableTables(
    busyTableIds: Set<Int>,
    request: TableReservationRequest
  ): List<EatingTable> {
    return tableRepository.findAll()
        .asSequence()
        .filter { !busyTableIds.contains(it.id!!) }
        .filter { it.maxSeats >= request.seats }
        .filter {
          when (request.type) {
            TableReservationTableType.NORMAL -> true
            TableReservationTableType.NEAR_WINDOW -> it.isNearWindow()
            TableReservationTableType.NEAR_BAR -> it.isNearBar()
          }
        }
        .toList()
  }

  companion object : KLogging()
}