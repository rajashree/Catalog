#
# Cookbook Name:: enablement
# Recipe:: iads-contenserver
#
# Copyright 2012, MarketVine by Dell
#
# All rights reserved - Do Not Redistribute
#

directory "#{node[:dell_acs][:deployFilesPath]}" do
	recursive true
	group "support"
	action :create
end

#cookbook_file "#{node[:dell_acs][:deployFilesPath]}/chef-deploy.sh" do
#	source "chef-deploy.sh"
#	group "support"
#	mode "0775"
#	owner "root"
#end

template "#{node[:dell_acs][:deployFilesPath]}/deploy.sh"  do
	source "deploy_contentserver.sh.erb"
	mode "0774"
	variables({
		:dell_acs_profile => node[:dell_acs][:profile],
		:updates_path => node[:dell_acs][:updates_path][:acs][:v2],
		:environment => node[:dell_acs][:environment],
		:war_file => node[:dell_acs][:war_file_to_deploy]
		})
end

cookbook_file "#{node[:dell_acs][:deployFilesPath]}/rollback.sh" do
	source "rollback_contentserver.sh"
	group "support"
	mode "0775"
	owner "root"
end

execute "dos2unix" do
	cwd "/etc/marketvine"
	command "dos2unix *.sh"
	
end

execute "chef-deploy" do
	cwd "/etc/marketvine"
	command "./deploy.sh"
	user "root"
end

template "#{node[:dell_acs][:install_path]}/WEB-INF/classes/application.properties"  do
	source "dell_acs.application.properties.erb"
	mode "0774"
	variables({
		:dell_acs_profile => node[:dell_acs][:profile]
		})
end

template "#{node[:dell_acs][:install_path]}/WEB-INF/classes/profiles/#{node[:dell_acs][:profile]}.properties"  do
	source "dell_acs_profile.properties.erb"
	mode "0774"
	variables({
		:config => node[:dell_acs][:config]
		})
end
