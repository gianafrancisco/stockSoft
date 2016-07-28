service mysql start && \
echo "create database stock" >/tmp/database && \
mysql -u root --password=toor < /tmp/database

