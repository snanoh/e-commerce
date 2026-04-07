package com.snanoh.e_commerce.delivery.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DeliveryRepository : JpaRepository<DeliveryEntity, Long>
