package com.dell.dw.managers;

import com.dell.dw.DuplicateKeyException;
import com.dell.dw.persistence.domain.SysMonEndPoint;
import com.dell.dw.persistence.domain.SysMonEndPointMetrics;
import com.dell.dw.persistence.domain.SysMonEndPointType;
import com.dell.dw.persistence.domain.SysMonServer;
import com.dell.dw.persistence.repository.*;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 8/2/12
 * Time: 3:19 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class SystemMonitorManagerImpl implements SystemMonitorManager{
    @Override
    @Transactional(readOnly = true)
    public List<SysMonEndPointType> getEndPointTypes() {
        return sysMonEndPointTypeRepository.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public SysMonEndPointType getEndPointType(Long id){
        return sysMonEndPointTypeRepository.get(id);
    }

    @Override
    @Transactional(readOnly = true)
    public SysMonEndPoint getEndPointByName(String endPointName){
        return sysMonEndPointRepository.getEndPointByName(endPointName);
    }

    @Override
    @Transactional(readOnly = true)
    public SysMonEndPoint getEndPoint(Long endPointId){
        return sysMonEndPointRepository.get(endPointId);
    }

    @Override
    @Transactional(rollbackFor = DuplicateKeyException.class)
    public void addEndPoint(SysMonEndPoint endPoint) throws DuplicateKeyException {
        try{
            sysMonEndPointRepository.insert(endPoint);
        }catch(Exception e){
            if (e instanceof ConstraintViolationException) {
                throw new DuplicateKeyException("Endpoint already exists");
            }
        }
    }

    @Override
    @Transactional
    public void deleteEndpoint(Long id){
        sysMonEndPointRepository.remove(id);
    }



    @Override
    @Transactional
    public void addEndPoints(List<SysMonEndPoint> endPoints) {
        sysMonEndPointRepository.insertAll(endPoints);
    }

    @Override
    @Transactional
    public List<SysMonEndPoint> getEndPoints(Long endPointTypeId) {
        List<SysMonEndPoint> sysMonEndPointList = sysMonEndPointRepository.getEndPoints(endPointTypeId);
        if(sysMonEndPointList != null && !sysMonEndPointList.isEmpty())
            for(SysMonEndPoint obj : sysMonEndPointList){
                obj.setEndPointMetricses(sysMonEndPointMetricsRepository.getEndPointMetrices(obj.getId()));
            }
        return sysMonEndPointList;
    }

    /*@Override
    @Transactional
    public void insertEndPointMetrics(Map<String,Map<String, Object>> endPointsResponse){
        List<SysMonEndPointMetrics> endPointMetricses = new ArrayList<SysMonEndPointMetrics>();
        for (String key : endPointsResponse.keySet()){
            Map<String, Object> metrics = endPointsResponse.get(key);
            SysMonEndPointMetrics obj = new SysMonEndPointMetrics((Long)metrics.get("responseTime"),new Date(),getEndPointByUrl(key));
            endPointMetricses.add(obj);
        }
        sysMonEndPointMetricsRepository.insertAll(endPointMetricses);
    }*/

    @Override
    @Transactional
    public void insertEndPointMetrics(SysMonEndPointMetrics sysMonEndPointMetrics){
        sysMonEndPointMetricsRepository.insert(sysMonEndPointMetrics);
    }

    @Override
    @Transactional
    public List<SysMonServer> getServerList() {
        List<SysMonServer> sysMonServers = sysMonServerRepository.getAll();
        if(sysMonServers != null && !sysMonServers.isEmpty())
            for(SysMonServer server : sysMonServers){
                server.setServerHealthMetricses(sysMonServerHealthMetricsRepository.getServerMetrics(server.getId()));
            }
        return sysMonServers;
    }

    @Override
    @Transactional(rollbackFor = DuplicateKeyException.class)
    public void addServer(SysMonServer server)  throws DuplicateKeyException{
        try{
            sysMonServerRepository.insert(server);
        }catch(Exception e){
            if (e instanceof ConstraintViolationException) {
                throw new DuplicateKeyException("Server already exists");
            }
        }
    }

    @Override
    @Transactional
    public void deleteServer(Long id){
        sysMonServerRepository.remove(id);
    }

    @Override
    @Transactional
    public SysMonServer getServer(Long serverId) {
        return sysMonServerRepository.get(serverId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateServer(SysMonServer obj) {
        sysMonServerRepository.put(obj);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateEndPoint(SysMonEndPoint obj) {
        sysMonEndPointRepository.put(obj);
    }

    @Override
    @Transactional
    public Collection<Object[]> getTotalOrdersByDate(){
        return orderRepository.getTotalOrdersByDate();
    }

    @Autowired
    SysMonEndPointTypeRepository sysMonEndPointTypeRepository;

    public SysMonEndPointTypeRepository getSysMonEndPointTypeRepository() {
        return sysMonEndPointTypeRepository;
    }

    public void setSysMonEndPointTypeRepository(SysMonEndPointTypeRepository sysMonEndPointTypeRepository) {
        this.sysMonEndPointTypeRepository = sysMonEndPointTypeRepository;
    }

    @Autowired
    SysMonEndPointRepository sysMonEndPointRepository;

    public SysMonEndPointRepository getSysMonEndPointRepository() {
        return sysMonEndPointRepository;
    }

    public void setSysMonEndPointRepository(SysMonEndPointRepository sysMonEndPointRepository) {
        this.sysMonEndPointRepository = sysMonEndPointRepository;
    }

    @Autowired
    SysMonEndPointMetricsRepository sysMonEndPointMetricsRepository;

    public SysMonEndPointMetricsRepository getSysMonEndPointMetricsRepository() {
        return sysMonEndPointMetricsRepository;
    }

    public void setSysMonEndPointMetricsRepository(SysMonEndPointMetricsRepository sysMonEndPointMetricsRepository) {
        this.sysMonEndPointMetricsRepository = sysMonEndPointMetricsRepository;
    }

    @Autowired
    SysMonServerRepository sysMonServerRepository;

    public SysMonServerRepository getSysMonServerRepository() {
        return sysMonServerRepository;
    }

    public void setSysMonServerRepository(SysMonServerRepository sysMonServerRepository) {
        this.sysMonServerRepository = sysMonServerRepository;
    }

    @Autowired
    SysMonServerHealthMetricsRepository sysMonServerHealthMetricsRepository;

    public SysMonServerHealthMetricsRepository getSysMonServerHealthMetricsRepository() {
        return sysMonServerHealthMetricsRepository;
    }

    public void setSysMonServerHealthMetricsRepository(SysMonServerHealthMetricsRepository sysMonServerHealthMetricsRepository) {
        this.sysMonServerHealthMetricsRepository = sysMonServerHealthMetricsRepository;
    }

    @Autowired
    OrderRepository orderRepository;

    public OrderRepository getOrderRepository() {
        return orderRepository;
    }

    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
}
