
# https://www.cnblogs.com/hbuuid/p/18298707
sed -i s/mirror.centos.org/vault.centos.org/g /etc/yum.repos.d/*.repo
sed -i s/^#.*baseurl=http/baseurl=http/g /etc/yum.repos.d/*.repo
sed -i s/^mirrorlist=http/#mirrorlist=http/g /etc/yum.repos.d/*.repo


sudo yum install epel-release


docker compose cp /home/peng/Downloads/centos7 master:/opt
docker compose cp CentOS-Base.repo master:/etc/yum.repos.d

docker compose cp /home/peng/Downloads/systemctl.py master:/usr/bin/systemctl

zips=$(ls)
for file in $zips
do
  unzip $file 
done


# https://stackoverflow.com/questions/49285658/how-to-solve-docker-issue-failed-to-connect-to-bus-no-such-file-or-directory
wget https://raw.githubusercontent.com/gdraheim/docker-systemctl-replacement/master/files/docker/systemctl.py -O /usr/local/bin/systemctl

# 查看docker容器使用资源情况
docker stats 