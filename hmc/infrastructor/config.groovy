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

def configBindings = [
  fs_defaultFS : "hdfs://master:8020",
  dfs_namenode_name_dir : "/data/hdfs/nn",
  dfs_datanode_data_dir : "/data/hdfs/dn",
  JAVA_HOME : "/usr/lib/jvm/java-1.8.0-openjdk"
]


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
    task name: "config $role", actions: {
      def shellCommand = ""
      if (role == "NAMENODE" || role == "DATANODE") {
        template {
          source = "/home/peng/Desktop/sulazang/myambari/hmc/infrastructor/config/hadoop/core-site.xml"
          target = "/etc/hadoop/conf/core-site.xml" 
          bindings = configBindings
          owner = "root"
          group = "root"
        }
        template {
          source = "/home/peng/Desktop/sulazang/myambari/hmc/infrastructor/config/hadoop/hdfs-site.xml"
          target = "/etc/hadoop/conf/hdfs-site.xml" 
          bindings = configBindings
          owner = "root"
          group = "root"
        }

        template {
          source = "/home/peng/Desktop/sulazang/myambari/hmc/infrastructor/config/hadoop/hadoop-env.sh"
          target = "/etc/hadoop/conf/hadoop-env.sh" 
          bindings = configBindings
          owner = "root"
          group = "root"
        }
      } else if (role == "SNAMENODE")  {
        shellCommand = "yum install -y hadoop-hdfs-secondarynamenode"
      } else if (role == "RESOURCEMANAGER")  {
        shellCommand = "yum install -y hadoop-yarn-resourcemanager"
      } else if (role == "NODEMANAGER") {
        shellCommand = "yum install -y hadoop-yarn-nodemanager"
      } else if (role == "DATANODE") {
        shellCommand = "yum install -y hadoop-hdfs-datanode"
        // shellCommand = "rm -f /root/DATANODE"
      } else {
        shellCommand = "echo 'do nothing'"
      }
      // shell shellCommand
       
    }
  }
}