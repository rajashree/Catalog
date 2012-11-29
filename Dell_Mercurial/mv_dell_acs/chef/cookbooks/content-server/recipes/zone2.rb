#
# Cookbook Name:: enablement
# Recipe:: iads-contenserver
#
# Copyright 2012, MarketVine by Dell
#
# All rights reserved - Do Not Redistribute
#

# Set the nodes zone to zone 2.

node[:dell_acs][:zone] = "2"