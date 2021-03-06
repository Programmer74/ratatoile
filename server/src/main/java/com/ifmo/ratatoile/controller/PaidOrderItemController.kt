package com.ifmo.ratatoile.controller

import com.ifmo.ratatoile.service.PaidOrderItemAccessService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    value = ["/api/1.0/order/history/"],
    produces = [MediaType.APPLICATION_JSON_VALUE])
class PaidOrderItemController(
  private val paidOrderItemService: PaidOrderItemAccessService
) {

  @GetMapping("/all")
  fun getAll() = paidOrderItemService.getPaidOrderItems()
}