node.default[:dell_acs][:profile] = "marketvine-chef"
node.default[:dell_acs][:install_path] = "/opt/dell_acs/tomcat-7.0.27/webapps/ROOT"
node.default[:dell_acs][:deployFilesPath] = "/etc/marketvine"
node.default[:dell_acs][:updates_path][:acs][:v2] = "/mnt/deployment-files/enablement/content-server/acs/v2"
node.default[:dell_acs][:updates_path][:acs][:v1] = "/mnt/deployment-files/enablement/content-server/acs/v1"
node.default[:dell_acs][:environment] = "dev"
node.default[:dell_acs][:war_file_to_deploy] = ""
node.default[:dell_acs][:zone] = "1"
node.default[:dell_acs][:config][:runFeedServices] = "false"
node.default[:dell_acs][:config][:datasource_hostname] = "172.30.0.49"
node.default[:dell_acs][:config][:datasource_database] = "dell_acs_stage"
node.default[:dell_acs][:config][:datasource_username] = "acs_stage"
node.default[:dell_acs][:config][:datasource_password] = "password"
node.default[:dell_acs][:config][:sftp_hostname] = "172.30.0.32"
node.default[:dell_acs][:config][:sftp_username] = "newprodfeed"
node.default[:dell_acs][:config][:sftp_password] = "N$};f6$)P"
node.default[:dell_acs][:config][:filesystem_cdnPrefix] = "http://172.30.0.57"
node.default[:dell_acs][:config][:hibernate_hbm2ddl_auto] = "validate"
node.default[:dell_acs][:config][:smtp_host] = "smtp.1and1.com"
node.default[:dell_acs][:config][:smtp_username] = "dell@snipl.com"
node.default[:dell_acs][:config][:smtp_password] = "$niplpa55"