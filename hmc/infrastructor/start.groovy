import groovy.sql.Sql

import static io.infrastructor.core.logging.ConsoleLogger.info

import io.infrastructor.core.inventory.BasicInventory
import io.infrastructor.core.inventory.Node

import bean.HostRole

def url = "jdbc:sqlite:/home/peng/Desktop/sulazang/myambari/datas/db/data.db"
def user = "sa"
def passwd = ""
def driver = "org.sqlite.JDBC"

def sql = Sql.newInstance(url, user, passwd, driver)

def hostRoles = []
sql.query("select role_id,cluster_name,host_name,component_name,state,desired_state from HostRoles") { resultSet ->

  while (resultSet.next()) {

    def map = [
      roleId : resultSet.getInt("role_id"),
      clusterName: resultSet.getString("cluster_name"),
      hostName: resultSet.getString("host_name"),
      componentName: resultSet.getString("component_name"),
      state: resultSet.getString("state"),
      desiredState: resultSet.getString("desired_state")
    ]

    def hostRole = new HostRole(map)
    hostRoles << hostRole

  }

}

def roleToHost = [:]

for (hostRole in hostRoles) {
  
  if (roleToHost.containsKey(hostRole.componentName)) {
    roleToHost[hostRole.componentName] << hostRole.hostName
  } else {
    def tmpList = []
    tmpList << hostRole.hostName
    roleToHost.put(hostRole.componentName ,tmpList)
  }

}


roleToHost.each { role, hosts ->
  def inventory = new BasicInventory()
  for (h in hosts) {
    if (h != "guyangyi") {
      def node = new Node() 
      // 如果是多个node，必须每个都要设置id，不然默认是null，导致在nodes添加的时候覆盖
      node.with {
        id = h
        host = h 
        username = "root"
        keyfile  = "/home/peng/Desktop/sulazang/myambari/datas/secret/master"
      }
      inventory << node
    }
  }

  inventory.provision {
    task name: "start $role", actions: {
      def shellCommand = ""
      if (role == "NAMENODE") {
        shell "yum install -y java-1.8.0-openjdk-devel"
        /// shellCommand = "service hadoop-hdfs-namenode start" 
      } else if (role = "DATANODE") {
        shell "yum install -y java-1.8.0-openjdk-devel"
        shellCommand = "/usr/lib/hadoop/sbin/hadoop-daemon.sh start datanode" 
        
      } else if (role == "SNAMENODE")  {
        shellCommand = "echo 'do nothing'"
      } else if (role == "RESOURCEMANAGER")  {
        shellCommand = "echo 'do nothing'"
      } else if (role == "NODEMANAGER") {
        shellCommand = "echo 'do nothing'"
      } else {
        shellCommand = "echo 'do nothing'"
      }
      shell {
        command = shellCommand
        user = "hdfs"
      }
       
    }
  }
}