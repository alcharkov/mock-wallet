package lt.alcharkov

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import lt.alcharkov.service.AccountService
import lt.alcharkov.service.TransactionService
import lt.alcharkov.db.H2
import lt.alcharkov.model.impl.Account
import lt.alcharkov.model.impl.Transaction
import spark.Spark.*
import org.sql2o.Sql2o

fun main() {

    val h2 = H2()
    val sql2o = Sql2o(h2.datasource)

    val accountService = AccountService(sql2o)
    val transactionService = TransactionService(sql2o)

    val mapper = ObjectMapper().registerModule(KotlinModule())

    get("/ping") { _,_ -> "pong" }

    path("/api/") {
        path("transaction") {
            get("/") { _,res ->
                res.type("application/json")
                mapper.writeValueAsString(transactionService.getTransactions())
            }
            get("/:id") { req,res ->
                res.type("application/json")
                mapper.writeValueAsString(transactionService.getTransaction(Integer.parseInt(req.params("id"))))
            }
            post("/add") { req,res ->
                res.type("application/json")
                mapper.writeValueAsString(transactionService.createTransaction(mapper.readValue(req.body(), Transaction::class.java)))
            }
        }
        path("/account") {
            get("/") { _,res ->
                res.type("application/json")
                mapper.writeValueAsString(accountService.getAccounts())
            }
            get("/:id") { req,res ->
                res.type("application/json")
                mapper.writeValueAsString(accountService.getAccount(Integer.parseInt(req.params("id"))))
            }
            post("/add") { req,res ->
                res.type("application/json")
                mapper.writeValueAsString(accountService.createAccount(mapper.readValue(req.body(), Account::class.java)))
            }
        }
    }

}