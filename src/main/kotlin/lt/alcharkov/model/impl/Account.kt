package lt.alcharkov.model.impl

import com.fasterxml.jackson.annotation.JsonProperty
import lt.alcharkov.model.Model

data class Account(@JsonProperty("id") val id: Int, @JsonProperty("balance") val balance: Int): Model
