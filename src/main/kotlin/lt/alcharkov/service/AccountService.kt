package lt.alcharkov.service

import lt.alcharkov.dao.AccountDao
import lt.alcharkov.model.impl.Account
import org.sql2o.Sql2o
class AccountService(private val sql2o: Sql2o) {

    private val accountDao = AccountDao(sql2o)

    fun createAccount(account: Account) :Int {
        return accountDao.create(account)
    }

    fun updateAccount(account: Account) :Int {
        return accountDao.update(account)
    }

    fun getAccount(id: Int) : Account {
        return accountDao.getById(id)
    }

    fun getAccounts() :List<Account> {
        return accountDao.getAll()
    }

}