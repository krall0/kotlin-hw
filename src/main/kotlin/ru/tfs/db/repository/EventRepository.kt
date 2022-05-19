package ru.tfs.db.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import ru.tfs.db.entity.EventEntity
import ru.tfs.model.EventStatus

interface EventRepository : JpaRepository<EventEntity, Int> {

    fun findAllByStatus(status: EventStatus): List<EventEntity>

    @Modifying
    @Query("update EventEntity e set e.status = ?2 where e.id = ?1")
    fun updateStatus(id: Int, status: EventStatus)
}
