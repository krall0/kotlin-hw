package ru.tfs.db.entity

import org.hibernate.Hibernate
import ru.tfs.model.EventStatus
import ru.tfs.model.EventType
import javax.persistence.*

@Entity
@Table(name = "events")
class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int? = null

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    lateinit var type: EventType

    @Column(name = "body", nullable = false)
    lateinit var body: String

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    lateinit var status: EventStatus

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as EventEntity

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}
