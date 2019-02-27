package lt.alcharkov.dao

import lt.alcharkov.model.impl.Account
import org.sql2o.Sql2o
import java.time.Instant
import java.util.concurrent.atomic.AtomicInteger

class AccountDao(private val sql2o: Sql2o) {
    fun create(account: Account) :Int {
        val id = AtomicInteger(Instant.now().nano).get()
        sql2o.open().createQuery("insert into account(id, balance) VALUES (:id, :balance)")
                .addParameter("id", id)
                .addParameter("balance", account.balance)
                .executeUpdate()
        return id
    }

    fun update(account: Account) :Int {
        sql2o.open().createQuery("update account set balance = :balance where id = :id")
                .addParameter("balance", account.balance)
                .addParameter("id", account.id)
                .executeUpdate()
        return account.id
    }

    fun getById(id: Int): Account {
        return sql2o.open().createQuery("select id, balance from account where id = :id")
                .addParameter("id", id)
                .executeAndFetchFirst(Account::class.java)
    }
    fun getAll(): List<Account> {
        return sql2o.open().createQuery("select id, balance from account")
                .executeAndFetch(Account::class.java)
    }
}