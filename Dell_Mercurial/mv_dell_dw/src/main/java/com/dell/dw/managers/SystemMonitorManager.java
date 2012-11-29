package com.dell.dw.managers;

import com.dell.dw.DuplicateKeyException;
import com.dell.dw.persistence.domain.SysMonEndPoint;
import com.dell.dw.persistence.domain.SysMonEndPointMetrics;
import com.dell.dw.persistence.domain.SysMonEndPointType;
import com.dell.dw.persistence.domain.SysMonServer;
import com.sourcen.core.managers.Manager;

import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 8/2/12
 * Time: 3:19 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SystemMonitorManager extends Manager {
    public List<SysMonEndPointType> getEndPointTypes();

    public SysMonEndPointType getEndPointType(Long id);

    public SysMonEndPoint getEndPointByName(String endPointName);

    public SysMonEndPoint getEndPoint(Long endPointId);

    public void addEndPoint(SysMonEndPoint endPoint) throws DuplicateKeyException;

    public void deleteEndpoint(Long id);

    public void addEndPoints(List<SysMonEndPoint> endPoints);

    public List<SysMonEndPoint> getEndPoints(Long endPointTypeId);

    //public void insertEndPointMetrics(Map<String,Map<String, Object>> endPointsResponse);

    public void insertEndPointMetrics(SysMonEndPointMetrics sysMonEndPointMetrics);

    public List<SysMonServer> getServerList();

    public void addServer(SysMonServer server)  throws DuplicateKeyException;

    public void deleteServer(Long id);

    public SysMonServer getServer(Long serverId);

    public void updateServer(SysMonServer obj);

    public void updateEndPoint(SysMonEndPoint endpoint);

    public Collection<Object[]> getTotalOrdersByDate();
}
