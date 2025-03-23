package io.github.chekahchek.scalaposapp.dataclass

final case class DbConfig(host: String,
                          port: Int,
                          username: String,
                          password: String,
                          database: String)
