package lt.alcharkov.model.impl

import com.fasterxml.jackson.annotation.JsonProperty
import lt.alcharkov.model.Model

data class Transaction(@JsonProperty("id") val id: Int,
                       @JsonProperty("afrom") val afrom: Int,
                       @JsonProperty("ato") val ato: Int,
                       @JsonProperty("amount") val amount: Int): Model
