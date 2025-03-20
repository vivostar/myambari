#
#
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
#
#
class hdp-nagios::params() inherits hdp::params
{   
 
  $nagios_user = "nagios"
  $nagios_group = "nagios"
  
  $conf_dir = hdp_default("nagios_conf_dir","/etc/nagios")

  $plugins_dir = "/usr/lib64/nagios/plugins"

  $nagios_obj_dir = hdp_default("nagios_obj_dir","/etc/nagios/objects")
  $nagios_host_cfg = hdp_default("nagios_host_cfg","${nagios_obj_dir}/hadoop-hosts.cfg")
  $nagios_hostgroup_cfg = hdp_default("nagios_hostgroup_cfg","${nagios_obj_dir}/hadoop-hostgroups.cfg")
  $nagios_servicegroup_cfg = hdp_default("nagios_servicegroup_cfg","${nagios_obj_dir}/hadoop-servicegroups.cfg")
  $nagios_service_cfg = hdp_default("nagios_service_cfg","${nagios_obj_dir}/hadoop-services.cfg")
  $nagios_command_cfg = hdp_default("nagios_command_cfg","${nagios_obj_dir}/hadoop-commands.cfg")
  
  $nagios_web_login = hdp_default("nagios_web_login","nagiosadmin")
  $nagios_web_password = hdp_default("nagios_web_password","admin")
  
  $dfs_data_dir = $hdp::params::dfs_data_dir
   
  $nagios_contact = hdp_default("nagios/nagios-contacts/nagios_contact","monitor\@monitor.com")

  $hostgroup_defs = {
    namenode => {host_member_info => 'namenode_host'},
    snamenode => {host_member_info => 'snamenode_host'},
    slaves => {host_member_info => 'slave_hosts'},
    nagios-server => {host_member_info => 'nagios_server_host'},
    jobtracker  => {host_member_info => 'jtnode_host'},
    ganglia-server => {host_member_info => 'ganglia_server_host'},
    zookeeper-servers => {host_member_info => 'zookeeper_hosts'},
    hbasemaster => {host_member_info => 'hbase_master_host'},
    hiveserver => {host_member_info => 'hive_server_host'},
    region-servers => {host_member_info => 'hbase_rs_hosts'},
    oozie-server => {host_member_info => 'oozie_server'},
    templeton-server => {host_member_info => 'templeton_server_host'}
  }
}
