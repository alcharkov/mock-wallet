package lt.alcharkov.service

import lt.alcharkov.dao.AccountDao
import lt.alcharkov.dao.TransactionDao
import lt.alcharkov.exception.NotEnoughMoneyException
import lt.alcharkov.model.impl.Account
import lt.alcharkov.model.impl.Transaction
import org.sql2o.Sql2o

class TransactionService(private val sql2o: Sql2o) {

    private val transactionDao = TransactionDao(sql2o)
    private val accountDao = AccountDao(sql2o)

    fun createTransaction(transaction: Transaction) :Int {
        val from =  accountDao.getById(transaction.afrom)
        val to =  accountDao.getById(transaction.ato)

        if (from.balance-transaction.amount < 0) {
            throw NotEnoughMoneyException()
        }

        accountDao.update(Account(from.id, from.balance-transaction.amount))
        accountDao.update(Account(to.id, to.balance+transaction.amount))

        return transactionDao.create(transaction)
    }

    fun getTransaction(id: Int) : Transaction {
        return transactionDao.getById(id)
    }

    fun getTransactions() :List<Transaction> {
        return transactionDao.getAll()
    }

}