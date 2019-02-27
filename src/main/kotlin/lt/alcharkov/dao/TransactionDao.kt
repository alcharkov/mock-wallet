package lt.alcharkov.dao

import lt.alcharkov.model.impl.Transaction
import java.time.Instant
import java.util.concurrent.atomic.AtomicInteger
import org.sql2o.Sql2o

class TransactionDao (private val sql2o: Sql2o) {
    fun create(model: Transaction): Int {
        val id = AtomicInteger(Instant.now().nano).get()
        sql2o.open()
                .createQuery("insert into transaction(id, afrom, ato, amount) VALUES (:id, :afrom, :ato, :amount)")
                .throwOnMappingFailure(false)
                .addParameter("id", id)
                .addParameter("afrom", model.afrom)
                .addParameter("ato", model.ato)
                .addParameter("amount", model.amount)
                .executeUpdate()
        return id
    }

    fun getById(id: Int): Transaction {
        return sql2o.open().createQuery("select id, afrom, ato, amount from transaction where id = :id")
                .addParameter("id", id)
                .executeAndFetchFirst(Transaction::class.java)
    }

    fun getAll(): List<Transaction> {
        return sql2o.open().createQuery("select id, afrom, ato, amount from transaction")
                .executeAndFetch(Transaction::class.java)
    }

}
