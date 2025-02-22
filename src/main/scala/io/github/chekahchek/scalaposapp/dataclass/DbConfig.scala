package io.github.chekahchek.scalaposapp.dataclass
//import pureconfig.ConfigReader


final case class DbConfig(host: String,
                          port: Int,
                          username: String,
                          password: String,
                          database: String)
