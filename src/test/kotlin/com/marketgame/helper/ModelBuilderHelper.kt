package com.marketgame.helper

import com.marketgame.enums.CustomerStatus
import com.marketgame.enums.Role
import com.marketgame.model.CustomerModel
import com.marketgame.model.GameModel
import com.marketgame.model.PurchaseModel
import java.math.BigDecimal
import java.util.*

fun buildCustomer(
    id: Int? = null,
    name: String = "Customer_name",
    email: String = "${UUID.randomUUID()}@email.com",
    password: String = "password"


) = CustomerModel(
    id = id,
    name = name,
    email = email,
    status = CustomerStatus.ATIVO,
    password = password,
    roles = setOf(Role.CUSTOMER)
)

fun buildPurchase(
    id:Int? = null,
    customer: CustomerModel = buildCustomer(),
    games: MutableList<GameModel> = mutableListOf(),
    nfe: String? = UUID.randomUUID().toString(),
    price: BigDecimal = BigDecimal.TEN

) = PurchaseModel(
    id = id,
    customer = customer,
    games = games,
    nfe = nfe,
    price = price
)