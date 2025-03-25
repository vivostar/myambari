import groovy.sql.Sql

import static io.infrastructor.core.logging.ConsoleLogger.info

import bean.Cluster

def url = "jdbc:sqlite:/home/peng/Desktop/sulazang/myambari/datas/db/data.db"
def user = "sa"
def passwd = ""
def driver = "org.sqlite.JDBC"

def sql = Sql.newInstance(url, user, passwd, driver)
sql.query("select cluster_name, version, state from Clusters") { resultSet ->

  while (resultSet.next()) {
    def map = [
      clusterName : resultSet.getString("cluster_name"),
      version : resultSet.getString("version"),
      state : resultSet.getString("state")
    ]
    def cluster = new Cluster(map)
    info cluster.toString()

  }

}

// result = sql.query("select cluster_name, version from Clusters")


info("you can do it")
sql.close()