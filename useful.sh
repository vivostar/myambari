# 初始化数据库
/opt/lampp/bin/php hmc/php/frontend/initializeHMC.php /home/peng/Desktop/sulazang/myambari/datas/db/data.db hmc/db/schema.dump

# 监控网页临时

http://localhost/mon%5Fdashboard/src/ui/home.html


# 初始化数据库
DROP TABLE Clusters;
DROP TABLE ConfigHistory;
DROP TABLE ConfigProperties;
DROP TABLE HostRoleConfig;
DROP TABLE HostRoles;
DROP TABLE HostS;
DROP TABLE ServiceComponents;
DROP TABLE ServiceConfig;
DROP TABLE ServiceDependencies;
DROP TABLE ServiceInfo;
DROP TABLE Services;
DROP TABLE SubTransactionStatus;
DROP TABLE TransactionStatus;     
DROP TABLE ServiceComponentDependencies;
DROP TABLE ServiceComponentInfo;